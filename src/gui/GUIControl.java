package gui;

import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.RadioButton;
import controlP5.Textarea;
import controlP5.Textfield;
import controlP5.Toggle;
import device.Microcontroller;
import pointerserver.SerialControl;
import processing.core.PApplet;

public class GUIControl {

	protected PApplet parent;
	public static ControlP5 gui;

	//DESIGN FIELDS
	protected int marginOut			= 10;
	protected int marginIn			= 5;
	protected int module			= 20;
	protected int lineWeight		= 2;	
	protected int BGcolor			= gui.NAVY;

	public GUIControl(PApplet parent) {
		this.parent = parent;
		gui = new ControlP5(parent);
		buildGui();
	}

	protected ControlP5 buildGui(){
		int x = 0;
		gui.addFrameRate(); //debug



		/*
		 * DISPLAY MODE AT MICROCONTROLLER
		 */
		
		Toggle TG_Param = gui.addToggle("TG_Param").setLabel("PARAMS").setId(0).setState(true);
		Toggle TG_Selected = gui.addToggle("TG_Selected").setLabel("SELECTED").setId(1);
		Toggle TG_Range = gui.addToggle("TG_Range").setLabel("RANGE").setId(2);

		
		CallbackListener _keepEnabled = new CallbackListener() { 
			public void controlEvent(CallbackEvent theEvent) {
				System.out.println(theEvent);
				Toggle t = gui.get(Toggle.class, theEvent.getController().getName());
				boolean b = t.getState();
				if(!b) t.setValue(true);
			}
		};
		
		CallbackListener _setDisplayMode = new CallbackListener() { 
			public void controlEvent(CallbackEvent theEvent) {
				Toggle t = gui.get(Toggle.class, theEvent.getController().getName());
				Microcontroller.setDisplayMode( t.getId() );
				Textfield tx = gui.get(Textfield.class, "displayModeText");
				if(t.getId() == 0){
					tx.hide();
				}else{
					tx.show();
					if(t.getId() == 1){
						tx.setLabel("Selected id");
						tx.setText(String.valueOf(Microcontroller.getSelectedId()));
					}else{
						tx.setLabel("Range from");
						tx.setText(String.valueOf(Microcontroller.getRangeFrom()));
					}
				}
			}
		};
		
		gui.addRadioButton("displayModeController")
		.setPosition(marginOut,marginOut)
		.setSize(module,  module)
		.addItem(TG_Param.onPress(_keepEnabled).onClick(_setDisplayMode), 0)
		.addItem(TG_Selected.onPress(_keepEnabled).onClick(_setDisplayMode),0)
		.addItem(TG_Range.onPress(_keepEnabled).onClick(_setDisplayMode),0)
		;
		
		x = 0;
		for(Toggle t:gui.get(RadioButton.class, "displayModeController").getItems()) {
			t.setPosition(module*2*x + marginIn*x,0).setSize(module*2, module).getCaptionLabel().align(3,3);
			x++;
		}
		
		x = 4;
		gui.addTextfield("displayModeText")
		.setPosition(module*2*x - marginIn*3,marginOut)
		.setText(  "0"  )
		.setSize(module, module)
		.plugTo(this)
		.setAutoClear(false)
		.hide()
		.onDoublePress(
				new CallbackListener() { 
					public void controlEvent(CallbackEvent theEvent) {
						gui.get(Textfield.class,"displayModeText").clear();
					}
				})
		.onChange(
				new CallbackListener() { 
					public void controlEvent(CallbackEvent theEvent) {
						int id = Integer.parseInt(gui.get(Textfield.class,"displayModeText").getStringValue());
						if(TG_Selected.getState()){
							Microcontroller.setSelectedId(  (byte)id  );
						}
						if(TG_Range.getState()){
							Microcontroller.setRangeFrom( id  );
						}
					}
				})
		
		.getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER).setPaddingX(5);
	

		
		
		

		/*
		 * STREAM
		 */
		Group GP_stream = gui.addGroup("GP_stream").setPosition(marginOut,marginOut).hideBar();
		x = 10;
		gui.addTextfield("updateRate")
		.setLabel("Stream Update Rate")
		.setPosition(module*2*x + marginIn*x,0)
		.setText(  String.valueOf( SerialControl.getUpdateRate())   )
		.setSize(module*2, module)
		.plugTo(this).moveTo(GP_stream)
		.setAutoClear(false)
		.onDoublePress(
				new CallbackListener() { 
					public void controlEvent(CallbackEvent theEvent) {
						gui.get(Textfield.class,"updateRate").setText("");
					}
				})
		.onChange(
				new CallbackListener() { 
					public void controlEvent(CallbackEvent theEvent) {
						gui.get(Textfield.class,"updateRate").setText( String.valueOf( SerialControl.getUpdateRate()) );
					}
				})
		
		.getCaptionLabel().align(ControlP5.LEFT_OUTSIDE, ControlP5.CENTER).setPaddingX(5);
		x++;
		gui.addToggle("serialStream").setBroadcast(false).setLabel("Stream").setPosition(module*2*x + marginIn*x,0).setSize(module*2, module).plugTo(this).moveTo(GP_stream).setBroadcast(true).getCaptionLabel().align(3,3);
		x++;
		gui.addToggle("echoInput").setBroadcast(false).setLabel("Echo In").setPosition(module*2*x + marginIn*x,0).setSize(module*2, module).plugTo(this).moveTo(GP_stream).setBroadcast(true).getCaptionLabel().align(3,3);



		/*
		 * CONSOLES
		 */
		Group GP_consoles = gui.addGroup("GP_consoles").setPosition(marginOut, marginOut + module + marginIn).hideBar();
		gui.addTextarea("consoleIn")
		.setFont(parent.createFont("arial",12))
		.setSize((parent.width/2) - marginOut*2, parent.height - module - marginIn - marginOut*2)
		.setPosition(0, 0)
		.setColorBackground(100)
		.moveTo(GP_consoles);
		gui.addTextarea("consoleOut")
		.setFont(parent.createFont("arial",12))
		.setSize((parent.width/2) - marginOut*2, parent.height - module - marginIn - marginOut*2)
		.setPosition( (parent.width/2) , 0)
		.setColorBackground(100)
		.moveTo(GP_consoles);
		return gui;
	}

	public static void setToggle(String name, Boolean value){
		gui.get(Toggle.class, name).setValue(value);
	}

	public void updateRate(String s){
		SerialControl.setUpdateRate( Integer.parseInt(s) );
	}

	public void serialStream(boolean sd){
		if(sd){
			SerialControl.startStreamingSerial();
		}else{
			System.out.println("paratudo");
			SerialControl.stopStreamingSerial();
		}
	}

	public void echoInput(boolean b){
		Microcontroller.setEchoMode(b);
	}

	public static Textarea getConsole(String theConsole){
		return gui.get(Textarea.class, theConsole);
	}

	public static Textarea  updateConsole(String theConsole, String theText){
		getConsole(theConsole).setText(theText);
		return getConsole(theConsole);
	}
}
