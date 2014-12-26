package com.karmick.android.citizenalert.alarm.sms;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.karmick.android.citizenalert.alarm.service.GPSLoggerService;

public class SMSReceiver extends BroadcastReceiver {

	/** Called when the activity is first created. */
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	public static int MSG_TPE = 0;

	public void onReceive(Context context, Intent intent) {

		String MSG_TYPE = intent.getAction();
		if (MSG_TYPE.equals(ACTION)) {

			Bundle myBundle = intent.getExtras();
			SmsMessage[] messages = null;
			String strMessage = "";
			String from = "";
			String email;
			String key = "@locateme";

			if (myBundle != null) {
				Object[] pdus = (Object[]) myBundle.get("pdus");
				messages = new SmsMessage[pdus.length];

				for (int i = 0; i < messages.length; i++) {
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
					from += messages[i].getOriginatingAddress();
					strMessage += messages[i].getMessageBody();

					if (strMessage.toLowerCase().contains(key.toLowerCase())) {
						abortBroadcast();
						try {

							boolean vj = isJSONValid(strMessage);
							if (vj) {
								JSONObject jObj = new JSONObject(strMessage);

								email = jObj.getString("email_id");
								Intent service = new Intent(context,
										GPSLoggerService.class);
								service.putExtra("alarm_id", 0);
								if (email != null) {
									service.putExtra("email_id", email);
								} else {
									service.putExtra("email_id", "");
								}
								context.startService(service);

							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}

			}
		}

	}

	public boolean isJSONValid(String test) {
		try {
			new JSONObject(test);
		} catch (JSONException ex) {

			try {
				new JSONArray(test);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}
}
