package com.example.deelproduct21.model;

public class DatasetEntry {
    private String key;
    private String title;

    public DatasetEntry(String key, String title) {
        this.key = key;
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title.replace(" jaar", "");
    }
}