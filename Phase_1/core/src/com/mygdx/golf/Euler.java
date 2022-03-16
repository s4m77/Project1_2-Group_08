package com.mygdx.golf;

import org.mariuszgromada.math.mxparser.Function;


public class Euler{
    
    FileInputManager f;
    
    //positions
    private double previousx, previousy, currentx, currenty;

    //speeds
    private double previousVx, previousVy, currentVx, currentVy;

    //accelerations
    private double previousAx, previousAy;

    //partial derivatives & heightprofile
    private double partialX, partialY;
    //private String z= "h(x,y) = e^(-((x*x)+(y*y))/40)";
    private String z= "h(x,y) = 0";
    Function h;

    State s;

    public final double TIMEMARGE; // step size
    private double Mk; 
    private double Ms;

    private double xLimit = 20.0, yLimit = 20.0;

    //constants
    static double GRAVITY= 9.81;
    

    public Euler(State s, Function h, double TIMEMARGE, FileInputManager f, double xLimit, double yLimit){
        this.s = s;
        this.previousx= s.getxPos(); this.previousy= s.getyPos();
        this.previousVx= s.getxVel(); this.previousVy= s.getyVel(); 
        this.previousAx= 0; this.previousAy= 0;
        this.h = h; z += h.getFunctionExpressionString();
        this.TIMEMARGE = TIMEMARGE;
        this.f = f;
        this.Mk = f.grassKinetic();
        this.Ms = f.grassStatic();

        this.xLimit = xLimit; this.yLimit = yLimit;
    }

    public void calcPartialDerivative(String z){
        //implement here calculation of partial derivative according to courseprofile
        //so derivativecalculator will return the partial derivative of a function
        //calcPartialDerivative needs to be called again every time there is a new x or y position
        Derivation.setFunction(z);

        this.partialX= Derivation.derivativeX(previousx, previousy); //example
        this.partialY= Derivation.derivativeY(previousx, previousy); //example
    }

    public void calculateMk(){}

    public void calculateMs(){}

    public void calcAcceleration(){
        //using previous speeds and the constant Mk and g and the partial derivative,
        //calculate the new acceleration, so currentAx and currentAy
        this.previousAx= (-1*GRAVITY*partialX) - Mk*GRAVITY*(previousVx/(Math.sqrt(previousVx*previousVx + previousVy*previousVy)));
        this.previousAy= (-1*GRAVITY*partialY) - Mk*GRAVITY*(previousVy/(Math.sqrt(previousVx*previousVx + previousVy*previousVy)));
    }

    public void calcSlidingAcceleration(){
        //using previous speeds and the constant Mk and g and the partial derivative,
        //calculate the new acceleration, so currentAx and currentAy
        //should the second term be negative or positive?
        this.previousAx= (-1*GRAVITY*partialX) + Mk*GRAVITY*(partialX/(Math.sqrt(partialX*partialX + partialY*partialY)));
        this.previousAy= (-1*GRAVITY*partialY) + Mk*GRAVITY*(partialY/(Math.sqrt(partialX*partialX + partialY*partialY)));
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
        this.previousVx= currentVx;
    }

    public void setPreviousVy(double currentVy){
        this.previousVy= currentVy;
    }

    public void setPreviousX(double currentx){
        this.previousx= currentx;
    }

    public void setPreviousY(double currenty){
        this.previousy= currenty;
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
        
        boolean check = previousVx>0 || previousVy>0;
        
        while(check){
            //check if ball went out of the field 
            if((previousx < 0 || previousx > xLimit )||(previousy<0 || previousy > yLimit)){
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

            check = previousVx>0 || previousVy>0;
        }
        checkSliding();

        s.setxPos(this.currentx); s.setyPos(this.currenty); s.setxVel(this.currentVx); s.setyVel(this.currentVy);
    }
}

