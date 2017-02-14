package nyc.c4q.rusili.weatherwidget.network.JSON;

public class ResponseForecastDay {
    forecast forecast;

    public forecast getForecast () {
        return forecast;
    }

    public class forecast {
        simpleforecast simpleforecast;

        public ResponseForecastDay.forecast.simpleforecast getSimpleforecast () {
            return simpleforecast;
        }

        public class simpleforecast {
            ForecastDay[] forecastday;

            public ForecastDay[] getForecastday () {
                return forecastday;
            }
        }
    }
}
