package nyc.c4q.rusili.weatherwidget.activities.configuration.color;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nyc.c4q.rusili.weatherwidget.R;

public class FragmentColor extends Fragment implements FragmentColorInterface.View{
	private FragmentColorInterface.Presenter presenter;
	private View view;

	private int page;
	private String title;

	public static FragmentColor newInstance(int page, String title) {
		FragmentColor fragmentColor = new FragmentColor();
		Bundle args = new Bundle();
		args.putInt("Page", page);
		args.putString("Title", title);
		fragmentColor.setArguments(args);
		return fragmentColor;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		page = getArguments().getInt("Page", 0);
		title = getArguments().getString("Title");
	}

	@Nullable
	@Override
	public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_configuration_font, container, false);
		initialize();
		return view;
	}

	@Override
	public void initialize () {
		if (presenter == null) {
			setPresenter(presenter);
		}

		setViews();
	}

	@Override
	public void setViews () {

	}

	@Override
	public void setPresenter (FragmentColorInterface.Presenter presenter) {
		this.presenter = new FragmentColorPresenter(this);
	}
}
