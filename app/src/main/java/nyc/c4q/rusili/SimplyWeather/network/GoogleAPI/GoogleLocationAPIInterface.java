package nyc.c4q.rusili.SimplyWeather.network.GoogleAPI;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;

public interface GoogleLocationAPIInterface extends GoogleApiClient.ConnectionCallbacks {

	public void startGoogleAPIClient(Context context, GoogleApiClient googleApiClient);

	boolean isNetworkConnected (Context context);

	public int getLocation(Location location);
}
