package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import nyc.c4q.rusili.SimplyWeather.R;
import nyc.c4q.rusili.SimplyWeather.activities.configuration.ConfigurationActivity;
import nyc.c4q.rusili.SimplyWeather.dagger.AppDagger;
import nyc.c4q.rusili.SimplyWeather.database.SQLiteDatabaseHandler;
import nyc.c4q.rusili.SimplyWeather.database.model.DBColor;
import nyc.c4q.rusili.SimplyWeather.network.JSON.CurrentObservation;
import nyc.c4q.rusili.SimplyWeather.network.JSON.ForecastDay;
import nyc.c4q.rusili.SimplyWeather.network.JSON.HourlyForecast;
import nyc.c4q.rusili.SimplyWeather.utilities.app.CalendarHelper;
import nyc.c4q.rusili.SimplyWeather.utilities.app.IconInflater;
import nyc.c4q.rusili.SimplyWeather.utilities.app.ScreenServiceAndReceiver;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.Constants;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.DebugMode;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.ShowToast;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;

public class WeatherWidget4x2 extends AppWidgetProvider implements WidgetInterface.WidgetProvider {
	@Inject
	WeatherPresenter weatherPresenter;

	private boolean isViewFlipperOpen = false;
	private boolean changeColor = false;
	private boolean firstRun = true;

	public RemoteViews remoteViews;

	private List <DBColor> dbColorList = new ArrayList <>();

