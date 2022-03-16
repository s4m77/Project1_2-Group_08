package com.mygdx.golf;

import org.mariuszgromada.math.mxparser.*;
import org.mariuszgromada.math.mxparser.mathcollection.*;

public class Derivation {
    //READ THIS FROM THE INPUT MODULE
    public static Function f = new Function("h(x,y) = 0.5*(sin((x-y)/7)+0.9)");
    
    public static void setFunction(String funcString) {
        f = new Function(funcString);
    }
    public static double derivativeX(double x, double y) {
        Argument xArg = new Argument("x", x);
        Argument yArg = new Argument("y", y);
        Expression e1 = new Expression("h(x,y)", f, xArg, yArg);
        return Calculus.derivative(e1, xArg, x, 3, 0.00000000000000001, 10);
    }
    public static double derivativeX(Function f, double x, double y) {
        Argument xArg = new Argument("x", x);
        Argument yArg = new Argument("y", y);
        Expression e1 = new Expression("h(x,y)",f, xArg, yArg);
        return Calculus.derivative(e1, xArg, x, 3, 0.00000000000000001, 10);
    }

    public static double derivativeY(double x, double y) {
        Argument xArg = new Argument("x", x);
        Argument yArg = new Argument("y", y);
        Expression e1 = new Expression("h(x,y)", f, xArg, yArg);
        return Calculus.derivative(e1, yArg, y, 3, 0.00000000000000001, 10);
    }
    public static double derivativeY(Function f, double x, double y) {
        Argument xArg = new Argument("x", x);
        Argument yArg = new Argument("y", y);
        Expression e1 = new Expression("h(x,y)", f, xArg, yArg);
        return Calculus.derivative(e1, yArg, y, 3, 0.00000000000000001, 10);
    }
    public static float calculateHeight(float x, float y) {
        Expression exp = new Expression("h(" + x + "," + y + ")", f);
        return (float) exp.calculate();
    }

    public static void main(String[] args) {
        System.out.println(f.getFunctionExpressionString());
        System.out.println(f.getDescription());
        System.out.println("Resutl :" + calculateHeight(2,2));
        System.out.println("Resut2 :" + f.calculate(2,2));
        double x=1.0;
        double y = 2.0;
        System.out.println(derivativeX(f, x, y));
        System.out.println(derivativeX(x, y));
        // System.out.println(derivativeY(x, y));
    }

}
