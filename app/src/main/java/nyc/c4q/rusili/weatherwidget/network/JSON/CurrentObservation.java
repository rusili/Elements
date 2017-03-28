package nyc.c4q.rusili.weatherwidget.network.JSON;

public class CurrentObservation {
    String forecast_url;
    String observation_time;
    String weather;
    double temp_f;
    double temp_c;
    String icon_url;
    String relative_humidity;
    double wind_mph;

    public double getWind_mph () {
        return wind_mph;
    }

    public String getRelative_humidity () {
        return relative_humidity;
    }

    public String getForecast_url () {
        return forecast_url;
    }

    public String getIcon_url () {
        return icon_url;
    }

    public double getTemp_c () {
        return temp_c;
    }

    public double getTemp_f () {
        return temp_f;
    }

    public String getWeather () {
        return weather;
    }

    public String getObservation_time () {
        return observation_time;
    }

    public class display_location {
        String city;
        String state;
        String county;
        String zip;
        String latitude;
        String longitude;

        public String getCity () {
            return city;
        }

        public String getState () {
            return state;
        }

        public String getCounty () {
            return county;
        }

        public String getZip () {
            return zip;
        }

        public String getLatitude () {
            return latitude;
        }

        public String getLongitude () {
            return longitude;
        }
    }
}
