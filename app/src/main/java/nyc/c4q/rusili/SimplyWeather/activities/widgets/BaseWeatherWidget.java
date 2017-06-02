package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.Manifest;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nyc.c4q.rusili.SimplyWeather.R;
import nyc.c4q.rusili.SimplyWeather.network.JSON.CurrentObservation;
import nyc.c4q.rusili.SimplyWeather.network.JSON.ForecastDay;
import nyc.c4q.rusili.SimplyWeather.network.RetroFitBase;
import nyc.c4q.rusili.SimplyWeather.utilities.Constants;
import nyc.c4q.rusili.SimplyWeather.utilities.IconInflater;

public abstract class BaseWeatherWidget extends AppWidgetProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6;

	public Context context;
	public GoogleApiClient mGoogleApiClient;
	public IconInflater iconInflater;
	public RetroFitBase retroFitBase;
	public Location mLastLocation;

	public RemoteViews remoteViews;

	public boolean locationPermissionGranted;
	private boolean screenOff;
	public int zipCode = 0;
	public int widgetID;
	private int numOfDays;

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
		this.context = context;

		if (isNetworkConnected(context)) {
			mGoogleApiClient.connect();
		} else {
			Toast.makeText(context, "No network detected", Toast.LENGTH_SHORT).show();
		}
	}

	private boolean isNetworkConnected (Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
			  activeNetwork.isConnectedOrConnecting();

		return isConnected;
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

		remoteViews.setTextViewText(R.id.widget_component_main_weekday_height2, weekday.format(now));
		remoteViews.setTextViewText(R.id.widget_component_main_day_height2, ifSingleDigit(month.format(now)) + "/" + ifSingleDigit(day.format(now)));
		remoteViews.setTextViewText(R.id.widget_component_main_currenttemp_height2, String.valueOf((int) currentObservation.getTemp_f()) + Constants.SYMBOLS.DEGREE);
		remoteViews.setTextViewText(R.id.widget_component_main_location_height2, currentObservation.getDisplay_location().getCity());
		remoteViews.setImageViewResource(R.id.widget_component_main_icon_height2, iconInflater.choose(currentObservation.getIcon()));

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
			remoteViews.setImageViewResource(resID, iconInflater.choose(forecastDays[i].getIcon()));
		}

		appWidgetManager.updateAppWidget(widgetID, remoteViews);
	}

	@Override
	public void onConnectionSuspended (int i) {
	}

	@Override
	public void onConnectionFailed (@NonNull ConnectionResult connectionResult) {
		Log.d("onConnectionFailed: ", connectionResult.getErrorMessage());
	}
}
