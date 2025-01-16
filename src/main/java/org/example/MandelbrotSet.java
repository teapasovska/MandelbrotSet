package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
                JButton startButton = new JButton("Start Mandelbrot");
                startButton.setFont(new Font("Arial", Font.BOLD, 14));
                startButton.setBackground(new Color(135, 206, 250));
                startButton.setForeground(Color.WHITE);
                startButton.setFocusPainted(false);
                startButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 2),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));

                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                contentPanel.add(startButton, gbc);

                setupFrame.add(contentPanel);
                setupFrame.setVisible(true);

                startButton.addActionListener(new ActionListener() {
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

                            // mandelbrot frame
                            JFrame frame = new JFrame("Mandelbrot Set - Sequential Implementation");
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
                        } catch (NumberFormatException ex) {
                            // handle invalid input
                            JOptionPane.showMessageDialog(setupFrame, "Please enter valid integer values for width and height.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            }
        });
    }
}
