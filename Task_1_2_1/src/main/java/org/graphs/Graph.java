package org.graphs;

import java.io.IOException;
import java.util.ArrayList;

public interface Graph {
    int addVertex();

    void deleteVertex(int num);

    void addEdge(int from, int to);

    void deleteEdge(int from, int to);

    ArrayList<Integer> neighboursOfVertex(int num);

    void readFromFile(String path) throws IOException;

    ArrayList<Integer> toposort();

}
