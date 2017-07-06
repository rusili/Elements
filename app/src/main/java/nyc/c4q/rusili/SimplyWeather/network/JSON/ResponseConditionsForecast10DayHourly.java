package nyc.c4q.rusili.SimplyWeather.network.JSON;

public class ResponseConditionsForecast10DayHourly {
	CurrentObservation current_observation;
	forecast forecast;
	HourlyForecast[] hourly_forecast;

	public CurrentObservation getCurrent_observation () {
		return current_observation;
	}

	public forecast getForecast () {
		return forecast;
	}

	public class forecast {
		forecast.simpleforecast simpleforecast;

		public forecast.simpleforecast getSimpleforecast () {
			return simpleforecast;
		}

		public class simpleforecast {
			ForecastDay[] forecastday;

			public ForecastDay[] getForecastday () {
				return forecastday;
			}
		}
	}

	public HourlyForecast[] getHourly_Forecast () {
		return hourly_forecast;
	}
}