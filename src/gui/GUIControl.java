package gui;

import controlP5.ControlP5;
import controlP5.Textarea;
import processing.core.PApplet;

public class GUIControl {
	
	protected PApplet parent;
	protected static ControlP5 gui;
	
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
		gui.addFrameRate();
		gui.addTextarea("console")
			.setFont(parent.createFont("arial",12))
			.setSize(parent.width - marginOut*2, parent.height - module - marginOut*2)
			.setPosition(marginOut, marginOut + module)
			.setColorBackground(100);
		return gui;
	}
	
	
	public static Textarea getConsole(){
		return gui.get(Textarea.class, "console");
	}
	
	public static Textarea updateConsole(String theText){
		getConsole().setText(theText);
		return getConsole();
	}
}
