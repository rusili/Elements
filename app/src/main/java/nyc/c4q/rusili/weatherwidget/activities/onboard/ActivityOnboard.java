package nyc.c4q.rusili.weatherwidget.activities.onboard;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import nyc.c4q.rusili.weatherwidget.R;

public class ActivityOnboard extends AppIntro {
	private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6;
	private boolean locationPermissionGranted;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize();
	}

	private void initialize () {
		getPermissions();
		setSlides();
	}

	private void setSlides () {
		// Introduction
		addSlide(AppIntroFragment.newInstance("Introduction", "Slide1", R.drawable.ic_mostlycloudy, getResources().getColor(R.color.transparent)));
		// OnClick: Update & Link
		addSlide(AppIntroFragment.newInstance("OnClick Update & Link", "Slide1", R.drawable.ic_mostlysunny, getResources().getColor(R.color.transparent)));
		// OnClick: Configuration
		addSlide(AppIntroFragment.newInstance("OnClick Configuration", "Slide1", R.drawable.slide_onclick2, getResources().getColor(R.color.transparent)));
		// Done!
		addSlide(AppIntroFragment.newInstance("Done", "Slide1", R.drawable.slide_done, getResources().getColor(R.color.transparent)));
	}

	@Override
	public void onSkipPressed(Fragment currentFragment) {
		super.onSkipPressed(currentFragment);
		endActivity();
	}

	@Override
	public void onDonePressed(Fragment currentFragment) {
		super.onDonePressed(currentFragment);
		endActivity();
	}

	private void endActivity(){
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
