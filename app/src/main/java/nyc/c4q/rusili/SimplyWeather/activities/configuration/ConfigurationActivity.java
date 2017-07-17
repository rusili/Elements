package nyc.c4q.rusili.SimplyWeather.activities.configuration;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import nyc.c4q.rusili.SimplyWeather.R;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.Constants;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.DebugMode;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

public class ConfigurationActivity extends AppCompatActivity implements ConfigurationInterface.View {
	private ConfigurationInterface.Presenter configurationPresenter;

	private ViewPager viewPager;
	private TabLayout tabLayout;

	private ConfigurationViewPagerAdapter configurationViewPagerAdapter;

	private int appWidgetId;

	@Override
	protected void onCreate (@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);
		setResult(RESULT_CANCELED);

		initialize();
	}

	private void initialize () {
		configurationPresenter = new ConfigurationPresenter(this);

		getAppWidgetID();
		setToolbar();
		setViews();
		setViewPager();


	}

	private void setViewPager () {
		configurationViewPagerAdapter = new ConfigurationViewPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(configurationViewPagerAdapter);
	}

	private void setToolbar () {
		Toolbar toolbar = (Toolbar) findViewById(R.id.activity_configuration_toolbar);
		setSupportActionBar(toolbar);
	}

	private void setViews () {
		viewPager = (ViewPager) findViewById(R.id.activityconfiguration_viewpager);
		tabLayout = (TabLayout) findViewById(R.id.activityconfiguration_tablayout);
	}

	private void getAppWidgetID () {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			DebugMode.logD(this, String.valueOf(extras.getInt(EXTRA_APPWIDGET_ID)));
			appWidgetId = extras.getInt(EXTRA_APPWIDGET_ID,
				  INVALID_APPWIDGET_ID);
		}
		if (appWidgetId == INVALID_APPWIDGET_ID) {
			finish();
		}
	}

	private void finishConfiguration () {
		Intent intent = new Intent();
		intent.setAction(Constants.ACTION.CONFIG_COMPLETE);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		getMenuInflater().inflate(R.menu.menu_configuration, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.activity_configuration_menu_button_accept) {
			finishConfiguration();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Context getContext () {
		return this;
	}
}