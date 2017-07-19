package nyc.c4q.rusili.SimplyWeather.activities.configuration.color.recyclerview;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import nyc.c4q.rusili.SimplyWeather.R;
import nyc.c4q.rusili.SimplyWeather.activities.configuration.color.FragmentColorInterface;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.MyAlertDialog;

public class ColorRecyclerviewViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
	private View view;
	private TextView textViewField;
	private ImageButton imageButtonColor;
	private MyAlertDialog.onClickColorListener onClickColorListener;
	private FragmentColorInterface.Presenter presenter;
	private int position;

	public ColorRecyclerviewViewholder (View itemView, MyAlertDialog.onClickColorListener onClickColorListener, FragmentColorInterface.Presenter presenter) {
		super(itemView);
		this.view = itemView;
		this.onClickColorListener = onClickColorListener;
		this.presenter = presenter;

		setViews();
	}

	private void setViews () {
		textViewField = (TextView) view.findViewById(R.id.recyclerview_color_textview);
		imageButtonColor = (ImageButton) view.findViewById(R.id.recyclerview_color_imagebutton);
		imageButtonColor.setOnClickListener(this);
	}

	public void bind (String field, int position) {
		textViewField.setText(field);
		this.position = position;
	}

	@Override
	public void onClick (View v) {
		createColorDialog(v, getButtonColor(imageButtonColor));
	}

	private int getButtonColor (ImageButton imageButton) {
		return ((ColorDrawable) imageButton.getBackground()).getColor();
	}

	private void createColorDialog (View v, int defaultColor) {
		onClickColorListener = new MyAlertDialog.onClickColorListener() {
			@Override
			public void returnColor (View view, int color) {
				presenter.saveColorToDatabase(view, color, position);
			}
		};
		MyAlertDialog.getMyAlertDialog().showColorPicker(onClickColorListener, v, defaultColor);
	}
}
