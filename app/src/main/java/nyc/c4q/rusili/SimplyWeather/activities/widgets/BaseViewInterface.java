package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.content.Context;

public interface BaseViewInterface<T> {

	void setPresenter(T presenter);

	void initialize (Context context);

	void setViews();

	void updateWidget();
}
