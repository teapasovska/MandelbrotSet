# Mandelbrot Set - Distributed (Java MPI with MPJ Express)

This project visualizes the Mandelbrot Set using a distributed algorithm powered by MPJ Express (Java MPI). It runs with multiple parallel processes to compute and render parts of the Mandelbrot Set efficiently.

## Project Structure

MandelbrotSet/
├── mpj/                 ← Full MPJ Express folder (with bin/ and lib/)
├── lib/                 ← Additional libraries like log4j, etc.
├── src/                 ← Java source files
├── target/              ← Compiled classes (generated after building)
├── .idea/               ← IntelliJ project settings (if included)
└── README.md

## How to Run the Project (After Unzipping)

1. Open the Project in IntelliJ
- Open IntelliJ IDEA
- Go to: File > Open
- Select the unzipped MandelbrotSet folder
- Wait for the project to load

2. Add MPJ Express JARs to the Project
- Go to File > Project Structure > Modules > Dependencies
- Click the ➕ at the bottom → choose JARs or directories...
- Navigate to: MandelbrotSet/mpj/lib/
- Select the following .jar files:
    - mpj.jar
    - starter.jar
    - mpjdev.jar
    - mpjbuf.jar
    - log4j-1.2.17.jar (or similar, if used)
    - Any other required .jar from the lib/ or mpj/lib/ folders
- Set the Scope to: Compile
- Click Apply and then OK

3. Build the Project
- Go to Build > Rebuild Project in IntelliJ


## Requirements
- Java 8 or later
- macOS / Linux / WSL (for mpjrun.sh)
- IntelliJ IDEA
- MPJ Express (included in this project)

## Notes
- You need to re-add the .jar files to IntelliJ after unzipping unless the .iml file is present and remembered
- MPJ Express is bundled under the mpj/ directory, so no global installation is needed


