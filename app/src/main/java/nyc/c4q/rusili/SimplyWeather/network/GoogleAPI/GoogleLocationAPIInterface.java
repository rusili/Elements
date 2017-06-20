package nyc.c4q.rusili.SimplyWeather.network.GoogleAPI;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public abstract class GoogleLocationAPIInterface implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	public abstract void startGoogleAPIClient(Context context, GoogleApiClient googleApiClient);

	public abstract int getLocationZipCode (Location location);

	@Override
	public void onConnected (@Nullable Bundle bundle) {}

	@Override
	public void onConnectionSuspended (int i) {}

	@Override
	public void onConnectionFailed (@NonNull ConnectionResult connectionResult) {}
}
