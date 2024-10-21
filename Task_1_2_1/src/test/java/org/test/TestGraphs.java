package org.test;

import org.graphs.AGraph;
import org.graphs.IGraph;
import org.graphs.LGraph;
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
    void testLoadAGraph() {
        String path = File.separator
                + "src" + File.separator
                + "test" + File.separator
                + "resources" + File.separator
                + "testAGraph.txt";
        AGraph graph = null;
        try {
            graph = new AGraph(System.getProperty("user.dir") + path);
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
    void testLoadLGraph() {
        String path = File.separator
                + "src" + File.separator
                + "test" + File.separator
                + "resources" + File.separator
                + "testLGraph.txt";
        LGraph graph = null;
        try {
            graph = new LGraph(System.getProperty("user.dir") + path);
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
    void testLoadIGraph() {
        String path = File.separator
                + "src" + File.separator
                + "test" + File.separator
                + "resources" + File.separator
                + "testIGraph.txt";

        IGraph graph = null;
        try {
            graph = new IGraph(System.getProperty("user.dir") + path);
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
    void testToposortAGraph() {
        AGraph graph = new AGraph();
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
    void testToposortLGraph() {
        LGraph graph = new LGraph();
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
    void testToposortIGraph() {
        IGraph graph = new IGraph();
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
    void testLoadBadAGraph() {
        String path = File.separator
                + "src" + File.separator
                + "test" + File.separator
                + "resources" + File.separator
                + "testBadAGraph.txt";
        AGraph graph = null;
        try {
            graph = new AGraph(System.getProperty("user.dir") + path);
        } catch (IOException e) {
            return;
        }
        Assertions.fail();
    }

    /**
     * Тест загрузки графа из файла с некорректным форматом.
     */
    @Test
    void testLoadBadLGraph() {
        String path = File.separator
                + "src" + File.separator
                + "test" + File.separator
                + "resources" + File.separator
                + "testBadLGraph.txt";
        LGraph graph = null;
        try {
            graph = new LGraph(System.getProperty("user.dir") + path);
        } catch (IOException e) {
            return;
        }
        Assertions.fail();
    }

    /**
     * Тест загрузки графа из файла с некорректным форматом.
     */
    @Test
    void testLoadBadIGraph() {
        String path = File.separator
                + "src" + File.separator
                + "test" + File.separator
                + "resources" + File.separator
                + "testBadIGraph.txt";
        IGraph graph = null;
        try {
            graph = new IGraph(System.getProperty("user.dir") + path);
        } catch (IOException e) {
            return;
        }
        Assertions.fail();
    }
}
