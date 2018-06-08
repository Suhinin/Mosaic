package com.childaplic.mosaic.di.modules;

import javax.inject.Singleton;

import com.childaplic.mosaic.repositories.levels.database.LevelsDao;
import com.childaplic.mosaic.repositories.levels.database.LevelsDaoSql;
import dagger.Module;
import dagger.Provides;

@Module
public class DaosModule {

    @Provides
    @Singleton
    public LevelsDao provideLevelsDao(LevelsDaoSql dao) {
        return dao;
    }

}
