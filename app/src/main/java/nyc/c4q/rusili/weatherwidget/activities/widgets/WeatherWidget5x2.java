package nyc.c4q.rusili.weatherwidget.activities.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import nyc.c4q.rusili.weatherwidget.R;
import nyc.c4q.rusili.weatherwidget.utilities.BaseWeatherWidget;
import nyc.c4q.rusili.weatherwidget.network.JSON.ForecastDay;
import nyc.c4q.rusili.weatherwidget.utilities.GlideWrapper;

public class WeatherWidget5x2 extends BaseWeatherWidget {
	private int numOfDays;
	public static final String ACTION_UPDATE_CLICK = "5x2_UPDATE_CLICK";

	@Override
	public void onUpdate (final Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		this.context = context;

		for (int widgetID : appWidgetIds) {
			this.widgetID = widgetID;
			remoteViews = new RemoteViews(context.getPackageName(),
				  R.layout.widget_layout_5x2);

			Intent intent = new Intent(context, WeatherWidget5x2.class);
			intent.setAction(ACTION_UPDATE_CLICK);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
			remoteViews.setOnClickPendingIntent(R.id.widget_layout_5x2_container, pendingIntent);

			glideWrapper = new GlideWrapper(context, remoteViews, widgetID);
			startGoogleAPIClient(context);
		}
	}

	@Override
	public void updateDays (Context context, AppWidgetManager appWidgetManager, int widgetID, ForecastDay[] forecastDays, int numOfDays) {
		numOfDays = 5;
		super.updateDays(context, appWidgetManager, widgetID, forecastDays, numOfDays);
	}
}