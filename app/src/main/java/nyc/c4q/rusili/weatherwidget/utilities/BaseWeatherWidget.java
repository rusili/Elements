package nyc.c4q.rusili.weatherwidget.utilities;

import android.Manifest;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import nyc.c4q.rusili.weatherwidget.R;
import nyc.c4q.rusili.weatherwidget.activities.configuration.ActivityConfiguration;
import nyc.c4q.rusili.weatherwidget.network.JSON.CurrentObservation;
import nyc.c4q.rusili.weatherwidget.network.JSON.ForecastDay;
import nyc.c4q.rusili.weatherwidget.network.RetroFitBase;

public abstract class BaseWeatherWidget extends AppWidgetProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6;

	public Context context;
	public GoogleApiClient mGoogleApiClient;
	public GlideWrapper glideWrapper;
	public RetroFitBase retroFitBase;
	public Location mLastLocation;

	public RemoteViews remoteViews;

	public boolean locationPermissionGranted;
	public int zipCode = 0;
	public int widgetID;
	private int numOfDays;

	@Override
	public void onUpdate (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		startGoogleAPIClient(context);
	}

	public void setOnClickUpdate () {
	}

	public CharSequence ifSingleDigit (String format) {
		CharSequence charSequence = format;

		if (Integer.parseInt(format) < 10) {
			charSequence = String.valueOf(format.charAt(1));
		}
		return charSequence;
	}

	public CharSequence getTwoCharWeekday (String weekdayShort) {
		CharSequence charSequence = weekdayShort;

		if (weekdayShort.contains("T") || weekdayShort.contains("S")) {
			charSequence = weekdayShort.substring(0, 2);
		} else {
			charSequence = String.valueOf(weekdayShort.charAt(0));
		}
		return charSequence;
	}

	public void startGoogleAPIClient (Context context) {
		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(context)
				  .addConnectionCallbacks(this)
				  .addOnConnectionFailedListener(this)
				  .addApi(LocationServices.API)
				  .build();
		}
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnected (@Nullable Bundle bundle) {
		if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
			  android.Manifest.permission.ACCESS_FINE_LOCATION)
			  == PackageManager.PERMISSION_GRANTED) {
			locationPermissionGranted = true;
		} else {
			ActivityCompat.requestPermissions((Activity) context,
				  new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
				  PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
		}
		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		getLastLocation(mLastLocation);
	}

	private void getLastLocation (Location mLastLocation) {
		Geocoder geocoder = new Geocoder(context, Locale.getDefault());
		try {
			List <Address> addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
			zipCode = Integer.parseInt(addresses.get(0).getPostalCode());
		} catch (IOException e) {
			e.printStackTrace();
		}
		downloadWeatherData(context, AppWidgetManager.getInstance(context), widgetID, remoteViews, zipCode);
		context = null;
	}

	private void downloadWeatherData (final Context context, final AppWidgetManager appWidgetManager, final int widgetID, final RemoteViews remoteViews, final int zipCode) {
		RetroFitBase.RetrofitListener retrofitListener;

		retroFitBase = new RetroFitBase(Constants.DEVELOPER_KEY.API_KEY, zipCode);
		retroFitBase.setRetrofitListener(retrofitListener = new RetroFitBase.RetrofitListener() {
			@Override
			public void onConditionsRetrieved (CurrentObservation currentObservation) {
				updateMain(appWidgetManager, widgetID, currentObservation);
			}

			@Override
			public void onForecastDaysRetrieved (ForecastDay[] forecastDays) {
				updateDays(context, appWidgetManager, widgetID, forecastDays, numOfDays);
			}
		});
		retroFitBase.getConditions();
		retroFitBase.getForecastDay();
	}

	private void updateMain (AppWidgetManager appWidgetManager, int widgetID, CurrentObservation currentObservation) {
		Date now = new Date();
		SimpleDateFormat weekday = new SimpleDateFormat("E");
		SimpleDateFormat month = new SimpleDateFormat("MM");
		SimpleDateFormat day = new SimpleDateFormat("dd");

		remoteViews.setTextViewText(R.id.widget_component_main_onClickDebug, String.valueOf(new Random(100)));

		remoteViews.setTextViewText(R.id.widget_component_main_weekday_height2, weekday.format(now));
		remoteViews.setTextViewText(R.id.widget_component_main_day_height2, ifSingleDigit(month.format(now)) + "/" + ifSingleDigit(day.format(now)));
		remoteViews.setTextViewText(R.id.widget_component_main_currenttemp_height2, String.valueOf((int) currentObservation.getTemp_f()) + Constants.SYMBOLS.DEGREE);
		remoteViews.setTextViewText(R.id.widget_component_main_location_height2, currentObservation.getDisplay_location().getCity());
		glideWrapper.inflateImage(R.id.widget_component_main_icon_height2, currentObservation.getIcon_url());

		appWidgetManager.updateAppWidget(widgetID, remoteViews);
	}

	public void updateDays (Context context, AppWidgetManager appWidgetManager, int widgetID, ForecastDay[] forecastDays, int numOfDays) {

		int resID = 0;
		remoteViews.setTextViewText(R.id.widget_component_main_hitemp_height2, String.valueOf(forecastDays[0].getHigh().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
		remoteViews.setTextViewText(R.id.widget_component_main_lowtemp_height2, String.valueOf(forecastDays[0].getLow().getFahrenheit()) + Constants.SYMBOLS.DEGREE);

		for (int i = 1; i < numOfDays; i++) {
			resID = context.getResources().getIdentifier("widget_component_day_weekday" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, getTwoCharWeekday(forecastDays[i].getDate().getWeekdayShort()));
			resID = context.getResources().getIdentifier("widget_component_day_day" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, forecastDays[i].getDate().getDay());
			resID = context.getResources().getIdentifier("widget_component_day_temphigh" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, String.valueOf(forecastDays[i].getHigh().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
			resID = context.getResources().getIdentifier("widget_component_day_templow" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, String.valueOf(forecastDays[i].getLow().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
			resID = context.getResources().getIdentifier("widget_component_day_icon" + String.valueOf(i + 1), "id", context.getPackageName());
			glideWrapper.inflateImage(resID, forecastDays[i].getIcon_url());
		}

		appWidgetManager.updateAppWidget(widgetID, remoteViews);
	}

	@Override
	public void onConnectionSuspended (int i) {
	}

	@Override
	public void onConnectionFailed (@NonNull ConnectionResult connectionResult) {
	}

	@Override
	public void onReceive (Context context, Intent intent) {
		super.onReceive(context, intent);

		if (intent.getAction().equals("4x2_UPDATE_CLICK")) {
			Log.d(String.valueOf(getClass()), "Clicked Update!");

			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(), getClass().getName());
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName); // 4x2 = 31
			onUpdate(context, appWidgetManager, appWidgetIds);
		} else if (intent.getAction().equals("4x2_CONFIG_CLICK")) {
			Log.d(String.valueOf(getClass()), "Clicked Config!");
			Intent intentConfig = new Intent(context, ActivityConfiguration.class);
			Bundle bundle = new Bundle();
			bundle.putString("Data", getClass().getName() + "/" + widgetID);
			intentConfig.putExtras(bundle);
			intentConfig.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intentConfig);
		}
	}
}
