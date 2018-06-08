package com.childaplic.mosaic.services.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;


public interface ImageLoaderService {

    void loadUrl(String url, int reqWidth, int reqHeight, ImageView imageView);

    void loadAssets(String path, ImageView imageView);
    void loadAssets(String path, int reqWidth, int reqHeight, ImageView imageView);
    void loadAssets(String path, int reqWidth, int reqHeight, OnLoadListener listener);
    void loadAssets(String path, OnLoadListener listener);

    Bitmap getAssets(String path, int reqWidth, int reqHeight);

    interface OnLoadListener {

        void onFinished(Bitmap bitmap);

    }

}
