package arsenlibs.com.mosaic.repositories.levels;

import java.util.Arrays;
import java.util.Comparator;

import javax.inject.Inject;

import arsenlibs.com.mosaic.repositories.levels.database.LevelDto;
import arsenlibs.com.mosaic.repositories.levels.database.LevelsDao;
import arsenlibs.com.mosaic.repositories.levels.domain.Level;
import arsenlibs.com.mosaic.repositories.levels.domain.LevelData;
import arsenlibs.com.mosaic.repositories.levels.domain.LevelImpl;
import arsenlibs.com.mosaic.repositories.levels.domain.LevelNull;
import arsenlibs.com.mosaic.repositories.levels.domain.LevelState;
import arsenlibs.com.mosaic.repositories.levels.levelbuilder.LevelBuilder;

public class LevelsRepositoryStandard implements LevelsRepository {

    // region Injections

    private LevelsDao mLevelsDao;
    private LevelBuilder mLevelBuilder;

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
    public Level[] getLevels() {
        LevelData[] levelDatas = mLevelBuilder.getLevels();
        if (levelDatas.length == 0) {
            return new Level[0];
        }

        Level[] levels = new Level[levelDatas.length];
        for (int i=0; i<levelDatas.length; i++) {
            levels[i] = createLevel(levelDatas[i]);
        }

        Arrays.sort(levels, new LevelsComparator());

        return levels;
    }

    @Override
    public Level getLevel(String id) {
        LevelData levelData = mLevelBuilder.getLevel(id);
        if (levelData == null) {
            return new LevelNull();
        }

        return createLevel(levelData);
    }

    @Override
    public void saveLevel(Level level) {
        LevelDto levelDto = toLevelDto(level);
        insertOrUpdateLevel(levelDto);
    }

    // endregion


    // region Private Methods

    private Level createLevel(LevelData levelData) {
        LevelImpl level = new LevelImpl();
        level.setId(levelData.getId());
        level.setNumber(levelData.getNumber());
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


    // region Inner Classes

    public static class LevelsComparator implements Comparator<Level> {

        @Override
        public int compare(Level l1, Level l2) {
            return l1.getNumber() - l2.getNumber();
        }

    }

    // endregion


}
