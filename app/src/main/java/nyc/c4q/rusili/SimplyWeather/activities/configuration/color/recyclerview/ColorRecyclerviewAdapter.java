package nyc.c4q.rusili.SimplyWeather.activities.configuration.color.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nyc.c4q.rusili.SimplyWeather.R;

public class ColorRecyclerviewAdapter extends RecyclerView.Adapter {
	String[] arrayOfColorFields = {"Background", "Weekday", "Date", "Current Temp", "High Temp", "Low Temp", "Location"};

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
		View view = layoutInflater.inflate(R.layout.recyclerview_color_viewholder, parent, false);
		ColorRecyclerviewViewholder colorViewholder = new ColorRecyclerviewViewholder(view);

		return colorViewholder;
	}

	@Override
	public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
		ColorRecyclerviewViewholder colorViewholder = (ColorRecyclerviewViewholder) holder;
		colorViewholder.bind(arrayOfColorFields[position]);
	}

	@Override
	public int getItemCount () {
		return arrayOfColorFields.length;
	}
}
