package interfaces;

import interfaces.Cell;

import java.util.List;

public interface Grid {
    void setCells(List<Cell> cellList);
    double getRc();
    double getL();
    double getM();

    void setM(double newm);
    List<Cell> getCells();
    List<Particle> getParticles();
}
