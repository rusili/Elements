package nyc.c4q.rusili.SimplyWeather.dagger;

import javax.inject.Singleton;

import dagger.Component;
import nyc.c4q.rusili.SimplyWeather.activities.onboard.OnboardActivity;
import nyc.c4q.rusili.SimplyWeather.activities.widgets.WeatherWidget4x2;

@Singleton
@Component (modules = {AppModule.class, PresenterModule.class})
public interface AppComponent {
	void inject (WeatherWidget4x2 target);

	void inject (OnboardActivity target);

}
