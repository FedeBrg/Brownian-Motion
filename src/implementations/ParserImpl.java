package implementations;

import interfaces.Parser;
import interfaces.Particle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.List;


public class ParserImpl implements Parser {
    private List<Particle> particles;
    private double L;
    private double M;
    private int N;
    private double Rc;
    private double Nu;

    public ParserImpl(){
        this.particles = new ArrayList<Particle>();
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public double getL() {
        return L;
    }

    public double getRc() {
        return Rc;
    }

    public double getNu() {
        return Nu;
    }

    public double getM() {
        return M;
    }

    public int getN() {
        return N;
    }

    public void setM(double m) {
        M = m;
    }

    public void parse() {
        try {


            File inputFile = new File("input.txt");

            Scanner inputReader = new Scanner(inputFile);
//
            N = inputReader.nextInt();
            L = inputReader.nextDouble();
            Rc = inputReader.nextDouble();
//            Nu = inputReader.nextDouble();




            int i = 0;

            while(inputReader.hasNext()){
                Particle p = new ParticleImpl(inputReader.nextDouble(),inputReader.nextDouble(),inputReader.nextDouble(),inputReader.nextDouble(),inputReader.nextDouble(),inputReader.nextDouble(),inputReader.nextInt());
                particles.add(p);
                i++;
            }

            double idealM = Rc;
            M = L/Math.floor(L/idealM);

//            M =Rc;

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}
