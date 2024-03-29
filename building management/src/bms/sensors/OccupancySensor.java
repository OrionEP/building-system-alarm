package bms.sensors;

import java.util.Objects;

/**
 * A sensor that measures the number of people in a room.
 *
 * @ass1
 */
public class OccupancySensor extends TimedSensor
        implements HazardSensor, ComfortSensor {
    /**
     * Maximum capacity of the space the sensor is monitoring.
     */
    private int capacity;

    /**
     * Creates a new occupancy sensor with the given sensor readings, update
     * frequency and capacity.
     * <p>
     * The given capacity must be greater than or equal to zero.
     *
     * @param sensorReadings  a non-empty array of sensor readings
     * @param updateFrequency indicates how often the sensor readings update,
     *                        in minutes
     * @param capacity        maximum allowable number of people in the room
     * @throws IllegalArgumentException if capacity is less than zero
     * @ass1
     */
    public OccupancySensor(int[] sensorReadings, int updateFrequency,
                           int capacity) {
        super(sensorReadings, updateFrequency);

        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity must be >= 0");
        }

        this.capacity = capacity;
    }

    /**
     * Returns the capacity of this occupancy sensor.
     *
     * @return capacity
     * @ass1
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the hazard level based on the ratio of the current sensor reading
     * to the maximum capacity.
     * <p>
     * When the current reading is equal to or more than the capacity, the
     * hazard level is equal to 100 percent.
     * <p>
     * For example, a room with a maximum capacity of 21 people and current
     * occupancy of 8 people would have a hazard level of 38.
     * A room with a maximum capacity of 30 people and a current occupancy of
     * 34 people would have a hazard level of 100.
     * <p>
     * Floating point division should be used when performing the calculation,
     * however the resulting floating point number should be <i>rounded to the
     * nearest integer</i> before being returned.
     *
     * @return the current hazard level as an integer between 0 and 100
     * @ass1
     */
    @Override
    public int getHazardLevel() {
        final int currentReading = this.getCurrentReading();

        if (currentReading >= this.capacity) {
            return 100;
        }
        double occupancyRatio = ((double) currentReading) / this.capacity;
        double occupancyPct = 100 * occupancyRatio;
        return (int) Math.round(occupancyPct);
    }

    /**
     * Returns the machine-readable string representation of this sensor.
     *
     * @return string encoded string representation of this sensor
     */
    @Override
    public String encode() {
        return String.format("OccupancySensor:%s:%d:%d", super.encode()
                , getUpdateFrequency(), capacity);

    }

    /**
     * whether two occupancy sensors are equal.
     *
     * @param obj other object to compare equality.
     * @return boolean true when this sensor is equal to the other
     * given sensor.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OccupancySensor)) {
            return false;
        }
        OccupancySensor sensor = (OccupancySensor) obj;
        if (sensor.getCapacity() != capacity) {
            return false;
        }
        return super.equals(obj);

    }

    /**
     * Two occupancy sensors that are equal according to equals(Object).
     * should have the same hash code.
     *
     * @return hash code of this sensor.
     */
    @Override
    public int hashCode() {
        return super.hashCode() + Objects.hashCode(capacity);
    }

    /**
     * Returns the comfort level detected by this sensor.
     *
     * @return int sensor's current comfort level, from 0 to 100
     */
    public int getComfortLevel() {
        float comfortLevel = 0;
        if (getCurrentReading() <= capacity) {
            comfortLevel = (1 - (float) (getCurrentReading()) / capacity)
                    *100;
        }
        return Math.round(comfortLevel);

    }


    /**
     * Returns the human-readable string representation of this occupancy
     * sensor.
     * <p>
     * The format of the string to return is
     * "TimedSensor: freq='updateFrequency', readings='sensorReadings',
     * type=OccupancySensor, capacity='sensorCapacity'"
     * without the single quotes, where 'updateFrequency' is this sensor's
     * update frequency (in minutes), 'sensorReadings' is a comma-separated
     * list of this sensor's readings, and 'sensorCapacity' is this sensor's
     * maximum capacity.
     * <p>
     * For example: "TimedSensor: freq=5, readings=27,28,28,25,3,1,
     * type=OccupancySensor, capacity=30"
     *
     * @return string representation of this sensor
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s, type=OccupancySensor, capacity=%d",
                super.toString(),
                this.capacity);
    }
}
