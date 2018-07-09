package com.childaplic.mosaic.ui.payment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.childaplic.mosaic.services.imageloader.ImageLoaderService;
import com.childaplic.mosaic.services.payments.GooglePayUtils;
import com.childaplic.mosaic.services.payments.PaymentsService;
import com.childaplic.mosaic.ui.common.Size;
import com.childaplic.mosaic.utils.LayoutHelper;
import com.childaplic.mosaic.utils.ScreenUtil;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodToken;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class PaymentActivity extends DaggerAppCompatActivity {

    // region Constants

    private final String TAG = PaymentActivity.class.getCanonicalName();

    private final String ASSETS_BACKGROUND = "images/board/bg_board.png";
    private final String ASSETS_PIG = "images/payment/pay_pig.png";
    private final String ASSETS_DESCRIPTION_EN = "images/payment/description_pay_en.png";
    private final String ASSETS_DESCRIPTION_RU = "images/payment/description_pay_ru.png";
    private final String ASSETS_BTN_PAY_NORMAL = "images/payment/btn_pay.png";
    private final String ASSETS_BTN_PAY_PRESSED = "images/payment/btn_pay_pressed.png";

    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    // endregion


    // region Fields

    private PaymentsClient mPaymentsClient;

    // endregion


    // region Injections

    @Inject
    protected PaymentsService mPaymentsService;
    @Inject
    protected ImageLoaderService mImageLoaderService;

    // endregion


    // region View Components

    private FrameLayout mRootView;
    private ImageView mImagePig;
    private ImageView mImageDescription;
    private ImageView mBtnPay;

    // endregion


    // region Implements DaggerAppCompatActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createView());

        mPaymentsClient = GooglePayUtils.createPaymentsClient(this);

        mBtnPay.setEnabled(true);
        checkIsReadyToPay();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                boolean isSuccess = handleResult(resultCode, data);
                mPaymentsService.onResult(isSuccess);
                finish();
                break;
        }
    }

    // endregion


    // region Init View

    private View createView() {
        mRootView = new FrameLayout(this);
        addBackground();
        addPig();
        addDescription();
        addPayButton();

        return mRootView;
    }

    private void addBackground() {
        ImageView imageBackground = new ImageView(this);
        imageBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mRootView.addView(imageBackground, LayoutHelper.createFramePx(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        Size screenSize = ScreenUtil.getScreenSize(this);
        mImageLoaderService.loadAssets(ASSETS_BACKGROUND, screenSize.getWidth(), screenSize.getHeight(), imageBackground);
    }

    private void addPig() {
        // TODO
    }

    private void addDescription() {
        // TODO
    }

    private void addPayButton() {
        mBtnPay = new ImageView(this);
        mBtnPay.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mBtnPay.setImageDrawable(createBtnPayDrawable());
        mBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPayment();
            }
        });

        mRootView.addView(mBtnPay, LayoutHelper.createFramePx(mBtnSize, Gravity.END | Gravity.TOP));
    }

    private Drawable createBtnPayDrawable() {
        Bitmap pressed = mImageLoaderService.getAssets(ASSETS_BTN_PAY_PRESSED, mBtnSize.getWidth(), mBtnSize.getHeight());
        Bitmap normal = mImageLoaderService.getAssets(ASSETS_BTN_PAY_NORMAL, mBtnSize.getWidth(), mBtnSize.getHeight());

        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, new BitmapDrawable(getResources(), pressed));
        drawable.addState(new int[]{}, new BitmapDrawable(getResources(), normal));

        return drawable;
    }

    // endregion


    // region Public Methods

    public void requestPayment() {
        TransactionInfo transaction = GooglePayUtils.createTransaction(mPaymentsService.getPrice());
        final PaymentDataRequest request = GooglePayUtils.createPaymentDataRequest(transaction);

        Task<PaymentData> futurePaymentData = mPaymentsClient.loadPaymentData(request);
        AutoResolveHelper.resolveTask(futurePaymentData, this, LOAD_PAYMENT_DATA_REQUEST_CODE);
    }

    // endregion


    // region Private Methods

    private void checkIsReadyToPay() {
        GooglePayUtils.isReadyToPay(mPaymentsClient).addOnCompleteListener(
                new OnCompleteListener<Boolean>() {
                    public void onComplete(Task<Boolean> task) {
                        try {
                            boolean isPayAvailable = task.getResult(ApiException.class);
                            if (isPayAvailable) {
                                mBtnPay.setEnabled(true);
                            } else {
                                Log.e(TAG, "checkIsReadyToPay result failed");
                            }
                        } catch (ApiException exception) {
                            Log.e(TAG, "isReadyToPay failed: " + exception);
                        }
                    }
                });
    }

    private boolean handleResult(int resultCode, Intent data) {
        switch (resultCode) {
            case Activity.RESULT_OK:
                PaymentData paymentData = PaymentData.getFromIntent(data);
                handlePaymentSuccess(paymentData);

                return true;
            case AutoResolveHelper.RESULT_ERROR:
                Status status = AutoResolveHelper.getStatusFromIntent(data);
                handleError(status.getStatusCode());

                return false;
            default:
                return false;
        }
    }

    private void handlePaymentSuccess(PaymentData paymentData) {
        PaymentMethodToken token = paymentData.getPaymentMethodToken();
        if (token == null) {
            return;
        }

        String billingName = paymentData.getCardInfo().getBillingAddress().getName();
        Toast.makeText(this, "payments show name " + billingName, Toast.LENGTH_LONG).show();
    }

    private void handleError(int statusCode) {
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }

    // endregion

}
