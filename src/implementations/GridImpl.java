package implementations;

import interfaces.Cell;
import interfaces.Grid;
import interfaces.Particle;

import java.util.ArrayList;
import java.util.List;

public class GridImpl implements Grid {
    private double L;
    private double M;
    private double Rc;

    private List<Cell> cells;

    public GridImpl(double L, double M, double Rc){
        this.L = L;
        this.M = M;
        this.Rc = Rc;
        this.cells = new ArrayList<Cell>();
    }

    public double getL() {
        return L;
    }

    public double getM() {
        return M;
    }

    public double getRc() {
        return Rc;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cellList) {
        this.cells = cellList;
    }

    public void setM(double newm){
        this.M = newm;
    }

    public List<Particle> getParticles(){
        List<Particle> particles = new ArrayList<Particle>();
        for(Cell c : cells){
            particles.addAll(c.getParticles());
        }
        return particles;
    }
}
