package com.prashantb.demo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class DetectInternetConnection {

	private Context mContext;

	public DetectInternetConnection(Context c) {
		this.mContext = c;
	}

	public boolean isConnectionToInternet() {

		boolean status = false;
		ConnectivityManager connect = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connect != null) {
			NetworkInfo[] info = connect.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
						status = true;
				}
			}
		}
		return status;
	}
}
