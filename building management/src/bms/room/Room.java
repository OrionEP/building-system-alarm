package bms.room;

import bms.exceptions.DuplicateSensorException;
import bms.hazardevaluation.HazardEvaluator;
import bms.hazardevaluation.WeightingBasedHazardEvaluator;
import bms.sensors.Sensor;
import bms.sensors.TemperatureSensor;
import bms.sensors.TimedSensor;
import bms.util.Encodable;

import java.util.*;

/**
 * Represents a room on a floor of a building.
 * <p>
 * Each room has a room number (unique for this floor, ie. no two rooms on the
 * same floor can have the same room number), a type to indicate its intended
 * purpose, and a total area occupied by the room in square metres.
 * <p>
 * Rooms also need to record whether a fire drill is currently taking place in
 * the room.
 * <p>
 * Rooms can have one or more sensors to monitor hazard levels
 * in the room.
 *
 * @ass1
 */
public class Room extends Object implements Encodable {

    /**
     * Unique room number for this floor.
     */
    private int roomNumber;

    /**
     * The type of room. Different types of rooms can be used for different
     * activities.
     */
    private RoomType type;

    /**
     * List of sensors located in the room. Rooms may only have up to one of
     * each type of sensor. Alphabetically sorted by class name.
     */
    private List<Sensor> sensors;

    /**
     * Area of the room in square metres.
     */
    private double area;

    /**
     * Minimum area of all rooms, in square metres.
     * (Note that dimensions of the room are irrelevant).
     * Defaults to 5.
     */
    private static final int MIN_AREA = 5;

    /**
     * Records whether there is currently a fire drill.
     */
    private boolean fireDrill;
    private HazardEvaluator hazardEvaluator;
    private boolean maintenance;


    /**
     * Creates a new room with the given room number.
     *
     * @param roomNumber the unique room number of the room on this floor
     * @param type       the type of room
     * @param area       the area of the room in square metres
     * @ass1
     */
    public Room(int roomNumber, RoomType type, double area) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.area = area;

