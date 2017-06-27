package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.content.Context;

public interface BasePresenterInterface<T> {

	void getGoogleAPILocation (Context context);

	void getWUndergroundAPIResponse(int zipCode);
}
