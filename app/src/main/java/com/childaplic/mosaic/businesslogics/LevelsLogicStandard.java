package com.childaplic.mosaic.businesslogics;

import javax.inject.Inject;

import com.childaplic.mosaic.repositories.levels.LevelsRepository;
import com.childaplic.mosaic.repositories.levels.domain.Level;
import com.childaplic.mosaic.repositories.levels.domain.LevelNull;
import com.childaplic.mosaic.services.shared.SharedService;

public class LevelsLogicStandard implements LevelsLogic {

    // region Constants

    private final String TAG = LevelsLogicStandard.class.getCanonicalName();

    private final String SHARED__IS_PAID = "shared__is_paid";

    // endregion


    // region Fields

    private String mCurrentLevelId;
    private long mLevelSelectedTimeMillis;

    // endregion


    // region Injections

    private LevelsRepository mLevelsRepository;
    private SharedService mSharedService;

    // endregion


    // region Constructors

    @Inject
    public LevelsLogicStandard(LevelsRepository levelsRepository, SharedService sharedService) {
        mLevelsRepository = levelsRepository;
        mSharedService = sharedService;
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

    @Override
    public void enablePaidVersion() {
        mSharedService.putBoolean(SHARED__IS_PAID, true);
        mLevelsRepository.openAllLevels();
    }

    @Override
    public boolean isPaid() {
        return mSharedService.getBoolean(SHARED__IS_PAID);
    }

    // endregion

}
