package nodes;

import main.Robot;
import main.RobotProgramNode;

public abstract class SensorNode{

	public abstract int evaluate(Robot robot);

	public static class FuelLeft extends SensorNode{
		@Override
		public int evaluate(Robot robot){
			return robot.getFuel();
		}
	}
	
	public static class OppLR extends SensorNode{
		@Override
		public int evaluate(Robot robot){
			return robot.getOpponentLR();
		}
	}
	
	public static class OppFB extends SensorNode{
		@Override
		public int evaluate(Robot robot){
			return robot.getOpponentFB();
		}
	}
	
	public static class NumBarrels extends SensorNode{
		@Override
		public int evaluate(Robot robot){
			return robot.numBarrels();
		}
	}
	
	public static class BarrelLR extends SensorNode{
		@Override
		public int evaluate(Robot robot){
			return robot.getClosestBarrelLR();
		}
	}
	
	public static class BarrelFB extends SensorNode{
		@Override
		public int evaluate(Robot robot){
			return robot.getClosestBarrelFB();
		}
	}
	
	public static class WallDistance extends SensorNode{
		@Override
		public int evaluate(Robot robot){
			return robot.getDistanceToWall();
		}
	}
}
