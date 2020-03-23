package interfaces;

import java.util.List;

public interface Particle {


    double getR();
    double getX();
    double getY();
    int getId();

    double getXVelocity();
    double getYVelocity();

    List<Particle> getNeighbors();
    void setNeighbors(List<Particle> list);

    void setXVelocity(double XVelocity);
    void setYVelocity(double YVelocity);

    double getM();

    double calculateWallCollision(double wall);
    double calculateParticleCollision(Particle other);
    void velocityAfterWallCollision(double wall);
    void velocityAfterParticleCollision(Particle other);
    void updatePosition(double t);
    void velocityAfterVerticalWallCollision();
    void velocityAfterHorizontalWallCollision();
    double calculateHorizontalWallCollision(double wall);
    double calculateVerticalWallCollision(double wall);
}
