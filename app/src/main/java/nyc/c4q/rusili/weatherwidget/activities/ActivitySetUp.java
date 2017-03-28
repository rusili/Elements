package nyc.c4q.rusili.weatherwidget.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nyc.c4q.rusili.weatherwidget.R;
import nyc.c4q.rusili.weatherwidget.network.RetroFitBase;

public class ActivitySetUp extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String apiKey = getResources().getString(R.string.wunderground_api_key);

        setUpRetrofit(apiKey, 11020);
    }

    private void setUpRetrofit (String apiKey, int i){
        RetroFitBase retroFitBase = new RetroFitBase(apiKey, i);
        retroFitBase.getConditions();
    }
}
