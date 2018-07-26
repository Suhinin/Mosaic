package com.childaplic.mosaic.repositories.levels.domain;


public enum  LevelState {

    DISABLED("disabled"),
    OPEN("open"),
    COMPLETED("completed");

    private String mValue;

    LevelState(String value) {
        mValue = value;
    }

    public static LevelState fromString(String text) {
        for (LevelState type : LevelState.values()) {
            if (type.mValue.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return mValue;
    }

}
