package nyc.c4q.rusili.SimplyWeather.activities.widgets;

public class WeatherPresenter {
	private static WeatherPresenter weatherPresenter;

	public WeatherPresenter (){}

	public static WeatherPresenter getWeatherPresenter(){
		if (weatherPresenter == null){
			weatherPresenter = new WeatherPresenter();
		}
		return weatherPresenter;
	}

}
