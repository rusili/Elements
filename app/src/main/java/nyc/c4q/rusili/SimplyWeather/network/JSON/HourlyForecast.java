package nyc.c4q.rusili.SimplyWeather.network.JSON;

public class HourlyForecast {
	temp temp;
	String icon;

	public String getIcon () {
		return icon;
	}

	public HourlyForecast.temp getTemp () {
		return temp;
	}

	public class temp{
		String english;

		public String getEnglish () {
			return english;
		}
	}
}
