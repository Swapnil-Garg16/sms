package buyhatke.com.sms.presenter;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

/**
 * Created by Divya on 5/1/2016.
 */

public class SmsApplication extends Application {

    public static final Uri INBOX_URI = Uri.parse("content://sms/inbox");

    @Override
    public void onCreate() {
        super.onCreate();
    }
}