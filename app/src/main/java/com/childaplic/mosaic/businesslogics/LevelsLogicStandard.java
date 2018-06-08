package com.childaplic.mosaic.businesslogics;

import javax.inject.Inject;

import com.childaplic.mosaic.repositories.levels.LevelsRepository;
import com.childaplic.mosaic.repositories.levels.domain.Level;
import com.childaplic.mosaic.repositories.levels.domain.LevelNull;

public class LevelsLogicStandard implements LevelsLogic {

    // region Constants

    private final String TAG = LevelsLogicStandard.class.getCanonicalName();

    // endregion


    // region Fields

    private String mCurrentLevelId;
    private long mLevelSelectedTimeMillis;

    // endregion


    // region Injections

    private LevelsRepository mLevelsRepository;

    // endregion


    // region Constructors

    @Inject
    public LevelsLogicStandard(LevelsRepository levelsRepository) {
        mLevelsRepository = levelsRepository;
    }

    // endregion


    // region Implements LevelsLogic

    @Override
    public String getCurrentLevelId() {
        return mCurrentLevelId;
    }

    @Override
    public void setCurrentLevelId(String levelId) {
        mCurrentLevelId = levelId;
        mLevelSelectedTimeMillis = System.currentTimeMillis();
    }

    @Override
    public Level getCurrentLevel() {
        if (mCurrentLevelId == null) {
            return new LevelNull();
        }

        return mLevelsRepository.getLevel(mCurrentLevelId);
    }

    @Override
    public long getLevelSelectedTimeMillis() {
        return mLevelSelectedTimeMillis;
    }

    // endregion


    // region Private Methods
    // endregion

}
