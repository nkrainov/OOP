package org.graphs;

import java.util.ArrayList;

public class IGraph implements Graph{
    private ArrayList<ArrayList<Integer>> matrix;

    public IGraph() {
        matrix = new ArrayList<ArrayList<Integer>>();
    }

    @Override
    public void addEdge(int from, int to) {
        if (from >= matrix.size() || to >= matrix.size()
                || from < 0 || to < 0){
            return;
        }

        if (from == to) {
            for (int i = 0; i < matrix.size(); i++) {
                if (i ==  from) {
                    matrix.get(i).add(2);
                } else {
                    matrix.get(i).add(0);
                }
            }
        } else {
            for (int i = 0; i < matrix.size(); i++) {
                if (i == from) {
                    matrix.get(i).add(1);
                } else if (i == to) {
                    matrix.get(i).add(-1);
                } else {
                    matrix.get(i).add(0);
                }
            }
        }
    }

    @Override
    public void deleteEdge(int from, int to) {
        if (from >= matrix.size() || to >= matrix.size()
                || from < 0 || to < 0){
            return;
        }
        for (int i = 0; i < matrix.size(); i++) {
            if (matrix.get(from).get(i) == 1
                && matrix.get(to).get(i) == -1) {
                for (ArrayList<Integer> list : matrix) {
                    list.remove(i);
                }
                return;
            }
        }
    }

    @Override
    public int addVertex() {
        ArrayList<Integer> newVertex = new ArrayList<Integer>();
        int size;
        if (matrix.isEmpty()) {
            size = 0;
        } else {
            size = matrix.getFirst().size();
        }

        for (int i = 0; i < size; i++) {
            newVertex.add(0);
        }
        matrix.add(newVertex);

        return matrix.size()-1;
    }

    @Override
    public void deleteVertex(int num) {
        if (num >= matrix.size() || num < 0) {
            return;
        }
        matrix.remove(num);
    }

    @Override
    public ArrayList<Integer> neighboursOfVertex(int num) {
        if (num >= matrix.size() || num < 0) {
            return null;
        }
        ArrayList<Integer> ans = new ArrayList<Integer>();
        for (int i = 0; i < matrix.get(num).size(); i++) {
            if (matrix.get(num).get(i) == 1) {
                for (int j = 0; j < matrix.size(); j++) {
                    if (matrix.get(j).get(i) == -1) {
                        ans.add(j);
                        break;
                    }
                }
            }
        }
        return ans;
    }

    @Override
    public void toposort() {

    }

    @Override
    public void readFromFile(String path) {

    }
}
