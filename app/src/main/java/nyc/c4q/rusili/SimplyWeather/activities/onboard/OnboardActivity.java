package nyc.c4q.rusili.SimplyWeather.activities.onboard;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import javax.inject.Inject;

import nyc.c4q.rusili.SimplyWeather.R;
import nyc.c4q.rusili.SimplyWeather.dagger.AppDagger;

public class OnboardActivity extends AppIntro implements OnboardInterface.View {
	@Inject
	OnboardPresenter onboardPresenter;

	private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6;
	public boolean locationPermissionGranted;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initialize();
	}

	private void initialize () {
		bind();
		setViews();

		getPermissions();
	}

	private void bind () {
		((AppDagger) getApplication()).getAppComponent().inject(this);
		onboardPresenter.bindView(this);
		onboardPresenter.setUp(getApplication());
	}

	private void setViews () {
		setSlides();
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

	private void endActivity () {
		finish();
	}
}