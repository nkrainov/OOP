package org.graphs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Класс, реализующий граф с матрицей инцидентности.
 */
public class IGraph implements Graph {
    private ArrayList<ArrayList<Integer>> matrix;

    /**
     * Конструктор пустого графа.
     */
    public IGraph() {
        matrix = new ArrayList<ArrayList<Integer>>();
    }

    /**
     * Конструктор графа из файла.
     */
    public IGraph(String path) throws IOException {
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

        if (from == to) {
            for (int i = 0; i < matrix.size(); i++) {
                if (i == from) {
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

    /**
     * Функция удаления ребра.
     */
    @Override
    public void deleteEdge(int from, int to) {
        if (from >= matrix.size() || to >= matrix.size()
                || from < 0 || to < 0) {
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

    /**
     * Функция добавления вершины.
     */
    @Override
    public int addVertex() {
        ArrayList<Integer> newVertex = new ArrayList<Integer>();
        int size;
        if (matrix.isEmpty()) {
            size = 0;
        } else {
            size = matrix.get(0).size();
        }

        for (int i = 0; i < size; i++) {
            newVertex.add(0);
        }
        matrix.add(newVertex);

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
        for (int i = 0; i < matrix.get(num).size(); i++) {
            if (matrix.get(num).get(i) == -1) {
                for (int j = 0; j < matrix.size(); j++) {
                    if (matrix.get(j).get(i) == 1) {
                        ans.add(j);
                        break;
                    }
                }
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

    private boolean dfsToposort(HashMap<Integer, Integer> colors, ArrayList<Integer> sort, Integer vertex) {
        if (colors.get(vertex) != 0) {
            return true;
        }
        colors.put(vertex, 1);
        for (int i = 0; i < matrix.size(); i++) {
            if (matrix.get(vertex).get(i) == 1) {
                for (int j = 0; j < matrix.size(); j++) {
                    if (matrix.get(j).get(i) == -1) {
                        if (colors.get(j) == 1) {
                            return false;
                        } else if (colors.get(j) == 0) {
                            boolean res = dfsToposort(colors, sort, j);
                            if (!res) {
                                return false;
                            }
                        }
                        break;
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
        int countVertices = 0, countEdges = 0;
        if (file.size() > 3) {
            if (!file.get(0).equals("Incident matrix")) {
                throw new IOException();
            }

            try {
                countVertices = Integer.parseInt(file.get(1));
            } catch (NumberFormatException e) {
                throw new IOException();
            }

            try {
                countEdges = Integer.parseInt(file.get(2));
            } catch (NumberFormatException e) {
                throw new IOException();
            }

            ArrayList<ArrayList<Integer>> newMatrix = new ArrayList<>();
            for (int i = 0; i < countVertices; i++) {
                newMatrix.add(new ArrayList<Integer>());
            }

            for (int i = 0; i < countEdges; i++) {
                String[] str = file.get(i + 3).split(" ");
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
