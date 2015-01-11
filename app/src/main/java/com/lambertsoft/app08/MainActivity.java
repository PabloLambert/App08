package com.lambertsoft.app08;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    AlarmManager alarmManager;
    PendingIntent pendingSvc;
    EditText textInterval;
    TextView textAndroidId;
    EditText textThreshold;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        Button buttonStart = (Button) findViewById(R.id.buttonStart);
        Button buttonStop = (Button)findViewById(R.id.buttonStop);
        textInterval = (EditText) findViewById(R.id.textInterval);
        textAndroidId = (TextView) findViewById(R.id.textAndriodId);
        textThreshold = (EditText) findViewById(R.id.textThreshold);



        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        textAndroidId.setText(android_id);


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick - starting alarm");
                long interval = Long.parseLong(textInterval.getText().toString());
                if (interval <= 0 ) interval = 20;

                Intent intent = new Intent();
                Context myContext = getApplicationContext();
                intent.setClass(myContext, BackgroundService.class);
                int threshold = Integer.parseInt(textThreshold.getText().toString());
                Log.d(TAG, "threshold: " + threshold);
                intent.putExtra("threshold", threshold);
                pendingSvc = PendingIntent.getService(myContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + interval * 1000 ,interval * 1000, pendingSvc );
            }
        });

        buttonStop.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick - stoping alarm");
                alarmManager.cancel(pendingSvc);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
