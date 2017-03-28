package nyc.c4q.rusili.weatherwidget.activities.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import nyc.c4q.rusili.weatherwidget.R;
import nyc.c4q.rusili.weatherwidget.activities.BaseWeatherWidget;
import nyc.c4q.rusili.weatherwidget.network.JSON.CurrentObservation;
import nyc.c4q.rusili.weatherwidget.network.JSON.ForecastDay;
import nyc.c4q.rusili.weatherwidget.network.RetroFitBase;
import nyc.c4q.rusili.weatherwidget.utilities.Constants;

public class WeatherWidget4x2 extends BaseWeatherWidget {
    private RetroFitBase retroFitBase;
    private RetroFitBase.RetrofitListener retrofitListener;
    private RemoteViews remoteViews;

    @Override
    public void onUpdate (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int widgetID : appWidgetIds) {
            remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout_4x2);

            //setUpUpdateOnClick(context, appWidgetManager, appWidgetIds, widgetID, remoteViews);
            downloadWeatherData(context, appWidgetManager, widgetID, remoteViews);
            //getCurrentObservation();

        }
    }

    private void downloadWeatherData (final Context context, final AppWidgetManager appWidgetManager, final int widgetID, final RemoteViews remoteViews) {
        retroFitBase = new RetroFitBase(Constants.DEVELOPER_KEY.API_KEY, 11020);
        retroFitBase.setRetrofitListener(retrofitListener = new RetroFitBase.RetrofitListener() {
            @Override
            public void onConditionsRetrieved (CurrentObservation currentObservation) {
                Log.d("onConditionsRetrieved", String.valueOf(currentObservation.getTemp_f()));
                updateMain(appWidgetManager, widgetID, currentObservation);
            }

            @Override
            public void onForecastDaysRetrieved (ForecastDay[] forecastDays) {
                updateDays(appWidgetManager, widgetID, forecastDays);
            }
        });
        retroFitBase.getConditions();
        retroFitBase.getForecastDay();
    }

    private void updateDays (AppWidgetManager appWidgetManager, int widgetID, ForecastDay[] forecastDays) {
        remoteViews.setTextViewText(R.id.widget_component_main_hitemp, String.valueOf(forecastDays[0].getHigh().getFahrenheit()));
        remoteViews.setTextViewText(R.id.widget_component_main_lowtemp, String.valueOf(forecastDays[0].getLow().getFahrenheit()));

        remoteViews.setTextViewText(R.id.widget_component_day_weekday2, forecastDays[1].getDate().getWeekdayShort());
        remoteViews.setTextViewText(R.id.widget_component_day_day2, forecastDays[1].getDate().getDay());
        remoteViews.setTextViewText(R.id.widget_component_day_temphigh2, String.valueOf(forecastDays[1].getHigh().getFahrenheit()));
        remoteViews.setTextViewText(R.id.widget_component_day_templow2, String.valueOf(forecastDays[1].getLow().getFahrenheit()));
        remoteViews.setTextViewText(R.id.widget_component_day_text_precip2, String.valueOf(forecastDays[1].getAvehumidity()));
        remoteViews.setTextViewText(R.id.widget_component_day_text_wind2, String.valueOf(forecastDays[1].getAvewind().getMph()));

        remoteViews.setTextViewText(R.id.widget_component_day_weekday3, forecastDays[2].getDate().getWeekdayShort());
        remoteViews.setTextViewText(R.id.widget_component_day_day3, forecastDays[2].getDate().getDay());
        remoteViews.setTextViewText(R.id.widget_component_day_temphigh3, String.valueOf(forecastDays[2].getHigh().getFahrenheit()));
        remoteViews.setTextViewText(R.id.widget_component_day_templow3, String.valueOf(forecastDays[2].getLow().getFahrenheit()));
        remoteViews.setTextViewText(R.id.widget_component_day_text_precip3, String.valueOf(forecastDays[2].getAvehumidity()));
        remoteViews.setTextViewText(R.id.widget_component_day_text_wind3, String.valueOf(forecastDays[2].getAvewind().getMph()));

        appWidgetManager.updateAppWidget(widgetID, remoteViews);
    }

    private void updateMain (AppWidgetManager appWidgetManager, int widgetID, CurrentObservation currentObservation) {
        Date now = new Date();
        SimpleDateFormat weekday = new SimpleDateFormat("E");
        SimpleDateFormat month = new SimpleDateFormat("MMM");
        SimpleDateFormat day = new SimpleDateFormat("dd");

        Log.d("updateMain: ", weekday.format(now));
        remoteViews.setTextViewText(R.id.widget_component_main_weekday, weekday.format(now));
        remoteViews.setTextViewText(R.id.widget_component_main_month, month.format(now));
        remoteViews.setTextViewText(R.id.widget_component_main_day, day.format(now));
        remoteViews.setTextViewText(R.id.widget_component_main_currenttemp, String.valueOf((int) currentObservation.getTemp_f()));
        //remoteViews.setImageViewUri(R.id.widget_component_main_icon, Uri.parse(currentObservation.getIcon_url()));
        remoteViews.setTextViewText(R.id.widget_component_main_text_precip, currentObservation.getRelative_humidity());
        remoteViews.setTextViewText(R.id.widget_component_main_text_wind, String.valueOf(currentObservation.getWind_mph()));

        appWidgetManager.updateAppWidget(widgetID, remoteViews);
    }

    private void setUpUpdateOnClick(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, int widgetID, RemoteViews remoteViews){
        Intent intent = new Intent(context, WeatherWidget4x2.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
        // PendingIntent updates the widget when the button is clicked
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        appWidgetManager.updateAppWidget(widgetID, remoteViews);

    }
}
