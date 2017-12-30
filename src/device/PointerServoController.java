package device;

import com.sun.javafx.geom.Vec2d;


public class PointerServoController {

int targetDistTolerance = 1;
	
	PointerServo servoX;
	PointerServo servoY;
	
	boolean invertX = false;
	boolean invertY = false;
	boolean switchXY = false;
	
	Vec2d pos, ppos, target;
	PointerDevice pointer;
	

	public PointerServoController(PointerDevice pointer) {
		this.pointer = pointer;
		servoX = new PointerServo(pointer);
		servoY = new PointerServo(pointer);
		pos = ppos = target = new Vec2d();
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
	
	public Vec2d getPosition(){
		return new Vec2d( servoX.getAngle(), servoY.getAngle() );
	}
	
	public Vec2d getPositionNorm(){
		return new Vec2d( servoX.getAngleNorm(), servoY.getAngleNorm() );
	}
	

	private boolean overTarget(){
		Vec2d targetXY = new Vec2d( (int)target.x, (int)target.y );
		return targetXY.distance( new Vec2d( (int)pos.x, (int)pos.y )) < targetDistTolerance;
	}
	
	private void alignToTarget(){
		pos.x = target.x;
		pos.y = target.y;
	}
	
	public void update(){
		if(!overTarget()){
		
		}
	}

}
