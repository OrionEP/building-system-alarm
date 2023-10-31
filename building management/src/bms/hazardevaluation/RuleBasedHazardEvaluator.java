package bms.hazardevaluation;

import bms.sensors.HazardSensor;
import bms.sensors.OccupancySensor;

import java.util.LinkedList;
import java.util.List;

/**
 * Evaluates the hazard level of a location using a rule based system.
 */
public class RuleBasedHazardEvaluator implements HazardEvaluator {
    // the list of hazard sensors.
    private List<HazardSensor> sensors;

    /**
     * Create a new rule-based hazard evaluator using the given sensor list.
     *
     * @param sensors sensors to be used in the hazard level calculation.
     */
    public RuleBasedHazardEvaluator(List<HazardSensor> sensors) {
        this.sensors = sensors;

    }

    /**
     * The calculated hazard level is returned based on applying a set
     * of rules to the list of sensors passed to the constructor.
     *
     * @return int calculated hazard level.
     */
    @Override
    public int evaluateHazardLevel() {
        if (sensors.size() == 0) {
            return 0;
        }
        if (sensors.size() == 1) {
            return sensors.get(0).getHazardLevel();
        }
        LinkedList<HazardSensor> hazardSensors = new LinkedList();
        OccupancySensor occupancySensor = null;
        for (HazardSensor sensor : sensors) {
            String name = sensor.getClass().getSimpleName();
            if (name.equals("OccupancySensor")) {
                occupancySensor = (OccupancySensor) sensor;
            } else {
                hazardSensors.add(sensor);
                if (sensor.getHazardLevel() == 100)
                    return 100;


            }

        }
        float avg = 0;
        float sum = 0;
        for (int i = 0; i <= hazardSensors.size() - 1; i++) {
            sum += hazardSensors.get(i).getHazardLevel();
        }
        avg = sum / hazardSensors.size();
        if (occupancySensor != null) {
            avg = avg * (float) (occupancySensor.getHazardLevel() / 100);


        }
        return Math.round(avg);

    }

    /**
     * the string representation of this hazard evaluator.
     *
     * @return string room string representation.
     */
    @Override
    public String toString() {
        return "RuleBased";
    }
}
