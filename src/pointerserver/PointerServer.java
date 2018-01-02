package pointerserver;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import bontempos.Game.Act.Act;
import device.Microcontroller;
import gui.GUIControl;
import processing.serial.Serial;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Timer;
import java.util.TimerTask;

import pointerserver.RequestHandler;

public class PointerServer {
	
	// Act act;
	public SerialControl sc;
PointerController pc;
	GUIControl gui;


    private ServerSocket server;
    private int port;

    public PointerServer(int port) throws Exception {
        this.port = port;
        this.server = new ServerSocket(port);
    }

    private void listen() throws Exception {
            System.out.println("Listening on " + this.port);
            while (true) {
                Socket client = this.server.accept();
                String clientAddress = client.getInetAddress().getHostAddress();
                System.out.println("\r\nNew connection from " + clientAddress);
                RequestHandler requestHandler = new RequestHandler(client);
                requestHandler.start();
            }
    }

	
	public static void main(String _args[]) {
        try {
            System.out.println("Starting Pointer Controller");
            TimerTask pointerUpdateTask = new PointerController();
            Timer timer = new Timer(true);
            timer.scheduleAtFixedRate(pointerUpdateTask, 0, 1000);
            System.out.println("Starting Pointer Server");
            PointerServer app = new PointerServer(9540);
            app.listen();
        }
        catch (Exception e) {
            System.out.println("Server exception! " + e);
        }
	}
	
	public void setup() {
		//act = new Act(this);
		pc = new PointerController();
		sc = new SerialControl();
	}

	public void serialEvent( Serial port ){
		sc.serialRead();
	}
	
	//for debug:
	public void mouseClicked(){
		
	}
}
