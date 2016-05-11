package nodes.conditional;

import main.*;
import nodes.SensorNode;

public class LessThen extends ConditionNode {

	protected SensorNode sensor;
	protected int number;

	public LessThen(SensorNode sensor, int number) {
		super();
		this.sensor = sensor;
		this.number = number;
	}

	@Override
	public boolean evaluate(Robot r){
		return sensor.evaluate(r) < number;
	}	
}