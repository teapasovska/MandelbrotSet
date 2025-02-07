package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int y = 0; y < height; y++) {
            executor.execute(new MandelbrotWorker(y, width, height, zoom, offsetX, offsetY, image));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();  // End time

        long runtime = endTime - startTime;
        System.out.println("Mandelbrot set computed in " + runtime + " ms.");
        repaint();
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
    }

    public double getZoom() {
        return zoom;
    }
}

// Updated MandelbrotWorker to compute row-wise
class MandelbrotWorker implements Runnable {
    private int y;
    private int width;
    private int height;
    private double zoom;
    private double offsetX;
    private double offsetY;
    private BufferedImage image;

    public MandelbrotWorker(int y, int width, int height, double zoom, double offsetX, double offsetY, BufferedImage image) {
        this.y = y;
        this.width = width;
        this.height = height;
        this.zoom = zoom;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.image = image;
    }

    @Override
    public void run() {
        for (int x = 0; x < width; x++) {
            double zx = (x - width / 2) / (0.5 * zoom * width) + offsetX;
            double zy = (y - height / 2) / (0.5 * zoom * height) + offsetY;
            int color = MandelbrotSequential.computeColor(zx, zy);
            synchronized (image) {
                image.setRGB(x, y, color);
            }
        }
    }
}
