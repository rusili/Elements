package nyc.c4q.rusili.SimplyWeather.network;

import nyc.c4q.rusili.SimplyWeather.network.JSON.ResponseConditions;
import nyc.c4q.rusili.SimplyWeather.network.JSON.ResponseForecastDay;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitInterface {

	@GET ("api/{apikey}/forecast10day/q/{zipcode}.json")
	Call <ResponseForecastDay> getForecast (@Path ("apikey") String apikeyParam, @Path ("zipcode") int zipcodeParam);

	@GET ("api/{apikey}/conditions/q/{zipcode}.json")
	Call <ResponseConditions> getConditions (@Path ("apikey") String apikeyParam, @Path ("zipcode") int zipcodeParam);
}
