package com.childaplic.mosaic.presenters.main;

import javax.inject.Inject;

import com.childaplic.mosaic.businesslogics.levels.LevelsLogic;
import com.childaplic.mosaic.services.logger.LoggerService;
import com.childaplic.mosaic.ui.main.MainContract;
import com.childaplic.mosaic.ui.main.MainViewNull;

public class MainPresenter implements MainContract.Presenter {

    // region Fields

    private MainContract.View mView;

    private boolean mShowBoardOnStart;

    // endregion


    // region Injections

    private LevelsLogic mLevelsLogic;
    private LoggerService mLoggerService;

    // endregion


    // region Constructors

    @Inject
    public MainPresenter(LevelsLogic levelsLogic, LoggerService loggerService) {
        mLevelsLogic = levelsLogic;
        mLoggerService = loggerService;

        mShowBoardOnStart = false;
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

    @Override
    public void setShowBoardOnStart() {
        mShowBoardOnStart = true;
    }

    @Override
    public boolean isShowBoardOnStart() {
        boolean isShowBoard = mShowBoardOnStart;
        mShowBoardOnStart = false;

        return isShowBoard;
    }

    @Override
    public boolean isPaid() {
        return mLevelsLogic.isPaid();
    }

    @Override
    public void logStartLevel() {
        int number = mLevelsLogic.getCurrentLevel().getNumber();
        mLoggerService.startLevel(number);
    }

    @Override
    public void logCompleteLevel() {
        int number = mLevelsLogic.getCurrentLevel().getNumber();
        mLoggerService.winLevel(number);
    }

    // endregion

}
