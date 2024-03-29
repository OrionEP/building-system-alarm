package bms.sensors;

/**
 * A sensor that measures ambient temperature in a room.
 *
 * @ass1
 */
public class TemperatureSensor
        extends TimedSensor
        implements HazardSensor, ComfortSensor {

    /**
     * Creates a new temperature sensor with the given sensor readings and
     * update frequency.
     * <p>
     * For safety reasons, all temperature sensors <b>must</b> have an update
     * frequency of 1 minute.
     *
     * @param sensorReadings a non-empty array of sensor readings
     * @ass1
     * @see TimedSensor#TimedSensor(int[], int)
     */
    public TemperatureSensor(int[] sensorReadings) {
        super(sensorReadings, 1);
    }

    /**
     * Returns the hazard level as detected by this sensor.
     * <p>
     * A temperature sensor detects a hazard if the current temperature reading
     * ({@link #getCurrentReading()}) is greater than or equal to 68 degrees,
     * indicating a fire.
     * In this case, a hazard level of 100 should be returned.
     * Otherwise, the returned hazard level is 0.
     *
     * @return sensor's current hazard level, 0 to 100
     * @ass1
     */
    @Override
    public int getHazardLevel() {
        if (this.getCurrentReading() >= 68) {
            return 100;
        }
        return 0;
    }

    /**
     * Returns the human-readable string representation of this temperature
     * sensor.
     * <p>
     * The format of the string to return is
     * "TimedSensor: freq='updateFrequency', readings='sensorReadings',
     * type=TemperatureSensor"
     * without the single quotes, where 'updateFrequency' is this sensor's
     * update frequency (in minutes) and 'sensorReadings' is a comma-separated
     * list of this sensor's readings.
     * <p>
     * For example: "TimedSensor: freq=1, readings=24,25,25,23,26,
     * type=TemperatureSensor"
     *
     * @return string representation of this sensor
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s, type=TemperatureSensor", super.toString());
    }

    /**
     * Returns the machine-readable string representation of this sensor.
     *
     * @return string encoded string representation of this sensor
     */
    @Override
    public String encode() {
        return String.format("TemperatureSensor:%s", super.encode());
    }

    /**
     * Returns the comfort level detected by this sensor.
     *
     * @return int sensor's current comfort level, from 0 to 100
     */
    public int getComfortLevel() {
        int comfortLevel = 100;
        if (getCurrentReading() >= 20 && getCurrentReading() <= 26) {
            comfortLevel = 100;
        }
        else if (getCurrentReading() <= 15 || getCurrentReading() >= 31) {
            comfortLevel = 0;
        }
        else if (getCurrentReading() < 20 || getCurrentReading() > 26) {
            comfortLevel -= 20;

        }
        return comfortLevel;
    }
}
