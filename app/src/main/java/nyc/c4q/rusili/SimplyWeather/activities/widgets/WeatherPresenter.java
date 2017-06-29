package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.app.ActivityManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import nyc.c4q.rusili.SimplyWeather.network.GoogleAPI.GoogleLocationAPI;
import nyc.c4q.rusili.SimplyWeather.network.WUndergroundAPI.JSON.ResponseConditionsForecast10DayHourly;
import nyc.c4q.rusili.SimplyWeather.network.WUndergroundAPI.WUndergroundRetrofit;
import nyc.c4q.rusili.SimplyWeather.utilities.Constants;
import nyc.c4q.rusili.SimplyWeather.utilities.ScreenServiceAndReceiver;

public class WeatherPresenter implements BasePresenterInterface {
	private static WeatherPresenter weatherPresenter;
	private WUndergroundRetrofit.RetrofitListener retrofitListener;
	private ActivityManager activityManager;

	private static GoogleLocationAPI googleLocationAPI;
	private static WUndergroundRetrofit wundergroundRetrofit;
	private static BaseWeatherWidget baseWeatherWidget;

	private final String apiKey = Constants.DEVELOPER_KEY.API_KEY;

	private WeatherPresenter(){}

	public static WeatherPresenter getInstance(){
		if (weatherPresenter == null){
			Log.d("Logging: ", "newWeatherPresenter");
			weatherPresenter = new WeatherPresenter();
		}
		Log.d("Logging: ", "getWeatherPresenter");
		return weatherPresenter;
	}

	public void initialize (AppWidgetProvider appWidgetProvider){
		this.baseWeatherWidget = (BaseWeatherWidget) appWidgetProvider;
	}

	@Override
	public void getGoogleAPILocation (final Context context) {
		Log.d("Logging: ", "getGoogleAPILocation");

		googleLocationAPI = googleLocationAPI.getInstance();
		googleLocationAPI.setRetrofitListener(new GoogleLocationAPI.GoogleLocationAPILIstener() {
			@Override
			public void onConnection (int zipCode) {
				getWUndergroundAPIResponse(context, zipCode);
			}
		});
		googleLocationAPI.getZipCode(context);
	}

	@Override
	public void getWUndergroundAPIResponse (final Context context, int zipCode) {
		Log.d("Logging: ", "getWUndergroundResponse");

		wundergroundRetrofit = wundergroundRetrofit.getInstance();
		wundergroundRetrofit.setRetrofitListener(retrofitListener = new WUndergroundRetrofit.RetrofitListener() {
			@Override
			public void onConditionsForecast10DayHourlytRetrieved (ResponseConditionsForecast10DayHourly jsonObject) {
				baseWeatherWidget.updateMain(baseWeatherWidget.getWidgetID(), jsonObject.getCurrent_observation());
				baseWeatherWidget.updateDays(context, baseWeatherWidget.getWidgetID(), jsonObject.getForecast().getSimpleforecast().getForecastday(), Constants.NUM_OF_DAYS.WIDGET);
				baseWeatherWidget.updateHours(context, baseWeatherWidget.getWidgetID(), jsonObject.getHourly_Forecast(), Constants.NUM_OF_DAYS.WIDGET);
			}
		});
		wundergroundRetrofit.getConditionsForecast10DayHourlyForecast(apiKey, zipCode);
	}

	public void isMyServiceRunning (Context context, Class <?> serviceClass) {
		activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
			if (!serviceClass.getName().equals(service.service.getClassName())) {
				context.startService(new Intent(context, ScreenServiceAndReceiver.class));
			}
		}
	}
}
