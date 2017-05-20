package nyc.c4q.rusili.weatherwidget.activities.onboard;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import nyc.c4q.rusili.weatherwidget.R;

public class ActivityOnboard extends AppIntro {
	private static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 5;
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
		addSlide(AppIntroFragment.newInstance("Slide1", "Slide1", R.drawable.cloudtemp, getResources().getColor(R.color.transparent)));
		addSlide(AppIntroFragment.newInstance("Slide1", "Slide1", R.drawable.windtemp, getResources().getColor(R.color.transparent)));
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

	// onSwipe code
}
