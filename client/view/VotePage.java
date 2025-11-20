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

    public VotePage(String cpf, VoteClient voteClient){
        this.votingInfo = voteClient.getVotingInfo();
        this.voteClient = voteClient;
        
        this.voter = new Voter(cpf, cpf);

        setTitle("Votação Distribuída - Tela de Votação");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Escolha sua opção de voto", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));

        group = new ButtonGroup();

        for (VoteOption opt : votingInfo.getOptions()) {
            JRadioButton rb = new JRadioButton((opt.getId() + 1) + " - " + opt.getDescription());

            rb.setAlignmentX(Component.CENTER_ALIGNMENT);
            rb.setFont(new Font("Arial", Font.PLAIN, 18));
            rb.setActionCommand(String.valueOf(opt.getId()));
            group.add(rb);
            optionsPanel.add(rb);
        }

        add(optionsPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton buttonSend = new JButton("Enviar");
        JButton buttonCancel = new JButton("Cancelar");

        buttonSend.setFont(new Font("Arial", Font.PLAIN, 18));
        buttonCancel.setFont(new Font("Arial", Font.PLAIN, 18));

        bottomPanel.add(buttonSend);
        bottomPanel.add(buttonCancel);
        add(bottomPanel, BorderLayout.SOUTH);

        buttonSend.addActionListener(e -> sendVote());
        buttonCancel.addActionListener(e -> System.exit(0));
    }

    private void sendVote(){
        String selectedOption = group.getSelection().getActionCommand();

        try {
            voteClient.sendVote(voter, Integer.parseInt(selectedOption));
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            
        JOptionPane.showMessageDialog(this, "Você votou na opção: " + (Integer.parseInt(selectedOption) + 1));
        dispose();
        
    }
    
}
