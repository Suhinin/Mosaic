package com.childaplic.mosaic.repositories.levels.domain;

public class Cell {

    private String mPieceId;
    private boolean mIsPicked;

    public Cell() {
        // empty default constructor
    }

    public Cell(String pieceId) {
        mPieceId = pieceId;
        mIsPicked = false;
    }

    public String getPieceId() {
        return mPieceId;
    }

    public void setPieceId(String pieceId) {
        mPieceId = pieceId;
    }

    public boolean isPicked() {
        return mIsPicked;
    }

    public void setPicked(boolean picked) {
        mIsPicked = picked;
    }

    public boolean isEmpty() {
        return mPieceId == null || mPieceId.equals(LevelData.EMPTY_CELL);
    }

}
