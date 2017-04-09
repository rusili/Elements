package nyc.c4q.rusili.weatherwidget.activities;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import nyc.c4q.rusili.weatherwidget.R;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

public class ConfigurationActivity extends AppCompatActivity{
    private int appWidgetId;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        setResult(RESULT_CANCELED);

        getAppWidgetID();
    }

    private void getAppWidgetID(){
        appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
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

    private void finishConfiguration(){
        Intent intentConfig = new Intent();
        intentConfig.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, intentConfig);
        finish();
    }
}
