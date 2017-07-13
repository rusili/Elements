package nyc.c4q.rusili.SimplyWeather.activities.configuration.color;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import nyc.c4q.rusili.SimplyWeather.R;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.MyAlertDialog;

public class FragmentColor extends Fragment implements FragmentColorInterface.View, View.OnClickListener {
	private FragmentColorInterface.Presenter presenter;
	private View view;
	private ImageButton imageButton;

	private int page;
	private String title;

	public static FragmentColor newInstance (int page, String title) {
		FragmentColor fragmentColor = new FragmentColor();
		Bundle args = new Bundle();
		args.putInt("Page", page);
		args.putString("Title", title);
		fragmentColor.setArguments(args);
		return fragmentColor;
	}

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		page = getArguments().getInt("Page", 0);
		title = getArguments().getString("Title");
	}

	@Nullable
	@Override
	public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_configuration_color, container, false);
		initialize();
		return view;
	}

	public void initialize () {
		if (presenter == null) {
			setPresenter(presenter);
		}
		setViews();
	}

	private void setViews () {
		imageButton = (ImageButton) view.findViewById(R.id.fragmentconfigurationcolor_imagebutton);
		imageButton.setOnClickListener(this);
	}

	private void setPresenter (FragmentColorInterface.Presenter presenter) {
		this.presenter = new FragmentColorPresenter(this);
	}

	private void createColorDialog (View colorView, int defaultColor) {
		MyAlertDialog.getMyAlertDialog().showColorPicker(colorView, "Pick a color:", defaultColor);
	}

	@Override
	public void onDestroy () {
		super.onDestroy();
	}

	@Override
	public void onClick (View v) {
		if (v == imageButton) {
			createColorDialog(v, getButtonColor(imageButton));
		}
	}

	private int getButtonColor (ImageButton imageButton) {
		return ((ColorDrawable) imageButton.getBackground()).getColor();
	}
}