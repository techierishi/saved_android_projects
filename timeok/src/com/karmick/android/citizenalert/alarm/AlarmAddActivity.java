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
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.karmick.android.citizenalert.R;
import com.karmick.android.citizenalert.common.BaseActivity;
import com.karmick.android.citizenalert.common.Popups;
import com.karmick.android.citizenalert.common.Popups.CustomDialogCallback;
import com.karmick.android.citizenalert.common.Popups.YesNoClickListener;
import com.karmick.android.citizenalert.contact.ContactListActivity;
import com.karmick.android.citizenalert.database.AlarmDatabase;

public class AlarmAddActivity extends BaseActivity {
	private Alarm alarm;

	EditText alarm_time;
	EditText alarm_interval;
	EditText alarm_counter;
	EditText alarm_message;

	CheckBox alarm_days_mon;
	CheckBox alarm_days_tue;
	CheckBox alarm_days_wed;
	CheckBox alarm_days_thu;
	CheckBox alarm_days_fri;
	CheckBox alarm_days_sat;
	CheckBox alarm_days_sun;

	CheckBox alarm_isactive;
	CheckBox alarm_vibrate;
	LinearLayout alarm_days;
	TextView alarm_contacts_add;
	TextView alarm_contacts;

	Button alarm_cancel;
	Button alarm_add;

	Calendar newAlarmTime = Calendar.getInstance();
	Calendar newAlarmTime2 = Calendar.getInstance();
	Calendar newAlarmTime3 = Calendar.getInstance();

	ArrayList<Integer> contact_ids = new ArrayList<Integer>();

