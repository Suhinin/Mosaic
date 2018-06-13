package com.childaplic.mosaic.di.modules;

import javax.inject.Singleton;

import com.childaplic.mosaic.services.advertising.AdvertisingGoogle;
import com.childaplic.mosaic.services.advertising.AdvertisingService;
import com.childaplic.mosaic.services.assets.AssetsService;
import com.childaplic.mosaic.services.assets.AssetsServiceStandard;
import com.childaplic.mosaic.services.imageloader.ImageLoaderService;
import com.childaplic.mosaic.services.imageloader.ImageLoaderStandard;
import com.childaplic.mosaic.services.logger.LoggerFabric;
import com.childaplic.mosaic.services.logger.LoggerService;
import com.childaplic.mosaic.services.shared.SharedPreferencesService;
import com.childaplic.mosaic.services.shared.SharedService;
import com.childaplic.mosaic.services.sqlite.DBManager;
import com.childaplic.mosaic.services.sqlite.SqlLiteManger;
import dagger.Module;
import dagger.Provides;


@Module
public class ServicesModule {

    @Provides
    @Singleton
    public SharedService provideSharedPreference(SharedPreferencesService service) {
        return service;
    }

    @Provides
    @Singleton
    public DBManager provideDBManager(SqlLiteManger service) {
        return service;
    }

    @Provides
    @Singleton
    public AssetsService provideAssets(AssetsServiceStandard service) {
        return service;
    }

    @Provides
    @Singleton
    public LoggerService provideLogger(LoggerFabric service) {
        return service;
    }

    @Provides
    @Singleton
    public AdvertisingService provideAdvertising(AdvertisingGoogle service) {
        return service;
    }

    @Provides
    @Singleton
    public ImageLoaderService provideImageLoader(ImageLoaderStandard service) {
        return service;
    }
//
//    @Provides
//    @Singleton
//    public NotificationService provideNotificationService(NotificationServiceStandard service) {
//        return service;
//    }
//
//    @Provides
//    @Singleton
//    public AlarmService provideAlarmService(AlarmServiceStandard service) {
//        return service;
//    }

}
