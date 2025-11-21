package client.view;

import shared.CPFValidator;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.MaskFormatter;

import client.VoteClient;

public class CpfPage extends JFrame {
    private final JFormattedTextField cpfField;
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

        JPanel centerPanel = new JPanel(new GridLayout(5, 1, 5, 5));

        try {
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            cpfMask.setAllowsInvalid(false);
        
            cpfField = new JFormattedTextField(cpfMask);
            cpfField.setColumns(14);
            cpfField.setFocusLostBehavior(JFormattedTextField.PERSIST);
        
            cpfField.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        cpfField.setCaretPosition(cpfField.getText().indexOf('_') != -1 ?
                                                   cpfField.getText().indexOf('_') : 0);
                    });
                }
            });

            cpfField.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    String text = cpfField.getText().replace(".", "").replace("-", "").trim();
                    if (text.replace("_", "").isEmpty()) {
                        cpfField.setText("");
                    }
                }
            });
        
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar máscaras.", e);
        }
        
        centerPanel.add(new JLabel("CPF: ", SwingConstants.CENTER));
        centerPanel.add(cpfField);

        ipField = new JFormattedTextField();
        ipField.setText("Ex: 192.168.0.1 ou vazio = localhost");
        ipField.setForeground(Color.GRAY);

        ipField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (ipField.getText().equals("Ex: 192.168.0.1 ou vazio = localhost")) {
                    ipField.setText("");
                    ipField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (ipField.getText().isEmpty()) {
                    ipField.setForeground(Color.GRAY);
                    ipField.setText("Ex: 192.168.0.1 ou vazio = localhost");
                }
            }
        });

        centerPanel.add(new JLabel("Use pontos no IP do servidor (ex: 192.168.0.1 ou localhost): ", SwingConstants.CENTER));
        centerPanel.add(ipField);

        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        centerPanel.add(errorLabel);

        JButton sendButton = new JButton("Prosseguir");
        sendButton.addActionListener(e -> {
            VoteClient vc = validateIp();
            
        if (vc == null){
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
        String cpf = cpfField.getText().replaceAll("\\D", "").trim();
        if (cpf.isEmpty()) {
            errorLabel.setText("Digite um CPF.");
        }

        if (CPFValidator.validate(cpf)) {
            try {
                if (voteClient.getVotingInfo() != null) {
                    new VotePage(cpf, voteClient).setVisible(true);
                    dispose();
                } else {
                    errorLabel.setText("Erro: Não foi possível obter informações de votação. Servidor indisponível.");
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
    
        if (ip.isEmpty() || ip.equals("Ex: 192.168.0.1 ou vazio = localhost")) {
                ip = "localhost";
                System.out.println("Usando localhost como IP do servidor.");
        } else {
            // Regex para validar IPv4
            String ipRegex = 
                "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}" +
                "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$";
                    
            if (!ip.matches(ipRegex)) {
                errorLabel.setText("Digite o IP do servidor corretamente (ex: 192.168.0.1 ou localhost).");
                return null;
            }
        }
    
        try {
            VoteClient voteClient = new VoteClient(ip);
            voteClient.listenAsync();
            return voteClient;
        } catch (Exception e) {
            errorLabel.setText("Não foi possível conectar ao servidor.");
            return null;
        }
    }
}