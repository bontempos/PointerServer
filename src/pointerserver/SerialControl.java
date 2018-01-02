package pointerserver;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import bontempos.Game.Act.Action;
import bontempos.Game.Act.Countdown;
import device.Microcontroller;
import gui.GUIControl;
import processing.core.PApplet;
import processing.serial.Serial;

public class SerialControl {

	private PApplet parent;
	protected Microcontroller mc;
	private static int baudRate 				= 	9600; // 9600 38400 115200 230400;
	private static int updateRate				= 	20; 
	private static int minUpdateRate			= 	10;
	private static boolean printToConsole		=	true;
	private static String serialOutput				= 	"";
	private static String serialInput				=   "";
	private static float[] sentCommandsSpan		= 	new float[2];
	private static long sentCommands;
	private static Serial serial;
	//private static String serialAddress;
	private boolean initialized 				= 	false;        // Whether we've heard from the microcontroller
	private static boolean autoStream			= 	true;		  // program starts streaming as soon as possible. turns off is stream is set to false;
	private static boolean streamingSerial		=   false;		  // true when servoUpdateTimer sends data at regular rate;
	private String serialReply					= 	null;
	private byte pointersPackage[] = null;		//built by pointerController
	private ArrayList<byte[]> queue;			//commands queued by external function
	static Countdown servoUpdateTimer;


	//TODO create interface to change parameters inside arduino

	boolean setSerialEcho = true;
	int activePointers = 8;
	byte[] setPWMboardsAddress = new byte[3];
	int servoInitPos = 90;
	int setBaudRate;




	public SerialControl() {
		//parent.registerMethod("pre", this);
		mc = new Microcontroller(this);
		queue = new ArrayList<byte[]>();
		serialInit( Serial.list()[1], baudRate);
	}



	public void serialInit(String serialAddress, int baudRate) {
		//this.serialAddress = serialAddress;
		serial = new Serial(parent, serialAddress, baudRate );
		Action servoUpdateAuto = new Action( this, "executePointerCommands");
		servoUpdateAuto.setEcho(false);
		servoUpdateTimer = new Countdown(updateRate,servoUpdateAuto); 
		servoUpdateTimer.setRepeat(true);

	}


	public void initializeSystem(){
		System.out.println("initializeSystem serial");
		initialized = true; 
		sendInitializeCommand();
		Microcontroller.setDisplayMode( 0 ); //start in PARAMS
	}

	public static void startStreamingSerial(){
		streamingSerial = true;
		servoUpdateTimer.start();	
	}

	public static void stopStreamingSerial(){
		autoStream = false;
		streamingSerial = false;
		servoUpdateTimer.stop();
	}



	public void pre(){ //infinite loop


		//happens until system is initialized
		if(!isInitialized()) {
			initializeSystem();
		}

		//autoStream turns off once stream is set to false;
		if(autoStream){
			if(isInitialized() && mc.EEPROMreceived && !isStreamingSerial() ){
				System.out.println("vai lista!");
				GUIControl.setToggle("serialStream", true);
			}
		}

	}



	public void sendInitializeCommand(){
		sendCommands ( new byte[]{ (byte)3, (byte)0, (byte)0, (byte)0 } );
	}


	int testX = 102;

	public void executePointerCommands(){
		//will execute update for all pointers if their trajectory arraylists have any trajectory fragment to perform.

		ArrayList<byte[]> bytePackage = new ArrayList<byte[]>();


		//overwriting pointersPackage for debuging only 
		byte pointersPackage[] =
				new byte[]{ (byte)32,(byte)0,(byte)1, (byte)testRand(),(byte)testRand(),
						(byte)1,(byte)1, (byte)testRand(),(byte)testRand(),
						(byte)2,(byte)1, (byte)testRand(),(byte)testRand(),
						(byte)3,(byte)1, (byte)testRand(),(byte)testRand(),
						(byte)4,(byte)1, (byte)testRand(),(byte)testRand(),
						(byte)5,(byte)1, (byte)testRand(),(byte)testRand(),
						(byte)6,(byte)1, (byte)testRand(),(byte)testRand(),
						(byte)7,(byte)1, (byte)testRand(),(byte)testRand()};


		if(pointersPackage != null)		bytePackage.add(pointersPackage);

		//append commands on a list
		if(!queue.isEmpty()){
			bytePackage.addAll(queue);
			queue.clear();
		}

		//converting ArrayList<byte[]> to byte[];
		int len = 0;
		for ( byte[] p: bytePackage ){
			len += p.length;
		}
		int c = 0;
		byte[] packsFinal = new byte[len];
		for( int i = 0; i < bytePackage.size() ; i++){
			byte[] pack = bytePackage.get(i);
			for(int j = 0; j < pack.length; j++){
				packsFinal[c] = pack[j];
				c++;
			}
		}

		sendCommands(  packsFinal );
	}




	public void queueCommand(byte[] serialPackage){
		queue.add( serialPackage );
	}


    int testRand (){
        //return (int) parent.constrain(  (parent.random(1f)>.5)?testX+10:testX-10, 0,180 );
        return 1;
    }

	/*
	 *  SEND an array of bytes to serial
	 */
	private void sendCommands(byte[] serialPackage){

		for(int i = 0; i < serialPackage.length; i++){
			//(first byte is the byteSize, than for each pointer: index, laser, servoX, servoY)
			if(printToConsole){
				if(i == 0){
					//sentCommandsSpan[0] = parent.millis();
				}else if(i == serialPackage.length - 1){
					//sentCommandsSpan[1] = parent.millis();
				}
				//serialOutput += "OUT>> ("+ millisToSec( parent.millis() ) +" : "+i+") serial package:" + serialPackage[i] + "\n";
			}
			serial.write(serialPackage[i]);
		}
		if(printToConsole){
			serialOutput += "[" +sentCommands + "] sent in "+ ((sentCommandsSpan[1] - sentCommandsSpan[0])/1000) +" seconds. \n";
			GUIControl.updateConsole( "consoleOut", serialOutput );
			//System.out.print(serialOutput);
			serialOutput = "";
		}
		sentCommands++ ;
	}




	/*
	 *  READ  microcontroller prints something on serial
	 */
	public void serialRead() {
		if ( serial.available() > 0) {  // If data is available,
			serialReply = serial.readStringUntil('\n');
			if (serialReply != null) {
				if(serialReply.startsWith("EEPROM")){
					mc.getDataFromEEPROM( serialReply );
				}
				//System.out.print( " >>IN " + parent.millis() + ":" + serialReply);
				//serialInput +=  " >>IN " + millisToSec( parent.millis() ) + ":" + serialReply;
				if(printToConsole){
					GUIControl.updateConsole( "consoleIn", serialInput );
				}
			}			
		}
	}


	 String millisToSec (int millis){
		return String.format("%d:%d\"", 
				TimeUnit.MILLISECONDS.toMinutes(millis),
				TimeUnit.MILLISECONDS.toSeconds(millis) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
				);
	}

	 

	public boolean isInitialized() {
		return initialized;
	}
	public static boolean isStreamingSerial(){
		return streamingSerial;
	}

	public static int  getUpdateRate(){
		return updateRate;
	}
	public static int  setUpdateRate(int rate){
		updateRate = Math.max(minUpdateRate,rate);
		servoUpdateTimer.setInterval(updateRate);
		return updateRate;
	}
}
