package com.childaplic.mosaic.repositories.levels;


import com.childaplic.mosaic.repositories.levels.domain.Level;

public interface LevelsRepository {

    void loadLevels();

    Level resetLevel(String id);

    Level[] getLevels();

    Level getLevel(String id);

    void saveLevel(Level level);

}
