package nyc.c4q.rusili.weatherwidget.activities.configuration;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import nyc.c4q.rusili.weatherwidget.R;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

public class ActivityConfiguration extends AppCompatActivity {
	private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

	@Override
	protected void onCreate (@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);
		setResult(RESULT_CANCELED);

		initialize();
	}

	private void initialize () {
		getAppWidgetID();
		setToolbar();
	}

	private void setToolbar () {
		Toolbar toolbar = (Toolbar) findViewById(R.id.activity_configuration_toolbar);
		setSupportActionBar(toolbar);
	}

	private void getAppWidgetID () {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			appWidgetId = extras.getInt(EXTRA_APPWIDGET_ID,
				  INVALID_APPWIDGET_ID);
		}
		if (appWidgetId == INVALID_APPWIDGET_ID) {
			finish();
		}
	}

	private void finishConfiguration () {
		Intent intentConfig = new Intent();
		intentConfig.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		setResult(RESULT_OK, intentConfig);
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
}
