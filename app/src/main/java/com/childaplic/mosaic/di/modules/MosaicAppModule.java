package com.childaplic.mosaic.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import com.childaplic.mosaic.app.MosaicApp;
import dagger.Module;
import dagger.Provides;


@Module
abstract public class MosaicAppModule {

    @Provides
    @Singleton
    public static Context context(MosaicApp app) {
        return app.getApplicationContext();
    }

}
