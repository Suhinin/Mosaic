package arsenlibs.com.mosaic.businesslogics;

import javax.inject.Inject;

import arsenlibs.com.mosaic.repositories.levels.LevelsRepository;
import arsenlibs.com.mosaic.repositories.levels.domain.Level;
import arsenlibs.com.mosaic.repositories.levels.domain.LevelNull;

public class LevelsLogicStandard implements LevelsLogic {

    // region Constants

    private final String TAG = LevelsLogicStandard.class.getCanonicalName();

    // endregion


    // region Fields

    private String mCurrentLevelId;

    // endregion


    // region Injections

    private LevelsRepository mLevelsRepository;

    // endregion


    // region Constructors

    @Inject
    public LevelsLogicStandard(LevelsRepository levelsRepository) {
        mLevelsRepository = levelsRepository;

        mCurrentLevelId = "Level1.json";     // TODO temp
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
    }

    @Override
    public Level getCurrentLevel() {
        if (mCurrentLevelId == null) {
            return new LevelNull();
        }

        return mLevelsRepository.getLevel(mCurrentLevelId);
    }

    // endregion


    // region Private Methods
    // endregion

}
