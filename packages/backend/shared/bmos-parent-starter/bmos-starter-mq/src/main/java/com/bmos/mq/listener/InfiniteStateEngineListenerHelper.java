package com.bmos.mq.listener;

import java.util.*;

public class InfiniteStateEngineListenerHelper {

    private final static Map<InfiniteEventType, List<InfiniteEventListener>> listenersCache = new HashMap<>();
    private final static List<InfiniteEventListener> listeners = new ArrayList<>();

    public synchronized static <T> void addListener(InfiniteEventType eventType, InfiniteEventListener listener) {
        listenersCache.computeIfAbsent(eventType, key -> new ArrayList<>()).add(listener);
    }

    public synchronized static <T> void addListener(InfiniteEventListener listener) {
        listeners.add(listener);
    }

    public static void notifyAll(InfiniteEvent event) {
        Optional.ofNullable(listenersCache.get(event.getEventType()))
                .ifPresent(presents -> presents.forEach(ele -> ele.notified(event)));
        listeners.forEach(listener -> listener.notified(event));
    }
}
