package org.test;

import org.graphs.AdjacencyGraph;
import org.graphs.IncidentGraph;
import org.graphs.ListGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Класс тестов.
 */
public class TestGraphs {
    /**
     * Тест загрузки графа из файла.
     */
    @Test
    void testLoadAdjacency() {
        String path = File.separator
                + "src" + File.separator
                + "test" + File.separator
                + "resources" + File.separator
                + "testAGraph.txt";
        AdjacencyGraph graph = null;
        try {
            graph = new AdjacencyGraph(System.getProperty("user.dir") + path);
        } catch (IOException e) {
            Assertions.fail();
        }

        ArrayList<Integer> ans = new ArrayList<>();
        ans.add(2);
        Assertions.assertEquals(ans, graph.neighboursOfVertex(1));
    }

    /**
     * Тест загрузки графа из файла.
     */
    @Test
    void testLoadListGraph() {
        String path = File.separator
                + "src" + File.separator
                + "test" + File.separator
                + "resources" + File.separator
                + "testLGraph.txt";
        ListGraph graph = null;
        try {
            graph = new ListGraph(System.getProperty("user.dir") + path);
        } catch (IOException e) {
            Assertions.fail();
        }

        ArrayList<Integer> ans = new ArrayList<>();
        ans.add(2);
        Assertions.assertEquals(ans, graph.neighboursOfVertex(1));
    }

    /**
     * Тест загрузки графа из файла.
     */
    @Test
    void testLoadIncidentGraph() {
        String path = File.separator
                + "src" + File.separator
                + "test" + File.separator
                + "resources" + File.separator
                + "testIGraph.txt";

        IncidentGraph graph = null;
        try {
            graph = new IncidentGraph(System.getProperty("user.dir") + path);
        } catch (IOException e) {
            Assertions.fail();
        }

        ArrayList<Integer> ans = new ArrayList<>();
        ans.add(2);
        Assertions.assertEquals(ans, graph.neighboursOfVertex(1));
    }

    /**
     * Тест создания графа с помощью add'ов и проведения топологической сортировки.
     */
    @Test
    void testToposortAdjacency() {
        AdjacencyGraph graph = new AdjacencyGraph();
        for (int i = 0; i < 5; i++) {
            graph.addVertex();
        }
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 0);
        Assertions.assertNull(graph.toposort());
        graph.deleteEdge(4, 0);
        graph.addVertex();
        graph.deleteVertex(graph.addVertex());
        graph.addEdge(0, 5);
        graph.addEdge(5, 1);
        ArrayList<Integer> ans = new ArrayList<>();
        ans.add(0);
        ans.add(5);
        ans.add(1);
        ans.add(2);
        ans.add(3);
        ans.add(4);
        Assertions.assertEquals(graph.toposort(), ans);
    }

    /**
     * Тест создания графа с помощью add'ов и проведения топологической сортировки.
     */
    @Test
    void testToposortListGraph() {
        ListGraph graph = new ListGraph();
        for (int i = 0; i < 5; i++) {
            graph.addVertex();
        }
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 0);
        Assertions.assertNull(graph.toposort());
        graph.deleteEdge(4, 0);
        graph.addVertex();
        graph.deleteVertex(graph.addVertex());
        graph.addEdge(0, 5);
        graph.addEdge(5, 1);
        ArrayList<Integer> ans = new ArrayList<>();
        ans.add(0);
        ans.add(5);
        ans.add(1);
        ans.add(2);
        ans.add(3);
        ans.add(4);
        Assertions.assertEquals(graph.toposort(), ans);
    }

    /**
     * Тест создания графа с помощью add'ов и проведения топологической сортировки.
     */
    @Test
    void testToposortIncidentGraph() {
        IncidentGraph graph = new IncidentGraph();
        for (int i = 0; i < 5; i++) {
            graph.addVertex();
        }
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 0);
        graph.deleteVertex(graph.addVertex());
        Assertions.assertNull(graph.toposort());
        graph.deleteEdge(4, 0);
        graph.addVertex();
        graph.addEdge(0, 5);
        graph.addEdge(5, 1);
        graph.addEdge(0, 0);
        ArrayList<Integer> ans = new ArrayList<>();
        ans.add(0);
        ans.add(5);
        ans.add(1);
        ans.add(2);
        ans.add(3);
        ans.add(4);
        Assertions.assertEquals(graph.toposort(), ans);
    }

    /**
     * Тест загрузки графа из файла с некорректным форматом.
     */
    @Test
    void testLoadBadAdjacency() {
        String path = File.separator
                + "src" + File.separator
                + "test" + File.separator
                + "resources" + File.separator
                + "testBadAGraph.txt";
        AdjacencyGraph graph = null;
        try {
            graph = new AdjacencyGraph(System.getProperty("user.dir") + path);
        } catch (IOException e) {
            return;
        }
        Assertions.fail();
    }

    /**
     * Тест загрузки графа из файла с некорректным форматом.
     */
    @Test
    void testLoadBadListGraph() {
        String path = File.separator
                + "src" + File.separator
                + "test" + File.separator
                + "resources" + File.separator
                + "testBadLGraph.txt";
        ListGraph graph = null;
        try {
            graph = new ListGraph(System.getProperty("user.dir") + path);
        } catch (IOException e) {
            return;
        }
        Assertions.fail();
    }

    /**
     * Тест загрузки графа из файла с некорректным форматом.
     */
    @Test
    void testLoadBadIncidentGraph() {
        String path = File.separator
                + "src" + File.separator
                + "test" + File.separator
                + "resources" + File.separator
                + "testBadIGraph.txt";
        IncidentGraph graph = null;
        try {
            graph = new IncidentGraph(System.getProperty("user.dir") + path);
        } catch (IOException e) {
            return;
        }
        Assertions.fail();
    }
}
