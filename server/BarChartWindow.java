package server;

import javax.swing.*;

import shared.NetProtocol;

import java.awt.*;
import java.util.HashMap;

public class BarChartWindow extends JFrame {

    private final BarChartPanel panel;

    public BarChartWindow(int[] values, String[] labels, String title, String description, String question, Runnable onCloseCallback) {
        super("Votação: " + title);

        JLabel ipLabel = new JLabel("IP do servidor: " + NetProtocol.getLocalIpAddress());
        ipLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        ipLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ipLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(ipLabel, BorderLayout.NORTH);
        System.out.println("IP do servidor: " + NetProtocol.getLocalIpAddress() + "\n");

        if (values.length != labels.length) {
            throw new IllegalArgumentException("values e labels devem ter o mesmo comprimento");
        }

        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new BarChartPanel(values, labels, description, question);
        add(panel);

                JButton endButton = new JButton("Encerrar votação");
        endButton.setFocusable(false);

        endButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente encerrar a votação?",
                "Confirmar encerramento",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                onCloseCallback.run();
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(endButton);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void updateValue(String label, int newValue) {
        panel.updateValue(label, newValue);
    }
}

class BarChartPanel extends JPanel {

    private final HashMap<String, Integer> values = new HashMap<>();
    private final String description;
    private final String question;

    public BarChartPanel(int[] valuesArray, String[] labelsArray, String description, String question) {
        this.description = description;
        this.question = question;

        for (int i = 0; i < valuesArray.length; i++) {
            values.put(labelsArray[i], valuesArray[i]);
        }
    }

    /**
     * Atualiza valor de uma coluna pelo nome.
     */
    public void updateValue(String label, int newValue) {
        if (!values.containsKey(label)) {
            System.err.println("❌ Label não existe: " + label);
            return;
        }

        values.put(label, newValue);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();

        int padding = 60;
        int headerHeight = 80; // espaço reservado para texto superior

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));

        // ===========================
        //  Descrição e Pergunta
        // ===========================
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString(description, 20, 30);

        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.drawString(question, 20, 55);

        int n = values.size();
        int barWidth = (width - 2 * padding) / n;

        // Encontrar o maior valor
        int maxVal = values.values().stream().max(Integer::compare).orElse(1);
        if (maxVal == 0) maxVal = 1;

        // Eixo Y
        g2.drawLine(padding, headerHeight, padding, height - padding);

        // Eixo X
        g2.drawLine(padding, height - padding, width - padding, height - padding);

        int i = 0;

        for (String label : values.keySet()) {
            int v = values.get(label);

            int x = padding + i * barWidth;
            int barHeight = (int) ((double) v / maxVal * (height - padding - headerHeight));
            int y = height - padding - barHeight;

            // Barra
            g2.setColor(Color.BLUE);
            g2.fillRect(x + 5, y, barWidth - 10, barHeight);

            // Contorno
            g2.setColor(Color.BLACK);
            g2.drawRect(x + 5, y, barWidth - 10, barHeight);

            // Valor
            String valStr = String.valueOf(v);
            g2.drawString(
                    valStr,
                    x + barWidth / 2 - g2.getFontMetrics().stringWidth(valStr) / 2,
                    y - 5
            );

            // Nome da coluna
            g2.drawString(
                    label,
                    x + barWidth / 2 - g2.getFontMetrics().stringWidth(label) / 2,
                    height - padding + 20
            );

            i++;
        }
    }
}
