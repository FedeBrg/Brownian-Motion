package interfaces;

import java.util.List;

public interface Parser {
    void parse();
    double getL();
    double getM();
    int getN();
    void setM(double m);
    double getRc();
    double getNu();
    List<Particle> getParticles();
}
