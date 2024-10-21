package org.graphs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Класс, реализующий граф со списком смежности.
 */
public class LGraph implements Graph {
    private HashMap<Integer, HashMap<Integer, Integer>> vertices;
    private int nextVertex;

    /**
     * Конструктор пустого графа.
     */
    public LGraph() {
        vertices = new HashMap<Integer, HashMap<Integer, Integer>>();
        nextVertex = 0;
    }

    /**
     * Конструктор графа из файла.
     */
    public LGraph(String path) throws IOException {
        readFromFile(path);
    }

    /**
     * Функция добавления ребра.
     */
    @Override
    public void addEdge(int from, int to) {
        if (!vertices.containsKey(from) || !vertices.containsKey(to)) {
            return;
        }

        HashMap<Integer, Integer> vertex = vertices.get(from);
        if (!vertex.containsKey(to)) {
            vertex.put(to, 1);
            return;
        }
        vertex.put(to, vertex.get(to) + 1);
    }

    /**
     * Функция удаления ребра.
     */
    @Override
    public void deleteEdge(int from, int to) {
        if (!vertices.containsKey(from) || !vertices.containsKey(to)) {
            return;
        }

        HashMap<Integer, Integer> vertex = vertices.get(from);
        if (vertex.get(to) != null) {
            if (vertex.get(to) == 1) {
                vertex.remove(to);
            } else {
                vertex.put(to, vertex.get(to) - 1);
            }
        }

    }

    /**
     * Функция добавления вершины.
     */
    @Override
    public int addVertex() {
        vertices.put(nextVertex, new HashMap<Integer, Integer>());
        do {
            nextVertex++;
        } while (vertices.containsKey(nextVertex));
        return nextVertex - 1;
    }

    /**
     * Функция удаления вершины.
     */
    @Override
    public void deleteVertex(int num) {
        vertices.remove(num);
        nextVertex = num;
    }

    /**
     * Функция, возвращающая список соседей вершины.
     */
    @Override
    public ArrayList<Integer> neighboursOfVertex(int num) {
        if (!vertices.containsKey(num)) {
            return null;
        }
        return new ArrayList<Integer>(vertices.get(num).keySet());
    }

    /**
     * Функция, возвращающая топологически отсортированный список вершин.
     */
    @Override
    public ArrayList<Integer> toposort() {
        HashMap<Integer, Integer> colors = new HashMap<>();
        ArrayList<Integer> sort = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            colors.put(i, 0);
        }
        for (int i = 0; i < vertices.size(); i++) {
            boolean res = dfsToposort(colors, sort, i);
            if (!res) {
                return null;
            }
        }
        Collections.reverse(sort);
        return sort;
    }

    private boolean dfsToposort(HashMap<Integer, Integer> colors, ArrayList<Integer> sort, Integer vertex) {
        if (colors.get(vertex) != 0) {
            return true;
        }
        colors.put(vertex, 1);
        for (Integer vert : vertices.get(vertex).keySet()) {
            if (colors.get(vert) == 1) {
                return false;
            } else if (colors.get(vert) == 0) {
                boolean res = dfsToposort(colors, sort, vert);
                if (!res) {
                    return false;
                }
            }
        }
        colors.put(vertex, 2);
        sort.add(vertex);

        return true;
    }

    /**
     * Функция чтения графа из файла.
     */
    @Override
    public void readFromFile(String path) throws IOException {
        List<String> file = Files.readAllLines(Path.of(path));
        int countVertices = 0, countEdges = 0;
        if (file.size() > 2) {
            if (!file.get(0).equals("Adjacency list")) {
                throw new IOException();
            }

            try {
                countVertices = Integer.parseInt(file.get(1));
            } catch (NumberFormatException e) {
                throw new IOException();
            }

            HashMap<Integer, HashMap<Integer, Integer>> newMatrix = new HashMap<>();
            for (int i = 0; i < countVertices; i++) {
                newMatrix.put(i, new HashMap<Integer, Integer>());
            }

            for (int i = 0; i < countVertices; i++) {
                String[] str = file.get(i + 2).split(" ");

                try {
                    countEdges = Integer.parseInt(str[0]);
                } catch (NumberFormatException e) {
                    throw new IOException();
                }

                if (str.length != countEdges + 1) {
                    throw new IOException();
                }

                for (int j = 0; j < countEdges; j++) {
                    try {
                        if (newMatrix.get(i).containsKey(Integer.parseInt(str[j + 1]))) {
                            newMatrix.get(i).put(Integer.parseInt(str[j + 1]), newMatrix.get(i).get(Integer.parseInt(str[j + 1])) + 1);
                        } else {
                            newMatrix.get(i).put(Integer.parseInt(str[j + 1]), 1);
                        }

                    } catch (NumberFormatException e) {
                        throw new IOException();
                    }
                }
            }
            vertices = newMatrix;
        } else {
            throw new IOException();
        }
    }
}
