import java.net.Socket;
import java.net.InetAddress;

Socket socket;
OutputStream out = null;
byte[] bytes = new byte[500];
int packetCount = 0;

void setup() {
  System.out.println("Hi");
  try {
    socket = new Socket(InetAddress.getLoopbackAddress(), 9540);
    out = socket.getOutputStream();
  }
  
  catch(Exception e) {
    System.out.println("Error connecting to server!" + e);
  }
}

void draw() {
  try {
    if (out != null) {
      out.write(bytes, 0, 500);
      packetCount++;
      System.out.println("Sent " + packetCount + " packets");
    }
  }
  catch(Exception e) {
    System.out.println("Error sending data!" + e);
  }
  
}