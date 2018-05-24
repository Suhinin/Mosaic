package arsenlibs.com.mosaic.di.components;

import javax.inject.Singleton;

import arsenlibs.com.mosaic.app.MosaicApp;
import arsenlibs.com.mosaic.di.modules.ActivitiesModule;
import arsenlibs.com.mosaic.di.modules.BroadcastsModule;
import arsenlibs.com.mosaic.di.modules.DaosModule;
import arsenlibs.com.mosaic.di.modules.FragmentsModule;
import arsenlibs.com.mosaic.di.modules.MosaicAppModule;
import arsenlibs.com.mosaic.di.modules.PresentersModule;
import arsenlibs.com.mosaic.di.modules.RepositoriesModule;
import arsenlibs.com.mosaic.di.modules.ServicesModule;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules = {
        MosaicAppModule.class,
        PresentersModule.class,
        RepositoriesModule.class,
        ServicesModule.class,
        DaosModule.class,
        FragmentsModule.class,
        ActivitiesModule.class,
        BroadcastsModule.class,
        AndroidSupportInjectionModule.class
})
public interface MosaicAppComponent extends AndroidInjector<MosaicApp> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<MosaicApp> {}

}
