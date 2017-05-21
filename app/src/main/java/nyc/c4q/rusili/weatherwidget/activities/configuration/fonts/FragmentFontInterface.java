package nyc.c4q.rusili.weatherwidget.activities.configuration.fonts;

import nyc.c4q.rusili.weatherwidget.utilities.BasePresenter;
import nyc.c4q.rusili.weatherwidget.utilities.BaseView;

public class FragmentFontInterface {

	interface Presenter extends BasePresenter {}

	interface View extends BaseView<Presenter> {

		void initialize ();

		void setViews ();
	}
}