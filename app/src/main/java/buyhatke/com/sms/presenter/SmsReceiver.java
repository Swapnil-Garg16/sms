package buyhatke.com.sms.presenter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.widget.Toast;

import buyhatke.com.sms.R;

/**
 * Created by Divya on 5/6/2016.
 */

public class SmsReceiver extends BroadcastReceiver
{
    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();

                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";
            }
            Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();
            showNotification(context, smsMessageStr);

        }


        else
        {
            Toast.makeText(context, "nnnn", Toast.LENGTH_SHORT).show();

        }
    }

    private void showNotification(Context c,String notificationTitle){

        Intent intent = new Intent(c, MainActivity.class);
        /*intent.putExtra("title", strtitle);
        intent.putExtra("text", message);
        */
        PendingIntent pIntent = PendingIntent.getActivity(c, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(c)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("SMS notification")
                        .setContentText(notificationTitle)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true);

        NotificationManager notificationmanager = (NotificationManager) c
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationmanager.notify(0, mBuilder.build());
    }
}
