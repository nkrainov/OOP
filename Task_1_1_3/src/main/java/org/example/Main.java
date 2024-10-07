package org.example;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        Expression expr = Expression.parse("367254 + 456*(X-43*y) / 456");
        expr.print();
    }
}