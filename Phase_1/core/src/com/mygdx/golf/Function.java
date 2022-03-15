package com.mygdx.golf;
/**
 * This class is used to represent a Function
 */
abstract class Function {
    
    public abstract Function derivate(String v);

    public Function multiply(Function f2){
        return new Multiplication(this, f2);
    }
    public Function add(Function f2){
        return new Addition(this, f2);
    }
    public Function division(Function f2){
        return new Division(this, f2);
    }

    public abstract Double getNumericalValue(Double value, String v);
}
/**
 * Representation of a Variable
 */
class Variable extends Function{
    private String var;
    public Variable(String var) {this.var = var;}

    public String getVar() {return this.var;}

    @Override
    public Function derivate(String v){
        if (var.equals(v)) return new Number("1");
        return new Number("0");
    }
    public String toString() { return this.var; }  

    public Double getNumericalValue(Double value, String v){
        return v.equals(var) ? value : 0;
    }
}
/**
 * Representation of a numerical value
 */
class Number extends Function{
    private String n;
    public Number(String n) {this.n = n;}

    public double getValue() {return Double.parseDouble(this.n);}

    @Override
    public Function derivate(String v) {
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
class Logarithm extends Function{

    private double base; private Function argument;

    Logarithm(double e, Function argument) {this.base = e; this.argument = argument;}

    public Double evaluateLog(double base, double argument) {return Math.log(argument)/Math.log(base);}
    
    @Override
    public Function derivate(String v) {
    
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
class Exponential extends Function{

    private Function f;
    public Exponential(Function f) {this.f = f;}
    
    @Override
    public Function derivate(String v) {return new Multiplication(new Exponential(f), f.derivate(v));}

    public String toString() {return "exp(" + f + ")";}

    @Override
    public Double getNumericalValue(Double value, String v) {
        return Math.exp(f.getNumericalValue(value, v));
    }
}
/**
 * Class to represent Trigs
 */
class Trigonometric extends Function{

    private String trig; private Function f;

    public Trigonometric(String trig, Function f) {this.trig = trig; this.f = f;}    
    
    @Override
    public Function derivate(String v) {

        if(this.trig.equals("sine"))
            return new Multiplication(new Trigonometric("cosine", f), f.derivate(v));
        else
            return new Multiplication(new Number("-1"), new Multiplication(new Trigonometric("sine", f), f.derivate(v)));
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
/**
 * Class for handeling addition
 */
class Addition extends Function{

    private Function f1, f2;
    public Addition(Function f1, Function f2) {this.f1 = f1; this.f2 = f2;}
    
    @Override
    public Function derivate(String v) {
        return new Addition(f1.derivate(v), f2.derivate(v));
    }
    public String toString() {return "[ " + f1.toString() + " + " + f2.toString() + " ]";}

    @Override
    public Double getNumericalValue(Double value, String v) {
        
        return f1.getNumericalValue(value, v) + f2.getNumericalValue(value, v);
    }
    
}
/**
 * Class for handeling multiplications 
 */
class Multiplication extends Function{

    private Function f1, f2;
    public Multiplication(Function f1, Function f2) { this.f1 = f1; this.f2 = f2;}
    
    @Override
    public Function derivate(String v) {
        
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
class Division extends Function{
    
    private Function f1, f2;
    public Division(Function f1, Function f2) {this.f1 = f1; this.f2 = f2;}

    @Override
    public Function derivate(String v) {
        return new Division(new Addition(new Multiplication(f1.derivate(v), f2), new Multiplication(f1, f2.derivate(v))), 
                            new Multiplication(f2, f2));
    }
    public String toString() {return "[ " + f1.toString() + " + " + f2.toString() + " ]";}

    @Override
    public Double getNumericalValue(Double value, String v) {
        
        return f1.getNumericalValue(value, v)/f2.getNumericalValue(value, v);
    }
    
    public static void main(String[] args) {
        Function x = new Variable("x"), y = new Variable("y"), a = new Number("-2"), b = new Number("3");

        Function f = x.multiply(a).add(y.multiply(b));
        System.out.println("Starting function: " + f);
        Function fx = f.derivate("x"), fy = f.derivate("y");
        System.out.println("d/dx = " + fx + "d/dy = " + fy);

        Double xTest = f.getNumericalValue(3.0, "x"), yTest = f.getNumericalValue(2.0, "y");
        System.out.println(xTest + " - " + yTest);

        
    }
}
