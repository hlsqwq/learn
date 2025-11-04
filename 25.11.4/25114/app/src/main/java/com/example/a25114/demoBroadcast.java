package com.example.a25114;

import static android.app.AlarmManager.RTC_WAKEUP;
import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Parcelable;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.util.Log;
import android.util.Rational;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class demoBroadcast extends BroadcastReceiver {

    public static final String ALARM_ACTION = "com.hls.alarm";

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(MainActivity.ACTION)) {
            Toast.makeText(context.getApplicationContext(), "receive broadcast success", Toast.LENGTH_SHORT)
                    .show();
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
            return;
        }
        if (action.equals(ALARM_ACTION)) {
            Log.i("hls", "receive alarm message");
            send(context);
            return;
        }
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra("reason");
            if (reason == null || reason.isEmpty() || (!reason.equals("homekey") && !reason.equals("recentapps"))) {
                return;
            }
            PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder();
            Rational rational = new Rational(5, 10);
            builder.setAspectRatio(rational);

        }
        NetworkInfo networkInfo = intent.getParcelableExtra("networkInfo");
        Log.i("hls", networkInfo.getTypeName());
        Log.i("hls", networkInfo.getSubtypeName());
        Log.i("hls", networkInfo.getState().toString());
    }


    public void send(Context context) {
        Intent intent = new Intent(ALARM_ACTION);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, FLAG_IMMUTABLE);
        AlarmManager systemService = context.getSystemService(AlarmManager.class);
        systemService.setAndAllowWhileIdle(RTC_WAKEUP, 1000, broadcast);
    }
}
