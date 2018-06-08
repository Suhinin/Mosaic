package com.childaplic.mosaic.repositories.levels.domain;


public interface Level {

    String getId();

    int getNumber();

    String getPreviewPath();

    LevelState getState();

    void setState(LevelState state);

    boolean isShowOnBoarding();

    void setShowOnBoarding(boolean isShow);

    int getIncorrectAnswers();

    void setIncorrectAnswers(int count);

    PalettePiece[] getPalette();

    String[][] getBoard();

}
