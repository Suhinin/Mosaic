package com.childaplic.mosaic.businesslogics.levels;

import com.childaplic.mosaic.repositories.levels.domain.Level;

public interface LevelsLogic {

    String getCurrentLevelId();

    void setCurrentLevelId(String levelId);

    Level getCurrentLevel();

    long getLevelSelectedTimeMillis();

    void enablePaidVersion();

    boolean isPaid();

}
