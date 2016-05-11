package nodes;

import java.util.List;

import main.Robot;
import main.RobotProgramNode;

public class LoopNode implements RobotProgramNode{

	private BlockNode block;
	
	public LoopNode(BlockNode b) {
		this.block = b;
	}
	@Override
	public void execute(Robot robot) {
		block.execute(robot);
	}

}
