package agency.vavien.ekolife;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

/**
 * Created by SD on 24.01.2018.
 * dilmacsedat@gmail.com
 * :)
 */

public class MyApplication extends Application {
    private static MyApplication singleton;

    public static MyApplication getInstance() {
        return singleton;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        singleton = this;

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
                    @Override
                    public void notificationOpened(OSNotificationOpenResult result) {
                        OSNotificationAction.ActionType actionType = result.action.type;
                        JSONObject data = result.notification.payload.additionalData;
                        Boolean bool = false;

                        Log.wtf("OneSignalExample", "data : " + data);

                        if (data != null)
                            bool = data.optBoolean("isMoodBool");
                        if (actionType == OSNotificationAction.ActionType.ActionTaken)
                            Log.wtf("OneSignalExample", "Button pressed with id: " + result.action.actionID);

                        if (bool) {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("isMoodBool", true);
                            startActivity(intent);
                        }
                    }
                })
                .init();

        // Call syncHashedEmail anywhere in your app if you have the user's email.
        // This improves the effectiveness of OneSignal's "best-time" notification scheduling feature.
        // OneSignal.syncHashedEmail(userEmail);
    }


}