	@Override
	public void onUpdate (final Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int widgetID : appWidgetIds) {
			if (firstRun) {
				initialize(context, widgetID);
			}

			DebugMode.logD(context, "onUpdate");
			loadFromDatabase(context);

			if (changeColor) {
				applyColors(appWidgetManager, widgetID, remoteViews);
			} else {
				weatherPresenter.startGoogleAPIClient(context, widgetID, remoteViews);
			}
		}
	}

	private void initialize (Context context, int widgetID) {
		bind(context);
		setViews(context);
		setOnClickListeners(context, widgetID);
		createService(context);
		firstRun = false;
	}

	private void bind (Context context) {
		((AppDagger) context.getApplicationContext()).getAppComponent().inject(this);
		weatherPresenter.bindView(this);
	}

	private void setViews (Context context) {
		remoteViews = new RemoteViews(context.getPackageName(),
			  R.layout.widget_layout_4x2);
	}

	private void setOnClickListeners (Context context, int widgetID) {
		//setOnClickUpdate(context);
		setOnClickConfig(context, widgetID);
		setViewFlipper(context);
	}

	private void createService (Context context) {
		context.startService(new Intent(context, ScreenServiceAndReceiver.class));
	}

	private void applyColors (AppWidgetManager appWidgetManager, int widgetID, RemoteViews remoteViews) {
		applyColorMain(appWidgetManager, widgetID);
		applyColorDays();
		applyColorHours();

		appWidgetManager.updateAppWidget(widgetID, remoteViews);
		changeColor = false;
	}

	private void applyColorMain (AppWidgetManager appWidgetManager, int widgetID) {
		remoteViews.setTextColor(R.id.widget_component_main_weekday_height2, dbColorList.get(1).getColor());
		remoteViews.setTextColor(R.id.widget_component_main_day_height2, dbColorList.get(2).getColor());
		remoteViews.setTextColor(R.id.widget_component_main_currenttemp_height2, dbColorList.get(3).getColor());
		appWidgetManager.updateAppWidget(widgetID, remoteViews);
	}

	private void applyColorDays () {

	}

	private void applyColorHours () {

	}

	private void loadFromDatabase (Context context) {
		dbColorList = SQLiteDatabaseHandler
			  .getSqLiteDatabaseHandler(context)
			  .getListOfColors();
		DebugMode.logD(context, "loadFromDatabase: " + dbColorList.get(0).getColor() + ", " + dbColorList.get(1).getColor() + ", " + dbColorList.get(2).getColor() + ", " + dbColorList.get(3).getColor() + ", " + dbColorList.get(4).getColor() + ", " + dbColorList.get(5).getColor() + ", ");
	}

	public void updateHours (Context context, AppWidgetManager appWidgetManager, int widgetID, HourlyForecast[] hourlyForecasts, int numOfDays) {
		int resID = 0;
		int nextHourOffset = 0;
		int hour = 1;

		if (CalendarHelper.getInstance().before30Minutes()) {
			nextHourOffset = 1;
		}
		for (int i = 1; i < numOfDays; i++) {
			resID = context.getResources().getIdentifier("widget_component_hour_hour" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, CalendarHelper.getInstance().change24to12hour(hourlyForecasts[hour - nextHourOffset].getFCTTIME().getHour()));
			remoteViews.setTextColor(resID, dbColorList.get(1).getColor());

			resID = context.getResources().getIdentifier("widget_component_hour_period" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, hourlyForecasts[hour - nextHourOffset].getFCTTIME().getAmpm());
			remoteViews.setTextColor(resID, dbColorList.get(2).getColor());

			resID = context.getResources().getIdentifier("widget_component_hour_temp" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, hourlyForecasts[hour - nextHourOffset].getTemp().getEnglish() + Constants.SYMBOLS.DEGREE);
			remoteViews.setTextColor(resID, dbColorList.get(4).getColor());

			resID = context.getResources().getIdentifier("widget_component_hour_icon" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setImageViewResource(resID, IconInflater.getInstance().choose(hourlyForecasts[hour - nextHourOffset].getIcon()));
			hour = hour + (i + 1);
		}
		appWidgetManager.updateAppWidget(widgetID, remoteViews);
	}

	public void updateMain (AppWidgetManager appWidgetManager, int widgetID, CurrentObservation currentObservation) {
		Date now = new Date();
		SimpleDateFormat weekday = new SimpleDateFormat("E");
		SimpleDateFormat month = new SimpleDateFormat("MM");
		SimpleDateFormat day = new SimpleDateFormat("dd");

		remoteViews.setTextViewText(R.id.widget_component_main_weekday_height2, weekday.format(now));
		remoteViews.setTextColor(R.id.widget_component_main_weekday_height2, dbColorList.get(1).getColor());

		remoteViews.setTextViewText(R.id.widget_component_main_day_height2, CalendarHelper.getInstance().ifSingleDigit(month.format(now)) + "/" + CalendarHelper.getInstance().ifSingleDigit(day.format(now)));
		remoteViews.setTextColor(R.id.widget_component_main_day_height2, dbColorList.get(2).getColor());

		remoteViews.setTextViewText(R.id.widget_component_main_currenttemp_height2, String.valueOf((int) currentObservation.getTemp_f()) + Constants.SYMBOLS.DEGREE);
		remoteViews.setTextColor(R.id.widget_component_main_currenttemp_height2, dbColorList.get(3).getColor());

		remoteViews.setTextViewText(R.id.widget_component_main_location_height2, currentObservation.getDisplay_location().getCity());

		remoteViews.setImageViewResource(R.id.widget_component_main_icon_height2, IconInflater.getInstance().choose(currentObservation.getIcon()));

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
			remoteViews.setTextColor(resID, dbColorList.get(1).getColor());

			resID = context.getResources().getIdentifier("widget_component_day_day" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, forecastDays[i].getDate().getDay());
			remoteViews.setTextColor(resID, dbColorList.get(2).getColor());

			resID = context.getResources().getIdentifier("widget_component_day_temphigh" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, String.valueOf(forecastDays[i].getHigh().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
			remoteViews.setTextColor(resID, dbColorList.get(4).getColor());

			resID = context.getResources().getIdentifier("widget_component_day_templow" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setTextViewText(resID, String.valueOf(forecastDays[i].getLow().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
			remoteViews.setTextColor(resID, dbColorList.get(5).getColor());

			resID = context.getResources().getIdentifier("widget_component_day_icon" + String.valueOf(i + 1), "id", context.getPackageName());
			remoteViews.setImageViewResource(resID, IconInflater.getInstance().choose(forecastDays[i].getIcon()));
		}

		appWidgetManager.updateAppWidget(widgetID, remoteViews);
	}

	private void setOnClickConfig (Context context, int widgetID) {
		Intent intent = new Intent(context, WeatherWidget4x2.class);
		intent.putExtra(EXTRA_APPWIDGET_ID, widgetID);
		intent.setAction(Constants.ACTION.CONFIG_CLICK);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget_layout_4x2_container, pendingIntent);
	}

	private void setViewFlipper (Context context) {
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
			createService(context);
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

		} else if (intent.getAction().equals(Constants.ACTION.CONFIG_CLICK)) {
			DebugMode.logD(context, "onReceive " + "CONFIG_CLICK" + String.valueOf(AppWidgetManager.EXTRA_APPWIDGET_ID) + " " + appWidgetIds[0]);

			Intent intentConfig = new Intent(context, ConfigurationActivity.class);
			intentConfig.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);    //set widget id
			intentConfig.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intentConfig);

		} else if (intent.getAction().equals(Constants.ACTION.CONFIG_COMPLETE)) {
			DebugMode.logD(context, "onReceive " + "CONFIG_COMPLETE");
			changeColor = true;
			onUpdate(context, appWidgetManager, appWidgetIds);

		} else if (intent.getAction().equals(Constants.ACTION.VIEWFLIPPER_CLICK)) {
			if (intent.getBooleanExtra("isOpen", false) == false) {
				root.showPrevious(R.id.widget_layout_4x2_viewflipper);
			} else if (intent.getBooleanExtra("isOpen", false) == true) {
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
		weatherPresenter.unBind();
		weatherPresenter = null;
		killService(context);
	}

	private void killService (Context context) {
		context.stopService(new Intent(context, ScreenServiceAndReceiver.class));
	}
}
