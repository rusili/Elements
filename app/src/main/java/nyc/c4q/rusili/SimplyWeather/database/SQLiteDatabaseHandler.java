package nyc.c4q.rusili.SimplyWeather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.QueryResultIterable;
import nyc.c4q.rusili.SimplyWeather.database.model.DBColor;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {
	private static SQLiteDatabase sqLiteDatabase;

	private static final String DATABASE_NAME = "simpleweather.db";
	private static final int DATABASE_VERSION = 1;

	private static SQLiteDatabaseHandler sqLiteDatabaseHandler;

	private List<DBColor> listOfDBColors = new ArrayList <>();

	static {
		cupboard().register(DBColor.class);
	}

	private SQLiteDatabaseHandler (Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static synchronized SQLiteDatabaseHandler getSqLiteDatabaseHandler (Context context) {
		if (sqLiteDatabaseHandler == null) {
			sqLiteDatabaseHandler = new SQLiteDatabaseHandler(context);
		}
		sqLiteDatabase = sqLiteDatabaseHandler.getWritableDatabase();
		return sqLiteDatabaseHandler;
	}

	public SQLiteDatabase getSqLiteDatabase(){
		return sqLiteDatabase;
	}

	@Override
	public void onCreate (SQLiteDatabase db) {
		cupboard().withDatabase(db).createTables();

		cupboard().withDatabase(db).put(new DBColor("Background", Color.BLUE));
		cupboard().withDatabase(db).put(new DBColor("FontWeekday", Color.WHITE));
		cupboard().withDatabase(db).put(new DBColor("FontDate", Color.WHITE));
		cupboard().withDatabase(db).put(new DBColor("FontCurrentTemp", Color.WHITE));
		cupboard().withDatabase(db).put(new DBColor("FontHighTemp", Color.WHITE));
		cupboard().withDatabase(db).put(new DBColor("FontLowTemp", Color.WHITE));
	}

	public List<DBColor> getListOfColors(){
		QueryResultIterable<DBColor> itr =cupboard().withDatabase(sqLiteDatabase).query(DBColor.class).query();
		for (DBColor color : itr) {
			listOfDBColors.add(color);
		}
		return listOfDBColors;
	}

	public void createItems(){
		cupboard().withDatabase(sqLiteDatabase).put(new DBColor("Background", Color.BLACK));
		cupboard().withDatabase(sqLiteDatabase).put(new DBColor("FontWeekday", Color.BLUE));
		cupboard().withDatabase(sqLiteDatabase).put(new DBColor("FontDate", Color.WHITE));
		cupboard().withDatabase(sqLiteDatabase).put(new DBColor("FontCurrentTemp", Color.BLUE));
		cupboard().withDatabase(sqLiteDatabase).put(new DBColor("FontHighTemp", Color.WHITE));
		cupboard().withDatabase(sqLiteDatabase).put(new DBColor("FontLowTemp", Color.WHITE));
	}

	public void deleteDatabase(){
		cupboard().withDatabase(sqLiteDatabase).delete(DBColor.class, null);
	}

	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
		cupboard().withDatabase(db).upgradeTables();
	}

}
