package com.MeadowEast.UpdateService;

import java.io.IOException;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;


public class Alarm extends BroadcastReceiver 
{    
	private static final String TAG = "Alarm BroadcastReceiver";
	
     @Override
     public void onReceive(Context context, Intent intent) 
     {   
         PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
         wl.acquire();
         Log.d(TAG, "made it to ALARM");
         // Put here YOUR code.
         //Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
        
         try 
	        {
				new CheckUpdate().execute();
			} 
         catch (IOException e) 
	        {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         
         wl.release();
     }

 public void SetAlarm(Context context)
 {
     AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
     Intent i = new Intent(context, Alarm.class);
     PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
     am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 60 * 24, pi); // Millisec * Second * Minute * Hours
 }

 public void CancelAlarm(Context context)
 {
     Intent intent = new Intent(context, Alarm.class);
     PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
     AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
     alarmManager.cancel(sender);
 }
}