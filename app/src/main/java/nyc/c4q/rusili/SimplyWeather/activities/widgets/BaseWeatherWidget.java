package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Date;

import nyc.c4q.rusili.SimplyWeather.R;
import nyc.c4q.rusili.SimplyWeather.network.WUndergroundAPI.JSON.CurrentObservation;
import nyc.c4q.rusili.SimplyWeather.network.WUndergroundAPI.JSON.ForecastDay;
import nyc.c4q.rusili.SimplyWeather.network.WUndergroundAPI.JSON.HourlyForecast;
import nyc.c4q.rusili.SimplyWeather.utilities.CalendarHelper;
import nyc.c4q.rusili.SimplyWeather.utilities.Constants;
import nyc.c4q.rusili.SimplyWeather.utilities.IconInflater;

public abstract class BaseWeatherWidget extends AppWidgetProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	public AppWidgetManager appWidgetManager;
	private CalendarHelper calendarHelper;

	public RemoteViews remoteViews;

	public int widgetID;
	private int numOfDays = Constants.NUM_OF_DAYS.WIDGET;

	public int getWidgetID(){
		return widgetID;
	}

	public void updateHours (Context context, int widgetID, HourlyForecast[] hourlyForecasts, int numOfDays) {
		int resID = 0;
		int nextHourOffset = 0;
		int hour = 1;

		calendarHelper = CalendarHelper.getInstance();
		if (calendarHelper.before30Minutes()){
			nextHourOffset = 1;
		}
		for (int i = 1; i < numOfDays; i++) {
			resID = context.getResources().getIdentifier("widget_component_hour_hour" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, calendarHelper.change24to12hour(hourlyForecasts[hour-nextHourOffset].getFCTTIME().getHour()));
			resID = context.getResources().getIdentifier("widget_component_hour_period" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, hourlyForecasts[hour-nextHourOffset].getFCTTIME().getAmpm());
			resID = context.getResources().getIdentifier("widget_component_hour_temp" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, hourlyForecasts[hour-nextHourOffset].getTemp().getEnglish() + Constants.SYMBOLS.DEGREE);
			resID = context.getResources().getIdentifier("widget_component_hour_icon" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setImageViewResource(resID, IconInflater.getInstance().choose(hourlyForecasts[hour-nextHourOffset].getIcon()));
			hour = hour + (i+1);
		}
		appWidgetManager.updateAppWidget(widgetID, remoteViews);
	}

	public void updateMain (int widgetID, CurrentObservation currentObservation) {
		Log.d("Logging: ", "updateMain");

		Date now = new Date();
		SimpleDateFormat weekday = new SimpleDateFormat("E");
		SimpleDateFormat month = new SimpleDateFormat("MM");
		SimpleDateFormat day = new SimpleDateFormat("dd");

		remoteViews.setTextViewText(R.id.widget_component_main_weekday_height2, weekday.format(now));
		remoteViews.setTextViewText(R.id.widget_component_main_day_height2, CalendarHelper.getInstance().ifSingleDigit(month.format(now)) + "/" + CalendarHelper.getInstance().ifSingleDigit(day.format(now)));
		remoteViews.setTextViewText(R.id.widget_component_main_currenttemp_height2, String.valueOf((int) currentObservation.getTemp_f()) + Constants.SYMBOLS.DEGREE);
		remoteViews.setTextViewText(R.id.widget_component_main_location_height2, currentObservation.getDisplay_location().getCity());
		remoteViews.setImageViewResource(R.id.widget_component_main_icon_height2,  IconInflater.getInstance().choose(currentObservation.getIcon()));

		appWidgetManager.updateAppWidget(widgetID, remoteViews);
	}

	public void updateDays (Context context, int widgetID, ForecastDay[] forecastDays, int numOfDays) {
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
			remoteViews.setImageViewResource(resID,  IconInflater.getInstance().choose(forecastDays[i].getIcon()));
		}

		appWidgetManager.updateAppWidget(widgetID, remoteViews);
	}

	@Override
	public void onConnectionSuspended (int i) {}

	@Override
	public void onConnectionFailed (@NonNull ConnectionResult connectionResult) {
		Log.d("onConnectionFailed: ", connectionResult.getErrorMessage());
	}
}
