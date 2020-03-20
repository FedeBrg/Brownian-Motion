public class ParticleImpl implements Particle{
    private double x;
    private double y;
    private double r;
    private double x0;
    private double y0;

    private double XVelocity;
    private double YVelocity;
    private int id;

    public ParticleImpl(double x, double y, double r, int id) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.id = id;
    }

    public double getXVelocity() {
        return XVelocity;
    }

    public double getYVelocity() {
        return YVelocity;
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

        return (dist-getR()-other.getR())<0;
    }

    private double calculateDistance(Particle p) {
        double toReturn = Math.sqrt(Math.pow(p.getX()-getX(),2) + Math.pow(p.getY()-getY(),2)) ;
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


    public double calculateWallCollision(double wall){
        double xCol;
        double yCol;

        if(XVelocity > 0){
            xCol = (wall - r - x0 ) / XVelocity;

        }
        else if(XVelocity < 0){
            xCol = (r - x0) / XVelocity;
        }
        else{
            xCol = Double.POSITIVE_INFINITY;
        }

        if(YVelocity > 0){
            yCol = (wall - r - x0 ) / YVelocity;

        }
        else if(YVelocity < 0){
            yCol =  (r - x0) / YVelocity;
        }
        else{
            yCol = Double.POSITIVE_INFINITY;
        }

        return Math.min(xCol,yCol);
    }

    public double calculateParticleCollision(Particle other){
        double dx = x - other.getX();
        double dy = y - other.getY();

        double dvx = XVelocity - other.getXVelocity();
        double dvy = YVelocity - other.getYVelocity();

        double dvdv = dvx*dvx + dvy*dvy;
        double drdr = dx*dx + dy*dy;
        double dvdr = dvx*dx + dvy*dy;
        double sigma = r+other.getR();
        double d = dvdr*dvdr - ( (dvdv)*(drdr - sigma*sigma));

        if(dvdr >= 0){
            return Double.POSITIVE_INFINITY;
        }
        else if( d <0){
            return Double.POSITIVE_INFINITY;
        }
        else{
            return (dvdr - Math.sqrt(d))/dvdv;
        }
    }
}
