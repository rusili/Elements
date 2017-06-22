package nyc.c4q.rusili.SimplyWeather.activities.widgets;

public interface BasePresenterInterface {

	void subscribe();

	void unsubscribe();

	void initialize();

	void getGoogleAPILocation();

	void getWUndergroundAPIResponse(int zipCode);
}
