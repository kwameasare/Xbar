package genius.Xbar;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.ByteArrayOutputStream;

import static android.content.ContentValues.TAG;

public class NotificationService extends NotificationListenerService {

    Context context;

    @Override

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();

    }
    @Override

    public void onNotificationPosted(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();
        String ticker ="";
        if(sbn.getNotification().tickerText !=null) {
            ticker = sbn.getNotification().tickerText.toString();
        }
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString(Notification.EXTRA_TITLE);
        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
        int idl = extras.getInt(Notification.EXTRA_SMALL_ICON);
        //Bitmap id = (Bitmap) sbn.getNotification().largeIcon;
        Bitmap id = extras.getParcelable(Notification.EXTRA_LARGE_ICON);

        Log.i("Package",pack);
        Log.i("Ticker",ticker);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Text : " + text);

        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("package", pack);
        msgrcv.putExtra("ticker", ticker);
        msgrcv.putExtra("title", title);
        if (text != null) {
            msgrcv.putExtra("text", text.toString());
        }
        msgrcv.putExtra("id",idl);
        if(id != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            id.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            msgrcv.putExtra("icon",byteArray);
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);


    }

    @Override



    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");

    }


}