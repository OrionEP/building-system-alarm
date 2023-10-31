package bms.hazardevaluation;

/**
 * Take readings from all available hazard sensors and return a single
 * hazard class component.
 */
public interface HazardEvaluator {
    /**
     * Calculates a hazard level between 0 and 100.
     * @return int the hazard level.
     */
    int evaluateHazardLevel();

}
