package arsenlibs.com.mosaic.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import javax.inject.Inject;

import arsenlibs.com.mosaic.R;
import arsenlibs.com.mosaic.presenters.main.MainPresenter;
import arsenlibs.com.mosaic.ui.board.BoardFragment;
import arsenlibs.com.mosaic.ui.loading.LoadingFragment;
import arsenlibs.com.mosaic.utils.FragmentStack;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements MainContract.View {

    // region Constants

    private final String TAG = MainActivity.class.getCanonicalName();

    // endregion


    // region Injections

    @Inject
    protected MainPresenter mPresenter;

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
//            mFragmentStack.replace(LoadingFragment.newInstance());
        mFragmentStack.replace(BoardFragment.newInstance());

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
    public void onInit() {
        // TODO
    }

    @Override
    public void onInitError(String message) {
        Toast.makeText(this, "onInitError: " + message, Toast.LENGTH_LONG).show();
    }

    // endregion


    // region implements Interaction Listeners

    // TODO for future

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

    // TODO

    // endregion

}
