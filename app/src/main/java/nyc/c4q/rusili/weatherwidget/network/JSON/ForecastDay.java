package nyc.c4q.rusili.weatherwidget.network.JSON;

public class ForecastDay {
	date date;
	high high;
	low low;
	avewind avewind;
	String conditions;
	int avehumidity;
	String icon;

	public class avewind {
		int mph;

		public int getMph () {
			return mph;
		}
	}

	public ForecastDay.avewind getAvewind () {
		return avewind;
	}

	public class date {
		String day;
		String monthname_short;
		String weekday_short;
		String tz_short;

		public String getDay () {
			return day;
		}

		public String getMonthnameShort () {
			return monthname_short;
		}

		public String getWeekdayShort () {
			return weekday_short;
		}

		public String getTz_short () {
			return tz_short;
		}
	}

	public class high {
		int fahrenheit;
		int celsius;

		public int getFahrenheit () {
			return fahrenheit;
		}

		public int getCelsius () {
			return celsius;
		}
	}

	public class low {
		int fahrenheit;
		int celsius;

		public int getFahrenheit () {
			return fahrenheit;
		}

		public int getCelsius () {
			return celsius;
		}
	}

	public ForecastDay.date getDate () {
		return date;
	}

	public ForecastDay.high getHigh () {
		return high;
	}

	public ForecastDay.low getLow () {
		return low;
	}

	public String getConditions () {
		return conditions;
	}

	public int getAvehumidity () {
		return avehumidity;
	}

	public String getIcon () {
		return icon;
	}
}
