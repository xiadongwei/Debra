/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Framework.Tools;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Copyright (c) 2014,
 * All rights reserved.
 * Project name：Debra v0.2.5.
 * Created by xiadongwei on 14/10/29.
 * Current Version: 1.0
 * Discription:
 */
public final class EventHelper<H,T extends Event>{
    private ArrayList eventList = new ArrayList();

    /**
     * construct EventHelper
     */
    public EventHelper(){
        super();
    }

    /**
     * addEvent Method
     */
    public void addEventHandler(H node,EventType<T> eventType,EventHandler<T> eventHandler) {
        EventStruct eventStruct = new EventStruct(node,eventType, eventHandler);
        boolean s = false;
        if (node instanceof Node){
            ((Node)node).addEventHandler(eventType, eventHandler);
            s = true;
        }else if (node instanceof MenuItem) {
            ((MenuItem) node).addEventHandler(eventType, eventHandler);
            s = true;
        }
        if (s){
            eventList.add(eventStruct);
        }
    }

    public void addEventHandler(H node,EventHandler<T> eventHandler) {
        this.addEventHandler(node, ((EventType) ActionEvent.ACTION),eventHandler);
    }
    /**
     * removeEvent Method
     */
    public boolean removeEventHandler(H node,EventType<T> eventType,EventHandler<T> eventHandler) {
        Iterator<EventStruct> it = eventList.iterator();
        while (it.hasNext()) {
            EventStruct es = it.next();
            if (es.getNode().equals(node)
                    && es.getEventType().equals(eventType)
                    && es.getEventHandler().equals(eventHandler)) {

                if (es.getNode() instanceof Node){
                    ((Node) es.getNode()).removeEventHandler(eventType, eventHandler);
                }else if (es.getNode() instanceof MenuItem){
                    ((MenuItem) es.getNode()).removeEventHandler(eventType, eventHandler);
                }
                it.remove();
                return true;
            }
        }
        return false;
    }

    public boolean removeEventHandler(H node,EventHandler<T> eventHandler) {
        return this.removeEventHandler(node, ((EventType) ActionEvent.ACTION), eventHandler);
    }
    /**
     * release Method
     */
    public void release(){
        Iterator<EventStruct> it = eventList.iterator();
        while (it.hasNext()) {
            EventStruct es = it.next();
            if (es.getNode() instanceof Node){
                ((Node) es.getNode()).removeEventHandler(es.eventType,es.eventHandler);
            }else if (es.getNode() instanceof MenuItem){
                ((MenuItem) es.getNode()).removeEventHandler(es.eventType,es.eventHandler);
            }
            it.remove();
        }
    }

    private final class EventStruct<H,T extends Event>{
        private H node;
        private EventType<T> eventType;
        private EventHandler<T> eventHandler;
        public void setEventType(EventType<T> eventType) {
            this.eventType = eventType;
        }

        public void setEventHandler(EventHandler<T> eventHandler) {
            this.eventHandler = eventHandler;
        }

        public void setNode(H node) {
            this.node = node;
        }

        public EventType<T> getEventType() {
            return eventType;
        }

        public EventHandler<T> getEventHandler() {
            return eventHandler;
        }

        public H getNode() {
            return node;
        }

        public EventStruct(H node,EventType<T> eventType,EventHandler<T> eventHandler){
            setNode(node);
            setEventType(eventType);
            setEventHandler(eventHandler);
        }
    }
}
