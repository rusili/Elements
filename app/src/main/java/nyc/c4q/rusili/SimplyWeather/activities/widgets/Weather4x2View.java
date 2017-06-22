package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.appwidget.AppWidgetProvider;
import android.content.Context;

public class Weather4x2View extends AppWidgetProvider implements BaseViewInterface{
	private WeatherPresenter weatherPresenter;
	private Context context;

	@Override
	public void onEnabled (Context context) {
		super.onEnabled(context);
		weatherPresenter = new WeatherPresenter(this);
		initialize(context);
	}

	@Override
	public void setPresenter (Object presenter) {}

	@Override
	public void initialize (Context context) {
		this.context = context;
	}

	@Override
	public void setViews () {

	}

	@Override
	public void updateWidget () {

	}

	public Context getContext () {
		return context;
	}
}
