package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import nyc.c4q.rusili.SimplyWeather.R;
import nyc.c4q.rusili.SimplyWeather.activities.configuration.ActivityConfiguration;
import nyc.c4q.rusili.SimplyWeather.network.WUnderground.JSON.CurrentObservation;
import nyc.c4q.rusili.SimplyWeather.network.WUnderground.JSON.ForecastDay;
import nyc.c4q.rusili.SimplyWeather.network.WUnderground.JSON.HourlyForecast;
import nyc.c4q.rusili.SimplyWeather.network.WUnderground.JSON.ResponseConditionsForecast10DayHourly;
import nyc.c4q.rusili.SimplyWeather.utilities.CalendarHelper;
import nyc.c4q.rusili.SimplyWeather.utilities.Constants;
import nyc.c4q.rusili.SimplyWeather.utilities.IconInflater;
import nyc.c4q.rusili.SimplyWeather.utilities.ScreenServiceAndReceiver;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;

public class Weather4x2View extends AppWidgetProvider implements BaseViewInterface{
	private WeatherPresenter weatherPresenter;
	private Context context;
	private RemoteViews remoteViews;
	private IconInflater iconInflater;
	private CalendarHelper calendarHelper;
	private AppWidgetManager appWidgetManager;
	private final int numOfDays = Constants.NUM_OF_DAYS.WIDGET;

	private boolean viewFlipperHours = false;
	private int widgetID;
	private int resID;
	private Date dateNow;

	@Override
	public void onEnabled (Context context) {
		super.onEnabled(context);

		weatherPresenter = new WeatherPresenter(this);
		remoteViews = new RemoteViews(context.getPackageName(),
			  R.layout.widget_layout_4x2);
		initialize(context);
	}

	@Override
	public void onUpdate (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		for (int widgetID : appWidgetIds) {
			this.widgetID = widgetID;
		}
	}

	@Override
	public void initialize (Context context) {
		this.context = context;
		iconInflater = IconInflater.getInstance();
		calendarHelper = CalendarHelper.getInstance();

		setOnClickListeners(context);
	}

	private void setOnClickListeners (Context context) {
		setOnClickUpdate(context);
		setOnClickConfig(context);
		setViewFlipper(context);
	}

