package com.adrianodigiovanni.dailywifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

public class WifiStateChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		String intentAction = intent.getAction();

		if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intentAction)) {
			if (WifiHelper.isActiveNetworkDailyWifi(context)) {
				Intent serviceIntent = new Intent(context,
						WifiAuthService.class);
				context.startService(serviceIntent);
			}
		}
	}
}
