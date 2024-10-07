package org.example;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        Expression expr = Expression.parse("d22=d +");
        expr.print();
    }
}