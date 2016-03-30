package com.jaysyko.wrestlechat.localStorage;

/**
 * Created by jarushaan on 2016-03-30
 */
public enum StorageFile {
    AUTH("AUTH"), MESSAGING("MESSAGING");

    private String file;

    StorageFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return this.file;
    }
}
