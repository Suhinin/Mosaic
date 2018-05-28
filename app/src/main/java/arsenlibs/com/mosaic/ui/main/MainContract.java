package arsenlibs.com.mosaic.ui.main;

public interface MainContract {

    interface View {

        void onInit();
        void onInitError(String message);

    }

    interface Presenter {

        void onAttachView(View view);
        void onDetachView();

    }

}
