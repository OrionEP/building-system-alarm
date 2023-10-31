package bms.building;

import bms.exceptions.*;
import bms.floor.Floor;
import bms.hazardevaluation.RuleBasedHazardEvaluator;
import bms.hazardevaluation.WeightingBasedHazardEvaluator;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * manages the initialisation and saving of buildings by reading and writing
 * data to a file.
 */
public class BuildingInitialiser {
    /**
     * construct the building initialiser.
     */
    public BuildingInitialiser() {
    }

    /**
     * Loads a list of buildings from a save file with the given filename.
     *
     * @param filename path of the file from which to load a list of buildings.
     * @return a list containing all the buildings loaded from the file.
     * @throws IOException         if an IOException is encountered when
     * calling any IO methods.
     * @throws FileFormatException if the file format of the given file is
     * invalid.
     */
    public static List<Building> loadBuildings(String filename)
            throws IOException,
            FileFormatException {

        FileReader fr = new FileReader(filename);
        List<Building> buildingList = new ArrayList<Building>();
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        List<String> list = new ArrayList<>();
        try {
            while ((line = br.readLine()) != null) {
                if (line.equals("")) {
                    throw new FileFormatException();
                }
                list.add(line);
            }
            br.close();
            List<Integer> buildingNumber = new ArrayList();
            buildingNumber.add(0);//first line is the first building

            for (int i = 1; i < list.size(); i++) {
                if (isBuilding(list, i))//determine whether the line is building
                {
                    buildingNumber.add(i);
                }
            }
            buildingNumber.add(list.size());
            for (int i = 0; i + 1 < buildingNumber.size(); i += 1) {
                List oneBuildingList = list.subList(buildingNumber.get(i),
                        buildingNumber.get(i + 1));//the whole building.
                buildingList.add(readBuilding(oneBuildingList));
            }

        } catch (Exception e) {
            throw new FileFormatException();
        }
        return buildingList;
    }

    /**
     * private help method to read a building.
     *
     * @param buildingList list of building
     * @return Building a building read by the file.
     * @throws IOException                if an IOException is encountered
     * when calling any IO methods.
     * @throws FileFormatException        if the file format of the given
     * file is invalid.
     * @throws InsufficientSpaceException when the space is not sufficient to
     * add rooms.
     * @throws DuplicateSensorException   when two same sensors in a room.
     * @throws DuplicateRoomException     when a room shown twice.
     * @throws DuplicateFloorException    when a duplicate floor shown in a
     * building.
     * @throws NoFloorBelowException      when a floor with no floor under
     * below.
     * @throws FloorTooSmallException     when the floor too small than all
     * calculated rooms
     */
    private static Building readBuilding(List<String> buildingList)
            throws IOException, FileFormatException,
            InsufficientSpaceException, DuplicateSensorException
            , DuplicateRoomException, DuplicateFloorException,
            NoFloorBelowException, FloorTooSmallException {
        List<Integer> lines = new ArrayList();
        String buildingName = buildingList.get(0);
        int floorAmount = Integer.parseInt(buildingList.get(1));
        lines.add(2);
        for (int i = 3; i < buildingList.size(); i++) {
            if (isLatterFloor(buildingList, i)) {
                lines.add(i);
            }
        }
        lines.add(buildingList.size());
        Building building = new Building(buildingName);
        //when building floor not equal to actual floor num.
        if (lines.size() - floorAmount != 1) {
            throw new FileFormatException();
        }
        for (int i = 0; i + 1 < lines.size(); i++) {
            int left = lines.get(i), right = lines.get(i + 1);

            Floor floor = readFloor(buildingList.subList(left, right));
            building.addFloor(floor);


        }
        return building;

    }

