package com.bmos.lims2.server.eln.record.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.BiMap;
import cn.hutool.core.map.MapUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 解决问题
 * <pre>
 * 1：图的构建
 * 1.1(获取所有公式数据，从新构建) 无参构造方法和有参构造方法
 * 1.2(增量创建图)  addWithValidateCycle和add方法
 * 2：图的维护
 * 2.1(节点的删除)  delete方法
 * 2.2(节点的增加同1.2) addWithValidateCycle和add方法
 * 3: 环状节点检测
 * 3.1(1.2需要)  existsCycle方法
 * 4：构建图后获取结算单元格的顺序(最大路径--计算，同最大路径顺序不影响)   findElementPathAsc方法
 * 5：遍历图中的元素从指定节点  traverse方法
 * 6: 序列化充当缓存  serialize方法
 * </pre>
 */
@NoArgsConstructor
public class Graph<T> {
    /**
     * <pre>
     * 存放数据和图元素实际存储下标的关系 开放get/set方法是为了序列化和反序列化不建议直接使用
     * </pre>
     */
    @Getter
    @Setter
    private Map<T, Integer> map = new HashMap<>();

    /**
     * <pre>
     * 存放图元素数据  开放get/set方法是为了序列化和反序列化不建议直接使用
     * </pre>
     */
    @Getter
    @Setter
    private List<List<T>> graph = new ArrayList<>();

    /**
     * 存储value值对应的List
     */
    @Getter
    @Setter
    private Map<T, List<T>> valueMap ;
    /**
     * 临时存储是否被遍历过 判断是否存在环状节点时计算
     */
    private Set<T> visitedSet;
    /**
     * 临时存储检测环过程，该元素是否已经遍历过 判断是否存在环状节点时计算
     */
    private boolean[] onPath;
    /**
     * 存储是否存在环 断是否存在环状节点时使用
     */
    private boolean hasCycle;

/*    *//**
     * 初始化构造方法
     *//*
    public Graph() {
       *//* this.map = new HashMap<>();
        this.graph = new ArrayList<>();*//*
    }*/

    /**
     * 有参的构造方法
     * (不建议直接使用，建议使用无参方法+add方法构建)
     *
     * @param map   map
     * @param graph graph
     */
    public Graph(Map<T, Integer> map, List<List<T>> graph) {
        this.map = map;
        this.graph = graph;
    }

    /**
     * 图增加节点同时校验是否存在环(图节点已存在，返回失败，存在环返回失败)
     *
     * @param key     指定元素
     * @param element 指定元素引用的元素
     * @return true 成功
     */
    public synchronized boolean addWithValidateCycle(T key, List<T> element) {
        if (map.containsKey(key)) {
            throw new BmosException(LimsResponseCode.EXPRESS_EXISTS_DUPLICATION);
        }
        map.put(key, graph.size());
        graph.add(element);
        if (existsCycle(key)) {
            delete(key);
            throw new BmosException(LimsResponseCode.EXPRESS_EXISTS_CYCLE);
        }
        return Boolean.TRUE;
    }

    /**
     * 图增加节点不校验是否存在环（若图节点已存在，方法返回添加失败）
     *
     * @param key     指定元素
     * @param element 指定元素引用的元素
     * @return true 成功
     */
    public synchronized boolean add(T key, List<T> element) {
        if (map.containsKey(key)) {
            throw new BmosException(LimsResponseCode.EXPRESS_EXISTS_DUPLICATION);
        }
        map.put(key, graph.size());
        graph.add(element);
        return Boolean.TRUE;
    }

    /**
     * 根据指定元素删除图中的数据关系
     *
     * @param key 指定元素
     */
    public synchronized void delete(T key) {
        if (!map.containsKey(key)) {
            return;
        }
        int index = map.get(key).intValue();
        map.forEach((entryKey, value) -> {
            if (value > index) {
                map.put(entryKey, value - 1);
            }
        });
        graph.remove(index);
        map.remove(key);
    }

    /**
     * 判断节点是否存在环
     *
     * @param key 指定元素
     * @return 是否存在环状图
     */
    public synchronized boolean existsCycle(T key) {
        visitedSet = new HashSet<>();
        onPath = new boolean[graph.size()];
        hasCycle = false;
        traverseWithCycle(key);
        return hasCycle;
    }

    private void traverseWithCycle(T s) {
        Integer index = map.get(s);
        if (index == null) {
            return;
        }
        if (onPath[index]) {
            // 发现环！！！
            hasCycle = true;
        }
        if (visitedSet.contains(s) || hasCycle) {
            return;
        }
        // 将当前节点标记为已遍历
        visitedSet.add(s);
        // 开始遍历节点 s
        onPath[index] = true;
        for (T t : graph.get(index)) {
            traverseWithCycle(t);
        }
        // 节点 s 遍历完成
        onPath[index] = false;
    }

    /**
     * 从指定元素遍历图节点，获取所有可以访问到的元素
     *
     * @param key        指定元素
     * @param elementSet 存储指定元素的Set集合
     */
    public void traverse(T key, Set<T> elementSet) {
        if (!elementSet.add(key)) {
            return;
        }
        Integer index = map.get(key);
        if (index == null) {
            return;
        }
        for (T t : graph.get(index)) {
            traverse(t, elementSet);
        }
    }

