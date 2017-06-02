package nyc.c4q.rusili.SimplyWeather.activities.configuration.fonts;

import nyc.c4q.rusili.SimplyWeather.utilities.BasePresenter;
import nyc.c4q.rusili.SimplyWeather.utilities.BaseView;

public class FragmentFontInterface {

	interface Presenter extends BasePresenter {
	}

	interface View extends BaseView <Presenter> {

		void initialize ();

		void setViews ();
	}
}
