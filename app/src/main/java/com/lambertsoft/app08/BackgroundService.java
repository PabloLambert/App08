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
import com.kinvey.java.Query;
import com.kinvey.java.User;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class BackgroundService extends IntentService {
    Client myKinveyClient;
    int count;
    static final private String TAG = BackgroundService.class.getSimpleName();

    public BackgroundService() {

        super("BackgroundService");
        count = 0;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "onHandleIntent..." + count);
        count ++;
        /*
        if (myKinveyClient == null ) {
            myKinveyClient = new Client.Builder("kid_WyE5rmap_", "b5f06467ecea486096b5e47104e4e098", getApplicationContext()).build();

            myKinveyClient.user().login(new KinveyUserCallback() {
                @Override
                public void onFailure(Throwable error) {
                    Log.e(TAG, "Login Failure", error);
                }
                @Override
                public void onSuccess(User result) {
                    Log.i(TAG, "Logged in a new implicit user with id: " + result.getId());
                }
            });
        }

        AsyncAppData<Counter> data = myKinveyClient.appData("Counter", Counter.class);

        Query myQuery = myKinveyClient.query();
        myQuery.equals("AndroidId", Settings.Secure.ANDROID_ID);

        data.get(myQuery, new KinveyListCallback<Counter>() {
                @Override
                public void onSuccess(Counter[] result) {
                        Log.d(TAG, "received" + result.length);
                    }

                    @Override
                public void onFailure(Throwable error) {
                        Log.e(TAG, "error" + error.toString());
                    }
                }

        );
*/

        
    }


}
