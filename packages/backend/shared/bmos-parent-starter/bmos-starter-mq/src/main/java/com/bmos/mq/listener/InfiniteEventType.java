package com.bmos.mq.listener;

import com.bmos.mq.listener.Event.StateEvent;

public enum InfiniteEventType {

    START_EVENT("START_EVENT", StateEvent.class);


    private final String eventType;

    private final Class<?> payloadType;

    InfiniteEventType(String eventType, Class<?> payloadType) {
        this.eventType = eventType;
        this.payloadType = payloadType;
    }

    public String getEventType() {
        return eventType;
    }

    public Class<?> getPayloadType() {
        return payloadType;
    }
}
