package com.mygdx.golf;

import org.mariuszgromada.math.mxparser.Function;

public class Euler {

    FileInputManager f;

    // positions
    private double previousx, previousy, currentx, currenty;

    // speeds
    private double previousVx, previousVy, currentVx, currentVy;

    // accelerations
    private double previousAx, previousAy;

    // partial derivatives & heightprofile
    private double partialX, partialY;
    // private String z= "h(x,y) = e^(-((x*x)+(y*y))/40)";
    private String z = "h(x,y) = ";
    Function h;

    State s;

    public final double TIMEMARGE; // step size
    private double Mk;
    private double Ms;

    private double xLimit = 20.0, yLimit = 20.0;

    // constants
    static double GRAVITY = 9.81;

    public Euler(State s, Function h, double TIMEMARGE, FileInputManager f, double xLimit, double yLimit) {
        this.s = s;
        this.previousx = s.getxPos();
        this.previousy = s.getyPos();
        this.previousVx = s.getxVel();
        this.previousVy = s.getyVel();
        this.previousAx = 0;
        this.previousAy = 0;
        this.h = h;
        z += h.getFunctionExpressionString();
        this.TIMEMARGE = TIMEMARGE;
        this.f = f;
        this.Mk = f.grassKinetic();
        this.Ms = f.grassStatic();

        this.xLimit = xLimit;
        this.yLimit = yLimit;
    }

    public void calcPartialDerivative(String z) {
        // implement here calculation of partial derivative according to courseprofile
        // so derivativecalculator will return the partial derivative of a function
        // calcPartialDerivative needs to be called again every time there is a new x or
        // y position
        Derivation.setFunction(z);

        this.partialX = Derivation.derivativeX(previousx, previousy); // example
        this.partialY = Derivation.derivativeY(previousx, previousy); // example
    }

    public void calculateMk(){
        double[] x = f.sandPitX(), y = f.sandPitY();
        double xPos = s.getxPos(), yPos = s.getyPos();
        if (((xPos > x[0]) && (xPos < x[1])) && ((yPos > y[0]) && (yPos < y[1])))
            this.Mk = f.sandKinetic();
        else 
            this.Mk = f.grassKinetic();
    }    

    public void calculateMs(){
        double[] x = f.sandPitX(), y = f.sandPitY();
        double xPos = s.getxPos(), yPos = s.getyPos();
        if (((xPos > x[0]) && (xPos < x[1])) && ((yPos > y[0]) && (yPos < y[1])))
            this.Ms = f.sandStatic();
        else 
            this.Ms = f.grassStatic();
    }

    public void calcAcceleration(){
        //using previous speeds and the constant Mk and g and the partial derivative,
        //calculate the new acceleration, so currentAx and currentAy
        calculateMk(); //check if on grass or sand
        this.previousAx= (-1*GRAVITY*partialX) - Mk*GRAVITY*(previousVx/(Math.sqrt(previousVx*previousVx + previousVy*previousVy)));
        this.previousAy= (-1*GRAVITY*partialY) - Mk*GRAVITY*(previousVy/(Math.sqrt(previousVx*previousVx + previousVy*previousVy)));
    }

    public void calcSlidingAcceleration(){
        //using previous speeds and the constant Mk and g and the partial derivative,
        //calculate the new acceleration, so currentAx and currentAy
        //should the second term be negative or positive?
        calculateMk(); // check if on grass or sand
        this.previousAx= (-1*GRAVITY*partialX) + Mk*GRAVITY*(partialX/(Math.sqrt(partialX*partialX + partialY*partialY)));
        this.previousAy= (-1*GRAVITY*partialY) + Mk*GRAVITY*(partialY/(Math.sqrt(partialX*partialX + partialY*partialY)));
    }

   
    public void calcVelocity() {
        this.currentVx = previousVx + TIMEMARGE * previousAx;
        this.currentVy = previousVy + TIMEMARGE * previousAy;
    }

    public void calcNewPos() {
        this.currentx = previousx + TIMEMARGE * previousVx;
        this.currenty = previousy + TIMEMARGE * previousVy;
    }


    public void setPreviousState(double currentx,double currenty,double currentVx,double currentVy) {
        this.previousx = currentx;
        this.previousy = currenty;
        this.previousVx = currentVx;
        this.previousVy = currentVy;
    }

    public void checkSliding() {
        if (partialX == 0 && partialY == 0) {
            // ball stays in rest
            return;
        }
        else if(partialX !=0 || partialY != 0){
            
            calculateMs(); // check if on grass or sand
            
            if(Ms> Math.sqrt(partialX*partialX + partialY*partialY)){
                //ball stays in rest
            }
            else{
                //ball slides
                while(Ms<= Math.sqrt(partialX*partialX + partialY*partialY)){
                    
                    calculateMs();//check if on grass or sand

                    calcPartialDerivative(z);
                    calcSlidingAcceleration();
                    calcVelocity();
                    calcNewPos();
                    System.out.println(currentx);
                    System.out.println(currenty);
                    setPreviousState(currentx, currenty, currentVx, currentVy);

                    calcPartialDerivative(z);
                }
            }
        }
        // ball slides
        while (Ms <= Math.sqrt(partialX * partialX + partialY * partialY)) {
          
            // this is for negative values to not eternally keep running
            calcPartialDerivative(z);
            calcSlidingAcceleration();
            calcVelocity();
            calcNewPos();
            System.out.println(currentx);
            System.out.println(currenty);
            setPreviousState(currentx, currenty, currentVx, currentVy);
        }

    }

    public void giveSolution() {


        while (previousVx > 0 || previousVy > 0) {
            // check if ball went out of the field
            if ((previousx < 0 || previousx > xLimit) || (previousy < 0 || previousy > yLimit)) {
                return;
            }
            
            calcPartialDerivative(z);
            calcAcceleration();
            calcVelocity();
            calcNewPos();
            System.out.println(currentx);
            System.out.println(currenty);
            setPreviousState(currentx, currenty, currentVx, currentVy);

            s.setxPos(this.currentx); s.setyPos(this.currenty); s.setxVel(this.currentVx); s.setyVel(this.currentVy);

        }
        checkSliding();

        s.setxPos(this.currentx);
        s.setyPos(this.currenty);
        s.setxVel(this.currentVx);
        s.setyVel(this.currentVy);
    }
    
}