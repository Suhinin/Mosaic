package com.childaplic.mosaic.di.modules;

import javax.inject.Singleton;

import com.childaplic.mosaic.repositories.levels.LevelsRepository;
import com.childaplic.mosaic.repositories.levels.LevelsRepositoryStandard;
import com.childaplic.mosaic.repositories.levels.levelbuilder.LevelBuilder;
import com.childaplic.mosaic.repositories.levels.levelbuilder.LevelBuilderStandard;
import dagger.Module;
import dagger.Provides;


@Module
public class RepositoriesModule {

    @Provides
    @Singleton
    public LevelsRepository provideLevelsRepository(LevelsRepositoryStandard repository) {
        return repository;
    }

    @Provides
    @Singleton
    public LevelBuilder provideLevelBuilder(LevelBuilderStandard builder) {
        return builder;
    }

}
