package client.view;

import shared.CPFValidator;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.MaskFormatter;

import client.VoteClient;

public class CpfPage extends JFrame {
    private final JFormattedTextField cpfField;
    private final JTextField ipField;
    private final JTextField portField;
    private final JLabel errorLabel;

    public CpfPage() {

        setTitle("Identificação do Eleitor");
        setSize(400, 370);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Insira seu CPF:", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

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
        
        JPanel cpfPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        cpfPanel.add(new JLabel("CPF:", SwingConstants.CENTER));
        cpfPanel.add(cpfField);
        cpfPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        centerPanel.add(cpfPanel);

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
        JPanel ipPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        ipPanel.add(new JLabel("IP do servidor (ex: 192.168.0.1 ou localhost):", SwingConstants.CENTER));
        ipPanel.add(ipField);
        ipPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        centerPanel.add(ipPanel);

        portField = new JFormattedTextField();
        portField.setText("Ex: 8000 ou vazio = 1234");
        portField.setForeground(Color.GRAY);

        portField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (portField.getText().equals("Ex: 8000 ou vazio = 1234")) {
                    portField.setText("");
                    portField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (portField.getText().isEmpty()) {
                    portField.setForeground(Color.GRAY);
                    portField.setText("Ex: 8000 ou vazio = 1234");
                }
            }
        });
        JPanel portPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        portPanel.add(new JLabel("Porta do servidor (vazio = 1234):", SwingConstants.CENTER));
        portPanel.add(portField);
        portPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        centerPanel.add(portPanel);

        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setMaximumSize(new Dimension(350, Integer.MAX_VALUE));
        errorLabel.setPreferredSize(new Dimension(350, errorLabel.getPreferredSize().height));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(errorLabel);

        JButton sendButton = new JButton("Prosseguir");
        sendButton.addActionListener(e -> {
            String ip = ipField.getText().trim();
            
            ip = validateIp(ip);

            if (ip == null) {
                return;
            }

            String port = portField.getText().trim();

            int portInt  = validatePort(port);
            if (portInt == -1) {
                return;
            }

            VoteClient voteClient = null;
            try {
                voteClient = new VoteClient(ip, portInt);
                voteClient.listenAsync();
            } catch (Exception ex) {
                errorLabel.setText("Não foi possível conectar ao servidor.");
                System.out.println(ex.getMessage());
            }

            validateCPF(voteClient);
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
                    errorLabel.setText("Erro: Informações de votação não obtidas. Servidor indisponível!");
                    errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                }
            } catch (Exception e) {
                errorLabel.setText("Erro ao conectar ao servidor de votação.");
                System.out.println(e.getMessage());
            }
        } else {
            errorLabel.setText("CPF inválido. Tente novamente.");
        }
    }

    private String validateIp(String ip) {
        if (ip.isEmpty() || ip.equals("Ex: 192.168.0.1 ou vazio = localhost")) {
                ip = "localhost";
                System.out.println("Usando localhost como IP do servidor.");
                return ip;
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
        return ip;
    }

    private int validatePort(String port) {
        if (port.isEmpty() || port.equals("Ex: 8000 ou vazio = 1234")) {
            System.out.println("Usando 1234 como porta do servidor.");
            
            return 1234;
        } else {
            String portRegex = "^([0-9]{1,5})$";
                    
            if (!port.matches(portRegex)) {
                errorLabel.setText("Digite a porta do servidor corretamente (ex: 8000 ou 1234).");
                return -1;
            }
        } 
    
        return Integer.parseInt(port);
    }
}