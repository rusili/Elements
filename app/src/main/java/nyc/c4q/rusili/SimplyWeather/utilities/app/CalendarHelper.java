package nyc.c4q.rusili.SimplyWeather.utilities.app;

import java.util.Calendar;
import java.util.Date;

public class CalendarHelper {
	private static CalendarHelper calendarHelper;

	private CalendarHelper(){}

	public static CalendarHelper getInstance(){
		if (calendarHelper == null){
			calendarHelper = new CalendarHelper();
		}
		return calendarHelper;
	}

	public Date getDate(){
		Date date = new Date();
		return date;
	}

	public CharSequence ifSingleDigit (String format) {
		CharSequence charSequence = format;

		if (Integer.parseInt(format) < 10) {
			charSequence = String.valueOf(format.charAt(1));
		}
		return charSequence;
	}

	public CharSequence getTwoCharWeekday (String weekdayShort) {
		CharSequence charSequence = weekdayShort;

		if (weekdayShort.contains("T") || weekdayShort.contains("S")) {
			charSequence = weekdayShort.substring(0, 2);
		} else {
			charSequence = String.valueOf(weekdayShort.charAt(0));
		}
		return charSequence;
	}

	public boolean before30Minutes () {
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.MINUTE) < 30){
			return true;
		}
		return false;
	}

	public String change24to12hour (String input) {
		int hour = Integer.parseInt(input);
		if (hour > 12){
			return String.valueOf(hour - 12);
		}
		return input;
	}
}