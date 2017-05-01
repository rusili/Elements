package nyc.c4q.rusili.weatherwidget.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

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

        bugTest();
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

    private void bugTest(){
        Date now = new Date();
        SimpleDateFormat weekday = new SimpleDateFormat("E");
        SimpleDateFormat month = new SimpleDateFormat("MM");
        SimpleDateFormat day = new SimpleDateFormat("dd");

        CharSequence a = month.format(now);
        CharSequence b = day.format(now);

        CharSequence charSequence = null;

        if (Integer.parseInt(day.format(now)) < 10){
            charSequence = String.valueOf(day.format(now).charAt(1));
        } else {
            charSequence = String.valueOf(day.format(now));
            charSequence.toString();
        }
    }
}
