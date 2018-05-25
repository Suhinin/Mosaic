package arsenlibs.com.mosaic.di.modules;


import arsenlibs.com.mosaic.ui.main.MainActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract public class ActivitiesModule {

    @ContributesAndroidInjector
    abstract MainActivity provideMainActivity();

}
