package com.mygdx.golf;

import org.mariuszgromada.math.mxparser.*;
import org.mariuszgromada.math.mxparser.mathcollection.*;

public class Derivation {
  
    //derives a multivariate function with respect to X
    public static double derivativeX(Function f, double x, double y) {
        Argument xArg = new Argument("x", x);
        Argument yArg = new Argument("y", y);
        Expression e1 = new Expression("h(x,y)",f, xArg, yArg);
        return Calculus.derivative(e1, xArg, x, 3, 0.00000000000000001, 10);
    }
    
    //derives a multivariate function with respect to Y
    public static double derivativeY(Function f, double x, double y) {
        Argument xArg = new Argument("x", x);
        Argument yArg = new Argument("y", y);
        Expression e1 = new Expression("h(x,y)", f, xArg, yArg);
        return Calculus.derivative(e1, yArg, y, 3, 0.00000000000000001, 10);
    }
    

    public static void main(String[] args) {
        
        
    }

}
