package com.karmick.android.citizenalert.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.karmick.android.citizenalert.models.Contact;

public class RemindersDbAdapter {

	//
	// Databsae Related Constants
	//
	private static final String DATABASE_NAME = "remindersdb.sqlite";
	private static final String DATABASE_TABLE = "reminders";
	private static final int DATABASE_VERSION = 3;

	public static final String KEY_TITLE = "title";
	public static final String KEY_BODY = "body";
	public static final String KEY_DATE_TIME = "reminder_date_time";
	public static final String KEY_ROWID = "_id";

	private static final String TAG = "ReminderDbAdapter";

	protected final Context mContext;
	protected SQLiteDatabase mDb;
	public DataBaseHelper mDbHelper;

	public RemindersDbAdapter(Context context) {
		this.mContext = context;
		mDbHelper = new DataBaseHelper(mContext);
	}

	public RemindersDbAdapter createDatabase() throws SQLException {
		try {
			mDbHelper.createDataBase();
		} catch (IOException mIOException) {
			Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
			throw new Error("UnableToCreateDatabase");
		}
		return this;
	}

	public RemindersDbAdapter open() throws SQLException {
		try {
			mDbHelper.openDataBase();
			mDbHelper.close();
			mDb = mDbHelper.getReadableDatabase();
		} catch (SQLException mSQLException) {
			Log.e(TAG, "open >>" + mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	// Rishi Change start

	public static void backupDatabase() throws IOException {
		// Open your local db as the input stream
		String inFileName = "/data/data/com.example/databases/" + DATABASE_NAME;
		File dbFile = new File(inFileName);
		FileInputStream fis = new FileInputStream(dbFile);

		String outFileName = Environment.getExternalStorageDirectory() + "/"
				+ DATABASE_NAME;
		// Open the empty db as the output stream
		OutputStream output = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = fis.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}
		// Close the streams
		output.flush();
		output.close();
		fis.close();
	}

	public boolean insertContact(Contact li_obj) {
		try {
			Log.d(" LineItem Object", "" + li_obj.toString());
			ContentValues cv = new ContentValues();
			cv.put("name", li_obj.getName());
			cv.put("phone", li_obj.getPhone());
			cv.put("email", li_obj.getEmail());
			cv.put("image", li_obj.getImage());

			mDb.insert("contacts", null, cv);
			Log.d("contacts", "informationsaved");
			return true;

		} catch (Exception ex) {
			Log.d("Exception on inserting values into table", ex.toString());
			return false;
		}

	}

	public boolean updateContact(Contact li_obj) {
		try {
			Log.d(" LineItem Object", "" + li_obj.toString());
			ContentValues cv = new ContentValues();
			cv.put("name", li_obj.getName());
			cv.put("phone", li_obj.getPhone());
			cv.put("email", li_obj.getEmail());
			cv.put("image", li_obj.getImage());

			mDb.update("contacts", cv, "_id=" + li_obj.get_id(), null);
			Log.d("contacts", "informationsaved");
			return true;

		} catch (Exception ex) {
			Log.d("Exception on inserting values into table", ex.toString());
			return false;
		}

	}

	public ArrayList<Contact> getAllContacts(String key, String val)

	{
		ArrayList<Contact> li_list;
		li_list = new ArrayList<Contact>();

		String selectQuery = "";
		if (key != null) {

			if (key.equals("a_id")) {
				selectQuery = "SELECT c.* FROM  contacts c INNER JOIN alarm_contact_map ctm ON c._id=ctm.c_id WHERE ctm.a_id="
						+ val;
			}
			if (key.equals("detail")) {
				selectQuery = "SELECT  * FROM contacts WHERE _id=" + val;
			}
		} else {
			selectQuery = "SELECT  * FROM contacts";
		}

		Cursor cursor = mDb.rawQuery(selectQuery, null);
		System.out.print(cursor.getCount());

		if (cursor.moveToFirst()) {
			do {

				Contact li_obj = new Contact();

				li_obj.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
				li_obj.setName(cursor.getString(cursor.getColumnIndex("name")));
				li_obj.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
				li_obj.setEmail(cursor.getString(cursor.getColumnIndex("email")));
				li_obj.setImage(cursor.getString(cursor.getColumnIndex("image")));

				li_list.add(li_obj);

			} while (cursor.moveToNext());
		}

		return li_list;
	}

	public void delete_row_from_table(String table_name, String clmnm,
			int exp_id) {

		String qry = "DELETE FROM " + table_name + " WHERE " + clmnm + "="
				+ exp_id;
		Log.d("Delete Query ", qry);
		mDb.execSQL(qry);
	}

	// Rishi Change ends

}
