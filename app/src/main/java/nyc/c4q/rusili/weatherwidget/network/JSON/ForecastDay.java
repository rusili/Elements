package nyc.c4q.rusili.weatherwidget.network.JSON;

public class ForecastDay {
    int day;
    int month;
    int year;
    high high;
    low low;
    String conditions;
    int avehumidity;
    String icon_url;

    public int getDay () {
        return day;
    }

    public int getMonth () {
        return month;
    }

    public int getYear () {
        return year;
    }

    public class date{
        String monthname;
        String weekday;
        String tz_short;

        public String getMonthname () {
            return monthname;
        }

        public String getWeekday () {
            return weekday;
        }

        public String getTz_short () {
            return tz_short;
        }
    }

    public class high{
        int fahrenheit;
        int celsius;
        public int getFahrenheit () {
            return fahrenheit;
        }
        public int getCelsius () {
            return celsius;
        }
    }

    public class low{
        int fahrenheit;
        int celsius;
        public int getFahrenheit () {
            return fahrenheit;
        }
        public int getCelsius () {
            return celsius;
        }
    }

    public ForecastDay.high getHigh () {
        return high;
    }

    public ForecastDay.low getLow () {
        return low;
    }

    public String getConditions () {
        return conditions;
    }

    public int getAvehumidity () {
        return avehumidity;
    }

    public String getIcon_url () {
        return icon_url;
    }
}
