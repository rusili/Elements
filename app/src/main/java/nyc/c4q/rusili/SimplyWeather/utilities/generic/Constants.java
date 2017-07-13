package nyc.c4q.rusili.SimplyWeather.utilities.generic;

public class Constants {

	public interface SQLiteDatabaseID{
		int COLOR_PRIMARY = 1;
		int COLOR_SECONDARY = 2;
		int COLOR_FONT_PRIMARY = 3;
		int COLOR_FONT_SECONDARY = 4;
	}

	public interface SHARED_PREFERENCES {
		public static String FILE_NAME = "SimplyWeather";
		public static String BOOLEAN_ISDEBUGMODE = "isDebugMode";
	}

	public interface API_URL {
		public static String WUNDERGROUND = "http://api.wunderground.com/";
	}

	public interface DEVELOPER_KEY {
		public static String API_KEY = "b2cac2700e06dc8e";
	}

	public interface SYMBOLS {
		public static String DEGREE = "\u00b0";
	}

	public interface NUM_OF_DAYS {
		public static int WIDGET = 4;
	}

	public interface ACTION {
		public static String UPDATE_SCREEN = "ACTION_UPDATE_SCREEN";
		public static String UPDATE_CLICK = "ACTION_UPDATE_CLICK";
		public static String CONFIG_CLICK = "ACTION_CONFIG_CLICK";
		public static String VIEWFLIPPER_CLICK = "ACTION_VIEWFLIPPER_CLICK";
	}

	public interface UPDATE_DELAY {
		public static long MILLISECONDS = 3600000;
	}
}
