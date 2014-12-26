package com.karmick.android.citizenalert.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.karmick.android.citizenalert.R;
import com.karmick.android.citizenalert.common.BaseActivity;

public class MainSplashScreen extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar();
		setContentView(R.layout.main_splash_screen);

		Thread background = new Thread() {
			public void run() {

				try {
					sleep(3 * 1000);

					Intent i = new Intent(getBaseContext(), LoginScreen.class);
					startActivity(i);

					finish();

				} catch (Exception e) {

				}
			}
		};

		background.start();

	}

	public void actionBar() {
		//getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
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
