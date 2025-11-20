package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import shared.VoteOption;
import shared.VotingInfoPayload;

public class Reports {
    public static void votersReport(HashMap<String, Integer> votes, VotingInfoPayload votingPayload) {

        File file = new File("voters_report.txt");

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {

            // Cabeçalho do relatório
            writer.println("===== RELATÓRIO DE VOTANTES =====");
            writer.println("Título: " + votingPayload.getTitle());
            writer.println("Descrição: " + votingPayload.getDescription());
            writer.println("Pergunta: " + votingPayload.getQuestion());
            writer.println("=================================");
            writer.println();
            writer.println("Lista de CPFs que votaram:");
            writer.println("----------------------------------");

            for (String cpf : votes.keySet()) {
                writer.println(cpf);
            }

            System.out.println("Relatório gerado: " + file.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Erro ao gerar relatório de votantes: " + e.getMessage());
        }
    }

    public static void optionsReport(HashMap<String, Integer> votes, VotingInfoPayload votingPayload) {

        File file = new File("options_report.txt");

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {

            // Cabeçalho
            writer.println("===== RELATÓRIO DE OPÇÕES =====");
            writer.println("Título: " + votingPayload.getTitle());
            writer.println("Descrição: " + votingPayload.getDescription());
            writer.println("Pergunta: " + votingPayload.getQuestion());
            writer.println("================================");
            writer.println();
            writer.println("Votos por opção:");
            writer.println("--------------------------------");

            // Conta votos
            HashMap<Integer, Integer> countMap = new HashMap<>();

            for (Integer optId : votes.values()) {
                countMap.put(optId, countMap.getOrDefault(optId, 0) + 1);
            }

            // Lista opções na ordem original
            for (VoteOption option : votingPayload.getOptions()) {
                int optionId = option.getId();
                int count = countMap.getOrDefault(optionId, 0);

                writer.println(option.getDescription() + ": " + count + " voto(s)");
            }

            System.out.println("Relatório gerado: " + file.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Erro ao gerar relatório de opções: " + e.getMessage());
        }
    }
}
