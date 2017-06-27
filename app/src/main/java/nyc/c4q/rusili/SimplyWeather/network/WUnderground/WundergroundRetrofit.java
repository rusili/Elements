package nyc.c4q.rusili.SimplyWeather.network.WUnderground;

import android.util.Log;

import nyc.c4q.rusili.SimplyWeather.network.WUnderground.JSON.ResponseConditionsForecast10DayHourly;
import nyc.c4q.rusili.SimplyWeather.utilities.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class WundergroundRetrofit {
	private static WundergroundRetrofit wundergroundRetrofit;
	private retrofit2.Retrofit retrofit;
	private RetrofitListener listener;

	private WundergroundRetrofit () {
		this.listener = null;
	}

	public static WundergroundRetrofit getInstance(){
		if (wundergroundRetrofit == null){
			wundergroundRetrofit = new WundergroundRetrofit();
		}
		return wundergroundRetrofit;
	}

	public void setRetrofitListener (RetrofitListener retrofitListener) {
		this.listener = retrofitListener;
	}

	private WundergroundRetrofitCall connect () {
		if (retrofit == null) {
			retrofit = new retrofit2.Retrofit.Builder()
				  .baseUrl(Constants.API_URL.WUNDERGROUND)
				  .addConverterFactory(GsonConverterFactory.create())
				  .build();
		}
		WundergroundRetrofitCall wundergroundRetrofitCall = retrofit.create(WundergroundRetrofitCall.class);

		return wundergroundRetrofitCall;
	}

	public void getConditionsForecast10DayHourlyForecast (String apiKey, int zipCode) {

		WundergroundRetrofitCall wundergroundRetrofitCall = connect();
		Call <ResponseConditionsForecast10DayHourly> getResponse = wundergroundRetrofitCall.getConditionsForecastHourly(apiKey, zipCode);
		getResponse.enqueue(new Callback <ResponseConditionsForecast10DayHourly>() {
			@Override
			public void onResponse (Call <ResponseConditionsForecast10DayHourly> call, Response <ResponseConditionsForecast10DayHourly> response) {
				ResponseConditionsForecast10DayHourly jsonResponse = response.body();
				Log.d("URL: ", call.request().url().toString());

				if (listener != null) {
					listener.onConditionsForecast10DayHourlytRetrieved(jsonResponse);
				}
			}

			@Override
			public void onFailure (Call <ResponseConditionsForecast10DayHourly> call, Throwable t) {
				Log.d("onFailure: ", t.toString());
			}
		});
	}

	public interface RetrofitListener {
		public void onConditionsForecast10DayHourlytRetrieved (ResponseConditionsForecast10DayHourly jsonObject);
	}
}
