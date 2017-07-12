package nyc.c4q.rusili.SimplyWeather.activities.configuration;

public class ConfigurationPresenter implements ConfigurationInterface.Presenter {
	private ConfigurationInterface.View configurationView;

	public ConfigurationPresenter(ConfigurationInterface.View configurationView){
		this.configurationView = configurationView;
	}

}
