package nyc.c4q.rusili.SimplyWeather.network.googleAPI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public abstract class GoogleLocationAPIInterface implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	public abstract void getZipCode (Context context);

	@Override
	public void onConnected (@Nullable Bundle bundle) {
	}

	@Override
	public void onConnectionSuspended (int i) {
	}

	@Override
	public void onConnectionFailed (@NonNull ConnectionResult connectionResult) {
	}
}