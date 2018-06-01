package arsenlibs.com.mosaic.di.modules;


import arsenlibs.com.mosaic.presenters.board.BoardPresenter;
import arsenlibs.com.mosaic.presenters.main.MainPresenter;
import arsenlibs.com.mosaic.ui.board.BoardContract;
import arsenlibs.com.mosaic.ui.main.MainContract;
import dagger.Module;
import dagger.Provides;


@Module
public class PresentersModule {

    @Provides
    public MainContract.Presenter provideMainPresenter(MainPresenter presenter){
        return presenter;
    }

    @Provides
    public BoardContract.Presenter provideBoardPresenter(BoardPresenter presenter){
        return presenter;
    }

}
