/* Copyright 2014 Rishikesh  www.karmicksolutions.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.karmick.android.citizenalert.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.karmick.android.citizenalert.R;
import com.karmick.android.citizenalert.alarm.AlarmAddActivity;
import com.karmick.android.citizenalert.alarm.UserSettingActivity;
import com.karmick.android.citizenalert.alarm.service.AlarmServiceBroadcastReciever;
import com.karmick.android.citizenalert.alarm.sms.SmsDeliveredReceiver;
import com.karmick.android.citizenalert.alarm.sms.SmsSentReceiver;
import com.karmick.android.citizenalert.contact.ContactListActivity;

public abstract class BaseActivity extends ActionBarActivity implements
		android.view.View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
			// Ignore
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.menu_item_new:
			Intent intnt = new Intent(this, AlarmAddActivity.class);
			startActivity(intnt);
			break;
		case R.id.menu_contact:
			Intent intt = new Intent(this, ContactListActivity.class);
			startActivity(intt);
			break;

		case R.id.menu_settings:
			Intent intt2 = new Intent(this, UserSettingActivity.class);
			startActivity(intt2);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	protected void callMathAlarmScheduleService() {
		Intent mathAlarmServiceIntent = new Intent(this,
				AlarmServiceBroadcastReciever.class);
		sendBroadcast(mathAlarmServiceIntent, null);
	}

	public void refreshActivity(Intent inntt) {
		finish();
		overridePendingTransition(0, 0);
		inntt.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(inntt);

	}

	public void fileCopy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	public String getFileExt(String curFileName) {
		String extension;
		int i = curFileName.lastIndexOf('.');
		if (i > 0) {
			extension = curFileName.substring(i + 1);
			return extension;
		}

		return null;
	}

	public static String getMimeType(String extension) {
		String type = null;
		if (extension != null) {
			MimeTypeMap mime = MimeTypeMap.getSingleton();
			type = mime.getMimeTypeFromExtension(extension);
		}
		return type;
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	private int getActionBarHeight() {
		int actionBarHeight = 0;
		TypedValue tv = new TypedValue();
		if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
					getResources().getDisplayMetrics());
		}
		return actionBarHeight;
	}

	public int getDp(int v) {
		final float scale = getApplicationContext().getResources()
				.getDisplayMetrics().density;

		int dp = (int) (v * scale + 0.5f);

		return dp;

	}

	@SuppressLint("NewApi")
	public void showImg(String cFilename, final ImageView contact_img) {
		File fileObj;
		fileObj = new File(Environment.getExternalStorageDirectory()
				+ "/TimeOK/Images/" + cFilename);

		String ext = getFileExt(cFilename);
		String mimeType = getMimeType(ext);

		if (fileObj.exists()) {

			if (mimeType != null) {
				if (mimeType.contains("image")) {
					Bitmap myBitmap = BitmapFactory.decodeFile(fileObj
							.getAbsolutePath());
					contact_img.setImageBitmap(myBitmap);
				} else {
					contact_img.setBackground(getResources().getDrawable(
							R.drawable.file_icon));
				}
			} else {

			}

		}
	}

	public String getAlarmTimeAsString(Calendar alarmTime) {

		String time = "";
		if (alarmTime.get(Calendar.HOUR_OF_DAY) <= 9)
			time += "0";
		time += String.valueOf(alarmTime.get(Calendar.HOUR_OF_DAY));
		time += ":";

		if (alarmTime.get(Calendar.MINUTE) <= 9)
			time += "0";
		time += String.valueOf(alarmTime.get(Calendar.MINUTE));

		return time;
	}

	/*
	 * BroadcastReceiver mBrSend; BroadcastReceiver mBrReceive;
	 */
	public void sendSMS(Context mContext, String phoneNumber, String message) {
		ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
		ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
		PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0,
				new Intent(mContext, SmsSentReceiver.class), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(mContext, 0,
				new Intent(mContext, SmsDeliveredReceiver.class), 0);
		try {
			SmsManager sms = SmsManager.getDefault();
			ArrayList<String> mSMSMessage = sms.divideMessage(message);
			for (int i = 0; i < mSMSMessage.size(); i++) {
				sentPendingIntents.add(i, sentPI);
				deliveredPendingIntents.add(i, deliveredPI);
			}
			sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
					sentPendingIntents, deliveredPendingIntents);

		} catch (Exception e) {

			e.printStackTrace();
			Toast.makeText(getBaseContext(), "SMS sending failed...",
					Toast.LENGTH_SHORT).show();
		}

	}

}
