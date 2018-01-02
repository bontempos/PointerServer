/*
 *  drives the laser brightness intensity
 */

package device;

import bontempos.Game.Act.Action;
import bontempos.Game.Act.Checker;
import processing.core.PVector;

public class PointerLaserController {
	
	private final PointerDevice parentPointer;
	
	Checker detectLaserChange;
	String detectLaserChangeActionCall;
	
	private float laserMaxBrightness 	  =   100; //percentage, because resolution depends on hardware;
	public float laserIntensity, previousLaserIntensity;
	public boolean allowLaserGradient 		  =   false;

	public PointerLaserController(PointerDevice parentPointer) {
		this.parentPointer = parentPointer;
		detectLaserChangeActionCall = "laserChange"+parentPointer.id;
	}
	
	public void update(){
		// the gradient thing or something else.
	}
	
	public void setLaser(float value) {
		laserIntensity = value;
		Action.perform(detectLaserChangeActionCall);
	}

	public void onLaserChange() {
		if(laserIntensity != previousLaserIntensity){
			previousLaserIntensity = laserIntensity;
			//nextAction(); //when used with moveToDraw, it was calling next call twice, and performing 2 actions at same time
		}
	}
	
	public float setLaserMaxBrigthtness ( float brightness ){
		return this.laserMaxBrightness = brightness;
	}

}
