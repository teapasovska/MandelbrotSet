package org.example;

import java.awt.*;

public class MandelbrotSequential {

    // max number of iterations to determine if a point is in the mandelbrot set
    private static final int MAX_ITER = 1000;

    public static int computeColor(double zx, double zy) {
        // store the initial real and imaginary parts of the complex number
        double cX = zx;
        double cY = zy;

        int i = 0;

        double smoothValue = 0;

        // iterate until the point escapes the mandelbrot set or the maximum number of iterations is reached
        while (zx * zx + zy * zy < 4 && i < MAX_ITER) {
            // calculate the new real part of the complex number
            double temp = zx * zx - zy * zy + cX;
            // calculate the new imaginary part of the complex number
            zy = 2.0 * zx * zy + cY;
            zx = temp;
            i++;
        }

        // if the point is not in the mandelbrot set
        if (i < MAX_ITER) {
            smoothValue = i + 1 - Math.log(Math.log(Math.sqrt(zx * zx + zy * zy))) / Math.log(2.0);
            float hue = 0.5f + (float) smoothValue / MAX_ITER * 5.0f;
            float saturation = 0.9f + (float) smoothValue / MAX_ITER * 0.4f;
            float brightness = 1.0f;
            return Color.HSBtoRGB(hue % 1.0f, saturation, brightness);
        } else {
            return Color.BLACK.getRGB();
        }
    }
}