package nyc.c4q.rusili.SimplyWeather.activities.widgets;

public interface BasePresenterInterface<T> {

	void initialize();

	void getGoogleAPILocation();

	void getWUndergroundAPIResponse(int zipCode);
}
