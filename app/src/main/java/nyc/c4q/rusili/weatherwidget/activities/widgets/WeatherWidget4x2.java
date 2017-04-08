package nyc.c4q.rusili.weatherwidget.activities.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.bumptech.glide.request.target.AppWidgetTarget;

import java.text.SimpleDateFormat;
import java.util.Date;

import nyc.c4q.rusili.weatherwidget.R;
import nyc.c4q.rusili.weatherwidget.activities.BaseWeatherWidget;
import nyc.c4q.rusili.weatherwidget.network.JSON.CurrentObservation;
import nyc.c4q.rusili.weatherwidget.network.JSON.ForecastDay;
import nyc.c4q.rusili.weatherwidget.network.RetroFitBase;
import nyc.c4q.rusili.weatherwidget.utilities.Constants;
import nyc.c4q.rusili.weatherwidget.utilities.GlideWrapper;

public class WeatherWidget4x2 extends BaseWeatherWidget {
    private RetroFitBase retroFitBase;
    private RetroFitBase.RetrofitListener retrofitListener;
    private RemoteViews remoteViews;
    private AppWidgetTarget appWidgetTarget;
    private GlideWrapper glideWrapper;
    private CharSequence twoCharWeekday;

    @Override
    public void onUpdate (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int widgetID : appWidgetIds) {
            remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout_4x2);

            glideWrapper = new GlideWrapper(context, remoteViews, widgetID);
            setUpUpdateOnClick(context, appWidgetManager, appWidgetIds, widgetID);
            downloadWeatherData(context, appWidgetManager, widgetID, remoteViews);
            setUpUpdateOnClick(context, appWidgetManager, appWidgetIds, widgetID);
        }
    }

    private void downloadWeatherData (final Context context, final AppWidgetManager appWidgetManager, final int widgetID, final RemoteViews remoteViews) {
        retroFitBase = new RetroFitBase(Constants.DEVELOPER_KEY.API_KEY, 11020);
        retroFitBase.setRetrofitListener(retrofitListener = new RetroFitBase.RetrofitListener() {
            @Override
            public void onConditionsRetrieved (CurrentObservation currentObservation) {
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
        remoteViews.setTextViewText(R.id.widget_component_main_hitemp, String.valueOf(forecastDays[0].getHigh().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
        remoteViews.setTextViewText(R.id.widget_component_main_lowtemp, String.valueOf(forecastDays[0].getLow().getFahrenheit()) + Constants.SYMBOLS.DEGREE);

        remoteViews.setTextViewText(R.id.widget_component_day_weekday2, getTwoCharWeekday(forecastDays[1].getDate().getWeekdayShort()));
        remoteViews.setTextViewText(R.id.widget_component_day_day2, forecastDays[1].getDate().getDay());
        remoteViews.setTextViewText(R.id.widget_component_day_temphigh2, String.valueOf(forecastDays[1].getHigh().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
        remoteViews.setTextViewText(R.id.widget_component_day_templow2, String.valueOf(forecastDays[1].getLow().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
        //remoteViews.setTextViewText(R.id.widget_component_day_text_precip2, String.valueOf(forecastDays[1].getAvehumidity() + "%"));
        //remoteViews.setTextViewText(R.id.widget_component_day_text_wind2, String.valueOf(forecastDays[1].getAvewind().getMph()));
        glideWrapper.inflateImage(R.id.widget_component_day_icon2, forecastDays[1].getIcon_url());

        remoteViews.setTextViewText(R.id.widget_component_day_weekday3, getTwoCharWeekday(forecastDays[2].getDate().getWeekdayShort()));
        remoteViews.setTextViewText(R.id.widget_component_day_day3, forecastDays[2].getDate().getDay());
        remoteViews.setTextViewText(R.id.widget_component_day_temphigh3, String.valueOf(forecastDays[2].getHigh().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
        remoteViews.setTextViewText(R.id.widget_component_day_templow3, String.valueOf(forecastDays[2].getLow().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
        //remoteViews.setTextViewText(R.id.widget_component_day_text_precip3, String.valueOf(forecastDays[2].getAvehumidity() + "%"));
        //remoteViews.setTextViewText(R.id.widget_component_day_text_wind3, String.valueOf(forecastDays[2].getAvewind().getMph()));
        glideWrapper.inflateImage(R.id.widget_component_day_icon3, forecastDays[2].getIcon_url());

        remoteViews.setTextViewText(R.id.widget_component_day_weekday4, getTwoCharWeekday(forecastDays[3].getDate().getWeekdayShort()));
        remoteViews.setTextViewText(R.id.widget_component_day_day4, forecastDays[3].getDate().getDay());
        remoteViews.setTextViewText(R.id.widget_component_day_temphigh4, String.valueOf(forecastDays[3].getHigh().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
        remoteViews.setTextViewText(R.id.widget_component_day_templow4, String.valueOf(forecastDays[3].getLow().getFahrenheit()) + Constants.SYMBOLS.DEGREE);
        //remoteViews.setTextViewText(R.id.widget_component_day_text_precip4, String.valueOf(forecastDays[3].getAvehumidity() + "%"));
        //remoteViews.setTextViewText(R.id.widget_component_day_text_wind4, String.valueOf(forecastDays[3].getAvewind().getMph()));
        glideWrapper.inflateImage(R.id.widget_component_day_icon4, forecastDays[3].getIcon_url());
        
        appWidgetManager.updateAppWidget(widgetID, remoteViews);
    }

    private void updateMain (AppWidgetManager appWidgetManager, int widgetID, CurrentObservation currentObservation) {
        Date now = new Date();
        SimpleDateFormat weekday = new SimpleDateFormat("E");
        SimpleDateFormat month = new SimpleDateFormat("MMM");
        SimpleDateFormat day = new SimpleDateFormat("dd");

        remoteViews.setTextViewText(R.id.widget_component_main_weekday, weekday.format(now));
        remoteViews.setTextViewText(R.id.widget_component_main_month, month.format(now));
        remoteViews.setTextViewText(R.id.widget_component_main_day, ifSingleDigit(day.format(now)));
        remoteViews.setTextViewText(R.id.widget_component_main_currenttemp, String.valueOf((int) currentObservation.getTemp_f()) + Constants.SYMBOLS.DEGREE);
        //remoteViews.setTextViewText(R.id.widget_component_main_text_precip, currentObservation.getRelative_humidity());
        //remoteViews.setTextViewText(R.id.widget_component_main_text_wind, String.valueOf(currentObservation.getWind_mph()));
        glideWrapper.inflateImage(R.id.widget_component_main_icon, currentObservation.getIcon_url());

        appWidgetManager.updateAppWidget(widgetID, remoteViews);
    }

    private CharSequence ifSingleDigit (String format) {
        CharSequence charSequence = null;

        if (Integer.parseInt(format) < 10){
            charSequence = String.valueOf(format.charAt(1));
        }
        return charSequence;
    }

    private void setUpUpdateOnClick(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, int widgetID){
        Intent intent = new Intent(context, WeatherWidget4x2.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //remoteViews.setOnClickPendingIntent(R.id.widget_layout_4x2_container, pendingIntent);
        appWidgetManager.updateAppWidget(widgetID, remoteViews);
    }

    public CharSequence getTwoCharWeekday (String weekdayShort) {
        CharSequence charSequence = null;

        if (weekdayShort.contains("T") || weekdayShort.contains("S")){
            charSequence = weekdayShort.substring(0,2);
        } else {
            charSequence = String.valueOf(weekdayShort.charAt(0));
        }

        return charSequence;
    }
}
