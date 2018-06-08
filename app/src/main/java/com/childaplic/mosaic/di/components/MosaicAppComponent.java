package com.childaplic.mosaic.di.components;

import javax.inject.Singleton;

import com.childaplic.mosaic.app.MosaicApp;
import com.childaplic.mosaic.di.modules.ActivitiesModule;
import com.childaplic.mosaic.di.modules.BroadcastsModule;
import com.childaplic.mosaic.di.modules.BusinessLogicsModule;
import com.childaplic.mosaic.di.modules.DaosModule;
import com.childaplic.mosaic.di.modules.FragmentsModule;
import com.childaplic.mosaic.di.modules.MosaicAppModule;
import com.childaplic.mosaic.di.modules.PresentersModule;
import com.childaplic.mosaic.di.modules.RepositoriesModule;
import com.childaplic.mosaic.di.modules.ServicesModule;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules = {
        MosaicAppModule.class,
        PresentersModule.class,
        BusinessLogicsModule.class,
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
