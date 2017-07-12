package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
import nyc.c4q.rusili.SimplyWeather.network.JSON.HourlyForecast;
import nyc.c4q.rusili.SimplyWeather.network.RetroFitBase;
import nyc.c4q.rusili.SimplyWeather.utilities.app.CalendarHelper;
import nyc.c4q.rusili.SimplyWeather.utilities.app.IconInflater;
import nyc.c4q.rusili.SimplyWeather.utilities.app.ScreenServiceAndReceiver;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.AppContext;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.Constants;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.DebugMode;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.ShowToast;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;

public class WeatherWidget4x2 extends AppWidgetProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	private AppContext appContext = new AppContext();

	private boolean isViewFlipperOpen = false;

	public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6;

	public Context context;
	public GoogleApiClient mGoogleApiClient;
	public IconInflater iconInflater;
	public CalendarHelper calendarHelper;
	public RetroFitBase retroFitBase;
	public Location mLastLocation;

	public RemoteViews remoteViews;

	public boolean locationPermissionGranted;
	public int zipCode = 0;
	public int widgetID;

	private int numOfDays = Constants.NUM_OF_DAYS.WIDGET;

	@Override
	public void onUpdate (final Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
			for (int widgetID : appWidgetIds) {
				this.widgetID = widgetID;

				DebugMode.logD(context, "onUpdate");

				remoteViews = new RemoteViews(context.getPackageName(),
					  R.layout.widget_layout_4x2);

				setOnClickUpdate(context);
				//setOnClickConfig(context, widgetID);
				setViewFlipper(context);

				iconInflater = IconInflater.getInstance();
				calendarHelper = CalendarHelper.getInstance();

				startGoogleAPIClient(context);
			}
	}

	private void startGoogleAPIClient (Context context) {
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
			ShowToast.show(context, "No network detected");
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
			ActivityCompat.requestPermissions((Activity) context.getApplicationContext(),
				  new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
				  PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
		}
		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		DebugMode.logD(context, mLastLocation.toString());
		getLastLocation(mLastLocation);
	}

	private void getLastLocation (Location mLastLocation) {
		Geocoder geocoder = new Geocoder(context, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
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

			@Override
			public void onHourlyRetrieved (HourlyForecast[] hourlyForecasts) {
				updateHours(context, appWidgetManager, widgetID, hourlyForecasts, numOfDays);
			}
		});
		retroFitBase.getConditions();
		retroFitBase.getForecastDay();
		retroFitBase.getHourlyForecast();
	}

	private void updateHours (Context context, AppWidgetManager appWidgetManager, int widgetID, HourlyForecast[] hourlyForecasts, int numOfDays) {
		int resID = 0;
		int nextHourOffset = 0;
		int hour = 1;

		if (CalendarHelper.getInstance().before30Minutes()){
			nextHourOffset = 1;
		}
		for (int i = 1; i < numOfDays; i++) {
			resID = context.getResources().getIdentifier("widget_component_hour_hour" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, CalendarHelper.getInstance().change24to12hour(hourlyForecasts[hour-nextHourOffset].getFCTTIME().getHour()));
			resID = context.getResources().getIdentifier("widget_component_hour_period" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, hourlyForecasts[hour-nextHourOffset].getFCTTIME().getAmpm());
			resID = context.getResources().getIdentifier("widget_component_hour_temp" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, hourlyForecasts[hour-nextHourOffset].getTemp().getEnglish() + Constants.SYMBOLS.DEGREE);
			resID = context.getResources().getIdentifier("widget_component_hour_icon" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setImageViewResource(resID, iconInflater.choose(hourlyForecasts[hour-nextHourOffset].getIcon()));
			hour = hour + (i+1);
		}
		appWidgetManager.updateAppWidget(widgetID, remoteViews);
	}

	private void updateMain (AppWidgetManager appWidgetManager, int widgetID, CurrentObservation currentObservation) {
		Date now = new Date();
		SimpleDateFormat weekday = new SimpleDateFormat("E");
		SimpleDateFormat month = new SimpleDateFormat("MM");
		SimpleDateFormat day = new SimpleDateFormat("dd");

		remoteViews.setTextViewText(R.id.widget_component_main_weekday_height2, weekday.format(now));
		remoteViews.setTextViewText(R.id.widget_component_main_day_height2, CalendarHelper.getInstance().ifSingleDigit(month.format(now)) + "/" + CalendarHelper.getInstance().ifSingleDigit(day.format(now)));
		remoteViews.setTextViewText(R.id.widget_component_main_currenttemp_height2, String.valueOf((int) currentObservation.getTemp_f()) + Constants.SYMBOLS.DEGREE);
		remoteViews.setTextViewText(R.id.widget_component_main_location_height2, currentObservation.getDisplay_location().getCity());
		remoteViews.setImageViewResource(R.id.widget_component_main_icon_height2, iconInflater.choose(currentObservation.getIcon()));

		appWidgetManager.updateAppWidget(widgetID, remoteViews);
	}

	public void updateDays (Context context, AppWidgetManager appWidgetManager, int widgetID, ForecastDay[] forecastDays, int numOfDays) {
		numOfDays = Constants.NUM_OF_DAYS.WIDGET;

		int resID = 0;
		remoteViews.setTextViewText(R.id.widget_component_main_hitemp_height2, String.valueOf(forecastDays[0].getHigh().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
		remoteViews.setTextViewText(R.id.widget_component_main_lowtemp_height2, String.valueOf(forecastDays[0].getLow().getFahrenheit()) + Constants.SYMBOLS.DEGREE);

		for (int i = 1; i < numOfDays; i++) {
			resID = context.getResources().getIdentifier("widget_component_day_weekday" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, CalendarHelper.getInstance().getTwoCharWeekday(forecastDays[i].getDate().getWeekdayShort()));
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

	private void setOnClickConfig (Context context, int widgetID) {
		Intent intent = new Intent(context, WeatherWidget4x2.class);
		intent.putExtra(EXTRA_APPWIDGET_ID, widgetID);
		intent.setAction(Constants.ACTION.CONFIG_CLICK);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget_layout_4x2_container, pendingIntent);
	}

	private void setViewFlipper (Context context){
		Intent intent = new Intent(context, WeatherWidget4x2.class);
		intent.setAction(Constants.ACTION.VIEWFLIPPER_CLICK);
		intent.putExtra("isOpen", isViewFlipperOpen);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget_layout_4x2_include_main2, pendingIntent);
	}

	private void setOnClickUpdate (Context context) {
		Intent intent = new Intent(context, WeatherWidget4x2.class);
		intent.setAction(Constants.ACTION.UPDATE_CLICK);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget_layout_4x2_container, pendingIntent);
	}

	@Override
	public void onReceive (Context context, Intent intent) {
		super.onReceive(context, intent);

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(), getClass().getName());
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
		RemoteViews root = new RemoteViews(context.getPackageName(), R.layout.widget_layout_4x2);

		if (!isMyServiceRunning(context, ScreenServiceAndReceiver.class)) {            // Checks to make sure the service is running. If not, restart the service.
			context.startService(new Intent(context, ScreenServiceAndReceiver.class));
		}

		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			DebugMode.logD(context, "onReceive " + "BOOT_COMPLETED");
			onUpdate(context, appWidgetManager, appWidgetIds);

		} else if (intent.getAction().equals(Constants.ACTION.UPDATE_SCREEN)) {
			DebugMode.logD(context, "onReceive " + "UPDATE_SCREEN");

			onUpdate(context, appWidgetManager, appWidgetIds);

		} else if (intent.getAction().equals(Constants.ACTION.UPDATE_CLICK)) {
			DebugMode.logD(context, "onReceive " + "UPDATE_CLICK");

			ShowToast.show(context, "SimplyWeather updated!");
			onUpdate(context, appWidgetManager, appWidgetIds);

		} else if (intent.getAction().equals(Constants.ACTION.VIEWFLIPPER_CLICK)) {
			if (intent.getBooleanExtra("isOpen", false) == false) {
				root.showPrevious(R.id.widget_layout_4x2_viewflipper);
			} else if (intent.getBooleanExtra("isOpen", false) == true){
				root.showPrevious(R.id.widget_layout_4x2_viewflipper);
			}
			appWidgetManager.updateAppWidget(appWidgetIds, root);
		}
	}

	private boolean isMyServiceRunning (Context context, Class <?> serviceClass) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				DebugMode.logD(context, "isMyServiceRunning " + "TRUE");
				return true;
			}
		}
		DebugMode.logD(context, "isMyServiceRunning " + "FALSE");
		return false;
	}

	@Override
	public void onDisabled (Context context) {
		super.onDisabled(context);

		DebugMode.logD(context, "Debug: " + "onDisabled");
		killService(context);
	}

	private void killService (Context context) {
		context.stopService(new Intent(context, ScreenServiceAndReceiver.class));
	}
}
