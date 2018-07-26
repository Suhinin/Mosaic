package com.childaplic.mosaic.di.modules;


import com.childaplic.mosaic.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract public class ActivitiesModule {

    @ContributesAndroidInjector
    abstract MainActivity provideMainActivity();

}
