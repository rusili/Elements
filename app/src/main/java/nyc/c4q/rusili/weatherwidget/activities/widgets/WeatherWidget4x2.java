package nyc.c4q.rusili.weatherwidget.activities.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Random;

import nyc.c4q.rusili.weatherwidget.R;
import nyc.c4q.rusili.weatherwidget.activities.BaseWeatherWidget;

public class WeatherWidget4x2 extends BaseWeatherWidget {

    @Override
    public void onUpdate (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int numofWidgets = appWidgetIds.length;

        for (int widgetID : appWidgetIds) {
            String number = String.format("%03d", (new Random().nextInt(900) + 100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout_4x2);

            Intent intent = new Intent(context, WeatherWidget4x2.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
            // PendingIntent updates the widget when the button is clicked
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            appWidgetManager.updateAppWidget(widgetID, remoteViews);
        }
    }
}
