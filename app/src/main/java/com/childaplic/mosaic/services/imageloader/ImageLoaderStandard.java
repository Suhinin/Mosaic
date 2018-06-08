package com.childaplic.mosaic.services.imageloader;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;


public class ImageLoaderStandard implements ImageLoaderService {

    private final String TAG = ImageLoaderService.class.getCanonicalName();

    private Context mContext;

    @Inject
    public ImageLoaderStandard(Context context) {
        mContext = context;
    }


    // region Implements ImageLoaderService

    @Override
    public void loadUrl(String url, int reqWidth, int reqHeight, ImageView imageView) {
        // TODO unreleased
    }

    @Override
    public void loadAssets(String path, ImageView imageView) {
        try {
            AssetManager assets = mContext.getResources().getAssets();
            InputStream buffer = new BufferedInputStream(assets.open(path));
            Bitmap bitmap = BitmapFactory.decodeStream(buffer);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            // TODO LOG error or throw
        }
    }

    @Override
    public void loadAssets(String path, int reqWidth, int reqHeight, ImageView imageView) {
        try {
            Bitmap bitmap = decodeSampledBitmapFromAssets(path, reqWidth, reqHeight);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e(TAG, "loadAssets error: " + e.getMessage());
        }
    }

    @Override
    public void loadAssets(String path, int reqWidth, int reqHeight, OnLoadListener listener) {
        try {
            Bitmap bitmap = decodeSampledBitmapFromAssets(path, reqWidth, reqHeight);
            listener.onFinished(bitmap);
        } catch (Exception e) {
            // TODO LOG error or throw
        }
    }

    @Override
    public void loadAssets(String path, OnLoadListener listener) {
        try {
            AssetManager assets = mContext.getResources().getAssets();
            InputStream buffer = new BufferedInputStream(assets.open(path));
            Bitmap bitmap = BitmapFactory.decodeStream(buffer);
            listener.onFinished(bitmap);
        } catch (Exception e) {
            // TODO LOG error or throw
        }
    }

    @Override
    public Bitmap getAssets(String path, int reqWidth, int reqHeight) {
        try {
            return decodeSampledBitmapFromAssets(path, reqWidth, reqHeight);
        } catch (Exception e) {
            // TODO LOG error or throw
            return null;
        }
    }

    // endregion


    // region Private Methods

    private Bitmap decodeSampledBitmapFromAssets(String path, int reqWidth, int reqHeight) throws IOException {
        AssetManager assets = mContext.getResources().getAssets();
        InputStream buffer = assets.open(path);

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(buffer, null, options);
        buffer.reset();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(buffer, null, options);
    }

    private Bitmap decodeSampledBitmapFromResource(int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(mContext.getResources(), resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(mContext.getResources(), resId, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    // endregion

}
