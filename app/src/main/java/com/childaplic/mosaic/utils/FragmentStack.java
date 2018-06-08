package com.childaplic.mosaic.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


public class FragmentStack {

    private final String TAG = FragmentStack.class.getCanonicalName();

    private FragmentManager mManager;
    private int mContainerId;

    /**
     *
     */
    public interface OnBackPressedHandlingFragment {
        boolean onBackPressed();
    }

    public FragmentStack(FragmentManager manager, int containerId) {
        this.mManager = manager;
        this.mContainerId = containerId;
    }

    /**
     * Pushes a fragment to the top of the stack.
     */
    public void push (Fragment fragment) {
        Fragment top = peek();
        if (top != null) {
            mManager.beginTransaction()
//                    .setCustomAnimations(R.anim.activity_open_enter_animation, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .remove(top)
                    .add(mContainerId, fragment, indexToTag(mManager.getBackStackEntryCount() + 1))
                    .addToBackStack(null)
                    .commit();
        } else {
            mManager.beginTransaction()
                    .add(mContainerId, fragment, indexToTag(0))
                    .commit();
        }

        mManager.executePendingTransactions();
    }

    /**
     * Pops the top item if the stack.
     */
    public boolean back () {
        Fragment top = peek();
        if (top instanceof OnBackPressedHandlingFragment) {
            if (((OnBackPressedHandlingFragment) top).onBackPressed() == true) {
                return true;
            }
        }

        return pop();
    }

    /**
     * Pops the topmost fragment from the stack.
     * The lowest fragment can't be popped, it can only be replaced.
     *
     * @return false if the stack can't pop or true if a top fragment has been popped.
     */
    public boolean pop () {
        if (mManager.getBackStackEntryCount() == 0) {
            return false;
        } else {
            mManager.popBackStackImmediate();
            return true;
        }
    }

    /**
     * Replaces stack contents with just one fragment.
     */
    public void replace (Fragment fragment) {
        mManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        mManager.beginTransaction()
                .replace(mContainerId, fragment, indexToTag(0))
                .commit();
        mManager.executePendingTransactions();
    }

    /**
     * Returns the topmost fragment in the stack.
     */
    public Fragment peek () {
        return mManager.findFragmentById(mContainerId);
    }

    /**
     *
     */
    private String indexToTag (int index) {
        return Integer.toString(index);
    }

}
