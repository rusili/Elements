package nyc.c4q.rusili.SimplyWeather.utilities.generic;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import nyc.c4q.rusili.SimplyWeather.utilities.app.StethoHelper;

public class DebugMode {

	public static void logD (Context context, String text) {
		SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES.FILE_NAME, 0);
		Boolean isDebugMode = prefs.getBoolean("isDebugMode", false);

		if (isDebugMode) {
			Log.d("Debug: ", text);
		}
	}

	public static void showToast (Context context, String text) {
		SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES.FILE_NAME, 0);
		Boolean isDebugMode = prefs.getBoolean("isDebugMode", false);

		if (isDebugMode) {
			Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
	}

	public static void loadStetho(Context context){
		SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES.FILE_NAME, 0);
		Boolean isDebugMode = prefs.getBoolean("isDebugMode", false);

		if (isDebugMode) {
			new StethoHelper(context);
		}
	}
}
