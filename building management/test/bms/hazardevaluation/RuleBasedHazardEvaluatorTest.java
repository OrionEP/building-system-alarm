package bms.hazardevaluation;

import bms.sensors.CarbonDioxideSensor;
import bms.sensors.HazardSensor;
import bms.sensors.NoiseSensor;
import bms.sensors.Sensor;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class RuleBasedHazardEvaluatorTest {
    @Test
    public void f(){
        List<HazardSensor> sensors=new LinkedList<>();
        NoiseSensor noiseSensor =new NoiseSensor(new int[]{15,20,30},2);

        CarbonDioxideSensor carbonDioxideSensor =
                new CarbonDioxideSensor((new int[]{25,26,28}),3,20,18);
        System.out.println(carbonDioxideSensor.getHazardLevel()+"---"+noiseSensor.getHazardLevel());
        sensors.add(noiseSensor);
        sensors.add(carbonDioxideSensor);
        RuleBasedHazardEvaluator evaluator=
                new RuleBasedHazardEvaluator(sensors);
        System.out.println(evaluator.evaluateHazardLevel());
    }

}