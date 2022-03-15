package com.mygdx.golf;

import org.mariuszgromada.math.mxparser.*;
import org.mariuszgromada.math.mxparser.mathcollection.*;

public class Derivation {
    //READ THIS FROM THE INPUT MODULE
    public static final Function f = new Function("h(x,y) = 0.5*(sin((x-y)/7)+0.9)");
    

    public static double derivativeX(double x, double y) {
        Argument xArg = new Argument("x", x);
        Argument yArg = new Argument("y", y);
        Expression e1 = new Expression("h(x,y)", f, xArg, yArg);
        return Calculus.derivative(e1, xArg, x, 3, 0.00000000000000001, 10);
    }

    public static double derivativeY(double x, double y) {
        Argument xArg = new Argument("x", x);
        Argument yArg = new Argument("y", y);
        Expression e1 = new Expression("h(x,y)", f, xArg, yArg);
        return Calculus.derivative(e1, yArg, y, 3, 0.00000000000000001, 10);
    }

    public static void main(String[] args) {
        System.out.println(f.getFunctionExpressionString());
        double x=1.0;
        double y = 2.0;
        System.out.println(derivativeX(x, y));
        System.out.println(derivativeY(x, y));
    }

}
