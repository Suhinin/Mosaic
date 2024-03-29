package com.childaplic.mosaic.repositories.levels.domain;

public class LevelNull implements Level {

    @Override
    public String getId() {
        return "";
    }

    @Override
    public int getNumber() {
        return 0;
    }

    @Override
    public String getPreviewPath() {
        return "";
    }

    @Override
    public LevelState getState() {
        return LevelState.DISABLED;
    }

    @Override
    public void setState(LevelState state) {
        // empty stub
    }

    @Override
    public boolean isShowOnBoarding() {
        return false;
    }

    @Override
    public void setShowOnBoarding(boolean isShow) {
        // empty stub
    }

    @Override
    public PalettePiece[] getPalette() {
        return new PalettePiece[0];
    }

    @Override
    public Cell[][] getBoard() {
        return new Cell[0][];
    }

    @Override
    public void setBoard(Cell[][] board) {
        // empty stub
    }
}
