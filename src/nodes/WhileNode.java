package nodes;
import main.Robot;
import main.RobotProgramNode;
import nodes.conditional.ConditionNode;

public class WhileNode implements RobotProgramNode{

	private BlockNode block;
	private ConditionNode condition;


	public WhileNode(BlockNode b, ConditionNode c){
		this.block = b;
		this.condition = c;
	}


	@Override
	public void execute(Robot robot) {
		while(condition.evaluate(robot))
			block.execute(robot);
	}

}


