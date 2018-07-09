package com.childaplic.mosaic.repositories.levels.database;

import android.content.ContentValues;
import android.database.Cursor;

import javax.inject.Inject;

import com.childaplic.mosaic.repositories.levels.domain.LevelState;
import com.childaplic.mosaic.services.sqlite.DBManager;


public class LevelsDaoSql implements LevelsDao {

    // region Constants

    private final String TABLE_LEVEL = "levels_table";

    private final String LEVEL__ID = "id";
    private final String LEVEL__STATE = "state";
    private final String LEVEL__IS_SHOW_ON_BOARDING = "is_show_on_boarding";
    private final String LEVEL__INCORRECT_ANSWERS = "incorrect_answers";
    private final String LEVEL__BOARD_JSON = "board_json";

    // endregion


    // region Fields

    private DBManager mDBManager;

    // endregion


    // region Constructors

    @Inject
    public LevelsDaoSql(DBManager dbManager) {
        mDBManager = dbManager;

        createLevelTable();
    }

    // endregion


    // region Implements LevelsDao

    @Override
    public void insert(LevelDto levelDto) {
        ContentValues levelValues = createLevelValues(levelDto);
        mDBManager.insert(TABLE_LEVEL, levelValues);
    }

    @Override
    public void update(LevelDto levelDto) {
        ContentValues values = createLevelValues(levelDto);

        String whereClause = LEVEL__ID + " = ?";
        String[] whereArgs = new String[] {levelDto.getId()};

        mDBManager.update(TABLE_LEVEL, values, whereClause, whereArgs);
    }

    @Override
    public LevelDto getById(String id) {
        String selection = LEVEL__ID + " = ?";
        String[] selectionArgs = new String[] {id};

        Cursor cursor = mDBManager.get(TABLE_LEVEL, selection, selectionArgs);

        LevelDto levelDto = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                levelDto = getLevelDto(cursor);
            }
            cursor.close();
        }

        return levelDto;
    }

    @Override
    public LevelDto[] getAll() {
        Cursor cursor = mDBManager.getAll(TABLE_LEVEL);

        LevelDto[] levelDtos = new LevelDto[0];
        if (cursor != null) {
            levelDtos = new LevelDto[cursor.getCount()];

            int i=0;
            while (cursor.moveToNext()) {
                LevelDto levelDto = getLevelDto(cursor);
                levelDtos[i] = levelDto;
                i++;
            }
            cursor.close();
        }

        return levelDtos;
    }

    // endregion


    // region Service Methods

    private void createLevelTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_LEVEL + "(" +
                LEVEL__ID + " TEXT PRIMARY KEY, " +
                LEVEL__STATE + " TEXT, " +
                LEVEL__IS_SHOW_ON_BOARDING + " INTEGER, " +
                LEVEL__INCORRECT_ANSWERS + " INTEGER, " +
                LEVEL__BOARD_JSON + " TEXT " +
                ");";

        mDBManager.createTable(sql);
    }

    private ContentValues createLevelValues(LevelDto dto) {
        ContentValues values = new ContentValues();

        values.put(LEVEL__ID, dto.getId());
        values.put(LEVEL__STATE, dto.getState());
        values.put(LEVEL__IS_SHOW_ON_BOARDING, dto.isShowOnBoarding());
        values.put(LEVEL__INCORRECT_ANSWERS, dto.getIncorrectAnswers());
        values.put(LEVEL__BOARD_JSON, dto.getBoardJson());

        return values;
    }

    private LevelDto getLevelDto(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(LEVEL__ID);
        int stateIndex = cursor.getColumnIndex(LEVEL__STATE);
        int isShowOnBoardingIndex = cursor.getColumnIndex(LEVEL__IS_SHOW_ON_BOARDING);
        int incorrectAnswersIndex = cursor.getColumnIndex(LEVEL__INCORRECT_ANSWERS);
        int boardJsonIndex = cursor.getColumnIndex(LEVEL__BOARD_JSON);

        LevelDto dto = new LevelDto();
        dto.setId(cursor.getString(idIndex));
        dto.setState(cursor.getString(stateIndex));
        dto.setShowOnBoarding(cursor.getInt(isShowOnBoardingIndex) == 1);
        dto.setIncorrectAnswers(cursor.getInt(incorrectAnswersIndex));
        dto.setBoardJson(cursor.getString(boardJsonIndex));

        return dto;
    }

    // endregion
    
}
