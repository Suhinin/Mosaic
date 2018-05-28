package arsenlibs.com.mosaic.di.modules;

import javax.inject.Singleton;

import arsenlibs.com.mosaic.services.advertising.AdvertisingGoogle;
import arsenlibs.com.mosaic.services.advertising.AdvertisingService;
import arsenlibs.com.mosaic.services.assets.AssetsService;
import arsenlibs.com.mosaic.services.assets.AssetsServiceStandard;
import arsenlibs.com.mosaic.services.imageloader.ImageLoaderService;
import arsenlibs.com.mosaic.services.imageloader.ImageLoaderStandard;
import arsenlibs.com.mosaic.services.logger.LoggerFabric;
import arsenlibs.com.mosaic.services.logger.LoggerService;
import arsenlibs.com.mosaic.services.sqlite.DBManager;
import arsenlibs.com.mosaic.services.sqlite.SqlLiteManger;
import dagger.Module;
import dagger.Provides;


@Module
abstract public class ServicesModule {

//    @Provides
//    @Singleton
//    public SharedService provideSharedPreference(SharedPreferencesService service) {
//        return service;
//    }
//
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
