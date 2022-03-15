package com.mygdx.golf;

abstract class Operator {
    
    public abstract Operator derivate(String v);
    public Operator multiply(Operator f2){
        return new Multiplication(this, f2);
    }
    public Operator add(Operator f2){
        return new Addition(this, f2);
    }
    
}
class Variable extends Operator{
    private String var;
    public Variable(String var) {this.var = var;}

    @Override
    public Operator derivate(String v){
        if (var.equals(v)) return new Number(1);
        return new Number(0);
    }
    public String toString() { return this.var; }  
}
class Number extends Operator{
    private double n;
    public Number(int n) {this.n = n;}

    @Override
    public Operator derivate(String v) {
        return new Number(0);
    }
    public String toString() {return Double.toString(this.n);}

}
class Addition extends Operator{

    private Operator f1, f2;
    public Addition(Operator f1, Operator f2) {this.f1 = f1; this.f2 = f2;}
    
    @Override
    public Operator derivate(String v) {
        return new Addition(f1.derivate(v), f2.derivate(v));
    }
    public String toString() {return "[ " + f1.toString() + " + " + f2.toString() + " ]";}

    
}
class Multiplication extends Operator{

    private Operator f1, f2;
    public Multiplication(Operator f1, Operator f2) { this.f1 = f1; this.f2 = f2;}
    
    @Override
    public Operator derivate(String v) {
        
        return new Addition(new Multiplication(f1, f2.derivate(v)), new Multiplication(f1, f2.derivate(v)));
    }
    public String toString() {return "[ " + f1.toString() + " + " + f2.toString() + " ]";}
}
