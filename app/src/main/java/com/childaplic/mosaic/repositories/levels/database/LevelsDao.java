package com.childaplic.mosaic.repositories.levels.database;


public interface LevelsDao {

    void insert(LevelDto level);

    void update(LevelDto level);

    LevelDto getById(String id);

    LevelDto[] getAll();

}
