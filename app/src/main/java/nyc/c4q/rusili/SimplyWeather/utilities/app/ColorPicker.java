package nyc.c4q.rusili.SimplyWeather.utilities.app;

import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

import nyc.c4q.rusili.SimplyWeather.R;
import nyc.c4q.rusili.SimplyWeather.utilities.generic.DebugMode;

public class ColorPicker {
	private static ColorPicker colorPicker;
	private int colorSave;

	private ColorPicker () {
	}

	public static ColorPicker getColorPicker () {
		if (colorPicker == null) {
			colorPicker = new ColorPicker();
		}
		return colorPicker;
	}

	public void setColorPicker (final AlertDialog alertDialog, int defaultColor) {
		colorSave = defaultColor;
		final ImageView imageViewColor = (ImageView) alertDialog.findViewById(R.id.fragmentconfigurationcolor_colorcircle);
		imageViewColor.getDrawable().setColorFilter(colorSave, PorterDuff.Mode.MULTIPLY);

		final HSLColorPicker colorPicker = (HSLColorPicker) alertDialog.findViewById(R.id.fragmentconfigurationcolor_HSLColorPicker);
		colorPicker.setColor(defaultColor);
		colorPicker.setColorSelectionListener(new SimpleColorSelectionListener() {
			@Override
			public void onColorSelected (int color) {
				imageViewColor.getDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
				colorSave = color;
				DebugMode.logD(alertDialog.getContext(), String.valueOf(color));
			}
		});
	}

	public int getColorSave () {
		return colorSave;
	}
}