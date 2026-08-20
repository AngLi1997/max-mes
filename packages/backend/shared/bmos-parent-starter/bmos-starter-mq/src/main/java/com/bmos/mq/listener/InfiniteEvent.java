package com.bmos.mq.listener;

public class InfiniteEvent<T> {

    private InfiniteEventType eventType;

    private T payload;

    public InfiniteEvent(InfiniteEventType eventType, T payload) {
        this.eventType = eventType;
        this.payload = payload;
    }

    public static <T> InfiniteEvent<T> of(InfiniteEventType eventType, T payload) {
        return new InfiniteEvent<>(eventType, payload);
    }

    public InfiniteEventType getEventType() {
        return eventType;
    }

    public void setEventType(InfiniteEventType eventType) {
        this.eventType = eventType;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
