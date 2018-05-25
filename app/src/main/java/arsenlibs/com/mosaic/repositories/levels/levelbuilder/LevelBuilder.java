package arsenlibs.com.mosaic.repositories.levels.levelbuilder;

import arsenlibs.com.mosaic.repositories.levels.domain.LevelData;

public interface LevelBuilder {

    LevelData[] getLevels();

    LevelData getLevel(String id);

}
