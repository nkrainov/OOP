package org.graphs;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        IGraph f = new IGraph();
        System.out.println(f.toposort());
    }
}