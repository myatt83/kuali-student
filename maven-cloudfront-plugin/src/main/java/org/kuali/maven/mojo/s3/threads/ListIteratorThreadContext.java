package org.kuali.maven.mojo.s3.threads;

import java.util.List;

import org.kuali.maven.mojo.s3.ProgressTracker;

public class ListIteratorThreadContext<T> {
    int id;
    int offset;
    int length;
    ThreadHandler threadHandler;
    ProgressTracker tracker;
    List<T> list;
    ListHandler<T> listHandler;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> contexts) {
        this.list = contexts;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ThreadHandler getThreadHandler() {
        return threadHandler;
    }

    public void setThreadHandler(ThreadHandler handler) {
        this.threadHandler = handler;
    }

    public ProgressTracker getTracker() {
        return tracker;
    }

    public void setTracker(ProgressTracker tracker) {
        this.tracker = tracker;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ListHandler<T> getListHandler() {
        return listHandler;
    }

    public void setListHandler(ListHandler<T> contextHandler) {
        this.listHandler = contextHandler;
    }

}
