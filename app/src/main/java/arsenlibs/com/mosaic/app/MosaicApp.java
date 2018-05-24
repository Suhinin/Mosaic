package arsenlibs.com.mosaic.app;

import arsenlibs.com.mosaic.di.components.DaggerMosaicAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class MosaicApp extends DaggerApplication {

    // region Constants

    private final String IS_ALARMS_ENABLED = "is_alarm_enabled";

    // endregion


    // region Injections

//    @Inject
//    AlarmService mAlarmService;
//    @Inject
//    SharedService mSharedService;

    // endregion


    // region Implements DaggerApplication

    @Override
    public void onCreate() {
        super.onCreate();

//        boolean isAlarmsEnabled = mSharedService.getBoolean(IS_ALARMS_ENABLED);
//        if (isAlarmsEnabled == false) {
//            startAlarm();
//            mSharedService.putBoolean(IS_ALARMS_ENABLED, true);
//        }
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerMosaicAppComponent.builder().create(this);
    }

    // endregion


    // region Private Methods

//    private void startAlarm() {
//        Intent intent = new Intent(this, AlarmNotificationPublisher.class);
//        PendingIntent publisherIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//        int hour = getResources().getInteger(R.integer.notification_hour);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        mAlarmService.setRepeating(calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, publisherIntent);
//    }

    // endregion

}
