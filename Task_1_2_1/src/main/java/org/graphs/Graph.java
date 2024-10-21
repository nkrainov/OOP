package org.graphs;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Интерфейс, задающий функции для работы с графом.
 */
public interface Graph {
    /**
     * Функция добавления вершины.
     */
    int addVertex();

    /**
     * Функция удаления вершины.
     */
    void deleteVertex(int num);

    /**
     * Функция добавления ребра.
     */
    void addEdge(int from, int to);

    /**
     * Функция удаления ребра.
     */
    void deleteEdge(int from, int to);

    /**
     * Функция, возвращающая список соседей вершины.
     */
    ArrayList<Integer> neighboursOfVertex(int num);

    /**
     * Функция чтения графа из файла.
     */
    void readFromFile(String path) throws IOException;

    /**
     * Функция, возвращающая топологически отсортированный список вершин.
     */
    ArrayList<Integer> toposort();

}
