import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BrownianMotion {

    public static void main(String[] args) {
        Particle p = new ParticleImpl(0,0,1,1);
        Particle p1 = new ParticleImpl(0,10,1,1);

        List<Particle> l = new ArrayList<>();

        l.add(p);
        System.out.println(Math.min(1,Double.POSITIVE_INFINITY));


    }


}
