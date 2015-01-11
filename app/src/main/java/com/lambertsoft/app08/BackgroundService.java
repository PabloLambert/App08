package com.lambertsoft.app08;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.AppData;
import com.kinvey.java.Query;
import com.kinvey.java.User;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class BackgroundService extends IntentService {
    static final private String TAG = BackgroundService.class.getSimpleName();
    static final public int NOTIFICATION_ID = 33;

    public BackgroundService() {

        super("BackgroundService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int thr = intent.getIntExtra("threshold", 0);
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d(TAG, "onHandleIntent: " + android_id + "  - thr: " + thr);

        try {
            Client myKinveyClient = new Client.Builder("kid_WyE5rmap_", "b5f06467ecea486096b5e47104e4e098", getApplicationContext()).build();

            if (myKinveyClient.user().isUserLoggedIn() == false ){
                myKinveyClient.user().loginBlocking().execute();
            }

            AppData<Counter> data = myKinveyClient.appData("Counter", Counter.class);

            Query myQuery = myKinveyClient.query();
            myQuery.equals("AndroidId", android_id);

            Counter[] result = data.getBlocking(myQuery).execute();

            int c = 1;

            if (result == null ) {
                Counter counter = new Counter(android_id, c);
                data.saveBlocking(counter).execute();
                Log.d(TAG, "counter 0");
            } else {

                Counter newCounter = result[0];
                Integer i = newCounter.getCounter();
                c = i;
                newCounter.setCounter(i + 1);
                data.saveBlocking(newCounter).execute();
                Log.d(TAG, "counter " + i);
            }

            if (c == thr) {
                Log.d(TAG, "Notification...");
                Context context = getApplicationContext();

                Intent intent1 = new Intent();
                intent1.setClass(context, DisplayActivity.class);
                PendingIntent operation = PendingIntent.getActivity(context, 0, intent1, 0);

                Notification notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Threshold")
                        .setContentText("You have reach the " + thr + "event!")
                        .setSmallIcon(android.R.drawable.sym_action_email)
                        .setContentIntent(operation)
                        .setAutoCancel(true)
                        .build();

                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICATION_ID, notification);



            }


        } catch (Exception e) {
            Log.e(TAG, "Error " + e.toString());
        }





    }


}
