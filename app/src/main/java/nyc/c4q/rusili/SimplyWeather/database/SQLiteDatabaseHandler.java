package nyc.c4q.rusili.SimplyWeather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import nyc.c4q.rusili.SimplyWeather.database.model.dbColor;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class SQLiteDatabaseHandler  extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "simpleweather.db";
	private static final int DATABASE_VERSION = 1;

	private static SQLiteDatabaseHandler sqLiteDatabaseHandler;

	static {
		cupboard().register(dbColor.class);
	}

	private SQLiteDatabaseHandler(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static synchronized SQLiteDatabaseHandler getSqLiteDatabaseHandler(Context context){
		if (sqLiteDatabaseHandler == null){
			sqLiteDatabaseHandler = new SQLiteDatabaseHandler(context);
		}
		return sqLiteDatabaseHandler;
	}

	@Override
	public void onCreate (SQLiteDatabase db) {
		cupboard().withDatabase(db).createTables();

		cupboard().withDatabase(db).put(new dbColor("Primary", Color.BLUE));
		cupboard().withDatabase(db).put(new dbColor("Secondary", Color.BLUE));
		cupboard().withDatabase(db).put(new dbColor("FontPrimary", Color.WHITE));
		cupboard().withDatabase(db).put(new dbColor("FontSecondary", Color.WHITE));
	}

	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
		cupboard().withDatabase(db).upgradeTables();
	}

}
