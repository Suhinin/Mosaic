package arsenlibs.com.mosaic.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import arsenlibs.com.mosaic.app.MosaicApp;
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
