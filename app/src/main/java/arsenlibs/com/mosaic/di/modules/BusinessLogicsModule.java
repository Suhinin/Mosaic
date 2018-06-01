package arsenlibs.com.mosaic.di.modules;

import arsenlibs.com.mosaic.businesslogics.LevelsLogic;
import arsenlibs.com.mosaic.businesslogics.LevelsLogicStandard;
import dagger.Module;
import dagger.Provides;

@Module
public class BusinessLogicsModule {

    @Provides
    public LevelsLogic provideLevelsLogic(LevelsLogicStandard logic){
        return logic;
    }

}
