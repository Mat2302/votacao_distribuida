package shared;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

// NetProtocol as base for anything to be sent to the network
// abstract class to begin a hierarchy
public abstract class NetProtocol implements Serializable {
   private static final long serialVersionUID = 1L;
   public static final String serverAddress = "localhost";
   public static final int port = 1234;

   // a convenient service method for this project that does not fit on other
   // classes
   public static String getLocalIpAddress() {
      String buffer = "<<IP Address>>";
      try {
         buffer = InetAddress.getLocalHost().getHostName() + " / " + InetAddress.getLocalHost().getHostAddress();
      } catch (UnknownHostException exceptionValue) {
         System.err.println("Error looking for local IP address: " + exceptionValue.getMessage());
      }
      return (buffer);
   }
}