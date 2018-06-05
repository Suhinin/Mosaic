package arsenlibs.com.mosaic.di.modules;


import arsenlibs.com.mosaic.presenters.board.BoardPresenter;
import arsenlibs.com.mosaic.presenters.loading.LoadingPresenter;
import arsenlibs.com.mosaic.presenters.main.MainPresenter;
import arsenlibs.com.mosaic.presenters.selectlevel.SelectLevelPresenter;
import arsenlibs.com.mosaic.ui.board.BoardContract;
import arsenlibs.com.mosaic.ui.loading.LoadingContract;
import arsenlibs.com.mosaic.ui.main.MainContract;
import arsenlibs.com.mosaic.ui.selectlevel.SelectLevelContract;
import dagger.Module;
import dagger.Provides;


@Module
public class PresentersModule {

    @Provides
    public MainContract.Presenter provideMainPresenter(MainPresenter presenter){
        return presenter;
    }

    @Provides
    public LoadingContract.Presenter provideLoadingPresenter(LoadingPresenter presenter){
        return presenter;
    }

    @Provides
    public SelectLevelContract.Presenter provideSelectLevelPresenter(SelectLevelPresenter presenter){
        return presenter;
    }

    @Provides
    public BoardContract.Presenter provideBoardPresenter(BoardPresenter presenter){
        return presenter;
    }

}
