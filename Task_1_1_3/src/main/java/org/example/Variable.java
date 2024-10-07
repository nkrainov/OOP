package org.example;

import java.util.HashMap;
import java.util.Map;

public class Variable extends Expression{
    private String var;

    public Variable(String newVar){
        var = newVar;
    }

    public void print(){
        System.out.print(var);
    }

    public Expression derivative(){
        return new Number(1);
    }

    public int eval(String varVal){
        Map<String, Integer> map = new HashMap<String, Integer>();
        String str = varVal.replaceAll(" ", "");
        String[] variablesPairs = str.split(";");
        String[] variables;
        for (String pair : variablesPairs){
            variables = pair.split("=");
            if (variables[0].equals(var)){
                return Integer.parseInt(variables[1]);
            }
        }

        return 0;
    }

    @Override
    public Expression simplification() {
        return this.clone();
    }

    public boolean hasVars() {
        return true;
    }

    public boolean equals(Expression expr) {
        if (!(expr instanceof Variable)){
            return false;
        }

        Variable variable = (Variable) expr;
        return var.equals(variable.var);
    }
}
