package nyc.c4q.rusili.SimplyWeather.activities.configuration.color;

import nyc.c4q.rusili.SimplyWeather.utilities.BasePresenter;
import nyc.c4q.rusili.SimplyWeather.utilities.BaseView;

public class FragmentColorInterface {

	interface Presenter extends BasePresenter {
	}

	interface View extends BaseView <Presenter> {

		void initialize ();

		void setViews ();
	}
}
