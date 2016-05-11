package nodes;

import main.Robot;
import main.RobotProgramNode;
import nodes.conditional.ConditionNode;

public class IfNode implements RobotProgramNode  {

	private BlockNode block;
	private ConditionNode condition;

	public IfNode(BlockNode b, ConditionNode c){
		this.block = b;
		this.condition = c;
	}

	@Override
	public void execute(Robot robot) {
		if(condition.evaluate(robot))
			block.execute(robot);
	}
}
