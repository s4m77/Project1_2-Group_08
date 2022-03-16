import com.mygdx.golf.Derivation;

public class Euler{
    
    //positions
    private double previousx, previousy, currentx, currenty;

    //speeds
    private double previousVx, previousVy, currentVx, currentVy;

    //accelerations
    private double previousAx, previousAy, currentAx, currentAy;

    //partial derivatives & heightprofile
    private double partialX, partialY;
    //private String z= "h(x,y) = e^(-((x*x)+(y*y))/40)";
    private String z= "h(x,y) = 2*x*x";

    //constants
    static double GRAVITY= 9.81;
    static double TIMEMARGE= 0.1; // step
    static double COURSEPROFILE= 0; //heightprofile / z
    static double Mk= 0.1; 
    static double Ms= 0.2;

    public Euler(double initialX, double initialY, double initialVx, double initialVy){
        this.previousx= initialX;
        this.previousy= initialY;
        this.previousVx= initialVx; //get input from inputfile with initial v's
        this.previousVy= initialVy; //get input from inputfile with initial v's
        this.previousAx= 0;
        this.previousAy= 0;
    }

    public void calcPartialDerivative(String z){
        //implement here calculation of partial derivative according to courseprofile
        //so derivativecalculator will return the partial derivative of a function
        //calcPartialDerivative needs to be called again every time there is a new x or y position
        Derivation.setFunction(z);

        this.partialX= Derivation.derivativeX(previousx, previousy); //example
        this.partialY= Derivation.derivativeY(previousx, previousy); //example
    }

    public void calcAcceleration(){
        //using previous speeds and the constant Mk and g and the partial derivative,
        //calculate the new acceleration, so currentAx and currentAy
        previousAx= (-1*GRAVITY*partialX) - Mk*GRAVITY*(previousVx/(Math.sqrt(previousVx*previousVx + previousVy*previousVy)));
        previousAy= (-1*GRAVITY*partialY) - Mk*GRAVITY*(previousVy/(Math.sqrt(previousVx*previousVx + previousVy*previousVy)));
    }

    public void calcSlidingAcceleration(){
        //using previous speeds and the constant Mk and g and the partial derivative,
        //calculate the new acceleration, so currentAx and currentAy
        //should the second term be negative or positive?
        previousAx= (-1*GRAVITY*partialX) + Mk*GRAVITY*(partialX/(Math.sqrt(partialX*partialX + partialY*partialY)));
        previousAy= (-1*GRAVITY*partialY) + Mk*GRAVITY*(partialY/(Math.sqrt(partialX*partialX + partialY*partialY)));
    }

    public void calcSpeed(){
        this.currentVx= previousVx + TIMEMARGE*previousAx;
        this.currentVy= previousVy + TIMEMARGE*previousAy;
    }

    public void calcNewPos(){
        this.currentx= previousx + TIMEMARGE*previousVx;
        this.currenty= previousy + TIMEMARGE*previousVy;
    }

    public void setPreviousVx(double currentVx){
        previousVx= currentVx;
    }

    public void setPreviousVy(double currentVy){
        previousVy= currentVy;
    }

    public void setPreviousX(double currentx){
        previousx= currentx;
    }

    public void setPreviousY(double currenty){
        previousy= currenty;
    }

    public void checkSliding(){
        if(partialX==0 && partialY==0){
            //ball stays in rest
        }
        else if(partialX !=0 || partialY != 0){
            if(Ms> Math.sqrt(partialX*partialX + partialY*partialY)){
                //ball stays in rest
            }
            else{
                //ball slides
                while(Ms<= Math.sqrt(partialX*partialX + partialY*partialY)){
                    /*if(previousx< 0 || previousy<0){
                        return;
                    }*/
                    //this is for negative values to not eternally keep running
                    calcPartialDerivative(z);
                    calcSlidingAcceleration();
                    calcSpeed();
                    calcNewPos();
                    System.out.println(currentx);
                    System.out.println(currenty);
                    setPreviousVx(currentVx);
                    setPreviousVy(currentVy);
                    setPreviousX(currentx);
                    setPreviousY(currenty);
                    calcPartialDerivative(z);
                    }
            }
        }
    }

    public void giveSolution(){
        while(previousVx>0 || previousVy>0){
            if(previousx< 0 || previousy<0){
                return;
            }
            calcPartialDerivative(z);
            calcAcceleration();
            calcSpeed();
            calcNewPos();
            System.out.println(currentx);
            System.out.println(currenty);
            setPreviousVx(currentVx);
            setPreviousVy(currentVy);
            setPreviousX(currentx);
            setPreviousY(currenty);
            calcPartialDerivative(z);
        }
            checkSliding();
    }

    
    /*public double getPreviousVx(){
        return previousVx;
    }

    public double getPreviousVy(){
        return previousVy;
    }

    public double getX(){
        return currentx;
    }

    public double getY(){
        return currenty;
    }

    public double getCurrentVx(){
        return currentVx;
    }

    public double getCurrentVy(){
        return currentVy;;s
    }*/

    public static void main(String[] args) {
        Euler eul= new Euler(0, 0, 1, 0);
        eul.giveSolution();
        //eng.giveSolution();

        /*while(eng.getPreviousVx()>0 || eng.getPreviousVy()>0){
            eng.calcPartialDerivative(0);
            eng.calcAcceleration();
            eng.calcSpeed();
            eng.calcNewPos();
            System.out.println(eng.getX());
            System.out.println(eng.getY());
            eng.setPreviousVx(eng.getCurrentVx());
            eng.setPreviousVy(eng.getCurrentVy());
            eng.setPreviousX(eng.getX());
            eng.setPreviousY(eng.getY());
            eng.calcPartialDerivative(z);
        }
    
            //check for sliding
            eng.checkSliding();
            */
    }
}