package bms.hazardevaluation;

import bms.sensors.HazardSensor;
import bms.sensors.OccupancySensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Evaluates the hazard level of a location using weightings for the sensor
 * values.
 */
public class WeightingBasedHazardEvaluator extends Object
        implements HazardEvaluator {
    /**map with key of hazard sensor and value with integer.*/
    private Map<HazardSensor, Integer> sensors;

    /**
     * weighting-based hazard evaluator with the given sensors and weightings.
     *
     * @param sensors mapping of sensors with their respective weighting.
     * @throws IllegalArgumentException if the weighting is not legal.
     */
    public WeightingBasedHazardEvaluator(Map<HazardSensor, Integer> sensors)
            throws IllegalArgumentException {
        this.sensors = sensors;
        int sum = 0;
        for (Integer weight : sensors.values()) {
            sum += weight;
            if ((weight < 0 || weight > 100)) {
                throw new IllegalArgumentException();
            }


        }
        if (sum != 100) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * The calculated hazard level is returned based on applying a set
     * of rules to the list of sensors passed to the constructor.
     *
     * @return int calculated hazard level.
     */
    @Override
    public int evaluateHazardLevel() {

        float res = 0;
        for (HazardSensor sensor : sensors.keySet()) {
            res += sensor.getHazardLevel() * (float) (sensors.get(sensor) / 100);


        }
        return Math.round(res);

    }

    /**
     * Returns a list containing the weights associated with all sensors
     * monitored by this hazard assessment program.
     *
     * @return List list of weightings.
     */
    public List<Integer> getWeightings() {
        List<Integer> res = new ArrayList<>();
        for (Integer value : sensors.values()) {
            res.add(value);
        }
        return res;
    }

    /**
     * the string representation of this hazard evaluator.
     *
     * @return string room string representation.
     */
    @Override
    public String toString() {
        return "WeightingBased";
    }
}
