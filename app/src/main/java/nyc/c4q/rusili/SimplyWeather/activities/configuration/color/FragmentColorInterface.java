package nyc.c4q.rusili.SimplyWeather.activities.configuration.color;

public interface FragmentColorInterface {

	interface Presenter {
	}

	interface View <Presenter> {

		void initialize ();

		void setViews ();

		void setPresenter(FragmentColorInterface.Presenter presenter);
	}
}