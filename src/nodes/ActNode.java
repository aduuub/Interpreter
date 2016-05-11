package nodes;
import main.Robot;
import main.RobotProgramNode;

public abstract class ActNode implements RobotProgramNode{

	@Override
	public abstract void execute(Robot robot);
	
	public static class Move extends ActNode{
		@Override
		public void execute(Robot robot){
			robot.move();
		}
	}

	public static class TurnL extends ActNode{
		@Override
		public void execute(Robot robot){
			robot.turnLeft();
		}
	}


	public static class TurnR extends ActNode{
		@Override
		public void execute(Robot robot){
			robot.turnRight();
		}
	}

	public static class TakeFuel extends ActNode{
		@Override
		public void execute(Robot robot){
			robot.takeFuel();
		}
	}


	public static class Wait extends ActNode{
		@Override
		public void execute(Robot robot){	
			//try{
				robot.idleWait();
//			}catch(InterruptedException e){
//				//e.printStackTrace();
//			}
		}
	}
	
	public static class TurnAround extends ActNode{
		@Override
		public void execute(Robot robot){	
			robot.turnAround();
		}
	}
	
	
	public static class ShieldOn extends ActNode{
		@Override
		public void execute(Robot robot){	
			robot.setShield(true);
		}
	}
	
	public static class ShieldOff extends ActNode{
		@Override
		public void execute(Robot robot){	
			robot.setShield(false);
		}
	}
	
	
	

}

