package com.childaplic.mosaic.repositories.levels.domain;


public class LevelData {

    // region Constants

    public static final String EMPTY_CELL = "empty_cell";

    // endregion


    // region Fields

    private String mId;
    private int mNumber;
    private boolean mOpen;
    private String mPreviewPath;
    private PalettePiece[] mPalette;
    private String[][] mBoard;

    // endregion


    // region Public Methods

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

    public boolean isOpen() {
        return mOpen;
    }

    public void setOpen(boolean open) {
        mOpen = open;
    }

    public String getPreviewPath() {
        return mPreviewPath;
    }

    public void setPreviewPath(String previewPath) {
        mPreviewPath = previewPath;
    }

    public PalettePiece[] getPalette() {
        return mPalette;
    }

    public void setPalette(PalettePiece[] palette) {
        mPalette = palette;
    }

    public String[][] getBoard() {
        return mBoard;
    }

    public void setBoard(String[][] board) {
        mBoard = board;
    }

    // endregion

}
