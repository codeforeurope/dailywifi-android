package com.adrianodigiovanni.dailywifi;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class WifiAuthService extends Service {

	public static final String TAG = "WifiAuthService";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (WifiHelper.isActiveNetworkDailyWifi(this)) {
			// TODO: Consume API
			Log.d(TAG, intent.toString());
		}
		stopSelf();
		
		return START_STICKY;
	}
}
