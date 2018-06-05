package arsenlibs.com.mosaic.di.modules;

import javax.inject.Singleton;

import arsenlibs.com.mosaic.businesslogics.LevelsLogic;
import arsenlibs.com.mosaic.businesslogics.LevelsLogicStandard;
import dagger.Module;
import dagger.Provides;

@Module
public class BusinessLogicsModule {

    @Provides
    @Singleton
    public LevelsLogic provideLevelsLogic(LevelsLogicStandard logic){
        return logic;
    }

}
