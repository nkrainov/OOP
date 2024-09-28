package org.example;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        Expression e = Expression.parse("(((x * (0 + 1))))");
        Expression s = e.simplification();
        s.print();
    }
}