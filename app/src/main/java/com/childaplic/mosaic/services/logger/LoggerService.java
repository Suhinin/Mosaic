package com.childaplic.mosaic.services.logger;


public interface LoggerService {

    void initCrashlytics();

    void startLevel(int levelNumber);

    void winLevel(int levelNumber);

    void levelTerminate(int levelNumber);

    void purchase();

    void purchaseError(int statusCode);

//    void openNotification();
//
//    void cancelNotification();
//
//    void installReferrer(String content);

}
