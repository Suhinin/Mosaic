package com.childaplic.mosaic.repositories.levels.domain;


public class LevelImpl implements Level {

    // region Fields

    private String mId;
    private int mNumber;
    private String mPreviewPath;
    private LevelState mState;
    private boolean mIsShowOnBoarding;
    private PalettePiece[] mPalette;
    private Cell[][] mBoard;

    // endregion


    // region Implements Level

    @Override
    public int getNumber() {
        return mNumber;
    }

    @Override
    public String getPreviewPath() {
        return mPreviewPath;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public LevelState getState() {
        return mState;
    }

    @Override
    public void setState(LevelState levelState) {
        mState = levelState;
    }

    @Override
    public boolean isShowOnBoarding() {
        return mIsShowOnBoarding;
    }

    @Override
    public void setShowOnBoarding(boolean showOnBoarding) {
        mIsShowOnBoarding = showOnBoarding;
    }

    @Override
    public PalettePiece[] getPalette() {
        return mPalette;
    }

    @Override
    public Cell[][] getBoard() {
        return mBoard;
    }

    @Override
    public void setBoard(Cell[][] board) {
        mBoard = board;
    }

    // endregion


    // region Public Methods

    public void setId(String id) {
        mId = id;
    }

    public void setNumber(int number) {
        mNumber = number;
    }

    public void setPreviewPath(String previewPath) {
        mPreviewPath = previewPath;
    }

    public void setPalette(PalettePiece[] palette) {
        mPalette = palette;
    }

    // endregion

}
