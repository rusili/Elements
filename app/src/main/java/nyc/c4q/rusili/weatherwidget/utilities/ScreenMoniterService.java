package nyc.c4q.rusili.weatherwidget.utilities;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import nyc.c4q.rusili.weatherwidget.activities.widgets.WeatherWidget4x2;

public class ScreenMoniterService extends Service{
	BroadcastReceiver broadcastReceiver=null;

	@Override
	public void onCreate () {
		super.onCreate();

		 Toast.makeText(getBaseContext(), "Service on create", Toast.LENGTH_SHORT).show();

		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		broadcastReceiver = new WeatherWidget4x2();
		registerReceiver(broadcastReceiver, filter);
	}

	@Override
	public void onStart (Intent intent, int startId) {
		boolean screenOn = false;

		try {
			// Get ON/OFF values sent from receiver ( AEScreenOnOffReceiver.java )
			screenOn = intent.getBooleanExtra("screen_state", false);
		} catch (Exception e) {}

		if (!screenOn) {
			// your code here
			// Some time required to start any service
			Toast.makeText(getBaseContext(), "Screen on, ", Toast.LENGTH_SHORT).show();
		} else {
			// your code here
			// Some time required to stop any service to save battery consumption
			Toast.makeText(getBaseContext(), "Screen off,", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDestroy () {
		super.onDestroy();

	}

	@Nullable
	@Override
	public IBinder onBind (Intent intent) {
		return null;
	}
}
