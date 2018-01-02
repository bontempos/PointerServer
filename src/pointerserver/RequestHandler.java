package pointerserver;

import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class RequestHandler extends Thread
{
    private Socket socket;
    private int packetCount = 0;

    RequestHandler( Socket socket )
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try {
            System.out.println( "Received a connection" );

            BufferedInputStream input = new BufferedInputStream( socket.getInputStream() );

            byte[] buffer = new byte[1000];
            boolean done = false;
            
            while(!done) {
                int bytesRead = input.read(buffer);
                if (bytesRead < 0) {
                    done = true;
                } else {
                   this.packetCount++;   
                   System.out.println("Read " + bytesRead + " bytes (Packet count: " + this.packetCount + ")" );
                }
            }

            // Close our connection
            input.close();
            socket.close();

            System.out.println( "Connection closed" );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }
}
