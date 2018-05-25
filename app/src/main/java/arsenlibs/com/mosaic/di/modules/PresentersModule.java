package arsenlibs.com.mosaic.di.modules;


import arsenlibs.com.mosaic.presenters.main.MainPresenter;
import arsenlibs.com.mosaic.ui.main.MainContract;
import dagger.Module;
import dagger.Provides;


@Module
abstract public class PresentersModule {

    @Provides
    public MainContract.Presenter provideMainPresenter(MainPresenter presenter){
        return presenter;
    }

}
