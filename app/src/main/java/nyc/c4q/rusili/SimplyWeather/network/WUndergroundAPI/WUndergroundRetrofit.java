
import android.util.Log;

import nyc.c4q.rusili.SimplyWeather.utilities.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class WUndergroundRetrofit {
	private static WUndergroundRetrofit WUndergroundRetrofit;
	private retrofit2.Retrofit retrofit;
	private RetrofitListener listener;

	private WUndergroundRetrofit () {
		this.listener = null;
	}

	public static WUndergroundRetrofit getInstance(){
		if (WUndergroundRetrofit == null){
			WUndergroundRetrofit = new WUndergroundRetrofit();
		}
		return WUndergroundRetrofit;
	}

	public void setRetrofitListener (RetrofitListener retrofitListener) {
		this.listener = retrofitListener;
	}

	private WUndergroundRetrofitCall connect () {
		if (retrofit == null) {
			retrofit = new retrofit2.Retrofit.Builder()
				  .baseUrl(Constants.API_URL.WUNDERGROUND)
				  .addConverterFactory(GWsonConverterFactory.create())
				  .build();
		}
		WUndergroundRetrofitCall WUndergroundRetrofitCall = retrofit.create(WUndergroundRetrofitCall.class);

		return WUndergroundRetrofitCall;
	}

	public void getConditionsForecast10DayHourlyForecast (String apiKey, int zipCode) {

		WUndergroundRetrofitCall WUndergroundRetrofitCall = connect();
		Call <ResponseConditionsForecast10DayHourly> getResponse = WUndergroundRetrofitCall.getConditionsForecastHourly(apiKey, zipCode);
		getResponse.enqueue(new Callback <ResponseConditionsForecast10DayHourly>() {
			@Override
			public void onResponse (Call <ResponseConditionsForecast10DayHourly> call, Response <ResponseConditionsForecast10DayHourly> response) {
				ResponseConditionsForecast10DayHourly jsonResponse = response.body();
				Log.d("URL: ", call.request().url().toString());

				if (listener != null) {
					listener.onConditionsForecast10DayHourlytRetrieved(jsonResponse);
					Log.d("listenernotnull: ", jsonResponse.toString());
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
