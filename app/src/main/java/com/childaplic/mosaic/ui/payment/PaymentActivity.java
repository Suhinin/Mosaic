package com.childaplic.mosaic.ui.payment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.childaplic.mosaic.R;
import com.childaplic.mosaic.businesslogics.Constants;
import com.childaplic.mosaic.services.imageloader.ImageLoaderService;
import com.childaplic.mosaic.services.logger.LoggerService;
import com.childaplic.mosaic.services.payments.GooglePayUtils;
import com.childaplic.mosaic.services.payments.PaymentsService;
import com.childaplic.mosaic.ui.common.Margin;
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

import java.util.Currency;
import java.util.Locale;

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
    private Size mBtnPaySize;
    private Margin mBtnPayMargin;
    private Size mPigSize;
    private Size mDescriptionSize;

    // endregion


    // region Injections

    @Inject
    protected PaymentsService mPaymentsService;
    @Inject
    protected ImageLoaderService mImageLoaderService;
    @Inject
    protected LoggerService mLoggerService;

    // endregion


    // region View Components

    private View mDecorView;

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

        mDecorView = getWindow().getDecorView();
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
                if (isSuccess) {
                    finish();
                }
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (hasFocus) {
                mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    // endregion


    // region Init View

    private View createView() {
        mRootView = new FrameLayout(this);

        calculatePayBtnSize();
        calculatePigWidth();
        calculateDescriptionSize();
        calculatePayBtnMargin();

        addBackground();
        addPig();
        addDescription();
        addPayButton();

        return mRootView;
    }

    private void calculatePigWidth() {
        Size screenSize = ScreenUtil.getScreenSize(this);
        int width = (int)(screenSize.getWidth()*0.4f);

        mPigSize = new Size(width, screenSize.getHeight());
    }

    private void calculatePayBtnSize() {
        int size = (int) getResources().getDimension(R.dimen.payment_activity__btn_pay_size);

        mBtnPaySize = new Size(size, size);
    }

    private void calculateDescriptionSize() {
        Size screenSize = ScreenUtil.getScreenSize(this);
        int width = screenSize.getWidth()-mPigSize.getWidth();

        int btnPayMargin = (int) getResources().getDimension(R.dimen.payment_activity__btn_pay_margin);
        int height = screenSize.getHeight() - mBtnPaySize.getHeight() - 2*btnPayMargin;

        mDescriptionSize = new Size(width, height);
    }

    private void calculatePayBtnMargin() {
        int left = mPigSize.getWidth() + (mDescriptionSize.getWidth() - mBtnPaySize.getWidth())/2;

        int btnPayMargin = (int) getResources().getDimension(R.dimen.payment_activity__btn_pay_margin);
        int top = mDescriptionSize.getHeight() + btnPayMargin;

        mBtnPayMargin = new Margin(left, top, 0,0);
    }

    private void addBackground() {
        ImageView imageBackground = new ImageView(this);
        imageBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mRootView.addView(imageBackground, LayoutHelper.createFramePx(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        Size screenSize = ScreenUtil.getScreenSize(this);
        mImageLoaderService.loadAssets(ASSETS_BACKGROUND, screenSize.getWidth(), screenSize.getHeight(), imageBackground);
    }

    private void addPig() {
        mImagePig = new ImageView(this);
        mImagePig.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mImagePig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPayment();
            }
        });

        mImageLoaderService.loadAssets(ASSETS_PIG, mPigSize.getWidth(), mPigSize.getWidth(), mImagePig);

        mRootView.addView(mImagePig, LayoutHelper.createFramePx(mPigSize, Gravity.START | Gravity.TOP));
    }

    private void addDescription() {
        mImageDescription = new ImageView(this);
        mImageDescription.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mImageDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPayment();
            }
        });

        String assetsPath = getDescriptionAssetsPath();
        mImageLoaderService.loadAssets(assetsPath, mDescriptionSize.getWidth(), mDescriptionSize.getHeight(), mImageDescription);

        mRootView.addView(mImageDescription, LayoutHelper.createFramePx(mDescriptionSize, Gravity.START | Gravity.TOP, mPigSize.getWidth(), 0, 0, 0));
    }

    private String getDescriptionAssetsPath() {
        Locale locale = getResources().getConfiguration().locale;
        switch (locale.getLanguage()) {
            case "ru":
                return ASSETS_DESCRIPTION_RU;
            default:
                return ASSETS_DESCRIPTION_EN;
        }
    }

    private String getBtnPayText() {
//        Locale locale = getResources().getConfiguration().locale;
//        Currency currency = Currency.getInstance(locale);
//        return currency.getCurrencyCode();

        // TODO use Constants.LEVELS_PRISE_USD

        return "3 $";
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

        TextView textView = new TextView(this);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLines(1);
        textView.setAllCaps(true);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setText(getBtnPayText());

        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.addView(mBtnPay, LayoutHelper.createFramePx(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        frameLayout.addView(textView, LayoutHelper.createFramePx(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));

        mRootView.addView(frameLayout, LayoutHelper.createFramePx(mBtnPaySize, Gravity.START | Gravity.TOP, mBtnPayMargin));
    }

    private Drawable createBtnPayDrawable() {
        Bitmap pressed = mImageLoaderService.getAssets(ASSETS_BTN_PAY_PRESSED, mBtnPaySize.getWidth(), mBtnPaySize.getHeight());
        Bitmap normal = mImageLoaderService.getAssets(ASSETS_BTN_PAY_NORMAL, mBtnPaySize.getWidth(), mBtnPaySize.getHeight());

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

        mLoggerService.purchase();
        Toast.makeText(this, R.string.payment_activity__payment_success, Toast.LENGTH_LONG).show();
    }

    private void handleError(int statusCode) {
        mLoggerService.purchaseError(statusCode);
        Log.e("handleError error", String.format("Error code: %d", statusCode));
        Toast.makeText(this, R.string.payment_activity__payment_failed, Toast.LENGTH_LONG).show();
    }

    // endregion

}
