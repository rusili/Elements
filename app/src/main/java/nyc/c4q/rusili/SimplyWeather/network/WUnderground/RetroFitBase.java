package nyc.c4q.rusili.SimplyWeather.network.WUnderground;

import android.util.Log;

import nyc.c4q.rusili.SimplyWeather.network.WUnderground.JSON.HourlyForecast;
import nyc.c4q.rusili.SimplyWeather.network.WUnderground.JSON.ResponseConditions;
import nyc.c4q.rusili.SimplyWeather.network.WUnderground.JSON.CurrentObservation;
import nyc.c4q.rusili.SimplyWeather.network.WUnderground.JSON.ForecastDay;
import nyc.c4q.rusili.SimplyWeather.network.WUnderground.JSON.ResponseForecastDay;
import nyc.c4q.rusili.SimplyWeather.network.WUnderground.JSON.ResponseHourly;
import nyc.c4q.rusili.SimplyWeather.utilities.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitBase {
	private RetrofitListener listener;

	private String apiKey;
	private int zipCode;

	public RetroFitBase (String apiKeyParam, int zipCodeParam) {
		this.listener = null;

		this.zipCode = zipCodeParam;
		apiKey = apiKeyParam;
	}

	public void setRetrofitListener (RetrofitListener retrofitListener) {
		this.listener = retrofitListener;
	}

	private RetrofitCalls connect () {
		retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
			  .baseUrl(Constants.API_URL.WUNDERGROUND)
			  .addConverterFactory(GsonConverterFactory.create())
			  .build();
		RetrofitCalls retrofitCalls = retrofit.create(RetrofitCalls.class);
		return retrofitCalls;
	}

	public void getForecastDay () {
		RetrofitCalls retrofitCalls = connect();
		Call <ResponseForecastDay> getResponse = retrofitCalls.getForecast(apiKey, zipCode);
		getResponse.enqueue(new Callback <ResponseForecastDay>() {
			@Override
			public void onResponse (Call <ResponseForecastDay> call, Response <ResponseForecastDay> response) {
				ForecastDay[] jsonResponse = response.body().getForecast().getSimpleforecast().getForecastday();
				if (listener != null) {
					listener.onForecastDaysRetrieved(jsonResponse);
				}
			}

			@Override
			public void onFailure (Call <ResponseForecastDay> call, Throwable t) {
				Log.d("onFailure: ", t.toString());
			}
		});
	}

	public void getConditions () {
		RetrofitCalls retrofitCalls = connect();
		Call <ResponseConditions> getResponse = retrofitCalls.getConditions(apiKey, zipCode);
		getResponse.enqueue(new Callback <ResponseConditions>() {
			@Override
			public void onResponse (Call <ResponseConditions> call, Response <ResponseConditions> response) {
				CurrentObservation jsonResponse = response.body().getCurrent_observation();
				Log.d("Conditions URL: ", call.request().url().toString());
				if (listener != null) {
					listener.onConditionsRetrieved(jsonResponse);
				}
			}

			@Override
			public void onFailure (Call <ResponseConditions> call, Throwable t) {
				Log.d("onFailure: ", t.toString());
			}
		});
	}

	public void getHourlyForecast () {
		RetrofitCalls retrofitCalls = connect();
		Call <ResponseHourly> getResponse = retrofitCalls.getHourly(apiKey, zipCode);
		getResponse.enqueue(new Callback <ResponseHourly>() {
			@Override
			public void onResponse (Call <ResponseHourly> call, Response <ResponseHourly> response) {
				HourlyForecast[] jsonResponse = response.body().getHourly_Forecast();
				Log.d("Hourly URL: ", call.request().url().toString());
				if (listener != null) {
					listener.onHourlyRetrieved(jsonResponse);
				}
			}

			@Override
			public void onFailure (Call <ResponseHourly> call, Throwable t) {
				Log.d("onFailure: ", t.toString());
			}
		});
	}

	public interface RetrofitListener {
		public void onConditionsRetrieved (CurrentObservation currentObservation);
		public void onForecastDaysRetrieved (ForecastDay[] forecastDays);
		public void onHourlyRetrieved (HourlyForecast[] hourlyForecasts);
	}
}
