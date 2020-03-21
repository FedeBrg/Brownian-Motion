package implementations;

import interfaces.Cell;
import interfaces.Particle;

import java.util.LinkedList;
import java.util.List;

public class CellImpl implements Cell {
    private int x;
    private int y;
    private List<Particle> particles;

    public CellImpl(int x, int y){
        this.x = x;
        this.y = y;
        this.particles = new LinkedList<Particle>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void setParticles(List<Particle> particles) {
        this.particles = particles;
    }

    public void addParticle(Particle p){
        particles.add(p);
    }
}
