package nyc.c4q.rusili.SimplyWeather.utilities;

import nyc.c4q.rusili.SimplyWeather.R;

public class IconInflater {

	public int choose (String resource) {
		int resourceID = 0;

		if (resource.contains("chance")) { // Chance
			if (resource.contains("rain")) {
				resourceID = R.drawable.ic_chancerain;
			} else if (resource.contains("sleet")) {
				resourceID = R.drawable.ic_chancesleet;
			} else if (resource.contains("flurries")) {
				resourceID = R.drawable.ic_chanceflurries;
			} else if (resource.contains("snow")) {
				resourceID = R.drawable.ic_chancesnow;
			} else if (resource.contains("storms")) {
				resourceID = R.drawable.ic_chancestorms;
			}
		} else if ((resource.contains("mostlycloudy") || resource.contains("partlysunny"))) {
			resourceID = R.drawable.ic_mostlycloudy;
		} else if ((resource.contains("partlycloudy") || resource.contains("mostlysunny"))) {
			resourceID = R.drawable.ic_mostlysunny;
		} else if (resource.contains("clear") || resource.contains("sunny")) {
			resourceID = R.drawable.ic_sun;
		} else if (resource.contains("cloudy")) {
			resourceID = R.drawable.ic_cloudy;
		} else if (resource.contains("rain")) {
			resourceID = R.drawable.ic_rain;
		} else if (resource.contains("sleet")) {
			resourceID = R.drawable.ic_sleet;
		} else if (resource.contains("flurries")) {
			resourceID = R.drawable.ic_flurries;
		} else if (resource.contains("snow")) {
			resourceID = R.drawable.ic_snow;
		} else if (resource.contains("fog") || (resource.contains("hazy"))) {
			resourceID = R.drawable.ic_fog;
		} else if (resource.contains("storms")) {
			resourceID = R.drawable.ic_storms;
		}
		return resourceID;
	}
}
