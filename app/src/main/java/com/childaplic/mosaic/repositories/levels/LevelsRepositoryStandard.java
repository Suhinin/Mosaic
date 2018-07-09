package com.childaplic.mosaic.repositories.levels;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.childaplic.mosaic.repositories.levels.database.LevelDto;
import com.childaplic.mosaic.repositories.levels.database.LevelsDao;
import com.childaplic.mosaic.repositories.levels.domain.Cell;
import com.childaplic.mosaic.repositories.levels.domain.Level;
import com.childaplic.mosaic.repositories.levels.domain.LevelData;
import com.childaplic.mosaic.repositories.levels.domain.LevelImpl;
import com.childaplic.mosaic.repositories.levels.domain.LevelNull;
import com.childaplic.mosaic.repositories.levels.domain.LevelState;
import com.childaplic.mosaic.repositories.levels.levelbuilder.LevelBuilder;
import com.google.gson.Gson;

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
    public Level resetLevel(String id) {
        LevelData defaultLevel = getDefaultLevel(id);
        if (defaultLevel == null) {
            return new LevelNull();
        }

        LevelImpl level = new LevelImpl();
        level.setId(defaultLevel.getId());
        level.setNumber(defaultLevel.getNumber());
        level.setPreviewPath(defaultLevel.getPreviewPath());
        level.setPalette(defaultLevel.getPalette());
        level.setBoard(createBoard(defaultLevel.getBoard()));
        level.setState(defaultLevel.isOpen() ? LevelState.OPEN : LevelState.DISABLED);
        level.setIncorrectAnswers(0);
        level.setShowOnBoarding(false);

        saveLevel(level);

        return level;
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
        if (mLevels.containsKey(level.getId())) {
            mLevels.put(level.getId(), level);
        }

        LevelDto levelDto = toLevelDto(level);
        insertOrUpdateLevel(levelDto);
    }

    @Override
    public void openAllLevels() {
        for (Level level : mLevels.values()) {
            if (level.getState() == LevelState.DISABLED) {
                level.setState(LevelState.OPEN);

                LevelDto levelDto = toLevelDto(level);
                insertOrUpdateLevel(levelDto);
            }
        }
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

        LevelDto levelDto = mLevelsDao.getById(levelData.getId());
        if (levelDto != null) {
            level.setState(LevelState.fromString(levelDto.getState()));
            level.setIncorrectAnswers(levelDto.getIncorrectAnswers());
            level.setBoard(deserializeBoard(levelDto.getBoardJson()));
        } else {
            level.setState(levelData.isOpen() ? LevelState.OPEN : LevelState.DISABLED);
            level.setIncorrectAnswers(0);
            level.setBoard(createBoard(levelData.getBoard()));
        }

        return level;
    }

    private Cell[][] deserializeBoard(String boardJson) {
        return new Gson().fromJson(boardJson, Cell[][].class);
    }

    private Cell[][] createBoard(String[][] pieceIds) {
        int rows = pieceIds.length;
        int cols = pieceIds.length > 0 ? pieceIds[0].length : 0;

        Cell[][] cells = new Cell[rows][cols];
        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                String pieceId = pieceIds[i][j];
                cells[i][j] = new Cell(pieceId);
            }
        }

        return cells;
    }

    private LevelDto toLevelDto(Level level) {
        LevelDto dto = new LevelDto();

        dto.setId(level.getId());
        dto.setState(level.getState().toString());
        dto.setShowOnBoarding(level.isShowOnBoarding());
        dto.setIncorrectAnswers(level.getIncorrectAnswers());
        dto.setBoardJson(serializeBoard(level.getBoard()));

        return dto;
    }

    private String serializeBoard(Cell[][] board) {
        return new Gson().toJson(board);
    }

    private void insertOrUpdateLevel(LevelDto level) {
        LevelDto exists = mLevelsDao.getById(level.getId());
        if (exists == null) {
            mLevelsDao.insert(level);
        } else {
            mLevelsDao.update(level);
        }
    }

    private LevelData getDefaultLevel(String id) {
        LevelData[] levelDatas = mLevelBuilder.createLevels();
        for (LevelData levelData : levelDatas) {
            if (id.equals(levelData.getId())) {
                return levelData;
            }
        }

        return null;
    }

    // endregion

}
