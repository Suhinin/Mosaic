package arsenlibs.com.mosaic.ui.loading;

import android.widget.ImageView;

public interface LoadingContract {

    interface View {

    }

    interface Presenter {

        void onAttachView(View view);
        void onDetachView();

    }
}
