package nyc.c4q.rusili.weatherwidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nyc.c4q.rusili.weatherwidget.network.RetroFitGet;

public class MainActivity extends AppCompatActivity {
    String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiKey = getResources().getString(R.string.wunderground_api_key);

        initRetrofit();
    }

    private void initRetrofit(){
        RetroFitGet retroFitGet = new RetroFitGet(apiKey, 11020);
        retroFitGet.getForecastDay();
    }
}
