package client.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class MainPage extends JFrame {

    public MainPage() {
        setTitle("Votação Distribuída - Tela Principal");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("VOTAÇÃO DISTRIBUÍDA", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 2, 10, 10));

        JButton buttonCpf     = new JButton("Votar");
        JButton buttonHelp    = new JButton("Ajuda");
        JButton buttonCredits = new JButton("Créditos");
        JButton buttonExit    = new JButton("Sair");

        centerPanel.add(createButtonLine(buttonCpf));
        centerPanel.add(createButtonLine(buttonHelp));
        centerPanel.add(createButtonLine(buttonCredits));
        centerPanel.add(createButtonLine(buttonExit));
        add(centerPanel, BorderLayout.CENTER);

        buttonCpf.addActionListener((ActionEvent e) -> new CpfPage().setVisible(true));
        buttonHelp.addActionListener((ActionEvent e) -> new HelpPage().setVisible(true));
        buttonCredits.addActionListener((ActionEvent e) -> new CreditsPage().setVisible(true));
        buttonExit.addActionListener(e -> System.exit(0));
    }

    private JPanel createButtonLine(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setPreferredSize(new Dimension(120, 50));

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.add(button);
        return panel;
    }
}
