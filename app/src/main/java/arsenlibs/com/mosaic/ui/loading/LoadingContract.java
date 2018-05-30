package arsenlibs.com.mosaic.ui.loading;

public interface LoadingContract {

    interface View {

    }

    interface Presenter {

        void onAttachView(View view);

        void onDetachView();

    }
}
