package arsenlibs.com.mosaic.services.logger;

import android.content.Context;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.core.CrashlyticsCore;

import javax.inject.Inject;

import arsenlibs.com.mosaic.BuildConfig;
import io.fabric.sdk.android.Fabric;

public class LoggerFabric implements LoggerService {

    @Inject
    public LoggerFabric(Context context) {

        Fabric fabric = new Fabric.Builder(context)
                .kits(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build(), new Answers())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
    }

//    @Override
//    public void startLevel(LevelType levelType) {
//        Answers.getInstance().logLevelStart(new LevelStartEvent()
//                .putLevelName(levelType.toString()));
//    }
//
//    @Override
//    public void winLevel(LevelType levelType, int scores) {
//        Answers.getInstance().logLevelEnd(new LevelEndEvent()
//                .putLevelName(levelType.toString())
//                .putScore(scores)
//                .putSuccess(true));
//    }
//
//    @Override
//    public void loseLevel(LevelType levelType) {
//        Answers.getInstance().logLevelEnd(new LevelEndEvent()
//                .putLevelName(levelType.toString())
//                .putSuccess(false));
//    }
//
//    @Override
//    public void levelTerminate(LevelType levelType) {
//        Answers.getInstance().logCustom(new CustomEvent("Level Terminated")
//                .putCustomAttribute("LevelName", levelType.toString()));
//    }
//
//    @Override
//    public void openNotification() {
//        Answers.getInstance().logCustom(new CustomEvent("Open Notification"));
//    }
//
//    @Override
//    public void cancelNotification() {
//        Answers.getInstance().logCustom(new CustomEvent("Cancel Notification"));
//    }
//
//    @Override
//    public void installReferrer(String content) {
//        Answers.getInstance().logCustom(new CustomEvent("Install Referrer")
//                .putCustomAttribute("Content", content));
//    }
}
