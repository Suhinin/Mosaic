package arsenlibs.com.mosaic.di.modules;

import arsenlibs.com.mosaic.ui.loading.LoadingFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract public class FragmentsModule {

    @ContributesAndroidInjector
    abstract LoadingFragment provideLoadingFragment();

}
