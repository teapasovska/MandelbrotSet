package org.example;

public class MandelbrotSequentialTest {

    public static void main(String[] args) {
        int[] sizes = {1000, 2000, 3000, 4000, 5000, 6000};

        for (int size : sizes) {
            int width = size;
            int height = size;

            long start = System.currentTimeMillis();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    double zx = (x - width / 2) / (0.5 * width);
                    double zy = (y - height / 2) / (0.5 * height);
                    MandelbrotSequential.computeColor(zx, zy);
                }
            }

            long end = System.currentTimeMillis();
            System.out.printf("%dx%d Mandelbrot set computed in %d ms.%n", width, height, (end - start));
        }
    }
}
