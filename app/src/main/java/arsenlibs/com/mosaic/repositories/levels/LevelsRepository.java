package arsenlibs.com.mosaic.repositories.levels;


import arsenlibs.com.mosaic.repositories.levels.domain.Level;

public interface LevelsRepository {

    void loadLevels();

    Level[] getLevels();

    Level getLevel(String id);

    void saveLevel(Level level);

}
