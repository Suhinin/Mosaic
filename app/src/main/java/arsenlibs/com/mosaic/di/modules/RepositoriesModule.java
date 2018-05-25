package arsenlibs.com.mosaic.di.modules;

import javax.inject.Singleton;

import arsenlibs.com.mosaic.repositories.levels.LevelsRepository;
import arsenlibs.com.mosaic.repositories.levels.LevelsRepositoryStandard;
import arsenlibs.com.mosaic.repositories.levels.levelbuilder.LevelBuilder;
import arsenlibs.com.mosaic.repositories.levels.levelbuilder.LevelBuilderStandard;
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
