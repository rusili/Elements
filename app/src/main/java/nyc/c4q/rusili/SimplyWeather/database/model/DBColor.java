package nyc.c4q.rusili.SimplyWeather.database.model;

public class DBColor {
	public Long _id; // for cupboard
	public String name;
	public int color;

	public DBColor () {
	}

	public DBColor (String name, int color) {
		this.name = name;
		this.color = color;
	}

	public Long get_id () {
		return _id;
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
