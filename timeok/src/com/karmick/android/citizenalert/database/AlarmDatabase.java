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
package com.karmick.android.citizenalert.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.karmick.android.citizenalert.alarm.Alarm;
import com.karmick.android.citizenalert.alarm.Alarm.Difficulty;

public class AlarmDatabase extends RemindersDbAdapter {

	Context ctx;

	public AlarmDatabase(Context context) {

		super(context);
		ctx = context;
	}

	public static final String ALARM_TABLE = "alarm";
	public static final String COLUMN_ALARM_ID = "_id";
	public static final String COLUMN_ALARM_ACTIVE = "alarm_active";
	public static final String COLUMN_ALARM_TIME = "alarm_time";
	public static final String COLUMN_ALARM_DAYS = "alarm_days";
	public static final String COLUMN_ALARM_DIFFICULTY = "alarm_difficulty";
	public static final String COLUMN_ALARM_TONE = "alarm_tone";
	public static final String COLUMN_ALARM_VIBRATE = "alarm_vibrate";
	public static final String COLUMN_ALARM_NAME = "alarm_name";
	public static final String COLUMN_ALARM_INTERVAL = "counter";
	public static final String COLUMN_ALARM_COUNTER = "interval";

	public long create(Alarm alarm) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
		cv.put(COLUMN_ALARM_TIME, alarm.getAlarmTimeString());

		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = null;
			oos = new ObjectOutputStream(bos);
			oos.writeObject(alarm.getDays());
			byte[] buff = bos.toByteArray();

