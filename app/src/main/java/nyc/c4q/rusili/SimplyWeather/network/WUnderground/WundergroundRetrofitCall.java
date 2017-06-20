package nyc.c4q.rusili.SimplyWeather.network.WUnderground;

import nyc.c4q.rusili.SimplyWeather.network.WUnderground.JSON.ResponseConditionsForecast10DayHourly;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WundergroundRetrofitCall {

	@GET ("api/{apikey}/conditions/forecast10day/hourly/q/{zipcode}.json")
	Call <ResponseConditionsForecast10DayHourly> getConditionsForecastHourly (@Path ("apikey") String apikeyParam, @Path ("zipcode") int zipcodeParam);
}
