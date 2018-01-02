package device;


import pointerserver.PointerCanvas;
import pointerserver.ProjectionMask;
import processing.core.PVector;

public class PointerDevice {
	
	
	private final PointerServoController servoController;
	private final PointerLaserController laserController;
	private final PointerProjectionPlane projPlane;
	private boolean active   =   true;
	public int id;

	
	public PointerDevice() {
		 id = pointerserver.PointerController.getPointersSize();
		 servoController = new PointerServoController(this);
		 laserController = new PointerLaserController(this);
		 projPlane = new PointerProjectionPlane(this);
	}
	
	
	public void update(){
		servoController.update();
		laserController.update();
	}
	
	
	/*
	*  gets the canvas position of a point in the projection mask 
	*/
	public PVector toProjectionPlane( PVector positionInCanvas , PointerCanvas canvas, ProjectionMask projMask  ){
		return projPlane.toHomography(positionInCanvas, canvas, projMask);  
	}


	/**
	 * @return the servoController
	 */
	public PointerServoController getServoController() {
		return servoController;
	}
	
	
	
	
	
}





//EVENTS AND LISTNERS
//public ActionList actionList; 
//public String evalNext ; // this replaces the "evalNext" for loading next action in a list of actions. This string must includ pointer id.