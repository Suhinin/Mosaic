package com.childaplic.mosaic.presenters.selectlevel;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.Arrays;
import java.util.Comparator;

import javax.inject.Inject;

import com.childaplic.mosaic.businesslogics.LevelsLogic;
import com.childaplic.mosaic.repositories.levels.LevelsRepository;
import com.childaplic.mosaic.repositories.levels.domain.Level;
import com.childaplic.mosaic.ui.selectlevel.SelectLevelContract;
import com.childaplic.mosaic.ui.selectlevel.SelectLevelViewNull;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SelectLevelPresenter implements SelectLevelContract.Presenter {

    // region Constants

    private final String TAG = SelectLevelPresenter.class.getCanonicalName();

    // endregion


    // region Fields

    private SelectLevelContract.View mView;

    private LevelItem[] mLevelItems;

    private String mInitErrorMsg;
    private long mLevelsRequestTimeMillis;

    // endregion


    // region Injections

    private LevelsLogic mLevelsLogic;
    private LevelsRepository mLevelsRepository;

    // endregion


    // region Constructors

    @Inject
    public SelectLevelPresenter(LevelsLogic levelsLogic, LevelsRepository levelsRepository) {
        mLevelsLogic = levelsLogic;
        mLevelsRepository = levelsRepository;

        mLevelItems = new LevelItem[0];
    }

    // endregion


    // region Implements SelectLevelContract.Presenter

    @Override
    public void onAttachView(SelectLevelContract.View view) {
        mView = view;

        if (needRequestLevels()) {
            startInitTask();
        }
    }

    @Override
    public void onDetachView() {
        mView = new SelectLevelViewNull();
    }

    @Override
    public LevelItem[] getLevels() {
        return mLevelItems;
    }

    @Override
    public void selectLevel(String levelId) {
        mLevelsLogic.setCurrentLevelId(levelId);
    }

    @Override
    public void enablePaidVersion() {
        mLevelsLogic.enablePaidVersion();
        createLevelItems();
    }

    @Override
    public String getLevelPriceUSD() {
        return "0.00";
    }

    // endregion


    // region Private Methods

    private boolean needRequestLevels() {
        return mLevelsRequestTimeMillis <= 0;
    }

    @SuppressLint("CheckResult")
    private void startInitTask() {
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
                        mView.onInit();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "startInitTask error: " + throwable.getMessage());
                        mInitErrorMsg = throwable.getMessage();
                        mView.onInitError(throwable.getMessage());
                    }
                });
    }

    private void init() {
        createLevelItems();
    }

    private void createLevelItems() {
        Level[] levels = mLevelsRepository.getLevels();

        mLevelItems = new LevelItem[levels.length];
        int i=0;
        for (Level level : levels) {
            mLevelItems[i++] = createLevelItem(level);
        }

        Arrays.sort(mLevelItems, new LevelsComparator());
    }

    private LevelItem createLevelItem(Level level) {
        LevelItem levelItem = new LevelItem();
        levelItem.setId(level.getId());
        levelItem.setNumber(level.getNumber());
        levelItem.setPreviewPath(level.getPreviewPath());
        levelItem.setState(level.getState());

        return levelItem;
    }

    // endregion


    // region Inner Classes

    public static class LevelsComparator implements Comparator<LevelItem> {

        @Override
        public int compare(LevelItem l1, LevelItem l2) {
            return l1.getNumber() - l2.getNumber();
        }

    }

    // endregion

}