	public static final int GETCONTACTS = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_add);

		actionBar();

		initViews();

		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.containsKey("alarm")) {
			setMathAlarm((Alarm) bundle.getSerializable("alarm"));
		} else {
			setMathAlarm(new Alarm());
		}

		fillAlarmData();

		eventCalls();
	}

	public void fillAlarmData() {

		if (getMathAlarm() != null) {
			if (getMathAlarm().getAlarmTimeString() != null) {
				alarm_time.setText(getMathAlarm().getAlarmTimeString());
			}
			if (getMathAlarm().getInterval() != 0) {
				alarm_interval.setText("" + getMathAlarm().getInterval());
			}

			if (getMathAlarm().getCounter() != 0) {
				alarm_counter.setText("" + getMathAlarm().getCounter());
			}

			if (getMathAlarm().getAlarmName() != null) {
				alarm_message.setText("" + getMathAlarm().getAlarmName());
			}

			if (getMathAlarm().getAlarmActive()) {
				alarm_isactive.setChecked(true);
			}

			for (Alarm.Day d : getMathAlarm().getDays()) {
				if (d != null) {

					if (d == Alarm.Day.MONDAY) {
						alarm_days_mon.setChecked(true);
					}
					if (d == Alarm.Day.TUESDAY) {
						alarm_days_tue.setChecked(true);
					}

					if (d == Alarm.Day.WEDNESDAY) {
						alarm_days_wed.setChecked(true);
					}

					if (d == Alarm.Day.THURSDAY) {
						alarm_days_thu.setChecked(true);
					}

					if (d == Alarm.Day.FRIDAY) {
						alarm_days_fri.setChecked(true);
					}

					if (d == Alarm.Day.SATURDAY) {
						alarm_days_sat.setChecked(true);
					}
					if (d == Alarm.Day.SUNDAY) {
						alarm_days_sun.setChecked(true);
					}
				}
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.findItem(R.id.menu_item_save).setVisible(false);
		menu.findItem(R.id.menu_item_delete).setVisible(true);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_item_delete:
			Popups.showYesNoPopup("Delete this alarm?", AlarmAddActivity.this,
					new YesNoClickListener() {

						@Override
						public void onYesButtonClick() {
							AlarmDatabase maDb = new AlarmDatabase(
									AlarmAddActivity.this);

							maDb.createDatabase();
							maDb.open();
							if (getMathAlarm().getId() < 1) {
								// Alarm not saved
							} else {
								maDb.deleteEntry(alarm);
								callMathAlarmScheduleService();
							}
							maDb.close();
							finish();

						}

						@Override
						public void onNoButtonClick() {

						}
					});

			break;
		}

		return super.onOptionsItemSelected(item);
	}

	protected void eventCalls() {
		alarm_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				validateAndSaveAlarm();
			}
		});

		alarm_contacts_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent nint = new Intent(AlarmAddActivity.this,
						ContactListActivity.class);
				nint.putExtra("for_result", true);
				startActivityForResult(nint, GETCONTACTS);

			}
		});

		alarm_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TimePickerDialog timePickerDialog = new TimePickerDialog(
						AlarmAddActivity.this,
						new OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker timePicker,
									int hours, int minutes) {
								newAlarmTime.set(Calendar.HOUR_OF_DAY, hours);
								newAlarmTime.set(Calendar.MINUTE, minutes);
								newAlarmTime.set(Calendar.SECOND, 0);

								newAlarmTime2.set(Calendar.HOUR_OF_DAY, hours);
								newAlarmTime2.set(Calendar.MINUTE, minutes);
								newAlarmTime2.set(Calendar.SECOND, 0);

								newAlarmTime3.set(Calendar.HOUR_OF_DAY, hours);
								newAlarmTime3.set(Calendar.MINUTE, minutes);
								newAlarmTime3.set(Calendar.SECOND, 0);

								alarm_time
										.setText(getAlarmTimeAsString(newAlarmTime));

							}
						}, alarm.getAlarmTime().get(Calendar.HOUR_OF_DAY),
						alarm.getAlarmTime().get(Calendar.MINUTE), true);
				timePickerDialog.setTitle("SetTime");
				timePickerDialog.show();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String contacts;
		if (requestCode == GETCONTACTS) {
			if (resultCode == RESULT_OK) {
				Bundle extra = data.getExtras();
				if (extra != null) {
					try {

						String names = "";
						contacts = extra.getString("contacts");

						Log.d(" Contacts >>", "" + contacts);
						if (contacts != null && !contacts.isEmpty()) {
							JSONArray jArr = new JSONArray(contacts);

							for (int i = 0; i < jArr.length(); i++) {
								JSONObject jObj = jArr.getJSONObject(i);
								contact_ids.add(Integer.parseInt(jObj
										.getString("id")));
								names += jObj.getString("name") + " \n";

							}

							alarm_contacts.setText(names);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	protected void actionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	public Alarm getMathAlarm() {
		return alarm;
	}

	public void setMathAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

	protected void initViews() {
		alarm_time = (EditText) findViewById(R.id.alarm_time);
		alarm_interval = (EditText) findViewById(R.id.alarm_interval);
		alarm_counter = (EditText) findViewById(R.id.alarm_counter);
		alarm_message = (EditText) findViewById(R.id.alarm_message);

		alarm_days_mon = (CheckBox) findViewById(R.id.alarm_days_mon);
		alarm_days_tue = (CheckBox) findViewById(R.id.alarm_days_tue);
		alarm_days_wed = (CheckBox) findViewById(R.id.alarm_days_wed);
		alarm_days_thu = (CheckBox) findViewById(R.id.alarm_days_thu);
		alarm_days_fri = (CheckBox) findViewById(R.id.alarm_days_fri);
		alarm_days_sat = (CheckBox) findViewById(R.id.alarm_days_sat);
		alarm_days_sun = (CheckBox) findViewById(R.id.alarm_days_sun);

		alarm_isactive = (CheckBox) findViewById(R.id.alarm_isactive);
		alarm_vibrate = (CheckBox) findViewById(R.id.alarm_vibrate);
		alarm_days = (LinearLayout) findViewById(R.id.alarm_days);
		alarm_contacts_add = (TextView) findViewById(R.id.alarm_contacts_add);
		alarm_contacts = (TextView) findViewById(R.id.alarm_contacts);
		alarm_cancel = (Button) findViewById(R.id.alarm_cancel);
		alarm_add = (Button) findViewById(R.id.alarm_add);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View arg0) {

	}

	protected void validateAndSaveAlarm() {

		// All alarm data is created here
		ArrayList<String> errs = setAlarmData();

		if (errs != null && !errs.isEmpty()) {

			String errs_str = "\n";

			for (String s : errs) {
				errs_str += "" + s + "\n";
			}

			Popups.showOkPopup(AlarmAddActivity.this, errs_str,
					new CustomDialogCallback() {
						@Override
						public void onOkClick() {

						}
					});

		} else {

			AlarmDatabase maDb = new AlarmDatabase(this);
			maDb.createDatabase();
			maDb.open();
			if (getMathAlarm().getId() < 1) {

				maDb.create(getMathAlarm());
			} else {
				maDb.update(getMathAlarm());
			}
			maDb.close();
			callMathAlarmScheduleService();
			Toast.makeText(AlarmAddActivity.this,
					getMathAlarm().getTimeUntilNextAlarmMessage(),
					Toast.LENGTH_LONG).show();
			finish();
		}

	}

	protected ArrayList<String> setAlarmData() {

		ArrayList<String> errs = new ArrayList<String>();

		boolean day_checked = false;
		boolean chkd;
		chkd = alarm_isactive.isChecked();
		alarm.setAlarmActive(chkd);

		alarm.setAlarmName(alarm_message.getText().toString());
		// hard code for now [start]
		Alarm.Difficulty d = Alarm.Difficulty.values()[0];
		alarm.setDifficulty(d);
		alarm.setAlarmTonePath("content://settings/system/alarm_alert");
		alarm.setVibrate(false);
		// hard code for now [ends]

		if (alarm_days_mon.isChecked()) {
			day_checked = true;
			alarm.addDay(Alarm.Day.MONDAY);

		} else {

			if (alarm.getDays().length > 1) {
				alarm.removeDay(Alarm.Day.MONDAY);
			}
		}
		if (alarm_days_tue.isChecked()) {
			day_checked = true;
			alarm.addDay(Alarm.Day.TUESDAY);
		} else {

			if (alarm.getDays().length > 1) {
				alarm.removeDay(Alarm.Day.TUESDAY);
			}
		}
		if (alarm_days_wed.isChecked()) {
			day_checked = true;
			alarm.addDay(Alarm.Day.WEDNESDAY);
		} else {

			if (alarm.getDays().length > 1) {
				alarm.removeDay(Alarm.Day.WEDNESDAY);
			}
		}
		if (alarm_days_thu.isChecked()) {
			day_checked = true;
			alarm.addDay(Alarm.Day.THURSDAY);
		} else {

			if (alarm.getDays().length > 1) {
				alarm.removeDay(Alarm.Day.THURSDAY);
			}
		}
		if (alarm_days_fri.isChecked()) {
			day_checked = true;
			alarm.addDay(Alarm.Day.FRIDAY);
		} else {

			if (alarm.getDays().length > 1) {
				alarm.removeDay(Alarm.Day.FRIDAY);
			}
		}
		if (alarm_days_sat.isChecked()) {
			day_checked = true;
			alarm.addDay(Alarm.Day.SATURDAY);
		} else {

			if (alarm.getDays().length > 1) {
				alarm.removeDay(Alarm.Day.SATURDAY);
			}
		}
		if (alarm_days_sun.isChecked()) {
			day_checked = true;
			alarm.addDay(Alarm.Day.SUNDAY);
		} else {

			if (alarm.getDays().length > 1) {
				alarm.removeDay(Alarm.Day.SUNDAY);
			}
		}

		if (contact_ids != null) {
			if (!contact_ids.isEmpty()) {
				alarm.setContact_ids(contact_ids);
			} else {
				errs.add("Please select atleast one contact.");
			}
		}

		if (!day_checked) {
			errs.add("Please select atleast one day.");
		}

		if (alarm_time.getText().toString().isEmpty()) {
			errs.add("Please enter alarm time");
		}

		int interval = 0;

		if (alarm_interval.getText().toString().isEmpty()) {

			errs.add("Please enter interval.");
		} else {
			interval = Integer.parseInt(alarm_interval.getText().toString());
			if (interval <= 0) {
				errs.add("Please enter interval greater than zero.");
			}
		}
		int counter = 0;

		if (alarm_counter.getText().toString().isEmpty()) {
			errs.add("Please enter counter.");
		} else {
			counter = Integer.parseInt(alarm_counter.getText().toString());
			if (counter <= 0) {
				errs.add("Please enter counter greater than zero.");
			}
		}

		// Checking if the calculated time is entring into next day or not
		int mu = interval * counter;
		Calendar _clndr0 = newAlarmTime;
		Calendar _clndr1 = newAlarmTime3;
		_clndr1.add(Calendar.MINUTE, mu);

		int day1 = _clndr1.get(Calendar.DAY_OF_WEEK);
		int day2 = _clndr0.get(Calendar.DAY_OF_WEEK);
		if (day1 != day2) {
			errs.add("Calulated time is entering into nex day.");
		}

		alarm.setAlarmTime(newAlarmTime);

		ArrayList<String> furtherAlarmTimes = new ArrayList<String>();
		Calendar clndr = newAlarmTime2;
		if (counter >= 1) {
			for (int c = 0; c < counter; c++) {
				furtherAlarmTimes.add(getAlarmTimeAsString(clndr));
				clndr.add(Calendar.SECOND, (interval * 60));
			}
		} else {
			furtherAlarmTimes.add(getAlarmTimeAsString(clndr));
		}

		alarm.setFurther_AlarmTimes(furtherAlarmTimes);
		alarm.setCounter(counter);
		alarm.setInterval(interval);

		return errs;
	}
}