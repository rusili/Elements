package nyc.c4q.rusili.SimplyWeather.utilities.generic;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

public class StethoHelper {

	public StethoHelper (Context context) {
		Stetho.initializeWithDefaults(context);
		initializeOKHTTP();
	}

	public void initializeOKHTTP () {
		new OkHttpClient.Builder()
			  .addNetworkInterceptor(new StethoInterceptor())
			  .build();
	}
}