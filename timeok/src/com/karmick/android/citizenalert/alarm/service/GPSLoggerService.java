package com.karmick.android.citizenalert.alarm.service;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.karmick.android.citizenalert.R;
import com.karmick.android.citizenalert.alarm.AlarmActivity;
import com.karmick.android.citizenalert.async.SendMailTask;
import com.karmick.android.citizenalert.async.SendMailTask.OnEmailSendUrlCalledListener;
import com.karmick.android.citizenalert.constant.Consts;

public class GPSLoggerService extends Service implements
		OnEmailSendUrlCalledListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	// ----Start-----//
	private LocationManager locationManager;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	private boolean isGPSEnabled;
	private boolean isNetworkEnabled;
	private Location location;
	private boolean canGetLocation;

	private double curLatitude;
	private double curLongitude;

	LocationClient mLocationClient;
	Location mCurrentLocation;
	LocationRequest mLocationRequest;

	Intent intent;

	// ----End-----//

	public GPSLoggerService() {

	}

	public double getCurLatitude() {
		return curLatitude;
	}

	public void setCurLatitude(double curLatitude) {
		this.curLatitude = curLatitude;
	}

	public double getCurLongitude() {
		return curLongitude;
	}

	public void setCurLongitude(double curLongitude) {
		this.curLongitude = curLongitude;
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

	public int onStartCommand(Intent _intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		mLocationClient = new LocationClient(this, this, this);
		// 4. create & set LocationRequest for Location update
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		// Set the update interval to 5 seconds
		mLocationRequest.setInterval(1000 * 5);
		// Set the fastest update interval to 1 second
		mLocationRequest.setFastestInterval(1000 * 1);
		mLocationClient.connect();

		intent = _intent;

		Log.d(this.getClass().getSimpleName(),
				"onStartCommand() Show Notification");

		return Service.START_STICKY;
	}

	private void stopLoggingService() {
		mLocationClient.disconnect();
		stopSelf();

	}

	// GooglePlayServicesClient.OnConnectionFailedListener
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
	}

	// GooglePlayServicesClient.ConnectionCallbacks
	@Override
	public void onConnected(Bundle arg0) {

		if (mLocationClient != null)
			mLocationClient.requestLocationUpdates(mLocationRequest, this);

		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

		if (mLocationClient != null) {
			// get location
			mCurrentLocation = mLocationClient.getLastLocation();
			try {

				if (mCurrentLocation != null) {
					// set Locations
					double lat = mCurrentLocation.getLatitude();
					double lon = mCurrentLocation.getLongitude();

					setCurLatitude(lat);
					setCurLongitude(lon);

					// callSendMailTask(getCurLatitude(), getCurLongitude());

				} else {
					// Old Way
					Location lo = getLocation();
					if (lo != null) {
						setCurLatitude(lo.getLatitude());
						setCurLongitude(lo.getLongitude());

						// callSendMailTask(getCurLatitude(),
						// getCurLongitude());
					} else {
						Consts.appendLog("Couldn't Find the location detail");
					}
				}

			} catch (NullPointerException npe) {

				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				npe.printStackTrace(pw);
				Consts.appendLog(sw.toString());

				Toast.makeText(this, "Failed to Connect", Toast.LENGTH_SHORT)
						.show();

			}
		}

	}

	protected void callSendMailTask(double lat, double lon) {
		final int alarm_id = (int) intent.getExtras().getInt("alarm_id");
		final String email_id = (String) intent.getExtras().getString(
				"email_id");

		if (getCurLatitude() != 0.0 && getCurLongitude() != 0.0) {
			if (email_id != null) {
				new SendMailTask(GPSLoggerService.this, " Current Location : ",
						lat + "," + lon, email_id).execute();
			} else {
				new SendMailTask(GPSLoggerService.this, " Current Location : ",
						lat + "," + lon, alarm_id).execute();
			}
		} else {
			String str = "Problem Getting the location.";
			showNotification(getApplicationContext(), str);
		}
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected.", Toast.LENGTH_SHORT).show();

	}

	// LocationListener
	@Override
	public void onLocationChanged(Location location) {
		Toast.makeText(this, "Location changed.", Toast.LENGTH_SHORT).show();
		mCurrentLocation = mLocationClient.getLastLocation();

		if (mCurrentLocation != null) {
			double lat = mCurrentLocation.getLatitude();
			double lon = mCurrentLocation.getLongitude();

			setCurLatitude(lat);
			setCurLongitude(lon);

			callSendMailTask(getCurLatitude(), getCurLongitude());
		} else {
			Location lo = getLocation();
			if (lo != null) {
				setCurLatitude(lo.getLatitude());
				setCurLongitude(lo.getLongitude());

				callSendMailTask(getCurLatitude(), getCurLongitude());
			} else {
				Consts.appendLog("Couldn't set the location detail");
			}

		}

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

	public Location getLocation() {
		try {
			locationManager = (LocationManager) GPSLoggerService.this
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, mLl);
					Log.d("Network", "Network Enabled");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, mLl);
						Log.d("GPS", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);

						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	// Old Location listener
	android.location.LocationListener mLl = new android.location.LocationListener() {

		@Override
		public void onLocationChanged(Location arg0) {

		}

		@Override
		public void onProviderDisabled(String arg0) {

		}

		@Override
		public void onProviderEnabled(String arg0) {

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

		}

	};

}
