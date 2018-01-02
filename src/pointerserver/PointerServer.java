package pointerserver;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import bontempos.Game.Act.Act;
import device.Microcontroller;
import gui.GUIControl;
import processing.core.PApplet;
import processing.serial.Serial;

public class PointerServer extends PApplet {
	
	Act act;
	public SerialControl sc;
	PointerController pc;
	GUIControl gui;


	
	public static void main(String _args[]) {
		PApplet.main(new String[] { pointerserver.PointerServer.class.getName() });
	}
	
	public void settings(){
		size(600,300,P2D);
	}

	public void setup() {
		act = new Act(this);
		pc = new PointerController(this);
		gui = new GUIControl(this);
		sc = new SerialControl(this);
	}

	public void draw() {
		background(180);
	}
	
	public void serialEvent( Serial port ){
		sc.serialRead();
	}
	
	//for debug:
	public void mouseClicked(){
		
	}
}
