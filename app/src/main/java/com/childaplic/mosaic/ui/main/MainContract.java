package com.childaplic.mosaic.ui.main;

public interface MainContract {

    interface View {

        void onError(String message);

    }

    interface Presenter {

        void onAttachView(View view);
        void onDetachView();

        void setShowBoardOnStart();
        boolean isShowBoardOnStart();

        boolean isPaid();

    }

}
