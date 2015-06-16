package com.prashantb.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.prashantb.demo.database.DBAdapter;
import com.prashantb.demo.utils.CsvFileWriter;
import com.prashantb.demo.utils.Data;
import com.prashantb.demo.utils.DetectInternetConnection;
import com.prashantb.demo.utils.SelectMode;
import com.prashantb.demo.utils.WebService;

public class SettingActivity extends ActionBarActivity {

	EditText et_name, et_splash, et_add;
	String AppName;
	String path_export; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		et_add = (EditText) findViewById(R.id.et_add);
		et_add.setKeyListener(null);
		et_splash = (EditText) findViewById(R.id.et_splash);
		et_splash.setKeyListener(null);
		et_name = (EditText) findViewById(R.id.et_name);

		SharedPreferences mSp = getSharedPreferences("Demo",
				Context.MODE_PRIVATE);
		String splash = mSp.getString("splash", "");
		String add = mSp.getString("add", "");
		String name = mSp.getString("name", "");
		AppName = name;

		et_add.setText(add);
		et_splash.setText(splash);
		et_name.setText(name);
	}

	@Override
	public void onStart() {
		super.onStart();
		setActionBar();

	}

	@Override
	public void onBackPressed() {

		Store_in_sp();

		startActivity(new Intent(getApplicationContext(), MainActivity.class));
		overridePendingTransition(R.anim.slide_in_from_left,
				R.anim.slide_out_to_right);
		finish();
	}

	private void Store_in_sp() {
		SharedPreferences msp = getSharedPreferences("Demo",
				Context.MODE_PRIVATE);
		Editor mEdit = msp.edit();
		mEdit.putString("splash", et_splash.getText().toString());
		mEdit.putString("add", et_add.getText().toString());
		mEdit.putString("name", et_name.getText().toString());
		mEdit.commit();

	}

	private void setActionBar() {
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#68308d")));
		if (AppName.length() > 0) {
			bar.setTitle(AppName);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_export:
			// new bgTask(getApplicationContext(), "Exporting Data ...")
			// .execute(1);

			Intent i = new Intent(SettingActivity.this, SelectActivity.class);
			i.putExtra(SelectActivity.EX_PATH, Environment
					.getExternalStorageDirectory().getAbsolutePath());
			i.putExtra(SelectActivity.EX_STYLE, SelectMode.SELECT_FOLDER);
			startActivityForResult(i, 123);

			break;

		case R.id.bt_update:
			if (new DetectInternetConnection(getApplicationContext())
					.isConnectionToInternet()) {
				new bgTask(getApplicationContext(), "Uploading Data ...")
						.execute(0);
			} else {
				Toast.makeText(getApplicationContext(),
						"Internet is not connected !!!", Toast.LENGTH_SHORT)
						.show();
			}
			break;

		case R.id.buttonAdd:
			Intent galleryIntent_1 = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			// Start the Intent
			startActivityForResult(galleryIntent_1, 300);
			break;

		case R.id.buttonSplash:
			Intent galleryIntent_2 = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			// Start the Intent
			startActivityForResult(galleryIntent_2, 400);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == 400 || requestCode == 300) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				// Get the cursor
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				// Move to first row
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String imgDecodableString = cursor.getString(columnIndex);
				cursor.close();
				// Set the Image in ImageView after decoding the String
				if (requestCode == 400) {
					et_splash.setText(imgDecodableString);
				} else if (requestCode == 300) {
					et_add.setText(imgDecodableString);
				}
				Store_in_sp();
			}
			else if(requestCode == 123)
			{
				path_export = data.getStringExtra(SelectActivity.EX_PATH_RESULT);
				new bgTask(getApplicationContext(), "Exporting Data ...")
				 .execute(1);
				//Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();
			}
		} catch (Exception ex) {

		}
	}

	class bgTask extends AsyncTask<Integer, Void, String> {

		public Context mContext;
		public ProgressDialog mProgress = null;
		public String message;
		public String notification = "";

		public bgTask(Context mcontext, String message) {
			this.mContext = mcontext;
			this.message = message;
		}

		public void onPreExecute() {
			mProgress = ProgressDialog.show(SettingActivity.this,
					"Please wait ...", message, true);
			mProgress.setCancelable(true);
		}

		@Override
		protected String doInBackground(Integer... data) {
			DBAdapter mAdapter = new DBAdapter(mContext);
			List<Data> datas = null;
			mAdapter.open();
			if (data[0] == 1) {
				try {
					datas = new ArrayList<Data>();
					SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
					String format = s.format(new Date());
					path_export +="/Demo_" + format + ".csv";
					notification = "Data Store on "
							+ path_export
							+ " Succesfully !!!";
				} catch (Exception ex) {

				}
			}

			if ((mAdapter.IsDbNull())) {
				Cursor mCursor = mAdapter.getDATADetail();
				mCursor.moveToFirst();
				do {
					if (data[0] == 0) {
						String op = WebService
								.makeHttpRequest(
										"http://dragonhusbandry.com.numenconsulting.com/HyperionsProjects/SimpleDataCollectionApp/webservice/addUser.php?name="
												+ mCursor.getString(1)
												+ "&email_id="
												+ mCursor.getString(3)
												+ "&phone_no="
												+ mCursor.getString(2), null,
										"GET");
						if (op.equals("Record added successfully")) {
							mAdapter.deleteAll(mCursor.getString(0));
							Log.d("Ojkkkkkkkkkkk", "Deleteeee");
							notification = "Data Upload on Server Succesfully !!!";
						}
					} else if (data[0] == 1) {
						try {
							if(datas!=null)
							{
								Data data1 = new Data(mCursor.getString(1), mCursor.getString(2), mCursor.getString(2));
								datas.add(data1);
							}
						} catch (Exception ex) {
						}
					}

				} while (mCursor.moveToNext());
				if (data[0] == 1) {
					CsvFileWriter.writeCsvFile(path_export , datas);
				}
			}
			mAdapter.close();
			return null;
		}

		@Override
		public void onPostExecute(String data) {
			mProgress.dismiss();
			if (notification.length() > 0)
				Toast.makeText(mContext, notification, Toast.LENGTH_LONG)
						.show();
		}

	}
}
