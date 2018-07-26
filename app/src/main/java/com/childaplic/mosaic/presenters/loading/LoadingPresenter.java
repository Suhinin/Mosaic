package com.childaplic.mosaic.presenters.loading;

import android.util.Log;

import javax.inject.Inject;

import com.childaplic.mosaic.repositories.levels.LevelsRepository;
import com.childaplic.mosaic.ui.loading.LoadingContract;
import com.childaplic.mosaic.ui.loading.LoadingViewNull;
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

    private String mLoadingErrorMsg;
    private long mLevelsRequestTimeMillis;

    // endregion


    // region Injections

    private LevelsRepository mLevelsRepository;

    // endregion


    // region Constructors

    @Inject
    public LoadingPresenter(LevelsRepository levelsRepository) {
        mLevelsRepository = levelsRepository;
    }

    // endregion


    // region Implements LoadingContract.Presenter

    @Override
    public void onAttachView(LoadingContract.View view) {
        mView = view;

        if (needRequestLevels()) {
            startLoadingTask();
        }
    }

    @Override
    public void onDetachView() {
        mView = new LoadingViewNull();
    }

    // endregion


    // region Private Methods

    private boolean needRequestLevels() {
        return mLevelsRequestTimeMillis <= 0;
    }

    private void startLoadingTask() {
        mView.onStartLoading();

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
                        mLevelsRequestTimeMillis = System.currentTimeMillis();
                        mView.onLoadingComplete();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "startLoadingTask error: " + throwable.getMessage());
                        mLoadingErrorMsg = throwable.getMessage();
                        mView.onLoadingError(throwable.getMessage());
                    }
                });
    }

    private void init() {
        mLevelsRepository.loadLevels();
    }

    // endregion

}
