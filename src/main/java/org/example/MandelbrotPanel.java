package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MandelbrotPanel extends JPanel {

    private int width;
    private int height;

    private BufferedImage image;

    private double zoom = 1.0;
    private double offsetX = -0.5;
    private double offsetY = 0.0;

    public MandelbrotPanel(int width, int height) {
        this.width = width;
        this.height = height;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        setDoubleBuffered(true);

        drawMandelbrotSet();
    }

    private void drawMandelbrotSet() {
        long startTime = System.currentTimeMillis();  // Start time

        // Identified parallelizable section -> convert to parallel processing
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double zx = (x - width / 2) / (0.5 * zoom * width) + offsetX;
                double zy = (y - height / 2) / (0.5 * zoom * height) + offsetY;
                int color = MandelbrotSequential.computeColor(zx, zy);
                image.setRGB(x, y, color);
            }
        }

        long endTime = System.currentTimeMillis();  // End time

        long runtime = endTime - startTime;
        System.out.println("Mandelbrot set computed in " + runtime + " ms.");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public void redraw() {
        drawMandelbrotSet();
        repaint();
    }

    public double getZoom() {
        return zoom;
    }
}
