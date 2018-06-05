package arsenlibs.com.mosaic.di.modules;

import arsenlibs.com.mosaic.ui.board.BoardFragment;
import arsenlibs.com.mosaic.ui.loading.LoadingFragment;
import arsenlibs.com.mosaic.ui.selectlevel.SelectLevelFragment;
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
