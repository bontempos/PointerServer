package device;

import pointerserver.PointerController;

public class PointerServo {

	float angularSpeed = 2;
	int initialAngle = 90;
	int[] range = {0,180};
	
	int angle;
	int previousAngle;

	PointerServoController controller;

	public PointerServo( PointerServoController controller) {
		this.controller = controller;
	    angle = initialAngle;
		 
	}
	
	public void setAngularSpeed( float speed ){
		this.angularSpeed = speed;
	}
	
	public void setInitialAngle(int initAngle){
		this.initialAngle = initAngle;
	}
	
	public void setRange( int min, int max){
		range = new int[]{min, max};
	}
	
	public void setAngle(int angle){
		this.angle = angle;
	}
	
	public void setAngleNorm(int normalizedAngle){
		this.angle = range[0] + normalizedAngle * (range[1] - range[0]) ;
	}

	public void setPreviousAngle(int previousAngle){
		this.previousAngle = previousAngle;
	}

	/**
	 * @return the angularSpeed
	 */
	public float getAngularSpeed() {
		return angularSpeed;
	}

	public int getAngle(){
		return angle;
	}
	
	public float getAngleNorm(){
		return normalize(angle, range[0], range[1]);
	}

	public int getPreviousAngle() {
		return previousAngle;
	}
	
	public double getPreviousAngleNorm() {
		return normalize(previousAngle, range[0], range[1]);
	}

	
	public float normalize(int value, int min, int max) {
		return (value - min) / (max - min);
	}
	
	public void update(){
		if(angle != previousAngle){
			PointerController.setNeedsUpdate(controller.parentPointer);
			controller.setOnMove(true);
		}else{
			if(controller.isOnMove())controller.setOnMove(false);
		}
		previousAngle = angle;
	}

}
