package nodes.conditional;

import main.*;
import nodes.SensorNode;

public class GreaterThen extends ConditionNode {

	protected SensorNode sensor;
	protected int number;
	
	public GreaterThen(SensorNode sensor, int number) {
		this.sensor = sensor;
		this.number = number;
	}

		@Override
		public boolean evaluate(Robot r){
			return sensor.evaluate(r) > number;
		}	
}