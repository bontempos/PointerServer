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
		pointers = new ArrayList<PointerDevice>();
	}
	
	
	public PointerDevice addPointer(int i){
		PointerDevice p = new PointerDevice();
		pointers.add(p);
		return p;
	}
	
	
	public static int getPointersSize(){
		return pointers.size();
	}
	
	
	public static void setServosInitAngle( int angle ){
		System.out.println("set angle for all guys: " + angle);
		for(PointerDevice p: pointers){
			p.getServoController().setInitialAngle( angle );
		}
	}
	
	
	public static void setNeedsUpdate(PointerDevice pointerToUpdate) {
		PointerController.needsUpdate.add(pointerToUpdate);
	}
	
}
