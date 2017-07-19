package nyc.c4q.rusili.SimplyWeather.activities.configuration.color;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.QueryResultIterable;
import nyc.c4q.rusili.SimplyWeather.database.SQLiteDatabaseHandler;
import nyc.c4q.rusili.SimplyWeather.database.model.DBColor;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.DebugMode;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class FragmentColorPresenter implements FragmentColorInterface.Presenter {
	private FragmentColorInterface.View fragmentColorView;
	private SQLiteDatabase sqLiteDatabase;

	private List<DBColor> dbColorArrayList;

	public FragmentColorPresenter (FragmentColorInterface.View fragmentColorView) {
		this.fragmentColorView = fragmentColorView;
		initialize();
	}

	private void initialize () {
		this.dbColorArrayList = loadColorsFromDatabase(fragmentColorView.getContext());
	}

	public List<DBColor> loadColorsFromDatabase(Context context){
		sqLiteDatabase = SQLiteDatabaseHandler.getSqLiteDatabaseHandler(context).getSqLiteDatabase();

		List<DBColor> dbColorArrayListTemp = new ArrayList<>();
		try {
			QueryResultIterable<DBColor> itr = cupboard().withDatabase(sqLiteDatabase).query(DBColor.class).query();
			for (DBColor dbColor : itr) {
				dbColorArrayListTemp.add(dbColor);
				DebugMode.logD(context, "loadColorsFromDatabase: " + dbColor.getName() + " : " + dbColor.getColor());
			}
			itr.close();
		} catch (Exception e) {}

		return dbColorArrayListTemp;
	}

	public void saveColorToDatabase (View view, int color, int position) {
		sqLiteDatabase = SQLiteDatabaseHandler.getSqLiteDatabaseHandler(view.getContext()).getSqLiteDatabase();

		DBColor dbColor = cupboard().withDatabase(sqLiteDatabase).get(DBColor.class, Integer.valueOf(position)+1);
		dbColor.setColor(color);
		cupboard().withDatabase(sqLiteDatabase).put(dbColor);

		DebugMode.logD(view.getContext(), "ID: " + dbColor.get_id() + " = " + color);
	}

	public void onDestroy () {
		this.fragmentColorView = null;
	}
}