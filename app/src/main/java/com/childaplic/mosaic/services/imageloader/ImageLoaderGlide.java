package com.childaplic.mosaic.services.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;


public class ImageLoaderGlide implements ImageLoaderService {

    private final String ASSETS_PREFIX = "file:///android_asset/";

    private Context mContext;

    @Inject
    public ImageLoaderGlide(Context context) {
        mContext = context;
    }

    @Override
    public void loadUrl(String url, int reqWidth, int reqHeight, ImageView imageView) {
        // Glide automatically limits the size of the image it holds in cache and memory to the ImageView dimensions.
        Glide.with(mContext)
                .load(url)
                .into(imageView);
    }

    @Override
    public void loadAssets(String path, ImageView imageView) {
        Glide.with(mContext)
                .load(Uri.parse(ASSETS_PREFIX + path))
                .into(imageView);
    }

    @Override
    public void loadAssets(String path, int reqWidth, int reqHeight, ImageView imageView) {
        // Glide automatically limits the size of the image it holds in cache and memory to the ImageView dimensions.
        Glide.with(mContext)
                .load(Uri.parse(ASSETS_PREFIX + path))
                .into(imageView);
    }

    @Override
    public void loadAssets(String path, int reqWidth, int reqHeight, final OnLoadListener listener) {
        // Glide automatically limits the size of the image it holds in cache and memory to the ImageView dimensions.
        Uri assetsUri = Uri.parse(ASSETS_PREFIX + path);

        Glide.with(mContext)
                .asBitmap()
                .load(assetsUri)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        listener.onFinished(resource);
                    }
                });
    }

    @Override
    public void loadAssets(String path, final OnLoadListener listener) {
        // Glide automatically limits the size of the image it holds in cache and memory to the ImageView dimensions.
        Uri assetsUri = Uri.parse(ASSETS_PREFIX + path);

        Glide.with(mContext)
                .asBitmap()
                .load(assetsUri)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        listener.onFinished(resource);
                    }
                });
    }

    @Override
    public Bitmap getAssets(String path, int reqWidth, int reqHeight) {
        // Glide automatically limits the size of the image it holds in cache and memory to the ImageView dimensions.
        Uri assetsUri = Uri.parse(ASSETS_PREFIX + path);

        try {
            return Glide.with(mContext)
                    .asBitmap()
                    .load(assetsUri)
                    .submit()
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
