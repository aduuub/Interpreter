package nodes;

import java.util.List;

import main.Robot;
import main.RobotProgramNode;

public class StatementNode implements RobotProgramNode {

	/*
	 * Either: Act | loop | if | while
	 */
	private RobotProgramNode statement;
	
	public StatementNode(RobotProgramNode s) {
		this.statement = s;
	}

	@Override
	public void execute(Robot robot) {
		statement.execute(robot);
	}
}
