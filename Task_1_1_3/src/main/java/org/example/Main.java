package org.example;

import org.expressions.Expression;
import org.parse.Parser;

public class Main {
    public static void main(String[] args) {
        Expression expr = Parser.parse("367254 + 456*(X-43*y) / 456");
        expr.print();
    }
}