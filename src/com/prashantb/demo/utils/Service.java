package com.prashantb.demo.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.prashantb.demo.database.DBAdapter;

public class Service extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (new DetectInternetConnection(context).isConnectionToInternet()) {
			new bgTask(context).execute();
		}
	}

	class bgTask extends AsyncTask<Void, Void, Void> {

		public Context mContext;

		public bgTask(Context mcontext) {
			this.mContext = mcontext;
		}

		@Override
		protected Void doInBackground(Void... data) {
			DBAdapter mAdapter = new DBAdapter(mContext);
			mAdapter.open();
			if ((mAdapter.IsDbNull())) {
				Cursor mCursor = mAdapter.getDATADetail();
				mCursor.moveToFirst();
				do {
					String op = WebService
							.makeHttpRequest(
									"http://dragonhusbandry.com.numenconsulting.com/HyperionsProjects/SimpleDataCollectionApp/webservice/addUser.php?name="
											+ mCursor.getString(1)
											+ "&email_id="
											+ mCursor.getString(3)
											+ "&phone_no="
											+ mCursor.getString(2), null, "GET");
					if (op.equals("Record added successfully")) {
						mAdapter.deleteAll(mCursor.getString(0));
						Log.d("Ojkkkkkkkkkkk", "Deleteeee");
					}
				} while (mCursor.moveToNext());
			}
			mAdapter.close();
			return null;
		}
	}
}