	private void setOnClickUpdate(Context context){
		Intent intent = new Intent(context, Weather4x2View.class);
		intent.setAction(Constants.ACTION.UPDATE_CLICK);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget_layout_4x2_container, pendingIntent);
	}

	private void setViewFlipper (Context context){
		Intent intent = new Intent(context, Weather4x2View.class);
		intent.setAction(Constants.ACTION.VIEWFLIPPER_CLICK);
		intent.putExtra("isOpen", viewFlipperHours);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget_layout_4x2_include_main2, pendingIntent);
	}

	public void setOnClickConfig (Context context) {
		Intent intent = new Intent(context, Weather4x2View.class);
		intent.putExtra(EXTRA_APPWIDGET_ID, widgetID);
		intent.setAction(Constants.ACTION.CONFIG_CLICK);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget_layout_4x2_container, pendingIntent);
	}

	@Override
	public void updateWidgetViews (ResponseConditionsForecast10DayHourly jsonObject) {
		CurrentObservation currentObservation = jsonObject.getCurrent_observation();
		ForecastDay[] forecastDayArray = jsonObject.getForecast().getSimpleforecast().getForecastday();
		HourlyForecast[] hourlyForecastArray = jsonObject.getHourly_Forecast();

		updateWidgetMain(currentObservation);
		updateWidgetDays(forecastDayArray);
		updateWidgetHours(hourlyForecastArray);

		appWidgetManager.updateAppWidget(widgetID, remoteViews);
	}

	private void updateWidgetHours (HourlyForecast[] hourlyForecastArray) {
		int nextHourOffset = 0;
		int hour = 1;

		if (calendarHelper.before30Minutes()){
			nextHourOffset = 1;
		}

		for (int i = 1; i < numOfDays; i++) {
			resID = context.getResources().getIdentifier("widget_component_hour_hour" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, calendarHelper.change24to12hour(hourlyForecastArray[hour-nextHourOffset].getFCTTIME().getHour()));
			resID = context.getResources().getIdentifier("widget_component_hour_period" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, hourlyForecastArray[hour-nextHourOffset].getFCTTIME().getAmpm());
			resID = context.getResources().getIdentifier("widget_component_hour_temp" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, hourlyForecastArray[hour-nextHourOffset].getTemp().getEnglish() + Constants.SYMBOLS.DEGREE);
			resID = context.getResources().getIdentifier("widget_component_hour_icon" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setImageViewResource(resID, iconInflater.choose(hourlyForecastArray[hour-nextHourOffset].getIcon()));
			hour = hour + (i+1);
		}
	}

	private void updateWidgetDays (ForecastDay[] forecastDayArray) {
		remoteViews.setTextViewText(R.id.widget_component_main_hitemp_height2, String.valueOf(forecastDayArray[0].getHigh().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
		remoteViews.setTextViewText(R.id.widget_component_main_lowtemp_height2, String.valueOf(forecastDayArray[0].getLow().getFahrenheit()) + Constants.SYMBOLS.DEGREE);

		for (int i = 1; i < numOfDays; i++) {
			resID = context.getResources().getIdentifier("widget_component_day_weekday" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, calendarHelper.getTwoCharWeekday(forecastDayArray[i].getDate().getWeekdayShort()));
			resID = context.getResources().getIdentifier("widget_component_day_day" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, forecastDayArray[i].getDate().getDay());
			resID = context.getResources().getIdentifier("widget_component_day_temphigh" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, String.valueOf(forecastDayArray[i].getHigh().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
			resID = context.getResources().getIdentifier("widget_component_day_templow" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, String.valueOf(forecastDayArray[i].getLow().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
			resID = context.getResources().getIdentifier("widget_component_day_icon" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setImageViewResource(resID, iconInflater.choose(forecastDayArray[i].getIcon()));
		}

		appWidgetManager.updateAppWidget(widgetID, remoteViews);
	}

	private void updateWidgetMain (CurrentObservation currentObservation) {
		dateNow = new Date();
		SimpleDateFormat weekday = new SimpleDateFormat("E");
		SimpleDateFormat month = new SimpleDateFormat("MM");
		SimpleDateFormat day = new SimpleDateFormat("dd");

		remoteViews.setTextViewText(R.id.widget_component_main_weekday_height2, weekday.format(dateNow));
		remoteViews.setTextViewText(R.id.widget_component_main_day_height2, calendarHelper.ifSingleDigit(month.format(dateNow)) + "/" + calendarHelper.ifSingleDigit(day.format(dateNow)));
		remoteViews.setTextViewText(R.id.widget_component_main_currenttemp_height2, String.valueOf((int) currentObservation.getTemp_f()) + Constants.SYMBOLS.DEGREE);
		remoteViews.setTextViewText(R.id.widget_component_main_location_height2, currentObservation.getDisplay_location().getCity());
		remoteViews.setImageViewResource(R.id.widget_component_main_icon_height2, iconInflater.choose(currentObservation.getIcon()));

		appWidgetManager.updateAppWidget(widgetID, remoteViews);
	}

	@Override
	public void onReceive (Context context, Intent intent) {
		super.onReceive(context, intent);

		appWidgetManager = AppWidgetManager.getInstance(context);
		ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(), getClass().getName());
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
		RemoteViews rootView = new RemoteViews(context.getPackageName(), R.layout.widget_layout_4x2);

		checkIfServiceIsRunning(context);

		if (intent.getAction().equals(Constants.ACTION.UPDATE_SCREEN)) {
			startUpdateWidget();

		} else if (intent.getAction().equals(Constants.ACTION.UPDATE_CLICK)) {
			Toast.makeText(context, "SimplyWeather updated!", Toast.LENGTH_SHORT).show();
			startUpdateWidget();

		} else if (intent.getAction().equals(Constants.ACTION.CONFIG_CLICK)) {
			Intent intentConfig = new Intent(context, ActivityConfiguration.class);
			Bundle bundle = new Bundle();
			bundle.putString("Data", getClass().getName() + "/" + widgetID);
			intentConfig.putExtras(bundle);
			intentConfig.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intentConfig);

		} else if (intent.getAction().equals(Constants.ACTION.VIEWFLIPPER_CLICK)) {
			if (intent.getBooleanExtra("isOpen", false) == false) {
				rootView.showPrevious(R.id.widget_layout_4x2_viewflipper);
			} else if (intent.getBooleanExtra("isOpen", false) == true){
				rootView.showPrevious(R.id.widget_layout_4x2_viewflipper);
			}
			appWidgetManager.updateAppWidget(appWidgetIds, rootView);
		}

	}

	private void startUpdateWidget () {
		weatherPresenter.getGoogleAPILocation();
	}

	private void checkIfServiceIsRunning (Context context) {
		weatherPresenter.isMyServiceRunning(context, ScreenServiceAndReceiver.class);
	}

	public Context getContext () {
		return this.context;
	}

	@Override
	public void onDisabled (Context context) {
		super.onDisabled(context);
		killService(context);
	}

	private void killService (Context context) {
		context.stopService(new Intent(context, ScreenServiceAndReceiver.class));
	}
}
