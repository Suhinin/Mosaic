package arsenlibs.com.mosaic.presenters.loading;

import android.util.Log;

import javax.inject.Inject;

import arsenlibs.com.mosaic.repositories.levels.LevelsRepository;
import arsenlibs.com.mosaic.ui.loading.LoadingContract;
import arsenlibs.com.mosaic.ui.loading.LoadingViewNull;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoadingPresenter implements LoadingContract.Presenter {

    // region Constants

    private final String TAG = LoadingPresenter.class.getCanonicalName();

    // endregion


    // region Fields

    private LoadingContract.View mView;

    private LoadingState mLoadingState;
    private String mLoadingErrorMsg;

    // endregion


    // region Injections

    private LevelsRepository mLevelsRepository;

    // endregion


    // region Constructors

    @Inject
    public LoadingPresenter(LevelsRepository levelsRepository) {
        mLevelsRepository = levelsRepository;

        mLoadingState = LoadingState.NONE;
    }

    // endregion


    // region Implements LoadingContract.Presenter

    @Override
    public void onAttachView(LoadingContract.View view) {
        mView = view;

        if (mLoadingState == LoadingState.NONE) {
            startLoadingTask();
        }
    }

    @Override
    public void onDetachView() {
        mView = new LoadingViewNull();
    }

    // endregion


    // region Private Methods

    private void startLoadingTask() {
        mView.onStartLoading();
        mLoadingState = LoadingState.LOADING;

        Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        init();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        mLoadingState = LoadingState.COMPLETE;
                        mView.onLoadingComplete();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "startLoadingTask error: " + throwable.getMessage());
                        mLoadingErrorMsg = throwable.getMessage();
                        mLoadingState = LoadingState.ERROR;
                        mView.onLoadingError(throwable.getMessage());
                    }
                });
    }

    private void init() {
        mLevelsRepository.loadLevels();
    }

    // endregion

}
