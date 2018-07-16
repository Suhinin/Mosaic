package com.childaplic.mosaic.ui.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import javax.inject.Inject;

import com.childaplic.mosaic.R;
import com.childaplic.mosaic.presenters.main.MainPresenter;
import com.childaplic.mosaic.services.advertising.AdvertisingOnClose;
import com.childaplic.mosaic.services.advertising.AdvertisingService;
import com.childaplic.mosaic.ui.board.BoardFragment;
import com.childaplic.mosaic.ui.board.BoardInteraction;
import com.childaplic.mosaic.ui.loading.LoadingFragment;
import com.childaplic.mosaic.ui.loading.LoadingInteraction;
import com.childaplic.mosaic.ui.selectlevel.SelectLevelFragment;
import com.childaplic.mosaic.ui.selectlevel.SelectLevelInteraction;
import com.childaplic.mosaic.utils.FragmentStack;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements
        MainContract.View,
        LoadingInteraction,
        SelectLevelInteraction,
        BoardInteraction {

    // region Constants

    private final String TAG = MainActivity.class.getCanonicalName();

    // endregion


    // region Injections

    @Inject
    protected MainPresenter mPresenter;
    @Inject
    protected AdvertisingService mAdvertisingService;

    // endregion


    // region Fields

    private FragmentStack mFragmentStack;
    private View mDecorView;

    // endregion


    // region Implements DaggerAppCompatActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createView());

        mDecorView = getWindow().getDecorView();
        mFragmentStack = new FragmentStack(getSupportFragmentManager(), R.id.main_activity__fragment_container_id);

        if (mPresenter.isShowBoardOnStart()) {
            showBoard();
        } else {
            showLoading();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (mPresenter.isShowBoardOnStart()) {
            showBoard();
        } else {
            showLoading();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.onAttachView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mPresenter.onDetachView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (hasFocus) {
                mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragmentStack.back() == false) {
            super.onBackPressed();
        }
    }

    // endregion


    // region Implements MainContract.View

    @Override
    public void onError(String message) {
        Toast.makeText(this, "onLoadingError: " + message, Toast.LENGTH_LONG).show();
    }

    // endregion


    // region Implements Interaction Listeners

    // region Implements LoadingInteraction

    @Override
    public void onLoadingCompleted() {
        showSelectLevel();
    }

    // endregion


    // region Implements SelectLevelInteraction

    @Override
    public void onLevelSelected() {
        if (mPresenter.isPaid()) {
            showBoard();
        } else {
            showAdvertising();
        }
    }

    // endregion


    // region Implements BoardInteraction

    @Override
    public void onLevelCompleted() {
        showSelectLevel();
    }

    // endregion

    // endregion


    // region Init View

    private View createView() {
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.addView(createFrameContainer());

        return frameLayout;
    }

    private View createFrameContainer() {
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(R.id.main_activity__fragment_container_id);

        return frameLayout;
    }

    // endregion


    // region Private Methods

    private void showSelectLevel() {
        mFragmentStack.replace(SelectLevelFragment.newInstance());
    }

    private void showAdvertising() {
        mAdvertisingService.show(new AdvertisingOnClose() {
            @Override
            public void onClose() {
                mAdvertisingService.loadNext();
                mPresenter.setShowBoardOnStart();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onError() {
                showBoard();
            }
        });
    }

    private void showBoard() {
        mFragmentStack.push(BoardFragment.newInstance());
    }

    private void showLoading() {
        mFragmentStack.replace(LoadingFragment.newInstance());
    }

    // endregion

}
