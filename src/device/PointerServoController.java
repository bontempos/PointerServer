/*
 *  A controller which drives 2 servos and feed them with segmented sub-targets after calculating a trajectory between origin and target defined by the program
 *  Serial is updated by Servo class
 */

package device;

import java.util.ArrayList;

import bontempos.Game.Act.Action;
import bontempos.Game.Act.Checker;
import pointerserver.SerialControl;
import processing.core.PVector;


public class PointerServoController {

	private int targetDistTolerance = 1;

	PointerServo servoX;
	PointerServo servoY;

	boolean invertX = false;
	boolean invertY = false;
	boolean switchXY = false;

	PVector target;

	PointerDevice parentPointer;

	Checker detectMoveComplete;
	String moveCompletedActionCall;

	private boolean onMove					   =   false;
	private boolean enableAccel                =   false;
	private boolean allowRetargeting           =   false;      		     	// if allowRetargeting is true and target changes, trajectory will be recalculated and restarted
	private int moveInterval;												// time servos takes to complete a trajectory
	private long moveStartTime;												// time movement starts
	private boolean onTarget                   =   false;          			// when false, calculates a trajectory path to the target and set moving to true
	private boolean waiting                    =   false;            		// paused by some countdown or timer progress

	ArrayList<PVector> targetList 	   =   new ArrayList<PVector>();//list of (extra/stored) targets to perform
	ArrayList<PVector>trajectoryPoints;		
	int trajectorySteps;


	public PointerServoController(PointerDevice pointer) {
		this.parentPointer = pointer;
		servoX = new PointerServo(this);
		servoY = new PointerServo(this);
		target = new PVector (servoX.getAngle(), servoY.getAngle());  //target is aligned with servos position
		moveCompletedActionCall = "moveCompleted"+parentPointer.id;
		detectMoveComplete = new Checker(moveCompletedActionCall, this, "onMoveComplete"); //name_of_action_to_check, targetClass, classMethod
		detectMoveComplete.setPermanent(true);
	}




	void update(){
		//IMPORTANT. serial is updated only if there is difference between servo angle and its previous angle.
		servoX.update();
		servoY.update();


		if(!overTarget()){ //below can only happen when target and current position are not aligned

			//pointer is on movement or on pre-movement (calculating new trajectory)
			if(!onMove){

				//1 - pre-movement, setting a new trajectory and start moving
				calculateServoTrajectory();
				setOnMove(true);
			}else{

				//2 - pointer has a trajectory to move and its moving.
				if(!trajectoryPoints.isEmpty()){

					//3 - there are targets in a fragmented trajectory to move to
					PVector closerPosition = trajectoryPoints.get( trajectoryPoints.size() - 1 );
					setServosAngle( closerPosition.x , closerPosition.y);
				}else{

					//4 - all targets in a fragmented trajectory was performed but current position and final target are not aligned
					alignToTarget();
				}
			}
		} else {

			//GOAL - no movement, Idle.
			alignToTarget(); //align if not
			if(onMove) setOnMove(false); //dispatches actions for movementCompleted
		}
	}





	/*
	 *  fragments a trajectory from current angle to target angle in X and Y servos and
	 *  creates a list of segments to be executed by each servo
	 *  each movement happens according to the serial update rate
	 *  the number of segments (steps) is also related to the serial update rate
	 */
	private void calculateServoTrajectory() {

		float travelX = Math.abs(target.x - servoX.getAngle()); 
		float travelY = Math.abs(target.y - servoY.getAngle()); 
		int timeX = (int)(travelX * servoX.getAngularSpeed()); 
		int timeY = (int)(travelY * servoY.getAngularSpeed());

		moveInterval = Math.max(timeX, timeY);  //picking up the slower one
		onTarget = false;						//than the movement can be updated

		//how many servoupdates are necessairy to perform this trajectory?
		trajectorySteps =  (int) Math.ceil ( moveInterval/SerialControl.getUpdateRate() ); 

		//TODO using acceleration
		// • using acceleration
		//if (enableAccel) {
			//float[] t = Tween.easeInOut2d( transition, new float[]{ipos.x, ipos.y}, new float[]{target.x-ipos.x, target.y-ipos.y}, 1f);               
			//set(t[0], t[1]);                                                            // <--  THIS LINE SETS THE CURRENT POSITION TO THESE VALUES
		//} 
		// • using uniform movement
		//else {
		//}

		float xFragment =  travelX / trajectorySteps ; 
		float yFragment =  travelY / trajectorySteps ; 

		//directions from current angle to target angle;
		int signalX = (target.x >= servoX.getAngle())? 1 : -1;
		int signalY = (target.y >= servoY.getAngle())? 1 : -1;

		//initial position (current)
		float init_x = servoX.getAngle();
		float init_y = servoY.getAngle();

		for (int i = 0; i < trajectorySteps; i++) {
			init_x = init_x + xFragment * signalX;
			init_y =  init_y + yFragment * signalY;
			trajectoryPoints.add( 0 ,new PVector(Math.round(init_x),Math.round(init_y))  );
		}

		moveStartTime = System.currentTimeMillis();
	}






	public void setOnMove(boolean bool) {
		if (!bool && onMove) {
			Action.perform(moveCompletedActionCall); //by performing this action, yelds a call on onMoveComplete()
		}
		onMove = bool;
	}


	public void onMoveComplete() {
		//nextAction(); //this action dispatches next line in a list if list is not empty. In case of many points to draw.
	}


	public boolean isOnMove(){
		return onMove;
	}


	public void setInvertX( boolean bool){
		invertX = bool;
	}

	public void setInvertY( boolean bool){
		invertY = bool;
	}

	public void setSwitchXY( boolean bool){
		switchXY = bool;
	}


	public void setServosAngle(float x, float y){
		servoX.setAngle( (int) x);
		servoY.setAngle( (int) y);
	}

	public void setAngularSpeed(float speed){
		servoX.setAngularSpeed(speed);
		servoY.setAngularSpeed(speed);
	}

	public void setInitialAngle(int initAngle){
		servoX.setInitialAngle(initAngle);
		servoX.setInitialAngle(initAngle);
	}

	public boolean isInvertX(){
		return invertX;
	}

	public boolean isInvertY(){
		return invertY;
	}

	public boolean isSwitchXY(){
		return switchXY;
	}

	public PVector getPosition(){
		return new PVector( servoX.getAngle(), servoY.getAngle() );
	}

	public PVector getPositionNorm(){
		return new PVector( servoX.getAngleNorm(), servoY.getAngleNorm() );
	}

	private boolean overTarget(){
		PVector targetXY = new PVector( (int)target.x, (int)target.y ); //not sure if rounding (int) is needed here
		return targetXY.dist( new PVector( servoX.getAngle(), servoY.getAngle() )) < targetDistTolerance;
	}

	private void alignToTarget(){
		servoX.setAngle( (int) target.x);
		servoY.setAngle( (int) target.y);
	}


}
