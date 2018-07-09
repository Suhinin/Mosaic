package com.childaplic.mosaic.services.payments;


import com.google.android.gms.wallet.WalletConstants;

import java.util.Arrays;
import java.util.List;

public class GooglePayConstants {

    // Changing this to ENVIRONMENT_PRODUCTION will make the API return real card information.
    // Please refer to the documentation to read about the required steps needed to enable
    // ENVIRONMENT_PRODUCTION.
    public static final int PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST;

    public static final List<Integer> SUPPORTED_NETWORKS = Arrays.asList(
            WalletConstants.CARD_NETWORK_AMEX,
            WalletConstants.CARD_NETWORK_DISCOVER,
            WalletConstants.CARD_NETWORK_VISA,
            WalletConstants.CARD_NETWORK_MASTERCARD
    );

    public static final List<Integer> SUPPORTED_METHODS = Arrays.asList(
            WalletConstants.PAYMENT_METHOD_CARD,
            WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD
    );

    public static final String CURRENCY_CODE = "USD";
    public static final String GATEWAY_TOKENIZATION_NAME = "example";

    private GooglePayConstants() {
        // private constructor
    }

}
