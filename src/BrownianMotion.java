import implementations.CellImpl;
import implementations.GridImpl;
import implementations.ParticleImpl;
import interfaces.Cell;
import interfaces.Grid;
import interfaces.Parser;
import interfaces.Particle;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BrownianMotion {

    public static void main(String[] args) {

        generateInputFile(10);

    }

    public static void generateInputFile(int N){
        double L = 0.5;

        Particle big = new ParticleImpl(L/2,L/2,0.05,0,0,100,1);

        List<Particle> l = new ArrayList<>();

        Random r = new Random();

        l.add(big);
        int i = 2;
        while(i<N+1){
            double v = r.nextDouble()*0.1;
            double angle = r.nextDouble()*2*Math.PI;
            double vx = v*Math.cos(angle);
            double vy = v*Math.sin(angle);

            double x = r.nextDouble()*L;
            double y = r.nextDouble()*L;

            Particle p = new ParticleImpl(x,y,0.005,vx,vy,0.1,i);
            if (!l.contains(p)){
                l.add(p);
                i++;
            }
        }

        StringBuilder sb = new StringBuilder();

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
            FileWriter myWriter = new FileWriter("inputFile.txt");
            myWriter.write(sb.toString());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
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
