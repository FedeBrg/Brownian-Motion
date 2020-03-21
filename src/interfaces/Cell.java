package interfaces;

import java.util.List;

public interface Cell {
    int getX();
    int getY();
    List<Particle> getParticles();
    void setParticles(List<Particle> particles);
    void addParticle(Particle p);
}
