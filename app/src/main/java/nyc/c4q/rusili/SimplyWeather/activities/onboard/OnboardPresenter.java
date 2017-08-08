package nyc.c4q.rusili.SimplyWeather.activities.onboard;

import android.app.Application;
import android.content.SharedPreferences;

import com.crashlytics.android.Crashlytics;

import dagger.Module;
import io.fabric.sdk.android.Fabric;
import nyc.c4q.rusili.SimplyWeather.BuildConfig;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.Constants;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.DebugMode;

@Module
public class OnboardPresenter implements OnboardInterface.Presenter {
	private OnboardInterface.View onboardView;

	public OnboardPresenter () {
	}

	@Override
	public void bindView (OnboardInterface.View view) {
		this.onboardView = view;
	}

	@Override
	public void setUp (Application application) {
		saveDebugMode(application);
		loadLibraries(application);
	}

	private void saveDebugMode (Application application) {
		SharedPreferences sharedPref = application.getSharedPreferences(Constants.SHARED_PREFERENCES.FILE_NAME, 0);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (BuildConfig.DEBUG) {
			editor.putBoolean(Constants.SHARED_PREFERENCES.BOOLEAN_ISDEBUGMODE, true);
		} else {
			editor.putBoolean(Constants.SHARED_PREFERENCES.BOOLEAN_ISDEBUGMODE, false);
		}

		editor.commit();
	}

	private void loadLibraries (Application application) {
		Fabric.with(application, new Crashlytics());
		DebugMode.loadStetho(application);
		DebugMode.loadCanary(application);
	}

	@Override
	public void unBind () {
		this.onboardView = null;
	}
}
