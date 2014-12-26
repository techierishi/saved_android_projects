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
package com.karmick.android.citizenalert.alarm.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.karmick.android.citizenalert.alarm.Alarm;
import com.karmick.android.citizenalert.alarm.service.AlarmServiceBroadcastReciever;
import com.karmick.android.citizenalert.alarm.service.GPSLoggerService;

public class AlarmAlertBroadcastReciever extends BroadcastReceiver {

	private Context ctx;

	@Override
	public void onReceive(Context context, Intent intent) {
		ctx = context;
		Intent mathAlarmServiceIntent = new Intent(context,
				AlarmServiceBroadcastReciever.class);
		context.sendBroadcast(mathAlarmServiceIntent, null);

		StaticWakeLock.lockOn(context);
		Bundle bundle = intent.getExtras();
		final Alarm alarm = (Alarm) bundle.getSerializable("alarm");

		// Newly Added
		Log.d("Alarm Detail >> ", " Id " + alarm.getId() + " Contact ids : "
				+ alarm.getContact_ids());

		Log.v("Tag", "AlarmReceiver called. ");
		Intent service = new Intent(context, GPSLoggerService.class);
		service.putExtra("alarm_id", alarm.getId());
		context.startService(service);

		// Newly Added ends

		// Show Time
		// Intent mathAlarmAlertActivityIntent;
		//
		// mathAlarmAlertActivityIntent = new Intent(context,
		// AlarmAlertActivity.class);
		//
		// mathAlarmAlertActivityIntent.putExtra("alarm", alarm);
		//
		// mathAlarmAlertActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//
		// context.startActivity(mathAlarmAlertActivityIntent);
	}

}
