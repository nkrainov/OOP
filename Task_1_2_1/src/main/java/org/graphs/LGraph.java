package org.graphs;

import java.util.ArrayList;
import java.util.HashMap;

public class LGraph implements Graph{
    private HashMap<Integer, HashMap<Integer, Integer>> vertices;
    private int nextVertex;

    public LGraph() {
        vertices = new HashMap<Integer, HashMap<Integer, Integer>>();
        nextVertex = 0;
    }

    @Override
    public void addEdge(int from, int to) {
        if (!vertices.containsKey(from) || !vertices.containsKey(to)){
            return;
        }

        HashMap<Integer, Integer> vertex = vertices.get(from);
        if (!vertex.containsKey(to)) {
            vertex.put(to, 1);
        }
        vertex.put(to, vertex.get(to)+1);
    }

    @Override
    public void deleteEdge(int from, int to) {
        if (!vertices.containsKey(from) || !vertices.containsKey(to)){
            return;
        }

        HashMap<Integer, Integer> vertex = vertices.get(from);
        if (vertex.get(to) != null) {
            if (vertex.get(to) == 1) {
                vertex.remove(to);
            } else {
                vertex.put(to, vertex.get(to)-1);
            }
        }

    }

    @Override
    public int addVertex() {
        vertices.put(nextVertex, new HashMap<Integer, Integer>());
        do {
            nextVertex++;
        } while (vertices.containsKey(nextVertex));
        return nextVertex-1;
    }

    @Override
    public void deleteVertex(int num) {
        vertices.remove(num);
        nextVertex = num;
    }

    @Override
    public ArrayList<Integer> neighboursOfVertex(int num) {
        if (!vertices.containsKey(num)) {
            return null;
        }
        return new ArrayList<Integer>(vertices.get(num).keySet());
    }

    @Override
    public void toposort() {

    }

    @Override
    public void readFromFile(String path) {

    }
}
