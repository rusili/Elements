package nyc.c4q.rusili.SimplyWeather.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.rusili.SimplyWeather.activities.onboard.OnboardPresenter;
import nyc.c4q.rusili.SimplyWeather.activities.widgets.WeatherPresenter;

@Module
public class PresenterModule {
	@Provides
	@Singleton
	WeatherPresenter provideWeatherPresenter () {
		return new WeatherPresenter();
	}

	@Provides
	@Singleton
	OnboardPresenter provideOnboardPresenter () {
		return new OnboardPresenter();
	}
}
