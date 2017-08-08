package nyc.c4q.rusili.SimplyWeather.activities.onboard;

import android.app.Application;

public interface OnboardInterface {

	public interface View {
	}

	public interface Presenter {
		void bindView (View view);

		void unBind ();

		void setUp (Application application);
	}
}
