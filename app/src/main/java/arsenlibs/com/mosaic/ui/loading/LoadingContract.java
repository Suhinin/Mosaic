package arsenlibs.com.mosaic.ui.loading;

public interface LoadingContract {

    interface View {

        void onStartLoading();

        void onLoadingComplete();

        void onLoadingError(String message);

    }

    interface Presenter {

        void onAttachView(View view);

        void onDetachView();

    }
}
