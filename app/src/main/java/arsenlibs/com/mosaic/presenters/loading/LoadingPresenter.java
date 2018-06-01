package arsenlibs.com.mosaic.presenters.loading;

import javax.inject.Inject;

import arsenlibs.com.mosaic.ui.loading.LoadingContract;
import arsenlibs.com.mosaic.ui.loading.LoadingViewNull;

public class LoadingPresenter implements LoadingContract.Presenter {

    // region Fields

    private LoadingContract.View mView;

    // endregion


    // region Constructors

    @Inject
    public LoadingPresenter() {
        // TODO
    }

    // endregion


    // region Implements LoadingContract.Presenter

    @Override
    public void onAttachView(LoadingContract.View view) {
        mView = view;
    }

    @Override
    public void onDetachView() {
        mView = new LoadingViewNull();
    }

    // endregion
}
