package com.childaplic.mosaic.presenters.board;


import com.childaplic.mosaic.repositories.levels.domain.LevelData;

public class CellItem {

    private String mPieceId;
    private boolean mIsPicked;

    public CellItem() {
        // empty default constructor
    }

    public CellItem(String pieceId) {
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
