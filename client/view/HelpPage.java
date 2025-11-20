package client.view;

import java.awt.*;
import javax.swing.*;

public class HelpPage extends JFrame {
    public HelpPage() {
        setTitle("Ajuda - Votação Distribuída");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Ajuda da Votação Distribuída", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JTextArea helpText = new JTextArea();
        helpText.setEditable(false);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);
        helpText.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        helpText.setText(
            "Bem-vindo ao Sistema de Votação Distribuída!\n\n"
          + "Nesta aplicação, você poderá:\n"
          + " • Inserir seu CPF para validação;\n"
          + " • Receber a pergunta da votação;\n"
          + " • Selecionar sua opção de voto;\n"
          + " • Enviar seu voto ao servidor;\n"
          + " • Acompanhar o fluxo da votação.\n\n"
        );

        JScrollPane scroll = new JScrollPane(helpText);

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
