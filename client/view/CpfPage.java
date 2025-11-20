package client.view;

import shared.CPFValidator;
import java.awt.*;
import javax.swing.*;

public class CpfPage extends JFrame {
    private final JTextField cpfField;
    private final JLabel errorLabel;
    
    public CpfPage() {
        setTitle("Identificação do Eleitor");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Insira seu CPF:", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        cpfField = new JTextField();
        centerPanel.add(new JLabel("CPF: ", SwingConstants.LEFT));
        centerPanel.add(cpfField);

        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        centerPanel.add(errorLabel);

        JButton sendButton = new JButton("Prosseguir");
        sendButton.addActionListener(e -> validateCPF());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(sendButton);

        content.add(title, BorderLayout.NORTH);
        content.add(centerPanel, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);

        add(content);
    }

    private void validateCPF() {
        String cpf = cpfField.getText().trim();
        if (cpf.isEmpty()) {
            errorLabel.setText("Digite um CPF.");
        }
        
        if (CPFValidator.validate(cpf)) {
            errorLabel.setText("Funcionou, enviando pra proxima página");
            // Depois tem que remover esse errorLabel e direcionar para a página de votação
            // new VotePage(cpf).setVisible(true);
            // dispose();
        } else {
            errorLabel.setText("CPF inválido. Tente novamente.");
        }
    }
}
