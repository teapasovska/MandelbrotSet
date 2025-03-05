package org.example;

import mpi.MPI;

public class MandelbrotMPI {
    public static void main(String[] args) {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int rowsPerProcess = 100;
        int startRow = rowsPerProcess * rank;
        int endRow = startRow + rowsPerProcess;

        System.out.println("Process " + rank + " started...");
        System.out.println("Process " + rank + " got assigned rows " + startRow + " to " + endRow);
        System.out.println("Process " + rank + " waiting for next step...");

        MPI.Finalize();
    }
}
