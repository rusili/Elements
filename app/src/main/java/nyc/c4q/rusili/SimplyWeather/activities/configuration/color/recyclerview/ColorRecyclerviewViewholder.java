package nyc.c4q.rusili.SimplyWeather.activities.configuration.color.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import nyc.c4q.rusili.SimplyWeather.R;

public class ColorRecyclerviewViewholder extends RecyclerView.ViewHolder {
	private View view;
	private TextView textViewField;
	private ImageButton imageButtonColor;

	public ColorRecyclerviewViewholder (View itemView) {
		super(itemView);
		this.view = itemView;

		setViews();
	}

	private void setViews () {
		textViewField = (TextView) view.findViewById(R.id.recyclerview_color_textview);
		imageButtonColor = (ImageButton) view.findViewById(R.id.recyclerview_color_imagebutton);
	}

	public void bind (String field) {
		textViewField.setText(field);
	}
}
