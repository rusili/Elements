package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.appwidget.AppWidgetProvider;

import nyc.c4q.rusili.SimplyWeather.network.GoogleAPI.GoogleLocationAPI;
import nyc.c4q.rusili.SimplyWeather.network.WUnderground.JSON.ResponseConditionsForecast10DayHourly;
import nyc.c4q.rusili.SimplyWeather.network.WUnderground.WundergroundRetrofit;

public abstract class BaseWeather extends AppWidgetProvider {
	private static GoogleLocationAPI googleLocationAPI;
	private static WundergroundRetrofit wundergroundRetrofit;

	private String getLocation(){
		googleLocationAPI = googleLocationAPI.getInstance();
		googleLocationAPI.getLocationZipCode();
	}

	public ResponseConditionsForecast10DayHourly getWeatherInformation(){
		wundergroundRetrofit = wundergroundRetrofit.getInstance();
	}

}
