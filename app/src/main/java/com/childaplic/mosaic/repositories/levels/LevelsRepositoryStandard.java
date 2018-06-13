package com.childaplic.mosaic.repositories.levels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.childaplic.mosaic.repositories.levels.database.LevelDto;
import com.childaplic.mosaic.repositories.levels.database.LevelsDao;
import com.childaplic.mosaic.repositories.levels.domain.Level;
import com.childaplic.mosaic.repositories.levels.domain.LevelData;
import com.childaplic.mosaic.repositories.levels.domain.LevelImpl;
import com.childaplic.mosaic.repositories.levels.domain.LevelNull;
import com.childaplic.mosaic.repositories.levels.domain.LevelState;
import com.childaplic.mosaic.repositories.levels.levelbuilder.LevelBuilder;

public class LevelsRepositoryStandard implements LevelsRepository {

    // region Injections

    private LevelsDao mLevelsDao;
    private LevelBuilder mLevelBuilder;

    // endregion


    // region Fields

    private Map<String, Level> mLevels;

    // endregion


    // region

    @Inject
    public LevelsRepositoryStandard(LevelsDao levelsDao, LevelBuilder levelBuilder) {
        mLevelsDao = levelsDao;
        mLevelBuilder = levelBuilder;
    }

    // endregion


    // region Implements LevelsRepository


    @Override
    public void loadLevels() {
        createLevels();
    }

    @Override
    public Level[] getLevels() {
        return mLevels.values().toArray(new Level[0]);
    }

    @Override
    public Level getLevel(String id) {
        if (mLevels.containsKey(id) == false) {
            return new LevelNull();
        }

        return mLevels.get(id);
    }

    @Override
    public void saveLevel(Level level) {
        LevelDto levelDto = toLevelDto(level);
        insertOrUpdateLevel(levelDto);
    }

    // endregion


    // region Private Methods

    private void createLevels() {
        mLevels = new HashMap<>();

        LevelData[] levelDatas = mLevelBuilder.createLevels();
        if (levelDatas.length == 0) {
            return;
        }

        for (LevelData levelData : levelDatas) {
            Level level = createLevel(levelData);
            mLevels.put(level.getId(), level);
        }
    }

    private Level createLevel(LevelData levelData) {
        LevelImpl level = new LevelImpl();
        level.setId(levelData.getId());
        level.setNumber(levelData.getNumber());
        level.setPreviewPath(levelData.getPreviewPath());
        level.setPalette(levelData.getPalette());
        level.setBoard(levelData.getBoard());

        LevelDto levelDto = mLevelsDao.getById(levelData.getId());
        if (levelDto != null) {
            level.setState(LevelState.fromString(levelDto.getState()));
            level.setIncorrectAnswers(levelDto.getIncorrectAnswers());
        } else {
            level.setState(LevelState.OPEN);
            level.setIncorrectAnswers(0);
        }

        return level;
    }

    private LevelDto toLevelDto(Level level) {
        LevelDto dto = new LevelDto();

        dto.setId(level.getId());
        dto.setState(level.getState().toString());
        dto.setShowOnBoarding(level.isShowOnBoarding());
        dto.setIncorrectAnswers(level.getIncorrectAnswers());

        return dto;
    }

    private void insertOrUpdateLevel(LevelDto level) {
        LevelDto exists = mLevelsDao.getById(level.getId());
        if (exists == null) {
            mLevelsDao.insert(level);
        } else {
            mLevelsDao.update(level);
        }
    }

    // endregion

}