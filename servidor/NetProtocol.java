package servidor;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

// Enumeration defining all possible commands
// toString to easy print the command
enum NetCommand {
   Acknowledge("Acknowledge Command"),
   Shutdown("Shutdown Command"),
   SendVotingInfo("Send Voting Info Command");

   private final String description;

   private NetCommand(String description) {
      this.description = description;
   }

   @Override
   public String toString() {
      return (this.description);
   }
}

// Look for a Payload class that extends NetProtocol in another file
// That class is a Data Transfer Object with all needed user information
// Not here for clarity

// NetProtocol as base for anything to be sent to the network
// abstract class to begin a hierarchy
abstract class NetProtocol implements Serializable {
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

// NetControl to encapsulate all commands in a standard way
// immutable object (only constructor and getter)
class NetControl extends NetProtocol {
   private static final long serialVersionUID = 1L;
   private final NetCommand netCommand;
   private final Object payload;

   public NetControl(NetCommand netCommand) {
      super();
      this.netCommand = netCommand;
      payload = null;
   }

   public NetControl(NetCommand netCommand, Object payload) {
      super();
      this.netCommand = netCommand;
      this.payload = payload;
   }

   final NetCommand getNetCommand() {
      return netCommand;
   }

   public Object getPayload() {
      return payload;
   }
}