    public synchronized List<T> findElementPathList(T element) {
        initValueMap();
        Map<T, Integer> depthMap = new HashMap<>();
        // 当前元素深度为1
        depthMap.put(element, 1);
        traverseWithDeptInfo(element, depthMap);
        return depthMap.entrySet().stream()
            .filter(entry -> entry.getValue() != 1)
            .sorted(Comparator.comparingInt(Map.Entry::getValue))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    /**
     * 从指定元素遍历图节点，获取所有可以访问到的元素距离指定节点的最大深度
     * 结果具有顺序性
     * @param element 指定元素
     */
    public synchronized LinkedHashMap<T, List<T>> findElementPath(T element) {
        initValueMap();
        Map<T, Integer> depthMap = new HashMap<>();
        // 当前元素深度为1
        depthMap.put(element, 1);
        traverseWithDeptInfo(element, depthMap);
        return depthMap.entrySet().stream()
            .filter(entry -> entry.getValue() != 1)
            .sorted(Comparator.comparingInt(Map.Entry::getValue))
            .map(Map.Entry::getKey)
            .collect(
                Collectors.toMap(value -> value, value -> graph.get(map.get(value)),
                    (a, b) -> b, LinkedHashMap::new)
            );
    }

    /**
     * 将graph中的数据根据(引用的元素  -> 谁引用)构建valueMap
     */
    private synchronized void initValueMap() {
        if (MapUtil.isNotEmpty(valueMap)) {
            return;
        }
        valueMap = new HashMap<>();
        BiMap<T, Integer> biMap = new BiMap<>(map);
        Map<Integer, T> inverseBiMap = biMap.getInverse();
        for (int i = 0; i < graph.size(); i++) {
            for (int j = 0; j < graph.get(i).size(); j++) {
                // 获取下标对象的Map key
                T t = biMap.getInverse().get(i);
                valueMap.computeIfAbsent(graph.get(i).get(j), key -> new ArrayList<T>()).add(inverseBiMap.get(i));
            }
        }
    }

    private void traverseWithDeptInfo(T key, Map<T, Integer> depthMap) {
        List<T> values = valueMap.get(key);
        if (CollectionUtil.isEmpty(values)) {
            return;
        }
        for (T t : values) {
            Integer enterDepth = depthMap.get(key);
            // 当前进入节点的深度
            Integer currentDepth = depthMap.getOrDefault(t, 1);
            depthMap.put(t, enterDepth > currentDepth ? enterDepth + 1 : currentDepth + 1);
            traverseWithDeptInfo(t, depthMap);
        }
    }

    public List<T> getRelationElement(T element) {
        Integer index = map.get(element);
        if (Objects.isNull(index)) {
            return Collections.emptyList();
        }
        return graph.get(index);
    }

    public synchronized Set<T> findElementPathAscRemoveElementSelf(Set<T> elements) {
        initValueMap();
        return elements.stream().map(this::findElementPathAscRemoveElementSelf)
            .flatMap(List::stream).distinct().collect(Collectors.toSet());
    }

    public synchronized List<T> findElementPathAscRemoveElementSelf(T element) {
        initValueMap();
        List<T> result = findElementPathAsc1Info(element);
        result.remove(element);
        return result;
    }

    public synchronized List<T> findElementPathAsc1Info(T element) {
        List<T> result = new ArrayList<>();
        result.add(element);

        List<T> valueList = valueMap.get(element);
        if (CollectionUtil.isEmpty(valueList)) {
            return result;
        }
        valueList.forEach(value -> result.addAll(findElementPathAsc1Info(value)));
        return result;
    }

    public String serialize() {
        initValueMap();
        return JsonUtils.toJsonString(this);
    }

    /**
     * 排序 按照入度从小到大排列
     * @return
     */
    public List<T> sortKahn() {
        initValueMap();
        List<T> allElements = getAllElements();
        // 建图：存储每个节点的入度和出度
        Map<T, Integer> inDegree = new HashMap<>();

        // 初始化入度
        allElements.forEach(e->{
            inDegree.put(e, 0);
        });
        // 计算节点入度并更新
        for (List<T> value : valueMap.values()) {
            for (T t : value) {
                inDegree.put(t, inDegree.getOrDefault(t, 0) + 1);
            }
        }

        Queue<T> queue = new LinkedList<>();
        for (Map.Entry<T, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey()); // 入度为0的节点入队
            }
        }
        List<T> sortedList = new ArrayList<>();

        while (!queue.isEmpty()) {
            T currentId = queue.poll();
            sortedList.add(currentId);
            List<T> ts = valueMap.get(currentId);
            if (ts != null) {
                // 遍历当前节点的所有下层节点
                for (T neighbor : ts) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1); // 邻居节点入度减1
                    if (inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor); // 入度为0的节点入队
                    }
                }
            }
        }

        return sortedList;

    }

    @JsonIgnore
    public List<T> getAllElements() {
        Set<T> keySet = map.keySet();
        Set<T> graphSet = graph.stream()
            .flatMap(List::stream)
            .collect(Collectors.toSet());
        return new ArrayList<>(CollUtil.union(keySet, graphSet));
    }

    @JsonIgnore
    public Set<T> getExpressElements() {
        return map.keySet();
    }

    @JsonIgnore
    public Set<T> getDataElements() {
        return graph.stream()
            .flatMap(List::stream)
            .collect(Collectors.toSet());
    }
}
