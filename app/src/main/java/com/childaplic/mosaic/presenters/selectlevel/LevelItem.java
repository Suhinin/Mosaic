package com.childaplic.mosaic.presenters.selectlevel;

import com.childaplic.mosaic.repositories.levels.domain.LevelState;

public class LevelItem {

    private String mId;
    private int mNumber;
    private String mPreviewPath;
    private LevelState mState;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        mNumber = number;
    }

    public String getPreviewPath() {
        return mPreviewPath;
    }

    public void setPreviewPath(String previewPath) {
        mPreviewPath = previewPath;
    }

    public LevelState getState() {
        return mState;
    }

    public void setState(LevelState state) {
        mState = state;
    }

}
