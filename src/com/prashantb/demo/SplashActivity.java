package com.prashantb.demo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

import com.prashantb.demo.utils.Service;

public class SplashActivity extends ActionBarActivity {

	String AppName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		SharedPreferences Sp = getSharedPreferences("Demo",
				Context.MODE_PRIVATE);
		String pathName = Sp.getString("splash", null);
		AppName = Sp.getString("name", "");
		try {
			if (pathName != null && pathName.length() > 0) {
				ImageView iv = (ImageView) findViewById(R.id.imageView1);
				iv.setImageBitmap(BitmapFactory
						.decodeFile(pathName));
			}
		} catch (Exception ex) {
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		setActionBar();

		new bgTask().execute();

		SharedPreferences mShared = getSharedPreferences("Demo",
				Context.MODE_PRIVATE);
		boolean flag = mShared.getBoolean("flag", false);
		if (!flag) {

			Editor mEdit = mShared.edit();
			mEdit.putBoolean("flag", true);
			mEdit.commit();

			PendingIntent pendingIntent;
			Intent alarmIntent = new Intent(SplashActivity.this, Service.class);
			pendingIntent = PendingIntent.getBroadcast(SplashActivity.this, 0,
					alarmIntent, 0);
			AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			int interval = 30000;
			manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis(), interval, pendingIntent);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}

	private void setActionBar() {
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#68308d")));
		if(AppName.length()>0)
		{
			bar.setTitle(AppName);
		}
	}

	class bgTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... data) {
			try {
				Thread.sleep(2000);
				return null;
			} catch (Exception ex) {
				return null;
			}
		}

		public void onPostExecute(String data) {
			startActivity(new Intent(getApplicationContext(),
					MainActivity.class));
			overridePendingTransition(R.anim.slide_in_from_right,
					R.anim.slide_out_to_left);
			finish();
		}
	}

}
