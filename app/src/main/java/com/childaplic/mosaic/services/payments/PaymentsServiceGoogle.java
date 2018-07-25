package com.childaplic.mosaic.services.payments;

import android.content.Context;
import android.content.Intent;

import com.childaplic.mosaic.ui.payment.PaymentActivity;

import javax.inject.Inject;

public class PaymentsServiceGoogle implements PaymentsService {

    // region Constants

    private final String TAG = PaymentsServiceGoogle.class.getCanonicalName();

    // endregion


    // region Fields

    private String mPriceUSD;
    private PaymentsListener mPaymentsListener;

    // endregion


    // region Injections

    private Context mContext;

    // endregion


    // region Constructors

    @Inject
    public PaymentsServiceGoogle(Context context) {
        mContext = context;
    }

    // endregion


    // region Implements PaymentsService


    @Override
    public void setListener(PaymentsListener paymentsListener) {
        mPaymentsListener = paymentsListener;
    }

    @Override
    public void requestPayment(String priceUSD) {
        mPriceUSD = priceUSD;

        Intent intent = new Intent(mContext, PaymentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public String getPrice() {
        return mPriceUSD;
    }

    @Override
    public void onResult(boolean isSuccess) {
        if (mPaymentsListener == null) {
            return;
        }

        mPaymentsListener.onResult(isSuccess);
    }

    // endregion

}
