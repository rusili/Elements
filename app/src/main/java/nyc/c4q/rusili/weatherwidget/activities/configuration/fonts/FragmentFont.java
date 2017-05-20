package nyc.c4q.rusili.weatherwidget.activities.configuration.fonts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nyc.c4q.rusili.weatherwidget.R;

public class FragmentFont extends Fragment implements FragmentFontInterface.View{
	private FragmentFontInterface.Presenter presenter;
	private View view;

	public static FragmentFont newInstance(int id) {
		FragmentFont fragment = new FragmentFont();

		Bundle args = new Bundle();
		args.putInt("id", id);
		fragment.setArguments(args);

		return fragment;
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
	public void setPresenter (FragmentFontInterface.Presenter presenter) {
		this.presenter = new FragmentFontPresenter(this);
	}
}
