package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import nyc.c4q.rusili.SimplyWeather.R;
import nyc.c4q.rusili.SimplyWeather.activities.configuration.ActivityConfiguration;
import nyc.c4q.rusili.SimplyWeather.utilities.Constants;
import nyc.c4q.rusili.SimplyWeather.utilities.ScreenServiceAndReceiver;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;

public class WeatherWidget4x2 extends BaseWeatherWidget {
	private static WeatherPresenter weatherPresenter;
	private boolean isViewFlipperOpen = false;

	@Override
	public void onUpdate (final Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int widgetID : appWidgetIds) {
			this.widgetID = widgetID;
			remoteViews = new RemoteViews(context.getPackageName(),
				  R.layout.widget_layout_4x2);
			this.appWidgetManager = appWidgetManager;

			//setOnClickUpdate(context);
			setOnClickConfig(context, widgetID);
			setViewFlipper(context);

			weatherPresenter = WeatherPresenter.getInstance();
			weatherPresenter.initialize(this);
			weatherPresenter.startNetworkCalls(context);
		}
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
		Log.d("Logging: ", "onReceived");

		super.onReceive(context, intent);

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(), getClass().getName());
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
		RemoteViews root = new RemoteViews(context.getPackageName(), R.layout.widget_layout_4x2);

		WeatherPresenter.getInstance().isMyServiceRunning(context, ScreenServiceAndReceiver.class);

		if (intent.getAction().equals(Constants.ACTION.UPDATE_SCREEN)) {
			onUpdate(context, appWidgetManager, appWidgetIds);

		} else if (intent.getAction().equals(Constants.ACTION.UPDATE_CLICK)) {
			Toast.makeText(context, "SimplyWeather updated!", Toast.LENGTH_SHORT).show();
			onUpdate(context, appWidgetManager, appWidgetIds);

		} else if (intent.getAction().equals(Constants.ACTION.CONFIG_CLICK)) {
				Log.d(String.valueOf(getClass()), "Clicked Config!");
				Intent intentConfig = new Intent(context, ActivityConfiguration.class);
				Bundle bundle = new Bundle();
				bundle.putString("Data", getClass().getName() + "/" + widgetID);
				intentConfig.putExtras(bundle);
				intentConfig.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intentConfig);

		} else if (intent.getAction().equals(Constants.ACTION.VIEWFLIPPER_CLICK)) {
			if (intent.getBooleanExtra("isOpen", false) == false) {
				root.showPrevious(R.id.widget_layout_4x2_viewflipper);
			} else if (intent.getBooleanExtra("isOpen", false) == true){
				root.showPrevious(R.id.widget_layout_4x2_viewflipper);
			}
			appWidgetManager.updateAppWidget(appWidgetIds, root);
		}
	}

	@Override
	public void onDisabled (Context context) {
		super.onDisabled(context);
		killService(context);
	}

	private void killService (Context context) {
		Log.d("Logging: ", "killService");

		context.stopService(new Intent(context, ScreenServiceAndReceiver.class));
	}

	@Override
	public void onConnected (@Nullable Bundle bundle) {}
}
