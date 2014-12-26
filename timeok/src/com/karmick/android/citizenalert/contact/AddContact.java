package com.karmick.android.citizenalert.contact;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.karmick.android.citizenalert.R;
import com.karmick.android.citizenalert.common.ImageChooserActivity;
import com.karmick.android.citizenalert.common.Popups;
import com.karmick.android.citizenalert.constant.Consts;
import com.karmick.android.citizenalert.database.RemindersDbAdapter;
import com.karmick.android.citizenalert.models.Contact;

public class AddContact extends ImageChooserActivity {

	EditText contact_name;
	EditText contact_phone;
	EditText contact_email;

	Button add_contact;

	private RemindersDbAdapter mDbHelper;

	private boolean is_edit_mode = false;
	private int contact_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_add);
		actionBar();
		initViews();

		super.fakeOnCreate();

		mDbHelper = new RemindersDbAdapter(this);
		mDbHelper.createDatabase();
		mDbHelper.open();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			is_edit_mode = true;
			contact_id = extras.getInt("contact_id");
			fillConactDetail(contact_id);
		}

		eventCalls();

	}

	public void fillConactDetail(int contact_id) {
		ArrayList<Contact> c_list = mDbHelper.getAllContacts("detail", ""
				+ contact_id);

		contact_name.setText(c_list.get(0).getName());
		contact_phone.setText(c_list.get(0).getPhone());
		contact_email.setText(c_list.get(0).getEmail());
		imageViewName.setText(c_list.get(0).getImage());
		File imgFile = new File(Consts.IMAGE_PATH + c_list.get(0).getImage());
		if (imgFile.exists()) {
			imageView.setVisibility(View.VISIBLE);
			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
					.getAbsolutePath());
			imageView.setImageBitmap(myBitmap);

		}
	}

	protected void actionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	public void initViews() {

		contact_name = (EditText) findViewById(R.id.contact_name);
		contact_phone = (EditText) findViewById(R.id.contact_phone);
		contact_email = (EditText) findViewById(R.id.contact_email);

		add_contact = (Button) findViewById(R.id.add_contact);
	}

	public void eventCalls() {

		add_contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				validateAndSaveContact();
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressLint("NewApi")
	private void validateAndSaveContact() {

		String contact_name_str = contact_name.getText().toString();
		String contact_phone_str = contact_phone.getText().toString();
		String contact_email_str = contact_email.getText().toString();
		String image_name = imageViewName.getText().toString();

		String errors = "\n";
		if (contact_name_str.isEmpty()) {
			errors += "Please Enter Name \n ";
		}
		if (contact_phone_str.isEmpty()) {
			errors += "Please Enter Phone \n ";
		}
		if (contact_email_str.isEmpty()) {
			errors += "Please Enter Email \n ";
		}

		if (!errors.equals("\n")) {
			Popups.showPopup(errors, AddContact.this);
		} else {
			Contact cObj = new Contact();

			cObj.setName(contact_name_str);
			cObj.setPhone(contact_phone_str);
			cObj.setEmail(contact_email_str);
			cObj.setImage(image_name);

			boolean tf = false;

			if (is_edit_mode) {
				cObj.set_id(contact_id);
				tf = mDbHelper.updateContact(cObj);
				if (tf) {
					is_edit_mode = false;
				}
			} else {
				tf = mDbHelper.insertContact(cObj);
			}
			if (tf) {
				Popups.showPopup("Contact Saved Successfully.", AddContact.this);
				contact_name.setText("");
				contact_phone.setText("");
				contact_email.setText("");
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
	}

}
