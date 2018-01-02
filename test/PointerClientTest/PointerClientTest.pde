import java.net.Socket;
import java.net.InetAddress;

Socket socket;

void setup() {
  System.out.println("Hi");
  try {
    socket = new Socket(InetAddress.getLoopbackAddress(), 9540);
  }
  
  catch(Exception e) {
    System.out.println("Error connecting to server!" + e);
  }
  
  
}