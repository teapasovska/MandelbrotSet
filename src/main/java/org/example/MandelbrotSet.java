package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MandelbrotSet {
    private static JFrame f;
    private static MandelbrotPanel p;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // set up frame
                JFrame setupFrame = new JFrame("Mandelbrot Set - Setup");
                setupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setupFrame.setSize(350, 250);

                setupFrame.setLocationRelativeTo(null);

                JPanel contentPanel = new JPanel();
                contentPanel.setBackground(new Color(173, 216, 230));
                contentPanel.setLayout(new GridBagLayout());

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                JLabel widthLabel = new JLabel("Enter the width of the frame:");
                gbc.gridx = 0;
                gbc.gridy = 0;
                contentPanel.add(widthLabel, gbc);

                JTextField widthField = new JTextField("800"); // default width value
                gbc.gridx = 1;
                gbc.gridy = 0;
                contentPanel.add(widthField, gbc);

                JLabel heightLabel = new JLabel("Enter the height of the frame:");
                gbc.gridx = 0;
                gbc.gridy = 1;
                contentPanel.add(heightLabel, gbc);

                JTextField heightField = new JTextField("600"); // default height value
                gbc.gridx = 1;
                gbc.gridy = 1;
                contentPanel.add(heightField, gbc);

                // start button
                JButton parallelButton = new JButton("Start Mandelbrot in parallel");
                parallelButton.setFont(new Font("Arial", Font.BOLD, 14));
                parallelButton.setBackground(new Color(135, 206, 250));
                parallelButton.setForeground(Color.WHITE);
                parallelButton.setFocusPainted(false);
                parallelButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 2),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));

                JButton distributedButton = new JButton("Start Mandelbrot in distributed");
                distributedButton.setFont(new Font("Arial", Font.BOLD, 14));
                distributedButton.setBackground(new Color(135, 206, 250));
                distributedButton.setForeground(Color.WHITE);
                distributedButton.setFocusPainted(false);
                distributedButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 2),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));

                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                contentPanel.add(parallelButton, gbc);

                gbc.gridy = 3;
                contentPanel.add(distributedButton, gbc);

                setupFrame.add(contentPanel);
                setupFrame.setVisible(true);

                parallelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            // parse user input for width and height
                            int width = Integer.parseInt(widthField.getText());
                            int height = Integer.parseInt(heightField.getText());

                            // validation
                            if (width <= 0 || height <= 0) {
                                JOptionPane.showMessageDialog(setupFrame, "Width and height must be positive integers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            setupFrame.dispose();
                            startParallel(width, height);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(setupFrame, "Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                distributedButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            int width = Integer.parseInt(widthField.getText());
                            int height = Integer.parseInt(heightField.getText());
                            if (width <= 0 || height <= 0) {
                                JOptionPane.showMessageDialog(setupFrame, "Width and height must be positive integers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            setupFrame.dispose();
                            startDistributed(width, height);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(setupFrame, "Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            }
        });
    }

    private static void startParallel(int width, int height) {
        // mandelbrot frame
        JFrame frame = new JFrame("Mandelbrot Set - Parallel Implementation");
        f = frame;

        MandelbrotPanel panel = new MandelbrotPanel(width, height);
        p = panel;
        f.add(p);
        f.setSize(width, height);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        f.setResizable(false);

        f.setLocationRelativeTo(null);

        f.setVisible(true);
        // key listener for interactive controls
        f.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP)
                    p.setOffsetY(p.getOffsetY() - 0.1 / p.getZoom());
                else if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    p.setOffsetY(p.getOffsetY() + 0.1 / p.getZoom());
                else if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    p.setOffsetX(p.getOffsetX() - 0.1 / p.getZoom());
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    p.setOffsetX(p.getOffsetX() + 0.1 / p.getZoom());
                else if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_EQUALS) {
                    p.setZoom(p.getZoom() * 1.5);
                } else if (e.getKeyCode() == KeyEvent.VK_MINUS)
                    p.setZoom(p.getZoom() / 1.5);
                p.redraw();
            }
        });

        System.out.println("Mandelbrot Set - Parallel Implementation started.");
    }

    private static void startDistributed(int width, int height) {
        try {
            String command = String.format("mpjrun.sh -np 4 -cp target/classes:$MPJ_HOME/lib/mpj.jar org.example.MandelbrotMPI %d %d", width, height);
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", command);
            builder.redirectErrorStream(true); // Merge stderr with stdout

            Process process = builder.start();

            // Read the output from the process and print it to the Java console
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[MPJ] " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();


            System.out.println("Started Distributed Mandelbrot with width=" + width + " and height=" + height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

