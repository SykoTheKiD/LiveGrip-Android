package com.jaysyko.wrestlechat.sharedPreferences;

/**
 * @author Jay Syko on 2016-07-11.
 */
public enum Preferences {
    NOTIFY_EVENTS("notifyEvents"), SETTINGS("settings");

    private String fileType;

    Preferences(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return fileType;
    }
}
