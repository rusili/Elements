package nyc.c4q.rusili.SimplyWeather.activities.configuration.color;

import android.view.View;

import nyc.c4q.rusili.SimplyWeather.database.SQLiteDatabaseHandler;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.DebugMode;

public class FragmentColorPresenter implements FragmentColorInterface.Presenter {
	private FragmentColorInterface.View fragmentColorView;

	public FragmentColorPresenter (FragmentColorInterface.View fragmentColorView) {
		this.fragmentColorView = fragmentColorView;
	}

	public void start () {

	}

	public void saveColorToDatabase (View view, int color){
		SQLiteDatabaseHandler.getSqLiteDatabaseHandler(fragmentColorView.getContext()).saveColorToDatabase(Integer.valueOf(view.getTag().toString()), color);
		DebugMode.logD(view.getContext(), "ID: " + Integer.valueOf(view.getTag().toString()) + " = " + color );
	}

	public void onDestroy () {

	}
}