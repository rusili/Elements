package nyc.c4q.rusili.SimplyWeather.activities.configuration.color;

import android.content.Context;

public interface FragmentColorInterface {

	public interface View {
		Context getContext ();
	}

	public interface Presenter {
		void saveColorToDatabase (android.view.View view, int ID);
	}
}