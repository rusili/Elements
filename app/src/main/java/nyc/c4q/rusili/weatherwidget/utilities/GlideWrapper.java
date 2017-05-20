package nyc.c4q.rusili.weatherwidget.utilities;

import android.content.Context;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;

import nyc.c4q.rusili.weatherwidget.R;

public class GlideWrapper {
	//        appWidgetTarget = new AppWidgetTarget(context, remoteViews, R.id.widget_component_main_icon, widgetID);
	//        Glide.with(context).load(currentObservation.getIcon_url()).asBitmap().into(appWidgetTarget);

	private Context context;
	private RemoteViews remoteViews;
	private int widgetID;
	private AppWidgetTarget appWidgetTarget;

	public GlideWrapper (Context contextP, RemoteViews remoteViewsP, int widgetIDP) {
		this.context = contextP;
		this.remoteViews = remoteViewsP;
		this.widgetID = widgetIDP;
	}

	public void inflateImage (int viewIDP, String resource) {
		int resourceID = chooseIcon(resource);
		appWidgetTarget = new AppWidgetTarget(context, remoteViews, viewIDP, widgetID);
		Glide.with(context).load(resourceID).asBitmap().into(appWidgetTarget);
	}

	private int chooseIcon (String resource) {
		int resourceID = 0;
		if (resource.contains("clear") || resource.contains("sunny")) {
			resourceID = R.drawable.ic_sun;
		} else if (resource.contains("clear-night")) {
			resourceID = R.drawable.ic_moon;
		} else if (resource.contains("storms")) {
			resourceID = R.drawable.ic_lightning;
		} else if (resource.contains("rain")) {
			resourceID = R.drawable.ic_rain;
		} else if (resource.contains("sleet")) {
			resourceID = R.drawable.ic_hail;
		} else if (resource.contains("snow") || resource.contains("flurries")) {
			resourceID = R.drawable.ic_snow;
		} else if (resource.contains("wind")) {
			resourceID = R.drawable.ic_wind;
		} else if (resource.contains("fog") || (resource.contains("hazy"))) {
			resourceID = R.drawable.ic_fog;
		} else if (resource.contains("cloudy")) {
			resourceID = R.drawable.ic_cloud;
		} else if (resource.contains("mostly") || resource.contains("partly")) {
			resourceID = R.drawable.ic_partly_cloudy;
		}
		return resourceID;
	}
}
