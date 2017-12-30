package pointerserver;

import bontempos.Game.Act.Action;
import bontempos.Game.Act.Countdown;
import gui.GUIControl;
import processing.core.PApplet;
import processing.serial.Serial;

public class SerialControl {

	private PApplet parent;
	private static int baudRate 				= 	38400; //230400;
	private static int updateRate				= 	1000; 
	private static boolean printToConsole		=	true;
	
	private static String serialInput 			= 	null;
	private static String cmdOutput				= 	"";
	private static float[] sentCommandsSpan		= 	new float[2];
	private static long sentCommands;
	private static Serial serial;
	private String arduinoReply					= 	null;
	Countdown servoUpdateTimer;

	
	//TODO create interface to change parameters inside arduino
	
	boolean setSerialEcho = true;
	int activePointers = 8;
	byte[] setPWMboardsAddress = new byte[3];
	int servoInitPos = 90;
	int setBaudRate;
	
	
	public SerialControl(PApplet parent) {
		this.parent = parent;
		serialInit( Serial.list()[1], baudRate);
	}

	
	
	public void serialInit(String serialAddress, int baudRate) {
		serial = new Serial(parent, serialAddress, baudRate );
		Action servoUpdateAuto = new Action( this, "executeServosTrajectories");
		servoUpdateAuto.setEcho(false);
		servoUpdateTimer = new Countdown(updateRate,servoUpdateAuto); 
		servoUpdateTimer.setRepeat(true);
		servoUpdateTimer.start();
	}
	
	
	
	
	public void executeServosTrajectories(){
		int testX = (int) parent.random(180);
		int testY = (int) parent.random(180);
		//will execute update for all pointers if their trajectory arraylists have any trajectory fragment to perform.
		sendCommands( new byte[]{ 4,0,1, (byte)testX,(byte)testX,1,4,(byte)testX,(byte)testX } );
	}
	
	

	/*
	 *  sending an array of bytes to serial
	 */
	private void sendCommands(byte[] serialPackage){
		
		for(int i = 0; i < serialPackage.length; i++){
			//(first byte is the byteSize, than for each pointer: index, laser, servoX, servoY)
			if(printToConsole){
				if(i == 0){
					sentCommandsSpan[0] = parent.millis();
				}else if(i == serialPackage.length - 1){
					sentCommandsSpan[1] = parent.millis();
				}
				cmdOutput += "------>> ("+ parent.millis() +" : "+i+") serial package:" + serialPackage[i] + "\n";
			}
			serial.write(serialPackage[i]);
		}
		if(printToConsole){
			cmdOutput += "[" +sentCommands + "] sent in "+ ((sentCommandsSpan[1] - sentCommandsSpan[0])/1000) +" seconds. \n";
			GUIControl.updateConsole( cmdOutput );
			System.out.print(cmdOutput);
			cmdOutput = "";
		}
		sentCommands++ ;
		
		
		
	}



}
