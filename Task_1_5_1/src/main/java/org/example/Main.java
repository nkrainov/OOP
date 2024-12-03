package org.example;

import org.markdown.*;

public class Main {
    public static void main(String[] args) {
        List.Builder builder = new List.Builder(List.Builder.TASKLIST);
        builder.add(new Text.Bold("rrrr"));
        builder.add(new Text.Bold("rrrr"));
        builder.add(new Text.Bold("rrrr"));
        builder.setVal(true, 1);
        System.out.println(builder.build());
    }
}