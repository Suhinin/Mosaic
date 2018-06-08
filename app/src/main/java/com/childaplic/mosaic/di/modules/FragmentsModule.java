package com.childaplic.mosaic.di.modules;

import com.childaplic.mosaic.ui.board.BoardFragment;
import com.childaplic.mosaic.ui.loading.LoadingFragment;
import com.childaplic.mosaic.ui.selectlevel.SelectLevelFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract public class FragmentsModule {

    @ContributesAndroidInjector
    abstract LoadingFragment provideLoadingFragment();

    @ContributesAndroidInjector
    abstract BoardFragment provideBoardFragment();

    @ContributesAndroidInjector
    abstract SelectLevelFragment provideSelectLevelFragment();

}
