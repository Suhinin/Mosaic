package com.childaplic.mosaic.repositories.levels.database;

public class LevelDto {

    private String mId;
    private String mState;
    private boolean mIsShowOnBoarding;
    private String mBoardJson;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public boolean isShowOnBoarding() {
        return mIsShowOnBoarding;
    }

    public void setShowOnBoarding(boolean showOnBoarding) {
        mIsShowOnBoarding = showOnBoarding;
    }

    public String getBoardJson() {
        return mBoardJson;
    }

    public void setBoardJson(String boardJson) {
        mBoardJson = boardJson;
    }
}
