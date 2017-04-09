package nyc.c4q.rusili.weatherwidget.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import nyc.c4q.rusili.weatherwidget.R;

public class ActivitySetUp extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 5;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6;
    private boolean locationPermissionGranted;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermissions();
    }

    private void getPermissions () {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

}
