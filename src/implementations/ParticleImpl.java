package implementations;

import interfaces.Particle;

import java.util.ArrayList;
import java.util.List;

public class ParticleImpl implements Particle {
    private double x;
    private double y;
    private double r;
    private double x0;
    private double y0;
    private double m;

    private double XVelocity;
    private double YVelocity;

    private int id;

    private List<Particle> neighbors;

    public ParticleImpl(double x, double y, double r, double XVelocity, double YVelocity, double mass, int id) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.XVelocity = XVelocity;
        this.YVelocity = YVelocity;
        this.m = mass;
        this.id = id;
        this.neighbors = new ArrayList<>();
    }

    public double getXVelocity() {
        return XVelocity;
    }

    public double getYVelocity() {
        return YVelocity;
    }


    public List<Particle> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Particle> neighbors) {
        this.neighbors = neighbors;
    }

    @Override
    public boolean equals(Object obj) {
        if((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }

        Particle other = (Particle) obj;

        if(getId() == other.getId()){
            return true;
        }

        double dist = calculateDistance(other);

        return (dist) == 0;
    }

    private double calculateDistance(Particle p) {
        double toReturn = Math.sqrt(Math.pow(p.getX()-getX(),2) + Math.pow(p.getY()-getY(),2)) -getR()-p.getR() ;
        return toReturn < 0 ? 0 : toReturn;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public int getId() {
        return id;
    }

    public double getM() {
        return m;
    }

    public void setXVelocity(double XVelocity) {
        this.XVelocity = XVelocity;
    }

    public void setYVelocity(double YVelocity) {
        this.YVelocity = YVelocity;
    }

    public double calculateVerticalWallCollision(double wall){
        double xCol;
        if(XVelocity > 0){
            xCol = (wall - r - x ) / XVelocity;

        }
        else if(XVelocity < 0){
            xCol = (r - x) / XVelocity;
        }
        else{
            xCol = Double.POSITIVE_INFINITY;
        }

        return xCol;
    }

    public double calculateHorizontalWallCollision(double wall){
        double yCol;
        if(YVelocity > 0){
            yCol = (wall - r - y ) / YVelocity;
        }
        else if(YVelocity < 0){
            yCol =  (r - y) / YVelocity;
        }
        else{
            yCol = Double.POSITIVE_INFINITY;
        }
        return yCol;
    }

    public void velocityAfterHorizontalWallCollision(){
        YVelocity = - YVelocity;
    }

    public void velocityAfterVerticalWallCollision(){
        XVelocity = - XVelocity;
    }

    public double calculateWallCollision(double wall){
        double xCol;
        double yCol;

        if(XVelocity > 0){
            xCol = (wall - r - x ) / XVelocity;

        }
        else if(XVelocity < 0){
            xCol = (r - x) / XVelocity;
        }
        else{
            xCol = Double.POSITIVE_INFINITY;
        }

        if(YVelocity > 0){
            yCol = (wall - r - y ) / YVelocity;

        }
        else if(YVelocity < 0){
            yCol =  (r - y) / YVelocity;
        }
        else{
            yCol = Double.POSITIVE_INFINITY;
        }

        return Math.min(xCol,yCol);
    }

    public double calculateParticleCollision(Particle other){
        double dx = other.getX() - x;
        double dy = other.getY() - y;

        double dvx = other.getXVelocity() - XVelocity;
        double dvy = other.getYVelocity() - YVelocity;

        double dvdv = dvx*dvx + dvy*dvy;
        double drdr = dx*dx + dy*dy;
        double dvdr = dvx*dx + dvy*dy;
        double sigma = r+other.getR();
        double d = (dvdr*dvdr) - ( (dvdv)*(drdr - sigma*sigma));

        if(dvdr > 0){
            return Double.POSITIVE_INFINITY;
        }
        else if(dvdv == 0){
            return  Double.POSITIVE_INFINITY;
        }
        else if( d <0){
            return Double.POSITIVE_INFINITY;
        }
        else{
            return -(dvdr + Math.sqrt(d))/dvdv;
        }
    }

    public void velocityAfterWallCollision(double wall){

        if(x>=wall/2){
            if(y>=wall/2){
                if(x>y){
                    XVelocity = - XVelocity;
                }
                else{
                    YVelocity = -YVelocity;
                }
            }
            else if(y<wall/2){
                if (wall-x < y){
                    XVelocity = -XVelocity;
                }
                else{
                    YVelocity = -YVelocity;
                }
            }
        }
        else if(x<wall/2){
            if(y>=wall/2){
                if(x<wall-y){
                    XVelocity = -XVelocity;
                }
                else{
                    YVelocity = -YVelocity;
                }
            }
            else if(y<wall/2){
                if(x<y){
                    XVelocity = -XVelocity;
                }
                else {
                    YVelocity = -YVelocity;
                }
            }
        }
    }

    public void velocityAfterParticleCollision(Particle other){

        double dx = other.getX() - x;
        double dy = other.getY() - y;

        double dvx =  other.getXVelocity() - XVelocity;
        double dvy =  other.getYVelocity() - YVelocity;
        double dvdr = dvx*dx + dvy*dy;

        double sigma = r+other.getR();


        double j = (2*m*other.getM()*dvdr)/(sigma*(m+other.getM()));

        double jx = j*dx/sigma;
        double jy = j*dy/sigma;

        XVelocity +=  jx/m;
        YVelocity +=  jy/m;

        other.setXVelocity(other.getXVelocity() - jx/other.getM());
        other.setYVelocity(other.getYVelocity() - jy/other.getM());

    }

    public void updatePosition(double t){
        x = x + XVelocity*t;
        y = y + YVelocity*t;
    }

}
