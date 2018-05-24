package arsenlibs.com.mosaic.services.advertising;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import javax.inject.Inject;

import arsenlibs.com.mosaic.R;


public class AdvertisingGoogle extends AdListener implements AdvertisingService {

    // region Fields

    private Context mContext;
    private InterstitialAd mInterstitialAd;

    private AdvertisingOnClose mListener;

    // endregion


    // region Constructors

    @Inject
    public AdvertisingGoogle(Context context) {
        mContext = context;

        String admobAppId = mContext.getString(R.string.advertising__admob_app_id);
        String adUnitId = mContext.getString(R.string.advertising__ad_unit_id);

        MobileAds.initialize(mContext, admobAppId);

        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setImmersiveMode(true);
        mInterstitialAd.setAdUnitId(adUnitId);
        mInterstitialAd.setAdListener(this);

        Handler mainHandler = new Handler(mContext.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                loadNext();
            }
        };
        mainHandler.post(myRunnable);
    }

    // endregion


    // region Implements AdvertisingService

    @Override
    public void show(AdvertisingOnClose listener) {
        mListener = listener;

        Handler mainHandler = new Handler(mContext.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.e("AdvertisingGoogle", "The interstitial wasn't loaded yet.");
                    if (mListener != null) {
                        mListener.onError();
                        mListener = null;
                    }
                    loadNext();
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    @Override
    public void loadNext() {
        if (mInterstitialAd == null) {
            return;
        }

        if (mInterstitialAd.isLoading() || mInterstitialAd.isLoaded()) {
            return;
        }

        AdRequest adRequest = new AdRequest.Builder()
                .tagForChildDirectedTreatment(true)
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    // endregion


    // region Extends AdListener

    @Override
    public void onAdClosed() {
        super.onAdClosed();

        if (mListener != null) {
            mListener.onClose();
            mListener = null;
        }
    }

    // endregion

}
