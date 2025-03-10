package org.example;

import mpi.MPI;

import java.awt.*;

public class MandelbrotMPI {
    private static final int MAX_ITERATIONS = 1000;

    private static int computeColor(double zx, double zy){
        double cX = zx;
        double cY = zy;
        int i = 0;
        while (zx * zx + zy*zy < 4 && i< MAX_ITERATIONS){
            double temp = zx *zx + zy * zy + cX;
            zy= 2.0 * zx *zy+cY;
            zx = temp;
            i++;
        }
        if (i == MAX_ITERATIONS){
            return Color.BLACK.getRGB();
        }else {
            float hue = (float) i /MAX_ITERATIONS;
            return Color.HSBtoRGB(hue, 1.0f, 1.0f);
        }
    }


    public static void main(String[] args) {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int rowsPerProcess = 100;
        int startRow = rowsPerProcess * rank;
        int endRow = startRow + rowsPerProcess;

        //System.out.println("Process " + rank + " started...");
        //System.out.println("Process " + rank + " got assigned rows " + startRow + " to " + endRow);
        //System.out.println("Process " + rank + " waiting for next step...");

        int width = 800, height = 600;
        int[] localPixels = new int[rowsPerProcess * width];

        System.out.println("Process" + rank+ " started computation");


        for (int y = startRow; y < endRow; y++){
            for (int x=0; x < width; x++){
                double zx = (x-width/2) / (0.5 * width);
                double zy = (y-height/2) / (0.5 * height);
                localPixels[(y-startRow)*width+x] = computeColor(zx, zy);
            }
        }
        System.out.println("Process" + rank+ " finished computation for rows: " + startRow+ " to " + endRow);
        MPI.Finalize();
    }
}
