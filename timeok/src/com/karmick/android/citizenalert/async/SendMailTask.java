package com.karmick.android.citizenalert.async;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.karmick.android.citizenalert.alarm.Alarm;
import com.karmick.android.citizenalert.constant.Consts;
import com.karmick.android.citizenalert.database.RemindersDbAdapter;
import com.karmick.android.citizenalert.models.Contact;

public class SendMailTask extends AsyncTask<String, String, String> {
	JSONParser jsonParser = new JSONParser();
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	Context ctx;
	String jsonData;
	ArrayList<Contact> items;
	RemindersDbAdapter mDbHelper;
	String message;
	int alarm_id;

	String email_id_from_sms;

	String latlong;

	public SendMailTask(Context _ctx, String _message, String _latlong,
			int _alarm_id) {
		ctx = _ctx;
		message = _message;
		alarm_id = _alarm_id;
		latlong = _latlong;
	}

	public SendMailTask(Context _ctx, String _message, String _latlong,
			String _email_id) {
		ctx = _ctx;
		message = _message;
		email_id_from_sms = _email_id;
		latlong = _latlong;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected String doInBackground(String... args) {

		int success;

		try {

			if (Consts.iNa(ctx)) {

				jsonData = getJsonDataForMail();

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("emaildata", jsonData));

				Log.d("request!", "starting");

				// Posting user data to script
				JSONObject json = jsonParser.makeHttpRequest(Consts.EMAIL_URL,
						"POST", params);

				// full json response
				Log.d("Login attempt", json.toString());

				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("User Created!", json.toString());

					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Login Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);

				}
			} else {
				return "Internet Not Available";
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;

	}

	private String getJsonDataForMail() {

		JSONObject jObj = new JSONObject();
		JSONArray jArr = new JSONArray();

		mDbHelper = new RemindersDbAdapter(ctx);
		mDbHelper.createDatabase();
		mDbHelper.open();
		if (alarm_id != 0) {
			items = mDbHelper.getAllContacts("a_id", "" + alarm_id);
		} else {
			items = null;
		}
		mDbHelper.close();

		if (items != null && !items.isEmpty()) {

			for (Contact contact : items) {

				Log.d(" Contact : ", "" + contact.toString());
				Log.d(" Email Id : ", "" + contact.getEmail());
				jArr.put(contact.getEmail());
			}

		} else {
			if (email_id_from_sms != null) {
				jArr.put(email_id_from_sms);
			} else {
				// Something really went wrong
				jArr.put("karmicksol206@gmail.com");
			}
		}
		try {
			jObj.put("emails", jArr);
			jObj.put("body", message);
			jObj.put("urllink", "https://www.google.com/maps/place/" + latlong);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("Final JSON >> ", "" + jObj.toString());

		return jObj.toString();
	}

	protected void onPostExecute(String file_url) {
		OnEmailSendUrlCalledListener listner = (OnEmailSendUrlCalledListener) ctx;
		listner.onEmailSendUrlCalled(file_url);
	}

	public interface OnEmailSendUrlCalledListener {
		public void onEmailSendUrlCalled(String str);
	}
}
