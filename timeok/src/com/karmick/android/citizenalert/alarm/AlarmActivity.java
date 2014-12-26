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

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.karmick.android.citizenalert.R;
import com.karmick.android.citizenalert.common.BaseActivity;
import com.karmick.android.citizenalert.common.Popups;
import com.karmick.android.citizenalert.common.Popups.YesNoClickListener;
import com.karmick.android.citizenalert.common.SwipeDismissListViewTouchListener;
import com.karmick.android.citizenalert.database.AlarmDatabase;

public class AlarmActivity extends BaseActivity {

	ImageButton newButton;
	ListView mathAlarmListView;
	AlarmListAdapter alarmListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alarm_activity);

		mathAlarmListView = (ListView) findViewById(android.R.id.list);
		mathAlarmListView.setLongClickable(true);

		// Swipe functionality starts here
		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
				mathAlarmListView,
				new SwipeDismissListViewTouchListener.DismissCallbacks() {
					@Override
					public boolean canDismiss(int position) {
						return true;
					}

					@Override
					public void onDismissLeft(ListView listView,
							int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							View v = listView.getChildAt(position);
							final int pos = (Integer) v.getTag();

							final Alarm alarm = (Alarm) alarmListAdapter
									.getItem(pos);
							// Deleting part
							Popups.showYesNoPopup(
									"Do you really want to delete this ?",
									AlarmActivity.this,
									new YesNoClickListener() {

										@Override
										public void onYesButtonClick() {
											AlarmDatabase maDb = new AlarmDatabase(
													AlarmActivity.this);

											maDb.createDatabase();
											maDb.open();
											maDb.deleteEntry(alarm);
											maDb.close();
											AlarmActivity.this
													.callMathAlarmScheduleService();

											updateAlarmList();

										}

										@Override
										public void onNoButtonClick() {

										}
									});
						}

					}

					@Override
					public void onDismissRight(ListView listView,
							int[] reverseSortedPositions) {

					}
				});
		mathAlarmListView.setOnTouchListener(touchListener);
		//Swipe functionality ends
		
		callMathAlarmScheduleService();

		alarmListAdapter = new AlarmListAdapter(this);
		this.mathAlarmListView.setAdapter(alarmListAdapter);
		mathAlarmListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
				Intent intent = new Intent(AlarmActivity.this,
						AlarmAddActivity.class);
				intent.putExtra("alarm", alarm);
				startActivity(intent);
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.findItem(R.id.menu_item_save).setVisible(false);
		menu.findItem(R.id.menu_item_delete).setVisible(false);
		return result;
	}

	@Override
	protected void onPause() {
		// setListAdapter(null);

		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateAlarmList();
	}

	public void updateAlarmList() {
		AlarmDatabase maDb = new AlarmDatabase(AlarmActivity.this);

		maDb.createDatabase();
		maDb.open();
		final List<Alarm> alarms = maDb.getAll();
		maDb.close();
		alarmListAdapter.setMathAlarms(alarms);

		runOnUiThread(new Runnable() {
			public void run() {
				// reload content
				AlarmActivity.this.alarmListAdapter.notifyDataSetChanged();
				if (alarms.size() > 0) {
					findViewById(android.R.id.empty).setVisibility(
							View.INVISIBLE);
				} else {
					findViewById(android.R.id.empty)
							.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		AlarmDatabase maDb = new AlarmDatabase(AlarmActivity.this);

		maDb.createDatabase();
		maDb.open();
		if (v.getId() == R.id.checkBox_alarm_active) {
			CheckBox checkBox = (CheckBox) v;
			Alarm alarm = (Alarm) alarmListAdapter.getItem((Integer) checkBox
					.getTag());
			alarm.setAlarmActive(checkBox.isChecked());
			maDb.update(alarm);
			AlarmActivity.this.callMathAlarmScheduleService();
			if (checkBox.isChecked()) {
				Toast.makeText(AlarmActivity.this,
						alarm.getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG)
						.show();
			}
		}
		maDb.close();
	}

}