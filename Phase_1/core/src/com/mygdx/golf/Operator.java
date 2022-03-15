package com.mygdx.golf;

abstract class Operator {
    
    public abstract Operator derivate(String v);
    public Operator multiply(Operator f2){
        return new Multiplication(this, f2);
    }
    public Operator add(Operator f2){
        return new Addition(this, f2);
    }
    public Operator division(Operator f2){
        return new Division(this, f2);
    }

    public abstract Double getNumericalValue(Double value, String v);

}
class Variable extends Operator{
    private String var;
    public Variable(String var) {this.var = var;}

    public String getVar() {return this.var;}

    @Override
    public Operator derivate(String v){
        if (var.equals(v)) return new Number("1");
        return new Number("0");
    }
    public String toString() { return this.var; }  

    public Double getNumericalValue(Double value, String v){
        return v.equals(var) ? value : 0;
    }
}
class Number extends Operator{
    private String n;
    public Number(String n) {this.n = n;}

    public double getValue() {return Double.parseDouble(this.n);}

    @Override
    public Operator derivate(String v) {
        return new Number("0");
    }
    public String toString() {return this.n;}

    public Double getNumericalValue(Double value, String v){
        return Double.parseDouble(this.n);
    }

}
/**
 * Class for handeling Logarithms 
 */
class Logarithm extends Operator{

    private double base; private Operator argument;

    Logarithm(double e, Operator argument) {this.base = e; this.argument = argument;}

    public Double evaluateLog(double base, double argument) {return Math.log(argument)/Math.log(base);}
    
    @Override
    public Operator derivate(String v) {
    
        return new Division(argument.derivate(v), new Multiplication(new Number(evaluateLog(Math.E, this.base).toString()), this.argument));
    }
    public String toString() {return "Log(" + base + ", " + argument + ")";}

    @Override
    public Double getNumericalValue(Double value, String v) {

        return evaluateLog(this.base, argument.getNumericalValue(value, v));
    }
}
/**
 * Class to represent Exponentials
 */
class Exponential extends Operator{

    private Operator f;
    public Exponential(Operator f) {this.f = f;}
    
    @Override
    public Operator derivate(String v) {return new Multiplication(new Exponential(f), f.derivate(v));}

    public String toString() {return "exp(" + f + ")";}

    @Override
    public Double getNumericalValue(Double value, String v) {
        return Math.exp(f.getNumericalValue(value, v));
    }
}
/**
 * Class to represent Trigs
 */
class Trigonometric extends Operator{

    private String trig; private Operator f;

    public Trigonometric(String trig, Operator f) {this.trig = trig; this.f = f;}    
    
    @Override
    public Operator derivate(String v) {
        return new Number("0");
    }
    public String toString() {return this.trig + "(" + f + ")";}

    @Override
    public Double getNumericalValue(Double value, String v) {
    
        if (this.trig.equals("sine"))
            return Math.sin(f.getNumericalValue(value, v));
        else
            return Math.cos(f.getNumericalValue(value, v));
    }
}
class Addition extends Operator{

    private Operator f1, f2;
    public Addition(Operator f1, Operator f2) {this.f1 = f1; this.f2 = f2;}
    
    @Override
    public Operator derivate(String v) {
        return new Addition(f1.derivate(v), f2.derivate(v));
    }
    public String toString() {return "[ " + f1.toString() + " + " + f2.toString() + " ]";}

    @Override
    public Double getNumericalValue(Double value, String v) {
        
        return f1.getNumericalValue(value, v) + f2.getNumericalValue(value, v);
    }
    
}
class Multiplication extends Operator{

    private Operator f1, f2;
    public Multiplication(Operator f1, Operator f2) { this.f1 = f1; this.f2 = f2;}
    
    @Override
    public Operator derivate(String v) {
        
        return new Addition(new Multiplication(f1.derivate(v), f2), new Multiplication(f1, f2.derivate(v)));
    }
    public String toString() {return "[ " + f1.toString() + " * " + f2.toString() + " ]";}

    @Override
    public Double getNumericalValue(Double value, String v) {
        
        return f1.getNumericalValue(value, v)*f2.getNumericalValue(value, v);
    }

}
/**
 * Class for handeling divisions
 */
class Division extends Operator{
    
    private Operator f1, f2;
    public Division(Operator f1, Operator f2) {this.f1 = f1; this.f2 = f2;}

    @Override
    public Operator derivate(String v) {
        return new Division(new Addition(new Multiplication(f1.derivate(v), f2), new Multiplication(f1, f2.derivate(v))), 
                            new Multiplication(f2, f2));
    }
    public String toString() {return "[ " + f1.toString() + " + " + f2.toString() + " ]";}

    @Override
    public Double getNumericalValue(Double value, String v) {
        
        return f1.getNumericalValue(value, v)/f2.getNumericalValue(value, v);
    }
    
    public static void main(String[] args) {
        Operator x = new Variable("x"), y = new Variable("y"), a = new Number("-2"), b = new Number("3");

        Operator f = x.multiply(a).add(y.multiply(b));
        System.out.println("Starting Operator: " + f);
        Operator fx = f.derivate("x"), fy = f.derivate("y");
        System.out.println("d/dx = " + fx + "d/dy = " + fy);

        Double xTest = f.getNumericalValue(3.0, "x"), yTest = f.getNumericalValue(2.0, "y");
        System.out.println(xTest + " - " + yTest);
        
    }
}
/**
 * Class for handeling powers
 */
class Power extends Operator{

    Operator f1, f2;

    public Power(Operator f1, Operator f2) {this.f1 = f1; this.f2 = f2;}

    @Override
    public Operator derivate(String v) {
    
        return new Multiplication(new Power(f1, f2), new Addition(new Multiplication(f1.derivate(v), new Logarithm(Math.E, f1)), 
                                  new Division(new Multiplication(f2, f1.derivate(v)), f1)));
    }

    @Override
    public Double getNumericalValue(Double value, String v) {
        
        return Math.pow(f1.getNumericalValue(value, v), f2.getNumericalValue(value, v));
    }
    
}
