package nyc.c4q.rusili.SimplyWeather.activities.configuration.color;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nyc.c4q.rusili.SimplyWeather.R;
import nyc.c4q.rusili.SimplyWeather.activities.configuration.color.recyclerview.ColorRecyclerviewAdapter;

public class FragmentColor extends Fragment implements FragmentColorInterface.View, View.OnClickListener {
	private FragmentColorInterface.Presenter presenter;
	private View view;
	private RecyclerView recyclerView;

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
		recyclerView = (RecyclerView) view.findViewById(R.id.fragment_configuration_recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
		recyclerView.setAdapter(new ColorRecyclerviewAdapter(presenter));
	}

	private void setPresenter (FragmentColorInterface.Presenter presenter) {
		this.presenter = new FragmentColorPresenter(this);
	}

	@Override
	public void onDestroy () {
		super.onDestroy();
	}

	@Override
	public void onClick (View v) {
	}

	@Override
	public Context getContext () {
		return view.getContext();
	}
}