package arsenlibs.com.mosaic.services.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import javax.inject.Inject;


public class ImageLoaderPicasso implements ImageLoaderService {

    private final String ASSETS_PREFIX = "file:///android_asset/";

    private Context mContext;

    @Inject
    public ImageLoaderPicasso(Context context) {
        mContext = context;
    }

    @Override
    public void loadUrl(String url, int reqWidth, int reqHeight, ImageView imageView) {
        // TODO fit() method resizes image to fit imageView dimensions (works only for imageView)
        Picasso.get()
                .load(url)
                .fit()
                .into(imageView);
    }

    @Override
    public void loadAssets(String path, ImageView imageView) {
        Picasso.get()
                .load(ASSETS_PREFIX + path)
                .fit()
                .into(imageView);
    }

    @Override
    public void loadAssets(String path, int reqWidth, int reqHeight, ImageView imageView) {
        // TODO fit() method resizes image to fit imageView dimensions (works only for imageView)
        Picasso.get()
                .load(ASSETS_PREFIX + path)
                .fit()
                .into(imageView);
    }

    @Override
    public void loadAssets(String path, int reqWidth, int reqHeight, final OnLoadListener listener) {
        Picasso.get()
                .load(ASSETS_PREFIX + path)
                .resize(reqWidth, reqHeight)
                .into(new com.squareup.picasso.Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        listener.onFinished(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        // TODO
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        // TODO
                    }
                });
    }

    @Override
    public void loadAssets(String path, final OnLoadListener listener) {
        Picasso.get()
                .load(ASSETS_PREFIX + path)
                .into(new com.squareup.picasso.Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        listener.onFinished(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        // TODO
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        // TODO
                    }
                });
    }

    @Override
    public Bitmap getAssets(String path, int reqWidth, int reqHeight) {
        try {
            return Picasso.get()
                    .load(ASSETS_PREFIX + path)
                    .resize(reqWidth, reqHeight)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
