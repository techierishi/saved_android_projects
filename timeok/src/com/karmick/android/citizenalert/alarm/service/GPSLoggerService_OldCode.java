package com.karmick.android.citizenalert.alarm.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.karmick.android.citizenalert.R;
import com.karmick.android.citizenalert.alarm.AlarmActivity;
import com.karmick.android.citizenalert.async.SendMailTask;
import com.karmick.android.citizenalert.async.SendMailTask.OnEmailSendUrlCalledListener;

public class GPSLoggerService_OldCode extends Service implements LocationListener,
		OnEmailSendUrlCalledListener {

	// ----Start-----//
	private LocationManager locationManager;
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	private boolean isGPSEnabled;
	private boolean isNetworkEnabled;
	private Location location;
	private double latitude = 0.0;;
	private double longitude = 0.0;;
	private boolean canGetLocation;

	// ----End-----//

	public GPSLoggerService_OldCode() {

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(this.getClass().getSimpleName(), "onCreate() Show Notification");
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		final int alarm_id = (int) intent.getExtras().getInt("alarm_id");
		final String email_id = (String) intent.getExtras().getString(
				"email_id");

		// Get Location Detail here
		getLocation();

		if (email_id != null) {
			new SendMailTask(GPSLoggerService_OldCode.this, " Current Location : ",
					latitude + "," + longitude, email_id).execute();
		} else {
			new SendMailTask(GPSLoggerService_OldCode.this, " Current Location : ",
					latitude + "," + longitude, alarm_id).execute();
		}
		Log.d(this.getClass().getSimpleName(),
				"onStartCommand() Show Notification");

		return Service.START_STICKY;
	}

	@Override
	public void onLocationChanged(Location location) {

		Log.d(this.getClass().getSimpleName(), "onLocationChanged() called");

		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	private void stopLoggingService() {
		stopSelf();
	}

	@Override
	public void onEmailSendUrlCalled(String str) {

		Log.d("Response Message >>", "" + str);
		showNotification(getApplicationContext(), str);

		stopLoggingService();
	}

	private void showNotification(Context context, String message) {
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, AlarmActivity.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("TimeOK").setContentText(message);
		mBuilder.setContentIntent(contentIntent);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mBuilder.setAutoCancel(true);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());

	}

	public void getLocation() {

		locationManager = (LocationManager) this
				.getSystemService(LOCATION_SERVICE);

		// getting GPS status
		isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// getting network status
		isNetworkEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!isGPSEnabled && !isNetworkEnabled) {
			// no GPS Provider and no network provider is enabled
		} else { // Either GPS provider or network provider is enabled

			// First get location from Network Provider
			if (isNetworkEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				if (locationManager != null) {
					location = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

					if (location != null) {
						latitude = location.getLatitude();
						longitude = location.getLongitude();
						this.canGetLocation = true;
					}
				}
			}// End of IF network enabled

			// if GPS Enabled get lat/long using GPS Services
			if (isGPSEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				if (locationManager != null) {
					location = locationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (location != null) {
						latitude = location.getLatitude();
						longitude = location.getLongitude();
						this.canGetLocation = true;
					}
				}

			}// End of if GPS Enabled
		}// End of Either GPS provider or network provider is enabled
	}
}
