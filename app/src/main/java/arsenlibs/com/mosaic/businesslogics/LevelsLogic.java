package arsenlibs.com.mosaic.businesslogics;

import arsenlibs.com.mosaic.repositories.levels.domain.Level;

public interface LevelsLogic {

    String getCurrentLevelId();

    void setCurrentLevelId(String levelId);

    Level getCurrentLevel();

    long getLevelSelectedTimeMillis();
}
