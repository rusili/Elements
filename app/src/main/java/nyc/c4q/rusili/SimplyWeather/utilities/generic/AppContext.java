package nyc.c4q.rusili.SimplyWeather.utilities.generic;

import android.app.Application;
import android.content.Context;

public class AppContext extends Application {
	private static Application application;

	public AppContext (Application application, Context context) {
		this.application = application;
	}

	public static Context getContext () {
		return application;
	}
}