    /**
     * private method to read a floor.
     *
     * @param list a list store the information of floor by read from file.
     * @return Floor process into function what read by the file.
     * @throws IOException                if an IOException is encountered
     * when calling any IO methods.
     * @throws FileFormatException        if the file format of the given
     * file is invalid.
     * @throws DuplicateSensorException   when two same sensors in a room.
     * @throws DuplicateRoomException     when a room shown twice in a floor.
     * @throws InsufficientSpaceException when the space is not sufficient to
     * add rooms.
     */
    private static Floor readFloor(List<String> list)
            throws IOException, FileFormatException, DuplicateSensorException
            , DuplicateRoomException, InsufficientSpaceException {
        HashMap<String, Room> roomMap = new HashMap<>();
        List<Integer> roomLines = new ArrayList<>();
        roomLines.add(1);
        LinkedList<String> roomNumList = new LinkedList();
        String[] floorInfos = list.get(0).split(":");
        if (floorInfos.length < 4) {
            throw new FileFormatException();
        }
        if (floorInfos.length > 5) {
            throw new FileFormatException();
        }
        if (floorInfos.length > 4) {
            for (String roomNumber : floorInfos[4].split(",")) {
                roomNumList.add(roomNumber);
            }
        }
        int floorNumber = Integer.parseInt(floorInfos[0]);
        double width = Double.parseDouble(floorInfos[1]);
        double height = Double.parseDouble(floorInfos[2]);
        int roomAmount = Integer.parseInt(floorInfos[3]);
        Floor floor = new Floor(floorNumber, width, height);
        for (int i = 2; i < list.size(); i++) {
            if (roomSplit(list, i)) {
                roomLines.add(i);
            }
        }
        roomLines.add(list.size());
        //floor can have no room.
        if (roomAmount == 0) {
            if (roomLines.size() != 2) {
                throw new FileFormatException();
            }
        } else {
            if (roomLines.size() - roomAmount != 1) {
                throw new FileFormatException();
            }
        }

        for (int i = 0; i + 1 < roomLines.size(); i++) {
            int left = roomLines.get(i), right = roomLines.get(i + 1);
            Room room = readRoom(list.subList(left, right));
            if (room != null) {
                roomMap.put("" + room.getRoomNumber(), room);

                floor.addRoom(room);

            }
        }
        List<Room> roomOrder = new LinkedList<>();
        for (String maintainRoom : roomNumList) {
            roomOrder.add(roomMap.get(maintainRoom));
        }
        if (roomOrder.size() != 0) {
            floor.createMaintenanceSchedule(roomOrder);
        }


        return floor;
    }

    /**
     * read a room.
     *
     * @param list list
     * @return Room
     * @throws FileFormatException      if the file format of the given file
     * is invalid.
     * @throws DuplicateSensorException when two same sensors in a room.
     */
    private static Room readRoom(List<String> list)
            throws FileFormatException, DuplicateSensorException {
        if (list.size() < 1) {
            return null;
        }

        String[] Roominfos = list.get(0).split(":");
        if (Roominfos.length < 4) {
            throw new FileFormatException();
        }
        int sensorAmount = Integer.parseInt(Roominfos[3]);
        Room room = new Room(Integer.parseInt(Roominfos[0]),
                RoomType.valueOf(Roominfos[1])
                , Double.parseDouble(Roominfos[2]));
        int flag = 0;
        if (Roominfos.length > 5) {
            throw new FileFormatException();
        }
        if (Roominfos.length == 5) {
            String s = Roominfos[4];
            if (s.equals("RuleBased")) {
                flag = 1;
            } else if (s.equals("WeightingBased")) {
                flag = 2;
            } else {
                throw new FileFormatException();
            }

        }
        HashMap<HazardSensor, Integer> weightMap = new HashMap<>();
        List<HazardSensor> hazardSensors = new ArrayList<>();
        if (list.size() - sensorAmount != 1) {
            throw new FileFormatException();
        }
        for (int i = 1; i < list.size(); i++) {
            Object[] objects = StringToSensor(list.get(i));
            Sensor sensor = (Sensor) objects[0];
            int weight = (Integer) objects[1];

            room.addSensor(sensor);
            weightMap.put((HazardSensor) sensor, weight);


        }
        //different flag,different type of evaluator.
        if (flag == 1) {
            for (Sensor sensor : room.getSensors()) {
                hazardSensors.add((HazardSensor) sensor);
            }
            room.setHazardEvaluator(new RuleBasedHazardEvaluator(hazardSensors));
        } else if (flag == 2) {
            room.setHazardEvaluator(new WeightingBasedHazardEvaluator(weightMap));
        }
        return room;
    }

    /**
     * whether it is building.
     *
     * @param list  string read by the file.
     * @param index the index of the list.
     * @return boolean if the string is a new building.
     */
    private static boolean isBuilding(List<String> list, int index) {
        if (index + 1 < list.size()) {
            String latter = list.get(index + 1);
            return isLatter(latter);
        }
        return false;
    }

