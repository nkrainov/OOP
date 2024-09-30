package org.example;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        Expression e = new Add(new Number(1), new Number(3));
        Expression s = new Sub(new Number(1), new Number(3));
        System.out.println(e.equals(s));
    }
}