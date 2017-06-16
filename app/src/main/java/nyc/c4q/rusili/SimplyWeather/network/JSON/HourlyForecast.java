package nyc.c4q.rusili.SimplyWeather.network.JSON;

public class HourlyForecast {
	FCTTIME FCTTIME;
	temp temp;
	String icon;

	public HourlyForecast.FCTTIME getFCTTIME () {
		return FCTTIME;
	}

	public class FCTTIME{
		String hour;

		public String getHour () {
			return hour;
		}
	}

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
