package com.box.common.utils.subpackage;

public class EventCenter<T> {

    /**
     * reserved data
     */
    private T data;

    /**
     * this code distinguish between different events
     */
    private int eventCode = -1;

    /**
     * 标签
     */
    private String tag;

    public EventCenter(int eventCode) {
        this(eventCode, null);
    }

    public EventCenter(int eventCode, T data) {
        this.eventCode = eventCode;
        this.data = data;
    }

    public EventCenter(int eventCode, String tag, T data) {
        this.eventCode = eventCode;
        this.tag = tag;
        this.data = data;
    }

    /**
     * get event code
     *
     * @return
     */
    public int getEventCode() {
        return this.eventCode;
    }

    /**
     * get event tag
     *
     * @return
     */
    public String getTag() {
        return tag;
    }

    /**
     * get event reserved data
     *
     * @return
     */
    public T getData() {
        return this.data;
    }
}
