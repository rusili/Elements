package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.Manifest;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import dagger.Module;
import nyc.c4q.rusili.SimplyWeather.network.JSON.CurrentObservation;
import nyc.c4q.rusili.SimplyWeather.network.JSON.ForecastDay;
import nyc.c4q.rusili.SimplyWeather.network.JSON.HourlyForecast;
import nyc.c4q.rusili.SimplyWeather.network.RetroFitBase;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.Constants;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.DebugMode;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.ShowToast;

@Module
public class WeatherPresenter implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	private WidgetInterface.WidgetProvider widgetProvider;

	public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6;

	public boolean locationPermissionGranted;

	private GoogleApiClient googleApiClient;
	private Context context;
	private Location lastLocation;
	public int zipCode = 11375;

	private int widgetID;
	private int numOfDays = Constants.NUM_OF_DAYS.WIDGET;
	private RemoteViews remoteViews;

	public WeatherPresenter(){
		initialize();
	}

	private void initialize () {}

	public void bindView(WidgetInterface.WidgetProvider widgetProvider){
		this.widgetProvider = widgetProvider;
	}

	public void startGoogleAPIClient (Context context, int widgetID, RemoteViews remoteViews) {
		if (googleApiClient == null) {
			googleApiClient = new GoogleApiClient.Builder(context)
				  .addConnectionCallbacks(this)
				  .addOnConnectionFailedListener(this)
				  .addApi(LocationServices.API)
				  .build();
		}
		this.context = context;
		this.widgetID = widgetID;
		this.remoteViews = remoteViews;

		if (isNetworkConnected(context)) {
			googleApiClient.connect();
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
		lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
		if (lastLocation != null) {
			DebugMode.logD(context, lastLocation.toString());
			getLastLocation(lastLocation);
		}
	}

	private void getLastLocation (Location mLastLocation) {
		int zipCode = 0;

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

		RetroFitBase retroFitBase = new RetroFitBase(Constants.DEVELOPER_KEY.API_KEY, zipCode);
		retroFitBase.setRetrofitListener(retrofitListener = new RetroFitBase.RetrofitListener() {
			@Override
			public void onConditionsRetrieved (CurrentObservation currentObservation) {
				widgetProvider.updateMain(appWidgetManager, widgetID, currentObservation);
			}

			@Override
			public void onForecastDaysRetrieved (ForecastDay[] forecastDays) {
				widgetProvider.updateDays(context, appWidgetManager, widgetID, forecastDays, numOfDays);
			}

			@Override
			public void onHourlyRetrieved (HourlyForecast[] hourlyForecasts) {
				widgetProvider.updateHours(context, appWidgetManager, widgetID, hourlyForecasts, numOfDays);
			}
		});
		retroFitBase.getConditions();
		retroFitBase.getForecastDay();
		retroFitBase.getHourlyForecast();
	}

	@Override
	public void onConnectionSuspended (int i) {
	}

	@Override
	public void onConnectionFailed (@NonNull ConnectionResult connectionResult) {
		Log.d("onConnectionFailed: ", connectionResult.getErrorMessage());
	}
}
