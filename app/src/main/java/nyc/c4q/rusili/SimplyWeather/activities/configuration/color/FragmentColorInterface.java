package nyc.c4q.rusili.SimplyWeather.activities.configuration.color;

import android.content.Context;

import java.util.List;

import nyc.c4q.rusili.SimplyWeather.database.model.DBColor;

public interface FragmentColorInterface {

	public interface View {
		Context getContext ();
	}

	public interface Presenter {
		void saveColorToDatabase (android.view.View view, int ID, int position);

		List<DBColor> getDbColorArrayList ();
	}
}