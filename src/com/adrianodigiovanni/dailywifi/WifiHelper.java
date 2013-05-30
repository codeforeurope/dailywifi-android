package com.adrianodigiovanni.dailywifi;

import android.content.Context;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiHelper {

	private WifiHelper() {
	}

	public static boolean isActiveNetworkDailyWifi(Context context) {
		boolean result = false;
		String serviceName = Context.WIFI_SERVICE;
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(serviceName);
		if (wifiManager.isWifiEnabled()) {
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			String ssid = wifiInfo.getSSID();
			if (null != ssid) {
				final String[] projection = { AccountsTable.COLUMN_SSID };
				final String selection = AccountsTable.COLUMN_SSID + "=?";
				final String[] selectionArgs = { ssid };

				Cursor cursor = context.getContentResolver().query(
						AccountsProvider.CONTENT_URI, projection, selection,
						selectionArgs, null);

				if (null != cursor) {
					if (0 != cursor.getCount()) {
						result = true;
					}
					cursor.close();
				}
			}
		}
		return result;
	}
}
