package pointerserver;

import java.util.ArrayList;

import device.PointerDevice;
import processing.core.PApplet;

public class PointerController {
	
	PApplet parent;
	protected static ArrayList<PointerDevice> pointers;
	protected static ArrayList<PointerDevice> needsUpdate;
	
	public PointerController(PApplet parent) {
		this.parent = parent;
	}
	
	public PointerDevice addPointer(int i){
		PointerDevice p = new PointerDevice();
		pointers.add(p);
		return p;
	}

	public static void setNeedsUpdate(PointerDevice pointerToUpdate) {
		PointerController.needsUpdate.add(pointerToUpdate);
	}
	
}
