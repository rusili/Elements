//package nyc.c4q.rusili.SimplyWeather.activities.widgets;
//
//import android.app.ActivityManager;
//import android.appwidget.AppWidgetProvider;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.support.v4.content.ContextCompat;
//import android.util.Log;
//import android.widget.Toast;
//
//import nyc.c4q.rusili.SimplyWeather.network.JSON.ResponseConditionsForecast10DayHourly;
//import nyc.c4q.rusili.SimplyWeather.network.googleAPI.GoogleLocationAPI;
//import nyc.c4q.rusili.SimplyWeather.network.wundergroundAPI.WUndergroundRetrofit;
//import nyc.c4q.rusili.SimplyWeather.utilities.generic.Constants;
//
//
//public class WeatherPresenter implements WidgetInterface.WidgetPresenter{
//	public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6;
//
//	private static WeatherPresenter weatherPresenter;
//	private WUndergroundRetrofit.RetrofitListener retrofitListener;
//	private ActivityManager activityManager;
//
//	private static GoogleLocationAPI googleLocationAPI;
//	private static WUndergroundRetrofit wundergroundRetrofit;
//	private BaseWeatherWidget baseWeatherWidget;
//
//	private final String apiKey = Constants.DEVELOPER_KEY.API_KEY;
//
//	private WeatherPresenter(){}
//
//	public static WeatherPresenter getInstance(){
//		if (weatherPresenter == null){
//			Log.d("Logging: ", "newWeatherPresenter");
//			weatherPresenter = new WeatherPresenter();
//		}
//		Log.d("Logging: ", "getWeatherPresenter");
//		return weatherPresenter;
//	}
//
//	public void initialize (AppWidgetProvider appWidgetProvider){
//		this.baseWeatherWidget = (BaseWeatherWidget) appWidgetProvider;
//	}
//
//	public void startNetworkCalls (Context context) {
//		Log.d("Logging: ", "startNetworkCalls");
//
//		if (isNetworkConnected(context) && checkPermissions(context)) {
//			downloadWeatherData(context);
//		} else {
//			Toast.makeText(context, "No network detected", Toast.LENGTH_SHORT).show();
//		}
//	}
//
//	private boolean checkPermissions (Context context) {
//		if (ContextCompat.checkSelfPermission(context,
//			  android.Manifest.permission.ACCESS_FINE_LOCATION)
//			  == PackageManager.PERMISSION_GRANTED) {
//			return true;
//		} else {
//			Toast.makeText(context, "You haven't granted location access to SimplyWeather", Toast.LENGTH_SHORT).show();
//			return false;
//		}
//	}
//
//	private boolean isNetworkConnected (Context context) {
//		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//		boolean isConnected = activeNetwork != null &&
//			  activeNetwork.isConnectedOrConnecting();
//
//		return isConnected;
//	}
//
//	private void downloadWeatherData (Context context) {
//		Log.d("Logging: ", "downloadWeatherData");
//
//		getGoogleAPILocation(context);
//	}
//
//	@Override
//	public void getGoogleAPILocation (final Context context) {
//		Log.d("Logging: ", "getGoogleAPILocation");
//
//		googleLocationAPI = new GoogleLocationAPI();
//		googleLocationAPI.setRetrofitListener(new GoogleLocationAPI.GoogleLocationAPILIstener() {
//			@Override
//			public void onConnection (int zipCode) {
//				getWUndergroundAPIResponse(context, zipCode);
//			}
//		});
//		googleLocationAPI.getZipCode(context);
//	}
//
//	@Override
//	public void getWUndergroundAPIResponse (final Context context, int zipCode) {
//		Log.d("Logging: ", "getWUndergroundResponse");
//getWUndergroundResponse
//		wundergroundRetrofit = wundergroundRetrofit.getInstance();
//		wundergroundRetrofit.setRetrofitListener(retrofitListener = new WUndergroundRetrofit.RetrofitListener() {
//			@Override
//			public void onConditionsForecast10DayHourlytRetrieved (ResponseConditionsForecast10DayHourly jsonObject) {
//				baseWeatherWidget.updateMain(baseWeatherWidget.getWidgetID(), jsonObject.getCurrent_observation());
//				baseWeatherWidget.updateDays(context, baseWeatherWidget.getWidgetID(), jsonObject.getForecast().getSimpleforecast().getForecastday(), Constants.NUM_OF_DAYS.WIDGET);
//				baseWeatherWidget.updateHours(context, baseWeatherWidget.getWidgetID(), jsonObject.getHourly_Forecast(), Constants.NUM_OF_DAYS.WIDGET);
//			}
//		});
//		wundergroundRetrofit.getConditionsForecast10DayHourlyForecast(apiKey, zipCode);
//	}
//
//	public void isMyServiceRunning (Context context, Class <?> serviceClass) {
//		activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
//			if (!serviceClass.getName().equals(service.service.getClassName())) {
//				context.startService(new Intent(context, ScreenServiceAndReceiver.class));
//			}
//		}
//	}
//}