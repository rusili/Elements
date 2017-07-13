package nyc.c4q.rusili.SimplyWeather.database.model;

public class dbColor {
	private String name;
	private int color;

	public dbColor (){}

	public dbColor (String name, int color){
		this.name = name;
		this.color = color;
	}

	public String getName () {
		return name;
	}

	public void setColor (int color) {
		this.color = color;
	}

	public int getColor () {
		return color;
	}
}
