package com.childaplic.mosaic.services.payments;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

public class GooglePayUtils {

    // region Public Methods

    public static PaymentsClient createPaymentsClient(Activity activity) {
        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                .setEnvironment(GooglePayConstants.PAYMENTS_ENVIRONMENT)
                .build();
        return Wallet.getPaymentsClient(activity, walletOptions);
    }

    public static PaymentDataRequest createPaymentDataRequest(TransactionInfo transactionInfo) {
        PaymentMethodTokenizationParameters.Builder paramsBuilder = PaymentMethodTokenizationParameters.newBuilder()
                .setPaymentMethodTokenizationType(WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
                .addParameter("gateway", GooglePayConstants.GATEWAY_TOKENIZATION_NAME)
                .addParameter("gatewayMerchantId", "exampleGatewayMerchantId");

        return createPaymentDataRequest(transactionInfo, paramsBuilder.build());
    }

    public static Task<Boolean> isReadyToPay(PaymentsClient client) {
        IsReadyToPayRequest.Builder request = IsReadyToPayRequest.newBuilder();
        for (Integer allowedMethod : GooglePayConstants.SUPPORTED_METHODS) {
            request.addAllowedPaymentMethod(allowedMethod);
        }
        return client.isReadyToPay(request.build());
    }

    public static TransactionInfo createTransaction(String price) {
        return TransactionInfo.newBuilder()
                .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                .setTotalPrice(price)
                .setCurrencyCode(GooglePayConstants.CURRENCY_CODE)
                .build();
    }

    // endregion


    // region Constructors

    private GooglePayUtils() {
        // private constructor
    }

    // endregion


    // region Private Methods

    private static PaymentDataRequest createPaymentDataRequest(TransactionInfo transactionInfo, PaymentMethodTokenizationParameters params) {
        PaymentDataRequest request = PaymentDataRequest.newBuilder()
                .setTransactionInfo(transactionInfo)
                .addAllowedPaymentMethods(GooglePayConstants.SUPPORTED_METHODS)
                .setCardRequirements(CardRequirements.newBuilder()
                        .addAllowedCardNetworks(GooglePayConstants.SUPPORTED_NETWORKS)
                        .setAllowPrepaidCards(true)
                        .setBillingAddressRequired(true)
                        .setBillingAddressFormat(WalletConstants.BILLING_ADDRESS_FORMAT_FULL)
                        .build())
                .setPaymentMethodTokenizationParameters(params)
                .setUiRequired(true)
                .build();

        return request;
    }

    // endregion

}
