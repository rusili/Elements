package nyc.c4q.rusili.weatherwidget.activities.widgets;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
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

import nyc.c4q.rusili.weatherwidget.R;
import nyc.c4q.rusili.weatherwidget.activities.BaseWeatherWidget;
import nyc.c4q.rusili.weatherwidget.network.JSON.CurrentObservation;
import nyc.c4q.rusili.weatherwidget.network.JSON.ForecastDay;
import nyc.c4q.rusili.weatherwidget.network.RetroFitBase;
import nyc.c4q.rusili.weatherwidget.utilities.Constants;
import nyc.c4q.rusili.weatherwidget.utilities.GlideWrapper;

public class WeatherWidget4x2 extends BaseWeatherWidget implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 5;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6;
    private GoogleApiClient mGoogleApiClient;
    private Context context;
    private RetroFitBase retroFitBase;
    private RemoteViews remoteViews;
    private Location mLastLocation;
    private boolean locationPermissionGranted;
    private int zipCode = 0;
    private GlideWrapper glideWrapper;
    private AppWidgetManager appWidgetManager;
    private int[] appWidgetIds;
    private int widgetID;
    private static final String ACTION_UPDATE_CLICK = "4x2_UPDATE_CLICK";

    @Override
    public void onUpdate (final Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.context = context;
        this.appWidgetManager = appWidgetManager;
        this.appWidgetIds = appWidgetIds;

        for (int widgetID : appWidgetIds) {
            this.widgetID = widgetID;
            remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout_4x2);

            Intent intent = new Intent(context, WeatherWidget4x2.class);
            intent.setAction(ACTION_UPDATE_CLICK);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget_layout_4x2_container, pendingIntent);

            glideWrapper = new GlideWrapper(context, remoteViews, widgetID);
            startGoogleAPIClient(context);
            //setUpUpdateOnClick(context, appWidgetManager, appWidgetIds, widgetID);
        }
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
                updateDays(context, appWidgetManager, widgetID, forecastDays);
            }
        });
        retroFitBase.getConditions();
        retroFitBase.getForecastDay();
    }

    private void updateDays (Context context, AppWidgetManager appWidgetManager, int widgetID, ForecastDay[] forecastDays) {
        int resID = 0;
        remoteViews.setTextViewText(R.id.widget_component_main_hitemp, String.valueOf(forecastDays[0].getHigh().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
        remoteViews.setTextViewText(R.id.widget_component_main_lowtemp, String.valueOf(forecastDays[0].getLow().getFahrenheit()) + Constants.SYMBOLS.DEGREE);

        for(int i=1 ; i<4 ; i++) {
            resID = context.getResources().getIdentifier("widget_component_day_weekday" + String.valueOf(i+1), "id", context.getPackageName());
            remoteViews.setTextViewText(resID, getTwoCharWeekday(forecastDays[i].getDate().getWeekdayShort()));
            resID = context.getResources().getIdentifier("widget_component_day_day" + String.valueOf(i+1), "id", context.getPackageName());
            remoteViews.setTextViewText(resID, forecastDays[i].getDate().getDay());
            resID = context.getResources().getIdentifier("widget_component_day_temphigh" + String.valueOf(i+1), "id", context.getPackageName());
            remoteViews.setTextViewText(resID, String.valueOf(forecastDays[i].getHigh().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
            resID = context.getResources().getIdentifier("widget_component_day_templow" + String.valueOf(i+1), "id", context.getPackageName());
            remoteViews.setTextViewText(resID, String.valueOf(forecastDays[i].getLow().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
            resID = context.getResources().getIdentifier("widget_component_day_icon" + String.valueOf(i+1), "id", context.getPackageName());
            glideWrapper.inflateImage(resID, forecastDays[i].getIcon_url());
        }

        appWidgetManager.updateAppWidget(widgetID, remoteViews);
    }

    private void updateMain (AppWidgetManager appWidgetManager, int widgetID, CurrentObservation currentObservation) {
        Date now = new Date();
        SimpleDateFormat weekday = new SimpleDateFormat("E");
        SimpleDateFormat month = new SimpleDateFormat("MM");
        SimpleDateFormat day = new SimpleDateFormat("dd");

        remoteViews.setTextViewText(R.id.widget_component_main_weekday, weekday.format(now));
        remoteViews.setTextViewText(R.id.widget_component_main_day, ifSingleDigit(month.format(now)) + "/" + ifSingleDigit(day.format(now)));
        remoteViews.setTextViewText(R.id.widget_component_main_currenttemp, String.valueOf((int) currentObservation.getTemp_f()) + Constants.SYMBOLS.DEGREE);
        remoteViews.setTextViewText(R.id.widget_component_main_location, currentObservation.getDisplay_location().getCity());
        glideWrapper.inflateImage(R.id.widget_component_main_icon, currentObservation.getIcon_url());

        appWidgetManager.updateAppWidget(widgetID, remoteViews);
    }

    private CharSequence ifSingleDigit (String format) {
        CharSequence charSequence = null;

        if (Integer.parseInt(format) < 10){
            charSequence = String.valueOf(format.charAt(1));
        }
        return charSequence;
    }

    private CharSequence getTwoCharWeekday (String weekdayShort) {
        CharSequence charSequence = null;

        if (weekdayShort.contains("T") || weekdayShort.contains("S")){
            charSequence = weekdayShort.substring(0,2);
        } else {
            charSequence = String.valueOf(weekdayShort.charAt(0));
        }
        return charSequence;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(ACTION_UPDATE_CLICK)){
            Log.d(String.valueOf(getClass()), "Clicked!");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance
                    (context);

            ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(),getClass().getName());

            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    thisAppWidgetComponentName);

            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    private void startGoogleAPIClient (Context context){
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
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
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
        downloadWeatherData(context, appWidgetManager, widgetID, remoteViews, zipCode);
        context = null;
    }

    @Override
    public void onConnectionSuspended (int i) {}
    @Override
    public void onConnectionFailed (@NonNull ConnectionResult connectionResult) {}
}
