package com.childaplic.mosaic.services.payments;


public interface PaymentsService {

    void setListener(PaymentsListener paymentsListener);

    void requestPayment(String priceUSD);

    String getPrice();

    void onResult(boolean isSuccess);

}
