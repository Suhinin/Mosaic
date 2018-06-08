package com.childaplic.mosaic.di.modules;


import com.childaplic.mosaic.presenters.board.BoardPresenter;
import com.childaplic.mosaic.presenters.loading.LoadingPresenter;
import com.childaplic.mosaic.presenters.main.MainPresenter;
import com.childaplic.mosaic.presenters.selectlevel.SelectLevelPresenter;
import com.childaplic.mosaic.ui.board.BoardContract;
import com.childaplic.mosaic.ui.loading.LoadingContract;
import com.childaplic.mosaic.ui.main.MainContract;
import com.childaplic.mosaic.ui.selectlevel.SelectLevelContract;
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
