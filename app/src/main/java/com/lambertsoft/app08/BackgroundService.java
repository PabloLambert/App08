package com.lambertsoft.app08;

import android.app.AlarmManager;
import android.app.IntentService;
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

    public BackgroundService() {

        super("BackgroundService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "onHandleIntent..." + android_id);

        try {
            Client myKinveyClient = new Client.Builder("kid_WyE5rmap_", "b5f06467ecea486096b5e47104e4e098", getApplicationContext()).build();

            if (myKinveyClient.user().isUserLoggedIn() == false ){
                myKinveyClient.user().loginBlocking().execute();
            }

            AppData<Counter> data = myKinveyClient.appData("Counter", Counter.class);

            Query myQuery = myKinveyClient.query();
            myQuery.equals("AndroidId", android_id);

            Counter[] result = data.getBlocking(myQuery).execute();

            if (result == null ) {
                Counter counter = new Counter(android_id, 1);
                data.saveBlocking(counter).execute();
                Log.d(TAG, "counter 0");
            } else {

                Counter newCounter = result[0];
                Integer i = newCounter.getCounter();
                newCounter.setCounter(i + 1);
                data.saveBlocking(newCounter).execute();
                Log.d(TAG, "counter " + i);
            }


        } catch (Exception e) {
            Log.e(TAG, "Error " + e.toString());
        }





    }


}
