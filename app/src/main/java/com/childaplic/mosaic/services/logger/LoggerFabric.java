package com.childaplic.mosaic.services.logger;

import android.content.Context;

import com.childaplic.mosaic.businesslogics.Constants;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.LevelEndEvent;
import com.crashlytics.android.answers.LevelStartEvent;
import com.crashlytics.android.answers.PurchaseEvent;
import com.crashlytics.android.core.CrashlyticsCore;

import javax.inject.Inject;

import com.childaplic.mosaic.BuildConfig;

import java.math.BigDecimal;
import java.util.Currency;

import io.fabric.sdk.android.Fabric;

public class LoggerFabric implements LoggerService {

    private Context mContext;

    @Inject
    public LoggerFabric(Context context) {
        mContext = context;
    }

    @Override
    public void initCrashlytics() {
        Fabric fabric = new Fabric.Builder(mContext)
                .kits(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build(), new Answers())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
    }

    @Override
    public void startLevel(int levelNumber) {
        Answers.getInstance().logLevelStart(new LevelStartEvent()
                .putLevelName(String.valueOf(levelNumber)));
    }

    @Override
    public void winLevel(int levelNumber) {
        Answers.getInstance().logLevelEnd(new LevelEndEvent()
                .putLevelName(String.valueOf(levelNumber))
                .putSuccess(true));
    }

    @Override
    public void levelTerminate(int levelNumber) {
        Answers.getInstance().logCustom(new CustomEvent("Level Terminated")
                .putCustomAttribute("LevelNumber", levelNumber));
    }

    @Override
    public void purchase() {
        double price = Double.parseDouble(Constants.LEVELS_PRISE_USD);

        Answers.getInstance().logPurchase(new PurchaseEvent()
                .putItemPrice(BigDecimal.valueOf(price))
                .putCurrency(Currency.getInstance("USD"))
                .putItemName("Open levels")
                .putSuccess(true));
    }

    @Override
    public void purchaseError(int statusCode) {
        double price = Double.parseDouble(Constants.LEVELS_PRISE_USD);

        Answers.getInstance().logPurchase(new PurchaseEvent()
                .putItemPrice(BigDecimal.valueOf(price))
                .putCurrency(Currency.getInstance("USD"))
                .putItemName("Open levels")
                .putItemName("status code = " + statusCode)
                .putSuccess(false));
    }

    //
//    @Override
//    public void openNotification() {
//        Answers.getInstance().logCustom(new CustomEvent("Open Notification"));
//    }
//
//    @Override
//    public void cancelNotification() {
//        Answers.getInstance().logCustom(new CustomEvent("Cancel Notification"));
//    }
//
//    @Override
//    public void installReferrer(String content) {
//        Answers.getInstance().logCustom(new CustomEvent("Install Referrer")
//                .putCustomAttribute("Content", content));
//    }
}
