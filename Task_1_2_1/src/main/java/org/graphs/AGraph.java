package org.graphs;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AGraph implements Graph {
    private ArrayList<ArrayList<Integer>> matrix;

    public AGraph() {
        matrix = new ArrayList<ArrayList<Integer>>();
    }

    @Override
    public void addEdge(int from, int to) {
        if (from >= matrix.size() || to >= matrix.size()
            || from < 0 || to < 0){
            return;
        }
        int val = matrix.get(from).get(to) + 1;
        matrix.get(from).set(to, val);
    }

    @Override
    public void deleteEdge(int from, int to) {
        if (from >= matrix.size() || to >= matrix.size()
                || from < 0 || to < 0){
            return;
        }
        int val = matrix.get(from).get(to) - 1;
        matrix.get(from).set(to, val);
    }

    @Override
    public int addVertex() {
        ArrayList<Integer> newVertexList = new ArrayList<Integer>();
        for (ArrayList<Integer> vertex : matrix) {
            newVertexList.add(0);
            vertex.add(0);
        }
        matrix.add(newVertexList);
        return matrix.size()-1;
    }

    @Override
    public void deleteVertex(int num) {
        if (num >= matrix.size() || num < 0) {
            return;
        }

        for (int i = 0; i < matrix.size(); i++) {
            if (i != num) {
                matrix.get(i).remove(num);
            }
        }
        matrix.remove(num);
    }

    @Override
    public ArrayList<Integer> neighboursOfVertex(int num) {
        if (num >= matrix.size() || num < 0) {
            return null;
        }
        ArrayList<Integer> ans = new ArrayList<Integer>();
        for (int i = 0; i < matrix.size(); i++) {
            ans.add(matrix.get(num).get(i));
        }
        return ans;
    }

    @Override
    public void toposort() {

    }

    @Override
    public void readFromFile(String path) throws IOException {
        List<String> file = Files.readAllLines(Path.of(path));
        int countVertices = 0;
        if (file.size() > 2) {
            if (!file.get(0).equals("Adjacency")){
                throw new IOException();
            }
            try {
                countVertices = Integer.parseInt(file.get(1));
            } catch (NumberFormatException e) {
                throw new IOException();
            }

            ArrayList<ArrayList<Integer>> newMatrix = new ArrayList<>();
            for (int i = 0; i < countVertices; i++) {
                newMatrix.add(new ArrayList<Integer>());
            }
            for (int i = 0; i < countVertices; i++) {
                String[] str = file.get(i+2).split(" ");
                if (str.length != countVertices) {
                    throw new IOException();
                }
                for (int j = 0; j < countVertices; j++) {
                    try {
                        newMatrix.get(j).add(Integer.parseInt(str[j]));
                    } catch (NumberFormatException e) {
                        throw new IOException();
                    }
                }
            }

            matrix = newMatrix;



        } else {
            throw new IOException();
        }

    }
}
