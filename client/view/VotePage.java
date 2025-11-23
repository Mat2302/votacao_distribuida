package client.view;

import java.awt.*;
import javax.swing.*;

import client.VoteClient;
import shared.VoteOption;
import shared.Voter;
import shared.VotingInfoPayload;

public class VotePage extends JFrame {
    private ButtonGroup group;
    private VotingInfoPayload votingInfo;
    private VoteClient voteClient;
    private Voter voter;

    public VotePage(String cpf, VoteClient voteClient) {
        this.votingInfo = voteClient.getVotingInfo();
        this.voteClient = voteClient;
        this.voter = new Voter(cpf, cpf);

        setTitle("Votação Distribuída - Tela de Votação");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Votação - " + votingInfo.getTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel descriptionLabel = new JLabel("Objetivo: " + votingInfo.getDescription(), SwingConstants.CENTER);
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 22));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(descriptionLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel questionLabel = new JLabel(votingInfo.getQuestion(), SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(questionLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        group = new ButtonGroup();

        for (VoteOption option : votingInfo.getOptions()) {
            JRadioButton rb = new JRadioButton((option.getId() + 1) + " - " + option.getDescription());
            rb.setFont(new Font("Arial", Font.PLAIN, 18));
            rb.setAlignmentX(Component.CENTER_ALIGNMENT);
            rb.setActionCommand(String.valueOf(option.getId()));
            group.add(rb);
            optionsPanel.add(rb);
            optionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        mainPanel.add(optionsPanel);

        add(mainPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton buttonSend = new JButton("Enviar");
        JButton buttonCancel = new JButton("Cancelar");
        buttonSend.setFont(new Font("Arial", Font.PLAIN, 18));
        buttonCancel.setFont(new Font("Arial", Font.PLAIN, 18));

        bottomPanel.add(buttonSend);
        bottomPanel.add(buttonCancel);
        add(bottomPanel, BorderLayout.SOUTH);

        buttonSend.addActionListener(e -> sendVote());
        buttonCancel.addActionListener(e -> dispose());
    }

    private void sendVote() {
        if (group.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma opção antes de enviar.", 
                                          "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedOption = group.getSelection().getActionCommand();

        try {
            voteClient.sendVote(voter, Integer.parseInt(selectedOption));

            // Opcional: Fechar a janela visualmente para não clicar 2 vezes
            this.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
