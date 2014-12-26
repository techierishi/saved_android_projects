package com.karmick.android.citizenalert.alarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.karmick.android.citizenalert.R;
import com.karmick.android.citizenalert.common.BaseActivity;
import com.karmick.android.citizenalert.common.Popups;

public class LoginScreen extends BaseActivity {

	EditText your_password;
	Button dialogButtonOK;
	String your_password_str;
	String password;

	TextView first_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar();
		setContentView(R.layout.login_screen);

		your_password = (EditText) findViewById(R.id.your_password);
		dialogButtonOK = (Button) findViewById(R.id.dialogButtonOK);
		first_password = (TextView) findViewById(R.id.first_password);

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(LoginScreen.this);
		password = sharedPrefs.getString("prefPassword", "admin");

		if (password.trim().equals("admin")) {
			first_password.setVisibility(View.VISIBLE);
		} else {
			first_password.setVisibility(View.GONE);
		}
		
		dialogButtonOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				your_password_str = your_password.getText().toString();

				if (your_password_str.trim().equals(password.trim())) {
					Intent i = new Intent(getBaseContext(), AlarmActivity.class);
					startActivity(i);
					finish();
				} else {
					Popups.showToast("Wrong password", LoginScreen.this);
				}

			}
		});

	}

	public void actionBar() {
		getActionBar().hide();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

	}

	@Override
	public void onClick(View arg0) {

	}
}
