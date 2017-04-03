package nyc.c4q.rusili.weatherwidget.utilities;

import android.content.Context;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;

public class GlideWrapper {
    //        appWidgetTarget = new AppWidgetTarget(context, remoteViews, R.id.widget_component_main_icon, widgetID);
    //        Glide.with(context).load(currentObservation.getIcon_url()).asBitmap().into(appWidgetTarget);
    private Context context;
    private RemoteViews remoteViews;
    private int widgetID;
    private AppWidgetTarget appWidgetTarget;

    public GlideWrapper(Context contextP, RemoteViews remoteViewsP, int widgetIDP){
        this.context = contextP;
        this.remoteViews = remoteViewsP;
        this.widgetID = widgetIDP;
    }

    public void inflateImage(int viewIDP, String resource){
        appWidgetTarget = new AppWidgetTarget(context, remoteViews, viewIDP, widgetID);
        Glide.with(context).load(resource).asBitmap().into(appWidgetTarget);
    }
}
