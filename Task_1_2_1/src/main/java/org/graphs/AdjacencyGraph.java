package org.graphs;

import org.exceptions.IncorrectFormat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Класс, реализующий граф с матрицей смежности.
 */
public class AdjacencyGraph implements Graph {
    private ArrayList<ArrayList<Integer>> matrix;

    /**
     * Конструктор пустого графа.
     */
    public AdjacencyGraph() {
        matrix = new ArrayList<ArrayList<Integer>>();
    }

    /**
     * Конструктор графа из файла.
     */
    public AdjacencyGraph(String path) throws IOException {
        readFromFile(path);
    }

    /**
     * Функция добавления ребра.
     */
    @Override
    public void addEdge(int from, int to) {
        if (from >= matrix.size() || to >= matrix.size()
                || from < 0 || to < 0) {
            return;
        }
        int val = matrix.get(from).get(to) + 1;
        matrix.get(from).set(to, val);
    }

    /**
     * Функция удаления ребра.
     */
    @Override
    public void deleteEdge(int from, int to) {
        if (from >= matrix.size() || to >= matrix.size()
                || from < 0 || to < 0) {
            return;
        }
        int val = matrix.get(from).get(to) - 1;
        matrix.get(from).set(to, val);
    }

    /**
     * Функция добавления вершины.
     */
    @Override
    public int addVertex() {
        ArrayList<Integer> newVertexList = new ArrayList<Integer>();
        for (ArrayList<Integer> vertex : matrix) {
            newVertexList.add(0);
            vertex.add(0);
        }
        newVertexList.add(0);
        matrix.add(newVertexList);
        return matrix.size() - 1;
    }

    /**
     * Функция удаления вершины.
     */
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

    /**
     * Функция, возвращающая список соседей вершины.
     */
    @Override
    public ArrayList<Integer> neighboursOfVertex(int num) {
        if (num >= matrix.size() || num < 0) {
            return null;
        }
        ArrayList<Integer> ans = new ArrayList<Integer>();
        for (int i = 0; i < matrix.size(); i++) {
            if (matrix.get(i).get(num) != 0) {
                ans.add(i);
            }
        }
        return ans;
    }

    /**
     * Функция, возвращающая топологически отсортированный список вершин.
     */
    @Override
    public ArrayList<Integer> toposort() {
        HashMap<Integer, Integer> colors = new HashMap<>();
        ArrayList<Integer> sort = new ArrayList<>();
        for (int i = 0; i < matrix.size(); i++) {
            colors.put(i, 0);
        }
        for (int i = 0; i < matrix.size(); i++) {
            boolean res = dfsToposort(colors, sort, i);
            if (!res) {
                return null;
            }
        }
        Collections.reverse(sort);
        return sort;
    }

    private boolean dfsToposort(HashMap<Integer,
            Integer> colors, ArrayList<Integer> sort, Integer vertex) {
        if (colors.get(vertex) != 0) {
            return true;
        }
        colors.put(vertex, 1);
        for (int i = 0; i < matrix.size(); i++) {
            if (matrix.get(vertex).get(i) > 0) {
                if (colors.get(i) == 1 && i != vertex) {
                    return false;
                } else if (colors.get(i) == 0) {
                    boolean res = dfsToposort(colors, sort, i);
                    if (!res) {
                        return false;
                    }
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
        int countVertices = 0;
        if (file.size() > 2) {
            if (!file.get(0).equals("Adjacency matrix")) {
                throw new IncorrectFormat("incorrect header");
            }
            try {
                countVertices = Integer.parseInt(file.get(1));
            } catch (NumberFormatException e) {
                throw new IncorrectFormat("incorrect format");
            }

            if (countVertices != file.size() - 2) {
                throw new IncorrectFormat("incorrect format");
            }
            ArrayList<ArrayList<Integer>> newMatrix = new ArrayList<>();
            for (int i = 0; i < countVertices; i++) {
                newMatrix.add(new ArrayList<Integer>());
            }
            for (int i = 0; i < countVertices; i++) {
                String[] str = file.get(i + 2).split(" ");
                if (str.length != countVertices) {
                    throw new IncorrectFormat("incorrect format");
                }
                for (int j = 0; j < countVertices; j++) {
                    try {
                        int number = Integer.parseInt(str[j]);
                        if (number < 0) {
                            throw new IncorrectFormat("incorrect format");
                        }
                        newMatrix.get(j).add(number);
                    } catch (NumberFormatException e) {
                        throw new IncorrectFormat("incorrect format");
                    }
                }
            }

            matrix = newMatrix;


        } else {
            throw new IncorrectFormat("incorrect format");
        }

    }
}
