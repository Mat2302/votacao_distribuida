package client.view;

import shared.CPFValidator;
import java.awt.*;
import javax.swing.*;

import client.VoteClient;

public class CpfPage extends JFrame {
    private final JTextField cpfField;
    private final JTextField ipField;
    private final JLabel errorLabel;

    public CpfPage() {
        setTitle("Identificação do Eleitor");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Insira seu CPF:", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        ipField = new JTextField();
        centerPanel.add(new JLabel("IP: ", SwingConstants.CENTER));
        centerPanel.add(ipField);

        cpfField = new JTextField();
        centerPanel.add(new JLabel("CPF: ", SwingConstants.CENTER));
        centerPanel.add(cpfField);

        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setSize(getPreferredSize());
        errorLabel.setForeground(Color.RED);
        centerPanel.add(errorLabel);

        JButton sendButton = new JButton("Prosseguir");
        sendButton.addActionListener(e -> {
            VoteClient vc = validateIp();
            
            if (vc == null){
                errorLabel.setText("IP inválido. Tente novamente.");
                return;
            }

            validateCPF(vc);
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(sendButton);

        content.add(title, BorderLayout.NORTH);
        content.add(centerPanel, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);

        add(content);
    }

    private void validateCPF(VoteClient voteClient) {
        String cpf = cpfField.getText().trim();
        if (cpf.isEmpty()) {
            errorLabel.setText("Digite um CPF.");
        }

        if (CPFValidator.validate(cpf)) {
            try {
                if (voteClient.getVotingInfo() != null) {
                    new VotePage(cpf, voteClient).setVisible(true);
                    dispose();
                } else {
                    errorLabel.setText("Erro: Não foi possível obter informações de votação.");
                }
            } catch (Exception e) {
                errorLabel.setText("Erro ao conectar ao servidor de votação.");
                System.out.println(e.getMessage());
            }
        } else {
            errorLabel.setText("CPF inválido. Tente novamente.");
        }
    }

    private VoteClient validateIp() {
        String ip = ipField.getText().trim();

        if (ip.isEmpty()) {
            return null;
        }

        VoteClient voteClient;
        try {
            voteClient = new VoteClient(ip);
            voteClient.listenAsync();
            return voteClient;
        } catch (Exception e) {
            errorLabel.setText("Não foi possível conectar ao servidor.");
            return null;
        }
    }

}