    /**
     * whether the latter is a number.
     *
     * @param latter the string in the next line.
     * @return boolean test whether the latter is number.
     */
    private static boolean isLatter(String latter) {
        try {
            Integer.parseInt(latter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * whether it is a floor.
     *
     * @param list  building list.
     * @param index index of the building list.
     * @return boolean test if it is floor.
     */
    private static boolean isLatterFloor(List<String> list, int index) {
        if (index + 1 < list.size()) {
            String cur = list.get(index), latter = list.get(index + 1);
            try {
                int start = Integer.parseInt(cur.split(":")[0]);//1...
                return latter.startsWith("" + start);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * whether it is room.
     *
     * @param list  floor lists.
     * @param index index of the list.
     * @return boolean test the first index of room is num.
     */
    private static boolean roomSplit(List<String> list, int index) {
        String current = list.get(index);
        String[] rooms = current.split(":");
        try {
            Integer.parseInt(rooms[0]);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * change the string to sensor.
     *
     * @param str given str for all sensors.
     * @return Object[]
     * @throws FileFormatException if the file format of the given file is
     * invalid.
     */
    private static Object[] StringToSensor(String str) throws FileFormatException {
        Sensor sensor = null;
        int weight = -1;
        String[] sensorInfo = str.split(":");
        String sensorName = sensorInfo[0];
        List<Integer> readings = new ArrayList<>();
        String[] readingNums = sensorInfo[1].split(",");
        //where the temperature sensor has the reading with weight.
        for (int i = 0; i < readingNums.length; i++) {
            String element = readingNums[i];
            if (sensorName.equals("TemperatureSensor") && i == readingNums.length - 1) {
                String[] splitNums = element.split("@");
                if (splitNums.length > 1) {
                    weight = Integer.parseInt(splitNums[1]);
                }
                element = splitNums[0];
            }
            readings.add(Integer.parseInt(element));
        }
        int[] sensorReadings = new int[readings.size()];
        for (int i = 0; i < sensorReadings.length; i++) {
            sensorReadings[i] = readings.get(i);
        }
        //different type of sensors.
        switch (sensorName) {
            case "CarbonDioxideSensor" -> {
                if (sensorInfo.length != 5) {
                    throw new FileFormatException();
                }
                String[] splitNums = sensorInfo[4].split("@");
                if (splitNums.length > 1) {
                    weight = Integer.parseInt(splitNums[1]);
                }
                CarbonDioxideSensor carbonDioxideSensor
                        = new CarbonDioxideSensor(sensorReadings
                        , Integer.parseInt(sensorInfo[2])
                        , Integer.parseInt(sensorInfo[3])
                        , Integer.parseInt(splitNums[0]));

                sensor = carbonDioxideSensor;
            }
            case "NoiseSensor" -> {
                if (sensorInfo.length != 3) {
                    throw new FileFormatException();
                }
                String[] splitNums = sensorInfo[2].split("@");
                if (splitNums.length > 1) {
                    weight = Integer.parseInt(splitNums[1]);
                }
                NoiseSensor noiseSensor = new NoiseSensor(
                        sensorReadings, Integer.parseInt(splitNums[0]));
                sensor = noiseSensor;
            }
            case "OccupancySensor" -> {
                if (sensorInfo.length != 4) {
                    throw new FileFormatException();
                }
                String[] splitNums = sensorInfo[3].split("@");
                if (splitNums.length > 1) {
                    weight = Integer.parseInt(splitNums[1]);
                }
                OccupancySensor occupancySensor = new OccupancySensor
                        (sensorReadings, Integer.parseInt(sensorInfo[2])
                                , Integer.parseInt(splitNums[0]));
                sensor = occupancySensor;
            }
            case "TemperatureSensor" -> {
                if (sensorInfo.length != 2) {
                    throw new FileFormatException();
                }
                TemperatureSensor temperatureSensor = new TemperatureSensor(
                        sensorReadings);
                sensor = temperatureSensor;
            }
            //when not fount, exception thrown.
            default -> throw new FileFormatException();
        }
        return new Object[]{sensor, weight};
    }

}
