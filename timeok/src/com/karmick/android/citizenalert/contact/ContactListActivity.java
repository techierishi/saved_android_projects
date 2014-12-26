package com.karmick.android.citizenalert.contact;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.karmick.android.citizenalert.R;
import com.karmick.android.citizenalert.common.BaseActivity;
import com.karmick.android.citizenalert.common.Popups;
import com.karmick.android.citizenalert.common.Popups.YesNoClickListener;
import com.karmick.android.citizenalert.common.SwipeDismissListViewTouchListener;
import com.karmick.android.citizenalert.database.RemindersDbAdapter;
import com.karmick.android.citizenalert.models.Contact;

public class ContactListActivity extends BaseActivity {

	private ArrayList<Contact> items = new ArrayList<Contact>();

	private ListView mListView;
	private ViewStub inflate_stub;
	private ContactLisAdapter mAdapter;

	Button add_contact;
	Button select_contact;

	private RemindersDbAdapter mDbHelper;
	boolean is_for_result = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);
		actionBar();
		initViews();

		inflate_stub = (ViewStub) findViewById(R.id.inflate_stub);
		inflate_stub.inflate();
		inflate_stub.setVisibility(View.GONE);

		mDbHelper = new RemindersDbAdapter(this);
		mDbHelper.createDatabase();
		mDbHelper.open();
		items = mDbHelper.getAllContacts(null, null);
		mDbHelper.close();

		mListView = (ListView) findViewById(R.id.listView);
		mAdapter = new ContactLisAdapter(items);
		mListView.setAdapter(mAdapter);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			is_for_result = extras.getBoolean("for_result");
		}
		evenCalls();

	}

	protected void actionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	public void initViews() {
		add_contact = (Button) findViewById(R.id.add_contact);
		select_contact = (Button) findViewById(R.id.select_contact);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.findItem(R.id.menu_item_save).setVisible(false);
		menu.findItem(R.id.menu_item_delete).setVisible(false);
		return result;
	}

	/**
	 * Contact List Adpater
	 * 
	 * @author Rishikesh
	 */

	public class ContactLisAdapter extends BaseAdapter {

		private ArrayList<Contact> mData;

		public ContactLisAdapter(ArrayList<Contact> data) {
			mData = data;
		}

		public void setListData(ArrayList<Contact> data) {
			mData = data;
		}

		public void clear() {
			mData = null;
		}

		public void changeData(ArrayList<Contact> data) {
			mData = data;
			if (mData.size() > 0) {
				inflate_stub.setVisibility(View.GONE);
			} else {
				inflate_stub.setVisibility(View.VISIBLE);
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			CheckBox contact_select;
			ImageButton get_location;
			TextView contact_name;
			ImageView contact_img;
			TextView contact_person_id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = new ViewHolder();
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.contact_list_row, null);
				holder.contact_name = (TextView) convertView
						.findViewById(R.id.contact_name);
				holder.contact_img = (ImageView) convertView
						.findViewById(R.id.contact_img);
				holder.contact_select = (CheckBox) convertView
						.findViewById(R.id.contact_select);
				holder.get_location = (ImageButton) convertView
						.findViewById(R.id.get_location);
				holder.contact_person_id = (TextView) convertView
						.findViewById(R.id.contact_person_id);
				convertView.setTag(holder);

				holder.contact_select
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								CheckBox cb = (CheckBox) v;
								Contact contact = (Contact) cb.getTag();

								contact.setSelected(cb.isChecked());
							}
						});

				holder.get_location.setTag(position);

				holder.get_location.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {

						getLocation(v);
					}
				});

				convertView.setTag(R.layout.contact_list_row, holder);
			} else {
				holder = (ViewHolder) convertView
						.getTag(R.layout.contact_list_row);
			}

			if (holder != null) {
				convertView.setTag(position);
				Contact contact = items.get(position);
				holder.contact_person_id.setText(String.valueOf(items.get(
						position).get_id()));
				holder.contact_name.setText(contact.getName());
				showImg(contact.getImage(), holder.contact_img);
				holder.contact_select.setChecked(contact.isSelected());
				holder.contact_select.setTag(contact);
			}
			return convertView;
		}

	}

	private void getLocation(View v) {
		// TODO Sms sending part here
		final int pos = (Integer) v.getTag();
		final JSONObject jObj = new JSONObject();
		Popups.showYesNoPopup("Click YES to get the location",
				ContactListActivity.this, new YesNoClickListener() {

					@Override
					public void onYesButtonClick() {

						try {
							String strr;
							SharedPreferences sharedPrefs = PreferenceManager
									.getDefaultSharedPreferences(ContactListActivity.this);

							strr = sharedPrefs.getString("prefEmail", "NULL");

							if (!strr.isEmpty()) {
								jObj.put("email_id", strr);
								jObj.put("key", "@locateme");

								Popups.showToast(
										"Phone Number : " + items.get(pos).getPhone(),
										ContactListActivity.this);

								// It may cost money
								// sendSMS(ContactListActivity.this, items
								// .get(pos).getPhone(), jObj.toString());

							} else {
								Popups.showToast(
										"Please save email in the settings first.",
										ContactListActivity.this);

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onNoButtonClick() {

					}

				});
	}

	private void evenCalls() {

		if (is_for_result) {
			select_contact.setVisibility(View.VISIBLE);
		} else {
			select_contact.setVisibility(View.GONE);
		}

		select_contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				JSONArray jArr = new JSONArray();
				ArrayList<Contact> contactList = mAdapter.mData;
				for (int i = 0; i < contactList.size(); i++) {
					Contact contact = contactList.get(i);
					if (contact.isSelected()) {
						try {
							JSONObject jObj = new JSONObject();
							jObj.put("id", contact.get_id());
							jObj.put("name", contact.getName());
							jArr.put(jObj);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}

				Intent i = new Intent();
				i.putExtra("contacts", jArr.toString());
				setResult(RESULT_OK, i);
				finish();

			}
		});
		add_contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intt = new Intent(ContactListActivity.this,
						AddContact.class);
				startActivity(intt);

			}
		});
		// Swipe functionality starts here
		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
				mListView,
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

							Log.d("Contacts : ", items.toString());

							// Deleting part
							Popups.showYesNoPopup(
									"Do you really want to delete this ?",
									ContactListActivity.this,
									new YesNoClickListener() {

										@Override
										public void onYesButtonClick() {
											int id = items.get(pos).get_id();
											mDbHelper.createDatabase();
											mDbHelper.open();
											mDbHelper.delete_row_from_table(
													"contacts", "_id", id);
											mDbHelper.close();

											items.get(pos).setVisible(true);
											items.remove(pos);

											mAdapter.changeData(items);

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
		mListView.setOnTouchListener(touchListener);
		// Swipe functionality ends here
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> vw, View view, int position,
					long id) {
				// ExpenseDetail page intent
				final int _pos = (Integer) view.getTag();
				int _id = items.get(_pos).get_id();
				Log.d(" Contact >> ", " Id : " + _id);
				Intent intnt = new Intent(ContactListActivity.this,
						AddContact.class);
				intnt.putExtra("contact_id", _id);
				startActivity(intnt);

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		callOnResume();
	}

	public void callOnResume() {
		RemindersDbAdapter mDbHelper = new RemindersDbAdapter(
				ContactListActivity.this);
		mDbHelper.createDatabase();
		mDbHelper.open();
		items = mDbHelper.getAllContacts(null, null);
		mDbHelper.close();

		mAdapter.changeData(items);
	}

	@Override
	public void onClick(View v) {

	}
}
