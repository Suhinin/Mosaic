package arsenlibs.com.mosaic.di.modules;

import javax.inject.Singleton;

import arsenlibs.com.mosaic.repositories.levels.database.LevelsDao;
import arsenlibs.com.mosaic.repositories.levels.database.LevelsDaoSql;
import dagger.Module;
import dagger.Provides;

@Module
abstract public class DaosModule {

    @Provides
    @Singleton
    public LevelsDao provideLevelsDao(LevelsDaoSql dao) {
        return dao;
    }

}
