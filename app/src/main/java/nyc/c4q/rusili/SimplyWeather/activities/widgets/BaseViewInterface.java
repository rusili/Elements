package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.content.Context;

import nyc.c4q.rusili.SimplyWeather.network.WUnderground.JSON.ResponseConditionsForecast10DayHourly;

public interface BaseViewInterface<T> {

	void initialize (Context context);

	void updateWidget (ResponseConditionsForecast10DayHourly jsonObject);
}
