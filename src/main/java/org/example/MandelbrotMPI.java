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

        if (args.length < 2){
            if (rank == 0){
                System.err.println("Width and height must be provided as arguments");
            }
            MPI.Finalize();
            return;
        }

        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);
        System.out.println("Process "+rank+" received width=" + width + " and height=" + height);

        int rowsPerProcess = height/size;
        int extraRows= height%size;
        int startRow = rank * rowsPerProcess + Math.min(rank, extraRows);
        int endRow = startRow + rowsPerProcess + (rank<extraRows?1:0);

        //System.out.println("Process " + rank + " started...");
        //System.out.println("Process " + rank + " got assigned rows " + startRow + " to " + endRow);
        //System.out.println("Process " + rank + " waiting for next step...");

//        int width = 800, height = 600;
        int[] localPixels = new int[(endRow-startRow)*width];

        System.out.println("Process" + rank+ " computing rows "+ startRow+" to "+endRow);


        for (int y = startRow; y < endRow; y++){
            for (int x=0; x < width; x++){
                double zx = (x-width/2) / (0.5 * width);
                double zy = (y-height/2) / (0.5 * height);
                localPixels[(y-startRow)*width+x] = computeColor(zx, zy);
            }
        }
        System.out.println("Process" + rank+ " finished computation for rows: " + startRow+ " to " + endRow);


        //colecting pixel data
        int[] fullimage = null;

        if (rank == 0){
            fullimage = new int[width*height];
        }
        System.out.println("Process" + rank+ " sending data");
        MPI.COMM_WORLD.Gather(localPixels, 0, localPixels.length, MPI.INT, fullimage, 0, localPixels.length, MPI.INT, 0);

        if (rank == 0){
            System.out.println("Received data");
        }
        MPI.Finalize();
    }
}
