package nyc.c4q.rusili.SimplyWeather.activities.onboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.crashlytics.android.Crashlytics;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import io.fabric.sdk.android.Fabric;
import nyc.c4q.rusili.SimplyWeather.BuildConfig;
import nyc.c4q.rusili.SimplyWeather.R;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.Constants;
import nyc.c4q.rusili.SimplyWeather.utilities.app.ScreenServiceAndReceiver;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.DebugMode;

public class ActivityOnboard extends AppIntro {
	private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6;
	public boolean locationPermissionGranted;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		initialize();
	}

	private void initialize () {
		checkDebugMode();
		loadStetho();
		killService();
		getPermissions();
		createService();
		setSlides();
	}

	private void loadStetho () {
		DebugMode.loadStetho(this);
	}

	private void createService () {
		startService(new Intent(ActivityOnboard.this, ScreenServiceAndReceiver.class));
	}

	private void killService () {
		stopService(new Intent(ActivityOnboard.this, ScreenServiceAndReceiver.class));
	}

	private void setSlides () {
		// Introduction
		addSlide(AppIntroFragment.newInstance("", "", R.drawable.banner, getResources().getColor(R.color.gray90)));
	}

	@Override
	public void onSkipPressed (Fragment currentFragment) {
		super.onSkipPressed(currentFragment);
		endActivity();
	}

	@Override
	public void onDonePressed (Fragment currentFragment) {
		super.onDonePressed(currentFragment);
		endActivity();
	}

	private void checkDebugMode () {
		SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREFERENCES.FILE_NAME, 0);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (BuildConfig.DEBUG) {
			editor.putBoolean(Constants.SHARED_PREFERENCES.BOOLEAN_ISDEBUGMODE, true);
		} else {
			editor.putBoolean(Constants.SHARED_PREFERENCES.BOOLEAN_ISDEBUGMODE, false);
		}

		editor.commit();
	}

	private void endActivity () {
		finish();
	}

	private void getPermissions () {
		if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
			  android.Manifest.permission.ACCESS_FINE_LOCATION)
			  == PackageManager.PERMISSION_GRANTED) {
			locationPermissionGranted = true;
		} else {
			ActivityCompat.requestPermissions(this,
				  new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},
				  PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
		}
	}
}
