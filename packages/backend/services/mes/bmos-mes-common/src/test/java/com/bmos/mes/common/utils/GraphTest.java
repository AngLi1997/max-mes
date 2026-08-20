package com.bmos.mes.common.utils;

import com.bmos.common.exception.BmosException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class GraphTest {
    private Graph<Integer> graph;

    @Test
    public void test_add() {
        graph = new Graph<>();
        Assertions.assertEquals(graph.add(16, Arrays.asList(17, 18, 19, 10)), Boolean.TRUE);
        // 重复加入同一个元素抛异常
        Assertions.assertThrows(BmosException.class, () -> graph.add(16, Arrays.asList(17, 18, 19, 10)));
    }

    @Test
    public void test_addWithValidateCycle() {
        graph = new Graph<>();
        Assertions.assertEquals(graph.addWithValidateCycle(16, Arrays.asList(17, 18, 19, 10)), Boolean.TRUE);

        Assertions.assertThrows(BmosException.class, () -> graph.addWithValidateCycle(16, Arrays.asList(17, 18, 19, 10)));

        Assertions.assertEquals(graph.addWithValidateCycle(10, Arrays.asList(11, 12, 13, 14, 15)), Boolean.TRUE);

        Assertions.assertThrows(BmosException.class, () -> graph.addWithValidateCycle(11, Arrays.asList(16, 17, 18, 19, 20)));

        Assertions.assertEquals(graph.addWithValidateCycle(12, Collections.singletonList(11)), Boolean.TRUE);
    }

    @Test
    public void test_delete() {
        graph = new Graph<>();
        graph.add(10, Arrays.asList(11, 12, 13, 14));
        graph.add(14, Arrays.asList(15, 16, 17));
        graph.delete(10);
        graph.add(16, Arrays.asList(17, 18));
        graph.add(19, Arrays.asList(15, 16));
        graph.delete(16);
        Assertions.assertTrue(graph.getMap().get(14) != null ||  graph.getMap().get(19) != null);
        Assertions.assertEquals(3, graph.getGraph().get(graph.getMap().get(14)).size());
        Assertions.assertEquals(2, graph.getGraph().get(graph.getMap().get(19)).size());

        Assertions.assertEquals(2, graph.getMap().size());
        Assertions.assertEquals(2, graph.getGraph().size());
        graph.delete(16);
        Assertions.assertEquals(2, graph.getMap().size());
        Assertions.assertEquals(2, graph.getGraph().size());
    }

    @Test
    public void testExistsCrycle() {
        graph = new Graph<>();
        graph.add(10, Arrays.asList(11, 12, 13, 14));
        graph.add(14, Arrays.asList(15, 16));
        Assertions.assertThrows(BmosException.class, () -> graph.addWithValidateCycle(16, Arrays.asList(10, 13)));
        graph.add(16, Arrays.asList(10, 13));
        Assertions.assertTrue(graph.existsCycle(16));
    }

    @Test
    public void test_findElementPathAsc() {
        graph = new Graph<>();
        graph.add(10, Arrays.asList(11, 12, 13, 14));
        graph.add(14, Arrays.asList(15, 16));
        graph.add(16, Arrays.asList(17, 18));
        graph.add(19, Arrays.asList(15, 16));
        graph.serialize();
        graph.delete(16);
        System.out.println(graph.findElementPathAsc1Info(16));
        LinkedHashMap<Integer, List<Integer>> elementPathAsc = graph.findElementPath(16);
        Assertions.assertEquals(3, elementPathAsc.size());
        Assertions.assertArrayEquals(new Object[]{19, 14, 10}, elementPathAsc.keySet().stream().toArray());
    }

    @Test
    public void test_findElementPathList() {
        graph = new Graph<>();
        graph.add(10, Arrays.asList(11, 12, 13, 14));
        graph.add(14, Arrays.asList(15, 16));
        graph.add(16, Arrays.asList(17, 18));
        graph.add(19, Arrays.asList(14, 15, 16));
        graph.serialize();
        System.out.println(graph.findElementPathAsc1Info(16));
        List<Integer> elementPathAsc = graph.findElementPathList(16);
        Assertions.assertEquals(3, elementPathAsc.size());
    }
}