        this.sensors = new ArrayList<>();
        this.fireDrill = false;
    }

    /**
     * Returns room number of the room.
     *
     * @return the room number on the floor
     * @ass1
     */
    public int getRoomNumber() {
        return this.roomNumber;
    }

    /**
     * Returns area of the room.
     *
     * @return the room area in square metres
     * @ass1
     */
    public double getArea() {
        return this.area;
    }

    /**
     * Returns the minimum area for all rooms.
     * <p>
     * Rooms must be at least 5 square metres in area.
     *
     * @return the minimum room area in square metres
     * @ass1
     */
    public static int getMinArea() {
        return MIN_AREA;
    }

    /**
     * Returns the type of the room.
     *
     * @return the room type
     * @ass1
     */
    public RoomType getType() {
        return type;
    }

    /**
     * Returns whether there is currently a fire drill in progress.
     *
     * @return current status of fire drill
     * @ass1
     */
    public boolean fireDrillOngoing() {
        return this.fireDrill;
    }

    /**
     * Returns the list of sensors in the room.
     * <p>
     * The list of sensors stored by the room should always be in alphabetical
     * order, by the sensor's class name.
     * <p>
     * Adding or removing sensors from this list should not affect the room's
     * internal list of sensors.
     *
     * @return list of all sensors in alphabetical order of class name
     * @ass1
     */
    public List<Sensor> getSensors() {
        return new ArrayList<>(this.sensors);
    }

    /**
     * Change the status of the fire drill to the given value.
     *
     * @param fireDrill whether there is a fire drill ongoing
     * @ass1
     */
    public void setFireDrill(boolean fireDrill) {
        this.fireDrill = fireDrill;
    }

    /**
     * Return the given type of sensor if there is one in the list of sensors;
     * return null otherwise.
     *
     * @param sensorType the type of sensor which matches the class name
     *                   returned by the getSimpleName() method,
     *                   e.g. "NoiseSensor" (no quotes)
     * @return the sensor in this room of the given type; null if none found
     * @ass1
     */
    public Sensor getSensor(String sensorType) {
        for (Sensor s : this.getSensors()) {
            if (s.getClass().getSimpleName().equals(sensorType)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Adds a sensor to the room if a sensor of the same type is not
     * already in the room.
     * <p>
     * The list of sensors should be sorted after adding the new sensor, in
     * alphabetical order by simple class name ({@link Class#getSimpleName()}).
     *
     * @param sensor the sensor to add to the room
     * @throws DuplicateSensorException if the sensor to add is of the
     *                                  same type as a sensor already in this
     *                                  room
     * @ass1
     */
    public void addSensor(Sensor sensor)
            throws DuplicateSensorException {
        for (Sensor s : sensors) {
            if (s.getClass().equals(sensor.getClass())) {
                throw new DuplicateSensorException(
                        "Duplicate sensor of type: "
                                + s.getClass().getSimpleName());
            }
        }
        sensors.add(sensor);
        //Adding a sensor should remove any hazard evaluator currently in the
        // room
        setHazardEvaluator(null);
        sensors.sort(Comparator.comparing(s -> s.getClass().getSimpleName()));
    }

    /**
     * this room's hazard evaluator,null if not exist.
     *
     * @return HazardEvaluator room's hazard evaluator.
     */
    public HazardEvaluator getHazardEvaluator() {
        return hazardEvaluator;
    }

    /**
     * Sets the room's hazard evaluator.
     *
     * @param hazardEvaluator new hazard evaluator for the room to use.
     */
    public void setHazardEvaluator(HazardEvaluator hazardEvaluator) {
        this.hazardEvaluator = hazardEvaluator;
    }

    /**
     * whether there is currently maintenance in progress.
     *
     * @return boolean maintenance status.
     */
    public boolean maintenanceOngoing() {
        return this.maintenance;

    }

    /**
     * Change the status of maintenance to the given value.
     *
     * @param maintenance boolean whether there is maintenance ongoing.
     */
    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }

    /**
     * Evaluates the room status based upon current information.
     *
     * @return RoomState current room status
     */
    public RoomState evaluateRoomState() {
        TemperatureSensor temperatureSensor = null;
        for (Sensor sensor : sensors) {
            if (sensor.getClass().getSimpleName().equals("TemperatureSensor")) {
                temperatureSensor = (TemperatureSensor) sensor;
            }
            if (temperatureSensor != null && temperatureSensor.getHazardLevel()
                    == 100) {
                return RoomState.EVACUATE;

            }
        }
        if (fireDrillOngoing()) {
            return RoomState.EVACUATE;
        }
        if (maintenanceOngoing() && !fireDrillOngoing()) {
            return RoomState.MAINTENANCE;
        }
        return RoomState.OPEN;
    }

    /**
     * whether this room is equal to the other given room.
     *
     * @param obj other object to compare equality.
     * @return boolean true if equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Room)) {
            return false;
        }
        Room room = (Room) obj;
        return room.getType() == type &&
                room.getRoomNumber() == roomNumber &&
                Math.abs(room.getArea() - area) <= 0.001 &&
                room.getSensors().size() == sensors.size() &&
                room.getSensors().containsAll(getSensors())
                && getSensors().containsAll(room.getSensors());
    }

    /**
     * Returns the hash code of this room.
     *
     * @return int hash code of this room.
     */
    @Override
    public int hashCode() {
        return Objects.hash(type, roomNumber, area, sensors.size(), sensors);
    }

    /**
     * Returns the human-readable string representation of this room.
     * <p>
     * The format of the string to return is
     * "Room #'roomNumber': type='roomType', area='roomArea'm^2,
     * sensors='numSensors'"
     * without the single quotes, where 'roomNumber' is the room's unique
     * number, 'roomType' is the room's type, 'area' is the room's type,
     * 'numSensors' is the number of sensors in the room.
     * <p>
     * The room's area should be formatted to two (2) decimal places.
     * <p>
     * For example:
     * "Room #42: type=STUDY, area=22.50m^2, sensors=2"
     *
     * @return string representation of this room
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("Room #%d: type=%s, area=%.2fm^2, sensors=%d",
                this.roomNumber,
                this.type,
                this.area,
                this.sensors.size());
    }

    /**
     * the machine-readable string representation of this room and all of its
     * sensors.
     *
     * @return string room encoded string representation.
     */
    @Override
    public String encode() {
        StringBuilder head = new StringBuilder();
        head.append(roomNumber)
                .append(":")
                .append(getType())
                .append(":")
                .append(String.format("%.2f", getArea()))
                .append(":")
                .append(getSensors().size());
        if (getHazardEvaluator() != null) {
            head.append(":")
                    .append(getHazardEvaluator().toString());
        }
        List<Object[]> al = new ArrayList<>();
        boolean flag = false;
        if (getHazardEvaluator() != null) {
            flag = (hazardEvaluator.getClass()
                    == WeightingBasedHazardEvaluator.class);
        }
        List<Integer> weights = null;
        if (flag) {
            weights = ((WeightingBasedHazardEvaluator) hazardEvaluator)
                    .getWeightings();
        }
        for (int i = 0; i < sensors.size(); i++) {
            Encodable sensor = (Encodable) sensors.get(i);
            if (flag) {
                Integer weight = weights.get(i);
                al.add(new Object[]{sensor, weight});
            } else {
                al.add(new Object[]{sensor});
            }
        }
        StringBuilder body = new StringBuilder();
        for (int i = 0; i < al.size(); i++) {
            Object[] o = al.get(i);
            Encodable e = (Encodable) o[0];
            if (flag) {
                Integer weight = (Integer) o[1];
                body.append(e.encode()).append("@").append(weight);
            } else {
                body.append(e.encode());
            }
            if (i < al.size() - 1) {
                body.append(System.lineSeparator());
            }
        }
        if (body.length() != 0) {
            head.append(System.lineSeparator());
            head.append(body);
        }
        return head.toString();
    }
}


