/*
 * Distribute between pointers devices a drawing task
 * it also converts a canvas position to a selected pointer (corners) position 
 */


package pointerserver;

import java.util.ArrayList;
import bontempos.Game.Act.Action;
import device.PointerDevice;
import processing.core.PVector;

public class DrawManager {

	ArrayList<?> drawingActions; //list of positions and laser intensities - still dont know the object
	ArrayList<Action> pointerActions;//list of commands to be executed by a pointer device //supposed to be a Future class
	
	public DrawManager() {
		
	}

	
	
	
	
	/*
	 * converts a position xy from canvas to a value in pointer projection mask 
	 */
	private PVector transformToPosition( PVector positionInCanvas, PointerDevice pointer, PointerCanvas canvas, ProjectionMask projMask){  //could be a Point2D instead of PVector
		PVector transformed = pointer.toProjectionPlane( positionInCanvas , canvas, projMask);
		return transformed;
	}
	
	
	
	
	/*
	 * converts a code of instructions from Canvas to Pointer Actions.
	 */
	private void convertToPointerAction(){
		for(int i = drawingActions.size()-1; i >= 0; i--){
			Object action = drawingActions.get(i);
			//do the magic, including transformToPosition(position_in_canvas)
			pointerActions.add(new Action("some action like move to"));
			drawingActions.remove(action);
		}
	}
	
	
}
