package shared;

// Look for a Payload class that extends NetProtocol in another file
// That class is a Data Transfer Object with all needed user information
// Not here for clarity

// NetControl to encapsulate all commands in a standard way
// immutable object (only constructor and getter)
public class NetControl extends NetProtocol {
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

   public final NetCommand getNetCommand() {
      return netCommand;
   }

   public Object getPayload() {
      return payload;
   }
}
