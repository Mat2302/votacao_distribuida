package shared;

// Enumeration defining all possible commands
// toString to easy print the command
public enum NetCommand {
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