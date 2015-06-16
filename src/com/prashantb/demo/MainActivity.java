package com.prashantb.demo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prashantb.demo.database.DBAdapter;
import com.prashantb.demo.utils.DetectInternetConnection;
import com.prashantb.demo.utils.WebService;

public class MainActivity extends ActionBarActivity {

	EditText et_name, et_email, et_number;
	Button mButton;
	String AppName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		et_name = (EditText) findViewById(R.id.et_name);
		et_email = (EditText) findViewById(R.id.et_email);
		et_number = (EditText) findViewById(R.id.et_number);
		mButton = (Button) findViewById(R.id.bt_add);
		
		SharedPreferences Sp = getSharedPreferences("Demo", Context.MODE_PRIVATE);		
		String pathName = Sp.getString("add", null);
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
	}

	public void onClick(View v) {
		String name = et_name.getText().toString();
		if (name.replace(" ", "").length() > 0) {
			String email = et_email.getText().toString();
			if (email.replace(" ", "").length() > 0) {
				String number = et_number.getText().toString();
				if (number.replace(" ", "").length() > 0) {
					new bgTask().execute(name, email, number);
					InputMethodManager key = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					key.hideSoftInputFromWindow(mButton.getWindowToken(), 0);
				} else {
					et_number.setError("Enter Number !!!");
				}
			} else {
				et_email.setError("Enter Email id !!!");
			}
		} else {
			et_name.setError("Enter Name !!!");
		}
	}

	private void setActionBar() {
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#68308d")));
		if(AppName.length()>0)
		{
			bar.setTitle(AppName);
		}
	}

	class bgTask extends AsyncTask<String, Void, String> {

		ProgressDialog mProgress = null;

		public void onPreExecute() {
			mProgress = ProgressDialog.show(MainActivity.this,
					"Please wait ...", "Collecting data ...", true);
			mProgress.setCancelable(false);
		}

		@Override
		protected String doInBackground(String... data) {
			String output = null;
			if (new DetectInternetConnection(getApplicationContext())
					.isConnectionToInternet()) {
				output = WebService
						.makeHttpRequest(
								"http://dragonhusbandry.com.numenconsulting.com/HyperionsProjects/SimpleDataCollectionApp/webservice/addUser.php?name="
										+ data[0]
										+ "&email_id="
										+ data[1]
										+ "&phone_no=" + data[2], null, "GET");
			} else {
				DBAdapter mAdapter = new DBAdapter(getApplicationContext());
				mAdapter.open();
					if (mAdapter.insertDATADetail(data[0], data[2], data[1])) {
						output = "ok";
					} else {
						output = null;
					}
				mAdapter.close();
			}
			return output;
		}

		public void onPostExecute(String data) {
			if (data != null) {
				if (data.equals("Record added successfully")) {
					Toast.makeText(getApplicationContext(),
							"Data Stored on server succesfully  !!!",
							Toast.LENGTH_SHORT).show();
				} else if (data.equals("ok")) {
					Toast.makeText(getApplicationContext(),
							"Data Stored in local database !!!",
							Toast.LENGTH_SHORT).show();
				}
				et_name.setText("");
				et_email.setText("");
				et_number.setText("");
			} else {
				Toast.makeText(getApplicationContext(),
						"Try again after some time !!!", Toast.LENGTH_SHORT)
						.show();
			}
			mProgress.dismiss();
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		show_customized_popup("Really Exit ?",
				"Are you sure you want to exit ?", 1);
	}
	
	public void show_customized_popup(String title, String message,
			final int flag) {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.popup_decigion);
		ImageView icon = (ImageView) dialog.findViewById(R.id.iv_decigion_icon);
		icon.setImageResource(R.drawable.ic_dialog_logout);

		TextView tv_header = (TextView) dialog
				.findViewById(R.id.tv_decigion_heading);
		tv_header.setText(title);

		TextView tv_title = (TextView) dialog
				.findViewById(R.id.tv_decigion_title);
		tv_title.setText(message);

		Button bt_yes = (Button) dialog.findViewById(R.id.bt_yes);
		bt_yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Button bt_no = (Button) dialog.findViewById(R.id.bt_no);
		bt_no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		switch (item.getItemId()) {
		case R.id.action_share:
			//if (new DetectInternetConnection(getApplicationContext()).isConnectionToInternet()) {
			//	new bgTask_up(getApplicationContext()).execute();
			//}
			startActivity(new Intent(getApplicationContext(),
					SettingActivity.class));
			overridePendingTransition(R.anim.slide_in_from_right,
					R.anim.slide_out_to_left);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}