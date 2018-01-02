/*
 *  A class which store the state of commands sent to micro controller.
 *  used for setting parameters on micro controller.
 *  Instantiated by SerialControl.
 */
package device;

import pointerserver.PointerController;
import pointerserver.SerialControl;

public class Microcontroller {
	
	//parameters map
	static byte _initialize       = 0;
	static byte _displayMode      = 1;
	static byte _selectedId       = 2;
	static byte _setRangeFrom     = 3;
	static byte _servosToInitPos  = 4;
	static byte _servoInitPos     = 5; //addr
	static byte _enableEcho       = 6; //addr
	static byte _setServoMin      = 7; //addr
	static byte _setServoMax      = 8; //addr (ueses 4 bytes to store)


	//hardware parameters
	public static enum displayMode {PARAMS, SELECTED, RANGE, SETTING};
	public static displayMode SHOWING = displayMode.PARAMS;
	public static byte POINTERS = 8;
	public static boolean ECHO;
	public static byte SERVO_INIT_POS;
	public static int SERVO_MIN;
	public static int SERVO_MAX;
	public static boolean EEPROMreceived = false;
	public static int rangeFrom = 0;
	public static byte selectedId = 0;
	private static SerialControl sc;

	public Microcontroller(SerialControl sc) {
		this.sc = sc;
	}

	
	/*
	 *  INITIALIZE SYSTEM
	 *  reset system millis() and prints eeprom saved data on serial
	 */
	public void initializeSystem(){
		sc.queueCommand ( new byte[]{ (byte)3, (byte)0, (byte)0, (byte)0 } );
	}
	
	
	
	
	/*
	 *  LOADS data stored in EEPROM and updates varaiables
	 */
	public void getDataFromEEPROM( String dataString ){
		//EEPROM255;255;255;255;255;90;0;90;244;1;0;0;255;255;255;255;255;255;255;255;finished
		String[] pieces = dataString.split(";");
		SERVO_INIT_POS = (byte) Integer.parseInt( pieces[ _servoInitPos ] ) ;
		ECHO = ( Integer.parseInt( pieces[ _enableEcho ] ) > 0)? true : false;	
		SERVO_MIN = Integer.parseInt( pieces[ _setServoMin ] ) ; 
		SERVO_MAX = Integer.parseInt( pieces[ _setServoMax +1 ] )*256 + Integer.parseInt( pieces[ _setServoMax ] ) ; 
		System.out.println("SERVO_MAX:" + SERVO_MAX);
		EEPROMreceived = true;
		
	}


	
	
	/*
	 *  DISPLAY MODE 
	 */
	public int getDisplayMode(){
		return SHOWING.ordinal();
	}
	public static void setDisplayMode( int mode ){
		setDisplayMode( displayMode.values()[mode] );
	}
	public static void setDisplayMode( displayMode mode ){
		SHOWING = mode;				
		switch( mode ){
		case PARAMS:
			sc.queueCommand ( new byte[]{ (byte)3, _displayMode, (byte)0, (byte)0 } ); 
			break;
		case SELECTED:
			sc.queueCommand ( new byte[]{ (byte)3, _displayMode, (byte)0, (byte)1 } ); 
			break;
		case RANGE:
			sc.queueCommand ( new byte[]{ (byte)3, _displayMode, (byte)0, (byte)2 } ); 
			break;
		case SETTING:
			break;
		}
	}
	public static byte getSelectedId(){
		return selectedId;
	}
	public static void setSelectedId( byte id){
		byte max = (byte) (POINTERS - 1);
		selectedId = (id > max)? max:id;
		sc.queueCommand ( new byte[]{ (byte)3, _selectedId, (byte)0, selectedId } ); 
	}
	public static int getRangeFrom(){
		return rangeFrom;
	}
	public static void setRangeFrom( int r){
		int max =  POINTERS - 1;
		rangeFrom = (r > max)? max:r;
		sc.queueCommand ( new byte[]{ (byte)3, _setRangeFrom, (byte)0, (byte)rangeFrom } ); 
	}
	
	


	
	/*
	 *  ECHO
	 */
	public boolean getEchoMode(){
		return ECHO;
	}
	public static void setEchoMode(boolean b){
		System.out.println("Echo:" + b);
		sc.queueCommand ( new byte[]{ (byte)3, _enableEcho, (byte)0, (byte)((b)?1:0) } ); 
		ECHO = b;
	}
	public void toggleEchoMode(){
		if(getEchoMode()){
			setEchoMode(false);
		}else{
			setEchoMode(true);
		}
	}


	/**
	 * @return the SERVO_INIT_POS
	 */
	public static byte getSERVO_INIT_POS() {
		return SERVO_INIT_POS;
	}

}
