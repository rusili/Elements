package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import nyc.c4q.rusili.SimplyWeather.R;
import nyc.c4q.rusili.SimplyWeather.network.WUnderground.JSON.ResponseConditionsForecast10DayHourly;
import nyc.c4q.rusili.SimplyWeather.utilities.Constants;
import nyc.c4q.rusili.SimplyWeather.utilities.IconInflater;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;

public class Weather4x2View extends AppWidgetProvider implements BaseViewInterface{
	private WeatherPresenter weatherPresenter;
	private Context context;
	private RemoteViews remoteViews;
	private IconInflater iconInflater;

	private boolean viewFlipperHours = false;
	private int widgetID;

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
		iconInflater = new IconInflater();

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
	public void updateWidget (ResponseConditionsForecast10DayHourly jsonObject) {
		//Need code here
	}

	public Context getContext () {
		return context;
	}
}
