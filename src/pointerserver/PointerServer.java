package pointerserver;

import bontempos.Game.Act.Act;
import gui.GUIControl;
import processing.core.PApplet;



public class PointerServer extends PApplet {
	
	Act act;
	SerialControl mc;
	PointerController pc;
	GUIControl gui;
	
	
	public void settings(){
		size(300,300,P2D);
	}

	public void setup() {
		act = new Act(this);
		mc = new SerialControl(this);
		pc = new PointerController(this);
		gui = new GUIControl(this);
	}

	public void draw() {
		background(200);
		
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { pointerserver.PointerServer.class.getName() });
	}
}
