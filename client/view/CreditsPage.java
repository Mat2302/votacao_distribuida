package client.view;

import java.awt.*;
import javax.swing.*;

public class CreditsPage extends JFrame {
    public CreditsPage() {
        setTitle("Créditos");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Créditos do Projeto", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JTextArea creditsText = new JTextArea();
        creditsText.setEditable(false);
        creditsText.setLineWrap(true);
        creditsText.setWrapStyleWord(true);
        creditsText.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        creditsText.setText(
            "Sistema de Votação Distribuída\n\n"
          + "Desenvolvido por:\n"
          + " • Giovani Manoel Da Silva - 173113\n"
          + " • Hitallo Alves Teles Azevedo - 196454\n"
          + " • Julyo Elias Hidalgo Da Silva - 185720\n"
          + " • Matheus Vitório Figueiredo De Oliveira - 194774\n"
          + " • Nelson Modenez Neto - 196220\n"
          + " • Otávio Marques Cruz - 281443\n\n"
          + "Disciplina: Programação Orientada a Objetos II\n"
          + "Professor: Prof. Dr. André Angelis"
        );

        JScrollPane scroll = new JScrollPane(creditsText);
        JButton closeButton = new JButton("Fechar");
        closeButton.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);

        content.add(title, BorderLayout.NORTH);
        content.add(scroll, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);

        add(content);
    }
}
