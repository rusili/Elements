package nyc.c4q.rusili.SimplyWeather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import nyc.c4q.rusili.SimplyWeather.database.model.DBColor;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class SQLiteDatabaseHandler  extends SQLiteOpenHelper{
	private static SQLiteDatabase sqLiteDatabase;

	private static final String DATABASE_NAME = "simpleweather.db";
	private static final int DATABASE_VERSION = 1;

	private static SQLiteDatabaseHandler sqLiteDatabaseHandler;

	static {
		cupboard().register(DBColor.class);
	}

	private static Object database;

	private SQLiteDatabaseHandler(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static synchronized SQLiteDatabaseHandler getSqLiteDatabaseHandler (Context context) {
		if (sqLiteDatabaseHandler == null) {
			sqLiteDatabaseHandler = new SQLiteDatabaseHandler(context);
		}
		sqLiteDatabase = sqLiteDatabaseHandler.getWritableDatabase();
		return sqLiteDatabaseHandler;
	}

	public void loadFromDatabase(){

	}

	public void saveColorToDatabase (int ID, int color){
		DBColor dbColor = cupboard().withDatabase(sqLiteDatabase).get(DBColor.class, ID);
		dbColor.setColor(color);
		cupboard().withDatabase(sqLiteDatabase).put(dbColor);
	}

	@Override
	public void onCreate (SQLiteDatabase db) {
		cupboard().withDatabase(db).createTables();

		cupboard().withDatabase(db).put(new DBColor("Primary", Color.BLUE));
		cupboard().withDatabase(db).put(new DBColor("Secondary", Color.BLUE));
		cupboard().withDatabase(db).put(new DBColor("FontPrimary", Color.WHITE));
		cupboard().withDatabase(db).put(new DBColor("FontSecondary", Color.WHITE));
	}

	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
		cupboard().withDatabase(db).upgradeTables();
	}

}
