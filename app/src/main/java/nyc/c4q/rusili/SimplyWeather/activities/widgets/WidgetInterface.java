package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;

import nyc.c4q.rusili.SimplyWeather.network.JSON.CurrentObservation;
import nyc.c4q.rusili.SimplyWeather.network.JSON.ForecastDay;
import nyc.c4q.rusili.SimplyWeather.network.JSON.HourlyForecast;

public interface WidgetInterface {

	public interface WidgetProvider {
		void updateMain (AppWidgetManager appWidgetManager, int widgetID, CurrentObservation currentObservation);

		void updateHours (Context context, AppWidgetManager appWidgetManager, int widgetID, HourlyForecast[] hourlyForecasts, int numOfDays);

		void updateDays (Context context, AppWidgetManager appWidgetManager, int widgetID, ForecastDay[] forecastDays, int numOfDays);
	}

	public interface WidgetPresenter {
		void bindView (WidgetInterface.WidgetProvider widgetProvider);

		void unBind ();
	}
}
