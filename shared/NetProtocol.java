package shared;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

// NetProtocol as base for anything to be sent to the network
// abstract class to begin a hierarchy
public abstract class NetProtocol implements Serializable {
   private static final long serialVersionUID = 1L;
   public static final String serverAddress = "localhost";
   public static int port;
   private static final Scanner scanner = new Scanner(System.in);

   public static void setPort() {
      int tempPort = 0;
      while (tempPort < 1024 || tempPort > 65535) {
         System.out.print("Informe a porta do servidor (pressione Enter para usar a porta padrão 1234): ");
         String line = scanner.nextLine().trim();
         if (line.isEmpty()) {
            tempPort = 1234; // default port
            break;
         }
         try {
            tempPort = Integer.parseInt(line);
         } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, insira um número de porta válido entre 1024 e 65535.");
         }
      }

      port = tempPort;
   }

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