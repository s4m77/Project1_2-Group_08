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
}
/**
 * Representation of a Variable
 */
class Variable extends Function{
    private String var;
    public Variable(String var) {this.var = var;}

    @Override
    public Function derivate(String v){
        if (var.equals(v)) return new Number(1);
        return new Number(0);
    }
    public String toString() { return this.var; }  
}
/**
 * Representation of a numerical value
 */
class Number extends Function{
    private double n;
    public Number(double n) {this.n = n;}

    @Override
    public Function derivate(String v) {
        return new Number(0);
    }
    public String toString() {return Double.toString(this.n);}

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

    public static void main(String[] args) {
        Function x = new Variable("x"), a = new Number(-2),
                 y = new Variable("y"), b = new Number(-1);
        Function f = a.multiply(x).multiply(x).add(b.multiply(y));
        System.out.println(f.toString());
        Function f1 = f.derivate("x");
        Function f2 = f.derivate("y");
        System.err.println(f1.toString());
        System.err.println(f2.toString());
    }   
}