			cv.put(COLUMN_ALARM_DAYS, buff);

		} catch (Exception e) {
		}

		cv.put(COLUMN_ALARM_DIFFICULTY, alarm.getDifficulty().ordinal());
		cv.put(COLUMN_ALARM_TONE, alarm.getAlarmTonePath());
		cv.put(COLUMN_ALARM_VIBRATE, alarm.getVibrate());
		cv.put(COLUMN_ALARM_NAME, alarm.getAlarmName());

		cv.put(COLUMN_ALARM_INTERVAL, alarm.getInterval());
		cv.put(COLUMN_ALARM_COUNTER, alarm.getCounter());

		long a_id = mDb.insert(ALARM_TABLE, null, cv);

		if (alarm.getContact_ids() != null && !alarm.getContact_ids().isEmpty()) {
			insertIntoContactMapTable(alarm.getContact_ids(), a_id);
		}

		if (alarm.getFurther_AlarmTimes() != null
				&& !alarm.getFurther_AlarmTimes().isEmpty()) {
			insertIntoTimeMapTable(alarm.getFurther_AlarmTimes(), a_id);
		}

		return a_id;
	}

	public void insertIntoContactMapTable(ArrayList<Integer> contact_id_list,
			long alarm_id) {

		for (Integer cid : contact_id_list) {
			ContentValues cv = new ContentValues();
			cv.put("a_id", alarm_id);
			cv.put("c_id", cid);
			mDb.insert("alarm_contact_map", null, cv);
		}
	}

	public void insertIntoTimeMapTable(ArrayList<String> ftimes, long alarm_id) {
		for (String ftime : ftimes) {
			ContentValues cv = new ContentValues();
			cv.put("a_id", alarm_id);
			cv.put("time", ftime);
			mDb.insert("alarm_time_map", null, cv);
		}
	}

	public double update(Alarm alarm) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
		cv.put(COLUMN_ALARM_TIME, alarm.getAlarmTimeString());

		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = null;
			oos = new ObjectOutputStream(bos);
			oos.writeObject(alarm.getDays());
			byte[] buff = bos.toByteArray();

			cv.put(COLUMN_ALARM_DAYS, buff);

		} catch (Exception e) {
		}

		cv.put(COLUMN_ALARM_DIFFICULTY, alarm.getDifficulty().ordinal());
		cv.put(COLUMN_ALARM_TONE, alarm.getAlarmTonePath());
		cv.put(COLUMN_ALARM_VIBRATE, alarm.getVibrate());
		cv.put(COLUMN_ALARM_NAME, alarm.getAlarmName());

		cv.put(COLUMN_ALARM_INTERVAL, alarm.getInterval());
		cv.put(COLUMN_ALARM_COUNTER, alarm.getCounter());

		long a_id = mDb.update(ALARM_TABLE, cv, "_id=" + alarm.getId(), null);

		// Delete the maped rows first
		deleteReference(a_id);

		if (alarm.getContact_ids() != null && !alarm.getContact_ids().isEmpty()) {
			insertIntoContactMapTable(alarm.getContact_ids(), a_id);
		}

		if (alarm.getFurther_AlarmTimes() != null
				&& !alarm.getFurther_AlarmTimes().isEmpty()) {
			insertIntoTimeMapTable(alarm.getFurther_AlarmTimes(), a_id);
		}

		return a_id;
	}

	public int deleteEntry(Alarm alarm) {
		return deleteEntry(alarm.getId());
	}

	public int deleteEntry(int id) {

		int i = mDb.delete(ALARM_TABLE, COLUMN_ALARM_ID + "=" + id, null);
		deleteReference(id);
		return i;
	}

	public void deleteReference(double id) {
		mDb.delete("alarm_time_map", "a_id=" + id, null);
		mDb.delete("alarm_contact_map", "a_id=" + id, null);
	}

	public int deleteAll() {
		return mDb.delete(ALARM_TABLE, "1", null);
	}

	public Alarm getAlarm(int id) {
		String[] columns = new String[] { COLUMN_ALARM_ID, COLUMN_ALARM_ACTIVE,
				COLUMN_ALARM_TIME, COLUMN_ALARM_DAYS, COLUMN_ALARM_DIFFICULTY,
				COLUMN_ALARM_TONE, COLUMN_ALARM_VIBRATE, COLUMN_ALARM_NAME,
				COLUMN_ALARM_INTERVAL, COLUMN_ALARM_COUNTER };
		Cursor c = mDb.query(ALARM_TABLE, columns, COLUMN_ALARM_ID + "=" + id,
				null, null, null, null);
		Alarm alarm = null;

		if (c.moveToFirst()) {

			alarm = new Alarm();
			alarm.setId(c.getInt(1));
			alarm.setAlarmActive(c.getInt(2) == 1);
			alarm.setAlarmTime(c.getString(3));
			byte[] repeatDaysBytes = c.getBlob(4);

			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					repeatDaysBytes);
			try {
				ObjectInputStream objectInputStream = new ObjectInputStream(
						byteArrayInputStream);
				Alarm.Day[] repeatDays;
				Object object = objectInputStream.readObject();
				if (object instanceof Alarm.Day[]) {
					repeatDays = (Alarm.Day[]) object;
					alarm.setDays(repeatDays);
				}
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			alarm.setDifficulty(Difficulty.values()[c.getInt(5)]);
			alarm.setAlarmTonePath(c.getString(6));
			alarm.setVibrate(c.getInt(7) == 1);
			alarm.setAlarmName(c.getString(8));
		}
		c.close();
		return alarm;
	}

	public Cursor getCursor() {
		String[] columns = new String[] { COLUMN_ALARM_ID, COLUMN_ALARM_ACTIVE,
				COLUMN_ALARM_TIME, COLUMN_ALARM_DAYS, COLUMN_ALARM_DIFFICULTY,
				COLUMN_ALARM_TONE, COLUMN_ALARM_VIBRATE, COLUMN_ALARM_NAME,
				COLUMN_ALARM_INTERVAL, COLUMN_ALARM_COUNTER };
		return mDb.query(ALARM_TABLE, columns, null, null, null, null, null);
	}

	public List<Alarm> getAll() {
		List<Alarm> alarms = new ArrayList<Alarm>();
		Cursor cursor = getCursor();
		if (cursor.moveToFirst()) {

			do {

				Alarm alarm = new Alarm();
				alarm.setId(cursor.getInt(0));
				alarm.setAlarmActive(cursor.getInt(1) == 1);
				alarm.setAlarmTime(cursor.getString(2));
				byte[] repeatDaysBytes = cursor.getBlob(3);

				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
						repeatDaysBytes);
				try {
					ObjectInputStream objectInputStream = new ObjectInputStream(
							byteArrayInputStream);
					Alarm.Day[] repeatDays;
					Object object = objectInputStream.readObject();
					if (object instanceof Alarm.Day[]) {
						repeatDays = (Alarm.Day[]) object;
						alarm.setDays(repeatDays);
					}
				} catch (StreamCorruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				alarm.setDifficulty(Difficulty.values()[cursor.getInt(4)]);
				alarm.setAlarmTonePath(cursor.getString(5));
				alarm.setVibrate(cursor.getInt(6) == 1);
				alarm.setAlarmName(cursor.getString(7));

				alarm.setInterval(cursor.getInt(cursor
						.getColumnIndex("interval")));
				alarm.setCounter(cursor.getInt(cursor.getColumnIndex("counter")));

				alarms.add(alarm);

			} while (cursor.moveToNext());
		}
		cursor.close();
		return alarms;
	}

	public List<Alarm> getAllForAlarm(String key, String val) {

		List<Alarm> alarms = new ArrayList<Alarm>();

		String selectQuery = "";
		if (key != null) {
			if (key == "join") {
				selectQuery = "SELECT a.* ,atm.time FROM  alarm a INNER JOIN alarm_time_map atm ON a._id=atm.a_id";

			}
		} else {
			selectQuery = "SELECT  * FROM " + "alarm";
		}

		Cursor cursor = mDb.rawQuery(selectQuery, null);
		System.out.print(cursor.getCount());

		if (cursor.moveToFirst()) {

			do {
				Alarm alarm = new Alarm();
				alarm.setId(cursor.getInt(cursor.getColumnIndex("_id")));

				alarm.setAlarmActive(cursor.getInt(cursor
						.getColumnIndex("alarm_active")) == 1);
				alarm.setAlarmTime(cursor.getString(cursor
						.getColumnIndex("time")));

				byte[] repeatDaysBytes = cursor.getBlob(cursor
						.getColumnIndex("alarm_days"));

				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
						repeatDaysBytes);
				try {
					ObjectInputStream objectInputStream = new ObjectInputStream(
							byteArrayInputStream);
					Alarm.Day[] repeatDays;
					Object object = objectInputStream.readObject();
					if (object instanceof Alarm.Day[]) {
						repeatDays = (Alarm.Day[]) object;
						alarm.setDays(repeatDays);
					}
				} catch (StreamCorruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				alarm.setDifficulty(Difficulty.values()[cursor.getInt(cursor
						.getColumnIndex("alarm_difficulty"))]);
				alarm.setAlarmTonePath(cursor.getString(cursor
						.getColumnIndex("alarm_tone")));
				alarm.setVibrate(cursor.getInt(cursor
						.getColumnIndex("alarm_vibrate")) == 1);
				alarm.setAlarmName(cursor.getString(cursor
						.getColumnIndex("alarm_name")));

				alarm.setInterval(cursor.getInt(cursor
						.getColumnIndex("interval")));
				alarm.setCounter(cursor.getInt(cursor.getColumnIndex("counter")));

				alarms.add(alarm);

			} while (cursor.moveToNext());
		}
		cursor.close();
		return alarms;

	}
}