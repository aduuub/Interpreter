package nodes;

import java.util.List;

import main.Robot;
import main.RobotProgramNode;

public class MultipleSubtrees implements RobotProgramNode{

	List<RobotProgramNode> nodes;
	
	public MultipleSubtrees(List<RobotProgramNode> n){
		this.nodes = n;
	}
	
	@Override
	public void execute(Robot robot) {
		for(RobotProgramNode n : nodes)
			n.execute(robot);
	}

}
