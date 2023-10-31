package bms.util;

import bms.building.Building;
import bms.floor.Floor;
import bms.room.Room;
import bms.room.RoomState;
import bms.room.RoomType;
import bms.sensors.*;

import java.util.ArrayList;
import java.util.List;

/**
 * provides a recommendation for a study room in a building.
 */
public class StudyRoomRecommender {
    /**
     * constructor of study room recommender.
     */
    public StudyRoomRecommender() {
    }

    /**
     * The best learning space in a given building.
     *
     * @param selectedBuilding Building Search for the building where the
     *                         study room is located.
     * @return Room the most suitable study room in the building; null if there
     * are none.
     */
    public static Room recommendStudyRoom(Building selectedBuilding) {
        List<Floor> floors = new ArrayList<>();

        Floor sortFloor;
        Room bestFloorRoom = null, bestBuildingRoom = null;
        float floorHighestComfort = 0;
        for (int i = 1; i <= selectedBuilding.getFloors().size(); i++) {
            sortFloor = selectedBuilding.getFloorByNumber(i);
            floors.add(sortFloor);
        }
        for (Floor floor : floors) {
            for (int j = 0; j < floor.getRooms().size(); j++) {
                Room room = floor.getRooms().get(j);
                if (room.getType() == RoomType.STUDY && room.evaluateRoomState()
                        == RoomState.OPEN) {
                    floorHighestComfort = roomComfort(floor.getRooms().get(0));
                    if (roomComfort(room) > floorHighestComfort) {
                        floorHighestComfort = roomComfort(room);
                        bestFloorRoom = room;

                    }


                }


            }
            if (bestBuildingRoom == null) {
                bestBuildingRoom = bestFloorRoom;
            } else if (roomComfort(bestBuildingRoom) <= floorHighestComfort) {
                bestBuildingRoom = bestFloorRoom;
            } else if (roomComfort(bestBuildingRoom) > floorHighestComfort) {
                return bestBuildingRoom;
            }


        }
        return bestBuildingRoom;

    }

    /**
     * comfortable level of a room.
     *
     * @param room a room to calculate the comfortable level.
     * @return float room comfort level.
     */
    private static float roomComfort(Room room) {
        int sum = 0;
        float comfortLevel = 0;
        float roomComfortLevel = 0;
        if (room == null) {
            return 0;
        }
        if (room.getSensors() == null || room.getSensors().isEmpty()) {
            roomComfortLevel = 0;
        }
        if (room.getSensors() != null) {
            int numSensor = room.getSensors().size();
            //which sensor in the room.
            for (Sensor sensor : room.getSensors()) {
                ComfortSensor sensor1 = (ComfortSensor) sensor;
                sum += sensor1.getComfortLevel();


            }

            if (numSensor == 0)
                return 0;
            roomComfortLevel = (float) sum / numSensor;


        }
        return roomComfortLevel;

    }
}
