package nyc.c4q.rusili.SimplyWeather.utilities.generic;

import android.content.Context;
import android.widget.Toast;

public class ShowToast {

	public static void show(Context context, String message){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
}
