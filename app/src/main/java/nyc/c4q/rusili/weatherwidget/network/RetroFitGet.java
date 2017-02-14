package nyc.c4q.rusili.weatherwidget.network;

import android.util.Log;

import nyc.c4q.rusili.weatherwidget.network.JSON.ForecastDay;
import nyc.c4q.rusili.weatherwidget.network.JSON.ResponseForecastDay;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitGet {
    private String apiKey;
    private int zipCode;

    public RetroFitGet(String apiKeyParam, int zipCodeParam){
        this.zipCode = zipCodeParam;
        apiKey = apiKeyParam;
    }

    private RetrofitInterface connect(){
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://api.wunderground.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        return retrofitInterface;
    }

    public void getForecastDay(){
        RetrofitInterface retrofitInterface = connect();
        Call<ResponseForecastDay> getResponse = retrofitInterface.getForecast(apiKey,zipCode);
        getResponse.enqueue(new Callback <ResponseForecastDay>() {
            @Override
            public void onResponse (Call <ResponseForecastDay> call, Response<ResponseForecastDay> response) {
                ResponseForecastDay jsonResponse = response.body();
                ForecastDay[] forecastDay = jsonResponse.getForecast().getSimpleforecast().getForecastday();
                Log.d("onSuccess: ", String.valueOf(forecastDay[0].getHigh().getFahrenheit()));
            }
            @Override
            public void onFailure (Call <ResponseForecastDay> call, Throwable t) {
                Log.d("onFailure: ", t.toString());
            }
        });
    }

    public void getConditions(){
        RetrofitInterface retrofitInterface = connect();
        Call<ResponseForecastDay> getResponse = retrofitInterface.getForecast(apiKey,zipCode);
        getResponse.enqueue(new Callback <ResponseForecastDay>() {
            @Override
            public void onResponse (Call <ResponseForecastDay> call, Response<ResponseForecastDay> response) {
                ResponseForecastDay jsonResponse = response.body();
                ForecastDay[] forecastDay = jsonResponse.getForecast().getSimpleforecast().getForecastday();
                Log.d("onSuccess: ", String.valueOf(forecastDay[0].getHigh().getFahrenheit()));
            }
            @Override
            public void onFailure (Call <ResponseForecastDay> call, Throwable t) {
                Log.d("onFailure: ", t.toString());
            }
        });
    }
}
