package nyc.c4q.rusili.weatherwidget.activities.widgets;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import nyc.c4q.rusili.weatherwidget.R;
import nyc.c4q.rusili.weatherwidget.activities.configuration.ActivityConfiguration;
import nyc.c4q.rusili.weatherwidget.network.JSON.ForecastDay;
import nyc.c4q.rusili.weatherwidget.utilities.BaseWeatherWidget;
import nyc.c4q.rusili.weatherwidget.utilities.IconInflater;
import nyc.c4q.rusili.weatherwidget.utilities.ScreenMoniterService;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;

public class WeatherWidget4x2 extends BaseWeatherWidget implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	private int numOfDays;
	public static final String ACTION_UPDATE_CLICK = "4x2_UPDATE_CLICK";
	public static final String ACTION_CONFIG_CLICK = "4x2_CONFIG_CLICK";

	@Override
	public void onUpdate (final Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int widgetID : appWidgetIds) {
			this.widgetID = widgetID;
			remoteViews = new RemoteViews(context.getPackageName(),
				  R.layout.widget_layout_4x2);

			setOnClickUpdate(context);
			//setOnClickConfig(context, widgetID);

			iconInflater = new IconInflater();
			startGoogleAPIClient(context);
		}
	}

	private void setOnClickConfig (Context context, int widgetID) {
		Intent intent = new Intent(context, WeatherWidget4x2.class);
		intent.putExtra(EXTRA_APPWIDGET_ID, widgetID);
		intent.setAction(ACTION_CONFIG_CLICK);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget_layout_4x2_container, pendingIntent);
	}

	public void setOnClickUpdate (Context context) {
		Intent intent = new Intent(context, WeatherWidget4x2.class);
		intent.setAction(ACTION_UPDATE_CLICK);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget_layout_4x2_container, pendingIntent);
	}

	@Override
	public void updateDays (Context context, AppWidgetManager appWidgetManager, int widgetID, ForecastDay[] forecastDays, int numOfDays) {
		numOfDays = 4;
		super.updateDays(context, appWidgetManager, widgetID, forecastDays, numOfDays);
	}

	@Override
	public void onReceive (Context context, Intent intent) {
		super.onReceive(context, intent);

		if (intent.getAction().equals("4x2_UPDATE_CLICK")) {
			Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();

			if (!isMyServiceRunning(context, ScreenMoniterService.class)){
				context.startService(new Intent(context, ScreenMoniterService.class));
			}

			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(), getClass().getName());
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
			onUpdate(context, appWidgetManager, appWidgetIds);
		} else if (intent.getAction().equals("4x2_CONFIG_CLICK")) {
			Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();

			Intent intentConfig = new Intent(context, ActivityConfiguration.class);
			Bundle bundle = new Bundle();
			bundle.putString("Data", getClass().getName() + "/" + widgetID);
			intentConfig.putExtras(bundle);
			intentConfig.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intentConfig);
		}
	}

	private boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
