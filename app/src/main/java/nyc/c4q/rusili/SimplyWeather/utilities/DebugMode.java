package nyc.c4q.rusili.SimplyWeather.utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import nyc.c4q.rusili.SimplyWeather.BuildConfig;

public class DebugMode {
	private final boolean isDebugMode;

	public DebugMode(){
		if (BuildConfig.DEBUG) {
			isDebugMode = true;
		} else {
			isDebugMode = false;
		}
	}

	public void logD(String other){
		Log.d("Debug: ", other);
		//Thread.currentThread().getStackTrace().toString();
	}

	public void showToast(Context context, String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT);
	}
}
