package shared;

// Enumeration defining all possible commands
// toString to easy print the command
public enum NetCommand {
   Acknowledge("Acknowledge Command"),
   Shutdown("Shutdown Command"),
   SendVote("Send Vote Command"),
   NotKnownProtocol("NotKnownProtocol Command"),
   InvalidCPF("Invalid CPF Command"),
   InvalidOption("Invalid Option Command"),
    AlreadyVoted("User Already Voted"),
   VoteReceived("Confirm Vote Receive Command"),
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