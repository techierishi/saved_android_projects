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
package com.karmick.android.citizenalert.alarm;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.karmick.android.citizenalert.R;
import com.karmick.android.citizenalert.database.AlarmDatabase;

public class AlarmListAdapter extends BaseAdapter {

	private AlarmActivity alarmActivity;
	private List<Alarm> alarms = new ArrayList<Alarm>();

	public static final String ALARM_FIELDS[] = {
			AlarmDatabase.COLUMN_ALARM_ACTIVE, AlarmDatabase.COLUMN_ALARM_TIME,
			AlarmDatabase.COLUMN_ALARM_DAYS };

	public AlarmListAdapter(AlarmActivity alarmActivity) {
		this.alarmActivity = alarmActivity;

	}

	@Override
	public int getCount() {
		return alarms.size();
	}

	@Override
	public Object getItem(int position) {
		return alarms.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		if (null == view)
			view = LayoutInflater.from(alarmActivity).inflate(
					R.layout.alarm_list_element, null);

		Alarm alarm = (Alarm) getItem(position);

		CheckBox checkBox = (CheckBox) view
				.findViewById(R.id.checkBox_alarm_active);
		TextView alarmTimeView = (TextView) view
				.findViewById(R.id.textView_alarm_time);
		TextView alarmDaysView = (TextView) view
				.findViewById(R.id.textView_alarm_days);
		Button buttonInterval = (Button) view.findViewById(R.id.buttonInterval);
		Button buttonCounter = (Button) view.findViewById(R.id.buttonCounter);

		checkBox.setChecked(alarm.getAlarmActive());
		checkBox.setTag(position);
		checkBox.setOnClickListener(alarmActivity);

		alarmTimeView.setText(alarm.getAlarmTimeString());
		alarmDaysView.setText(alarm.getRepeatDaysString());

		buttonInterval.setText(String.valueOf(alarm.getInterval()));
		buttonCounter.setText(String.valueOf(alarm.getCounter()));

		view.setTag(position);
		
		return view;
	}

	public List<Alarm> getMathAlarms() {
		return alarms;
	}

	public void setMathAlarms(List<Alarm> alarms) {
		this.alarms = alarms;
	}

}
