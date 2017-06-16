package nyc.c4q.rusili.SimplyWeather.activities.widgets;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import nyc.c4q.rusili.SimplyWeather.utilities.Constants;

public class ScreenServiceAndReceiver extends Service {
	private BroadcastReceiver broadcastReceiver;

	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		broadcastReceiver = new ScreenReceiver();
		registerReceiver(broadcastReceiver, filter);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy () {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	@Nullable
	@Override
	public IBinder onBind (Intent intent) {
		return null;
	}

	public ScreenServiceAndReceiver getServiceObject () {
		return this;
	}

	// Broadcast Receiver
	private final class ScreenReceiver extends BroadcastReceiver {

		@Override
		public void onReceive (final Context context, final Intent intent) {
			// check if service is alive here. recreate if dead
			if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

				Toast.makeText(context, "Service ScreenCheck!", Toast.LENGTH_SHORT).show();

					AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
					ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(), "WeatherWidget4x2");
					int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
					Intent updateWidgetIntent = new Intent(context, WeatherWidget4x2.class);
					updateWidgetIntent.setAction(Constants.ACTION.UPDATE_SCREEN);
					updateWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
					sendBroadcast(updateWidgetIntent);
				}
			}
		}
}
