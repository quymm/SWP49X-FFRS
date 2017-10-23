package com.capstone.ffrs.entity;

/**
 * Created by HuanPMSE61860 on 10/23/2017.
 */

public class DataSnapshotEntrySet<K,T> {
    private K key;
    private T value;

    public DataSnapshotEntrySet() {
    }

    public DataSnapshotEntrySet(K key, T value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
