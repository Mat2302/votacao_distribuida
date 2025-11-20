package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import shared.NetProtocol;
import shared.VoteOption;
import shared.VotingInfoPayload;

public class Server {
    public static void main(String[] args) {
        try {
            HashMap<String, Integer> votes = new HashMap<>();

            System.out.println("Server starting: " + NetProtocol.getLocalIpAddress() + " @ " + NetProtocol.port);

            ServerController controller = new ServerController(votes);
            VotingInfoPayload votingPayload = setupVotation();

            String[] chartLabels = votingPayload.getOptions()
                    .stream()
                    .map(option -> option.getDescription())
                    .toArray(String[]::new);

            int[] chartValues = votingPayload.getOptions()
                    .stream()
                    .mapToInt(option -> 0)
                    .toArray();

            BarChartWindow[] holder = new BarChartWindow[1];
            SwingUtilities.invokeAndWait(() -> {
                holder[0] = new BarChartWindow(chartValues, chartLabels, votingPayload.getTitle(),
                        votingPayload.getDescription(), votingPayload.getQuestion(), () -> controller.stop());
            });

            BarChartWindow chart = holder[0];

            controller.start(votingPayload, chart);
            System.out.println("Server is shutting down now.");

            System.out.println("Generating reports...");
            
            Reports.optionsReport(votes, votingPayload);
            Reports.votersReport(votes, votingPayload);

            System.exit(0);

        } catch (Exception e) {
            System.err.println("Unexpected exception: " + e.getMessage());
        }
    }

    private static VotingInfoPayload setupVotation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Qual é o titulo da votação?");
        String title = scanner.nextLine();
        System.out.println("Qual é o objetivo da votação?");
        String description = scanner.nextLine();
        System.out.println("Qual é a pergunta da votação?");
        String question = scanner.nextLine();
        ArrayList<VoteOption> options = new ArrayList<>();
        String option;
        System.out.println("Informe as opções de voto do usuário:");
        do {
            option = scanner.nextLine();
            if (option.length() > 0) {
                int id = options.size();
                options.add(new VoteOption(id, option));
            }
        } while (option.length() != 0);
        scanner.close();

        return new VotingInfoPayload("Pacote com os dados de votação.", 0, question, options, title, description);
    }
}

class ServerController {

    private final ExecutorService clientPool = Executors.newCachedThreadPool();

    private volatile boolean running = true;

    private HashMap<String, Integer> votes;

    ServerController(HashMap<String, Integer> votes) {
        this.votes = votes;
    }

    public void stop() {
        System.out.println("Encerrando votação por comando do botão...");
        running = false;
        try {
            // Abrir uma conexão dummy para destravar o accept()
            new Socket("localhost", NetProtocol.port).close();
        } catch (IOException ignored) {
        }
    }

    boolean isRunning() {
        return running;
    }

    void start(VotingInfoPayload votingPayload, BarChartWindow chart) {
        try (ServerSocket serverSocket = new ServerSocket(NetProtocol.port)) {
            System.out.println("Server listening on port " + NetProtocol.port);

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Accepted connection from " + clientSocket.getRemoteSocketAddress());

                    clientPool.submit(new ClientHandler(clientSocket, votingPayload, votes, chart));
                } catch (IOException ioe) {
                    if (running)
                        System.err.println("Error accepting client connection: " + ioe.getMessage());
                    else
                        System.out.println("Server stopped accepting connections.");
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + NetProtocol.port + ". " + e.getMessage());
        } finally {
            shutdownAndAwaitTermination();
        }
    }

    private void shutdownAndAwaitTermination() {
        clientPool.shutdown();
        try {
            if (!clientPool.awaitTermination(10, TimeUnit.SECONDS)) {
                clientPool.shutdownNow();
                if (!clientPool.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.err.println("Client pool did not terminate.");
                }
            }
        } catch (InterruptedException ie) {
            clientPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}