package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.content.Context;

interface BasePresenterInterface {
	void getGoogleAPILocation (Context context);

	void getWUndergroundAPIResponse (Context context, int zipCode);
}
