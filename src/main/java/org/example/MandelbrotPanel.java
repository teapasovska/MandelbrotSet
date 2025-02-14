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
    private static final int ROW_BLOCK_SIZE = 20; // Process multiple rows per thread, optimized block size for performance

    public MandelbrotPanel(int width, int height) {
        this.width = width;
        this.height = height;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        setDoubleBuffered(true);

        drawMandelbrotSet();
    }

    private void drawMandelbrotSet() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                long startTime = System.currentTimeMillis();
                int availableThreads = Runtime.getRuntime().availableProcessors();
                ExecutorService executor = Executors.newFixedThreadPool(availableThreads * 2); //Increased parallelism

                for (int y = 0; y < height; y += ROW_BLOCK_SIZE) {
                    int startRow = y;
                    int endRow = Math.min(y + ROW_BLOCK_SIZE, height);
                    executor.execute(new MandelbrotWorker(startRow, endRow, width, height, zoom, offsetX, offsetY, image));
                }

                executor.shutdown();
                try {
                    if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                        executor.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    executor.shutdownNow();
                    Thread.currentThread().interrupt();
                }

                long endTime = System.currentTimeMillis();
                System.out.println("Mandelbrot set computed in " + (endTime - startTime) + " ms.");
                return null;
            }

            @Override
            protected void done() {
                repaint();
            }
        };
        worker.execute();
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

// Updated MandelbrotWorker to process row blocks instead of single rows
class MandelbrotWorker implements Runnable {
    private int startRow;
    private int endRow;
    private int width;
    private int height;
    private double zoom;
    private double offsetX;
    private double offsetY;
    private BufferedImage image;

    public MandelbrotWorker(int startRow, int endRow, int width, int height, double zoom, double offsetX, double offsetY, BufferedImage image) {
        this.startRow = startRow;
        this.endRow = endRow;
        this.width = width;
        this.height = height;
        this.zoom = zoom;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.image = image;
    }

    @Override
    public void run() {
        int[] rowBuffer = new int[width];
        for (int y = startRow; y < endRow; y++) {
            for (int x = 0; x < width; x++) {
                double zx = (x - width / 2) / (0.5 * zoom * width) + offsetX;
                double zy = (y - height / 2) / (0.5 * zoom * height) + offsetY;
                rowBuffer[x] = MandelbrotSequential.computeColor(zx, zy);
            }
            image.setRGB(0, y, width, 1, rowBuffer, 0, width);
        }
    }
}
