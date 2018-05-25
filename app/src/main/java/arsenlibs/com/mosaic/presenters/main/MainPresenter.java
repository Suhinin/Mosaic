package arsenlibs.com.mosaic.presenters.main;

import javax.inject.Inject;

import arsenlibs.com.mosaic.ui.main.MainContract;
import arsenlibs.com.mosaic.ui.main.MainViewNull;

public class MainPresenter implements MainContract.Presenter {

    // region Fields

    private MainContract.View mView;

    // endregion


    // region Injections

    // TODO

    // endregion


    // region Constructors

    @Inject
    public MainPresenter() {
        // TODO
    }

    // endregion


    // region Implements MainContract.Presenter

    @Override
    public void onAttachView(MainContract.View view) {
        mView = view;
    }

    @Override
    public void onDetachView() {
        mView = new MainViewNull();
    }

    // endregion

}
