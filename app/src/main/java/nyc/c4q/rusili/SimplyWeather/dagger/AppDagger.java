package nyc.c4q.rusili.SimplyWeather.dagger;

import android.app.Application;

public class AppDagger extends Application {
	private AppComponent appComponent;

	@Override
	public void onCreate () {
		super.onCreate();
		appComponent = initDagger(this);
	}

	public AppComponent getAppComponent () {
		return appComponent;
	}

	protected AppComponent initDagger (AppDagger application) {
		return DaggerAppComponent.builder()
			  .appModule(new AppModule(application))
			  .build();
	}
}
