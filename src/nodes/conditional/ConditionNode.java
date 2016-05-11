package nodes.conditional;

import main.*;
import nodes.SensorNode;

public abstract class ConditionNode {

	protected SensorNode sensor;
	protected int number;

	public abstract boolean evaluate(Robot r);
	
}