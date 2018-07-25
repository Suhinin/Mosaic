package com.childaplic.mosaic.di.modules;

import javax.inject.Singleton;

import com.childaplic.mosaic.businesslogics.levels.LevelsLogic;
import com.childaplic.mosaic.businesslogics.levels.LevelsLogicStandard;
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
