import implementations.CellImpl;
import implementations.GridImpl;
import implementations.ParserImpl;
import implementations.ParticleImpl;
import interfaces.Cell;
import interfaces.Grid;
import interfaces.Parser;
import interfaces.Particle;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BrownianMotion {

    public static void main(String[] args) {
        //CLEAR OVITO FILE BEFORE NEW SIMULATION
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("outputOVITO.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print("");
        writer.close();
        //for(int j = 0; j<50;j++) {

        int N = 50;
        double L = 0.5;
        double rc = 0.1;
        double vmax = 0.5;
        generateInputFile(N,L,rc,vmax);


        boolean startCounting = false;

        Parser parser = new ParserImpl();
        parser.parse();
        double KE = 0;
        Particle bigone = new ParticleImpl(0,0,0,0,0,0,0);
        for (Particle particle : parser.getParticles()){
            KE += particle.getKE();
            if(particle.getId() == 1){
                bigone=particle;
            }
        }

        double x0 = bigone.getX();
        double y0 = bigone.getY();
        int col = 0;

        double k = 1.38066e-23;
        //System.out.printf("System temperature: %f\n",(KE*2)/(3*k*parser.getParticles().size()));

        Grid grid = fillGrid(parser);

        generateOvitoFile(grid);


        int FRAMES = 10000;
        double nextCollisionTime;
        double calculatedCollision;
        boolean isVerticalWall = false;
        Particle p1 = null;
        Particle p2 = null;

        double time = 0;
        double oldtime = 0;
        int collisions = 0;

            while(true){
           // for (int i = 0; i < FRAMES; i++) {
                //Sacamos los vecinos previos
                for (Particle particle : grid.getParticles()) {
                    particle.getNeighbors().clear();
                }

                //Calculamos vecinos
                CIM(grid);

                //Definimos el minimo tiempo de colisión
                nextCollisionTime = Double.POSITIVE_INFINITY;

                //Vemos si choca contra una pared
                for (Particle particle : grid.getParticles()) {
                    calculatedCollision = particle.calculateHorizontalWallCollision(parser.getL());
                    if (nextCollisionTime > calculatedCollision) {
                        nextCollisionTime = calculatedCollision;
                        isVerticalWall = false;
                        p1 = particle;
                    }

                    calculatedCollision = particle.calculateVerticalWallCollision(parser.getL());
                    if (nextCollisionTime > calculatedCollision) {
                        nextCollisionTime = calculatedCollision;
                        isVerticalWall = true;
                        p1 = particle;
                    }
                }

                //Vemos si choca contra otra particula
                for (Particle particle : grid.getParticles()) {
                    for (Particle other : grid.getParticles()) {
                        if (particle.getId() != other.getId()) {
                            calculatedCollision = particle.calculateParticleCollision(other);
                            if (nextCollisionTime > calculatedCollision) {
                                nextCollisionTime = calculatedCollision;
                                p1 = particle;
                                p2 = other;
                            }
                        }
                    }
                }

                //Actualizamos las posiciones de las particulas involucradas dado este valor
                for (Particle p : grid.getParticles()) {
                    p.updatePosition(nextCollisionTime);
                    if (!p.equals(p1) && !p.equals(p2)) {
                    }
                }

                time += nextCollisionTime;


//                System.out.println(time-oldtime);
                oldtime=time;

//                if(time>oldtime+0.1 ){
////                    System.out.printf("%f\t%f\n",time,Math.sqrt(Math.pow(x0-bigone.getX(),2)+Math.pow(y0-bigone.getY(),2)));
////                    System.out.printf("%f\t%f\n",bigone.getX(),bigone.getY());
//
//                    oldtime = time;
//                }
                col++;

                if (p2 != null) {
                    p1.velocityAfterParticleCollision(p2);
                } else if (isVerticalWall) {
                    p1.velocityAfterVerticalWallCollision();
                    if(p1.getId() == 1)
                    {
                        break;
                    }
                } else {
                    p1.velocityAfterHorizontalWallCollision();
                    if(p1.getId() == 1)
                    {
                        break;
                    }
                }


                p1 = null;
                p2 = null;
                isVerticalWall = false;

                generateOvitoFile(grid);
            }
        //System.out.printf("%f\t%d",time,col);


        }

   // }

        public static void generateInputFile(int N, double L, double rc,double vmax){

            Particle big = new ParticleImpl(L/2,L/2,0.05,0,0,0.1,1);

            List<Particle> l = new ArrayList<>();

            Random r = new Random();
            double max = L-0.005;
            double min = 0.005;

            l.add(big);
            int i = 2;
            while(i<N+1){
                double v = r.nextDouble()*vmax;
                double angle = r.nextDouble()*2*Math.PI;
                double vx = v*Math.cos(angle);
                double vy = v*Math.sin(angle);

                double x = min + (max - min) * r.nextDouble();
                double y = min + (max - min) * r.nextDouble();

//                double x = r.nextDouble()*L;
//                double y = r.nextDouble()*L;

                Particle p = new ParticleImpl(x,y,0.005,vx,vy,0.0001,i);
                if (!l.contains(p)){
                    l.add(p);
                    i++;
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append(N);
            sb.append("\n");
            sb.append(L);
            sb.append("\n");
            sb.append(rc);

            sb.append("\n");
            for (Particle p : l){
                sb.append(p.getX());
                sb.append("\t");
                sb.append(p.getY());
                sb.append("\t");
                sb.append(p.getR());
                sb.append("\t");
                sb.append(p.getXVelocity());
                sb.append("\t");
                sb.append(p.getYVelocity());
                sb.append("\t");
                sb.append(p.getM());
                sb.append("\t");
                sb.append(p.getId());
                sb.append("\n");
            }

            try {
                FileWriter myWriter = new FileWriter("input.txt");
                myWriter.write(sb.toString());
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }


        }

    public static void generateOvitoFile(Grid grid){
        StringBuilder sb = new StringBuilder();
        sb.append(grid.getParticles().size());
        sb.append("\n");
        sb.append("\n");

        for (Particle p: grid.getParticles()){
            sb.append(p.getX());
            sb.append("\t");
            sb.append(p.getY());
            sb.append("\t");
            sb.append(p.getR());
            sb.append("\t");

            if(p.getId() == 1) {
                sb.append(255);
                sb.append("\t");
                sb.append(0);
            }
            else{
                sb.append(0);
                sb.append("\t");
                sb.append(255);
            }


            sb.append("\n");
        }

        try {

            // Open given file in append mode.
            BufferedWriter out = new BufferedWriter(
                    new FileWriter("outputOVITO.txt", true));
            out.write(sb.toString());
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occoured" + e);
        }
    }


    private static Grid fillGrid(Parser parser){
        double L = parser.getL();
        double M = parser.getM();
        double Rc = parser.getRc();
        int N = parser.getN();
        Grid grid = new GridImpl(L, M, Rc);
        int xCellPosition = 0, yCellPosition = 0;
        int cellQuantity =  (int) (L/M) * (int)(L/M);
        List<Cell> cellList = new ArrayList<Cell>();
        Cell cell = new CellImpl(0, 0);
        cellList.add(cell);

        for(int i = 1; i < cellQuantity; i++){
            int cellsPerRow = (int) (L/M);
            if(i % cellsPerRow == 0){
                xCellPosition = 0;
                yCellPosition++;
                cell = new CellImpl(xCellPosition, yCellPosition);
                cellList.add(cell);
            }
            else{
                xCellPosition++;
                cell = new CellImpl(xCellPosition, yCellPosition);
                cellList.add(cell);
            }
        }

        List<Particle> particles = parser.getParticles();
        Particle p;
        int cellNumber;

        for(int i = 0; i < N; i++){
            p = particles.get(i);
            cellNumber = calculateCellNumber(p.getX(), p.getY(), M, L);
            cellList.get(cellNumber).addParticle(p);
        }

        grid.setCells(cellList);
        return grid;
    }

    private static int calculateCellNumber(double x, double y, double M, double L){
        return (int) (Math.min(Math.floor(x/M), (L/M)-1) + (int)(L/M) * Math.min(Math.floor(y/M), (L/M)-1));
    }

    private static void CIM(Grid grid){
        for(Cell c : grid.getCells()){
            for(Particle p : c.getParticles()){
                getNeighbours2(p,c.getX(),c.getY(), grid);
                getNeighbours2(p,c.getX(),c.getY()+1, grid);
                getNeighbours2(p,c.getX()+1,c.getY()+1, grid);
                getNeighbours2(p,c.getX()+1,c.getY(), grid);
                getNeighbours2(p,c.getX()+1,c.getY()-1, grid);
            }
        }
    }

    private static void getNeighbours2(Particle p, int x, int y, Grid grid) {
        int cellsPerRow = (int) (grid.getL()/grid.getM());

        if(x<0 || x >= cellsPerRow || y<0 || y>= cellsPerRow){
            return;
        }

        if(y==cellsPerRow){
            y = 0;
        }
        else if(y==-1){
            y += cellsPerRow;
        }

        if(x == cellsPerRow){
            x = 0;
        }

        Cell c = grid.getCells().get((int)(x+y*cellsPerRow));

        for(Particle other : c.getParticles()){
            if(p.getId() != other.getId()){
                p.getNeighbors().add(other);
                other.getNeighbors().add(p);
            }
        }
    }
}
