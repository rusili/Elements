package nyc.c4q.rusili.SimplyWeather.utilities.generic;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import nyc.c4q.rusili.SimplyWeather.R;
import nyc.c4q.rusili.SimplyWeather.utilities.app.ColorPicker;

public class MyAlertDialog {
	private static MyAlertDialog myAlertDialog;
	private onClickColorListener onClickColorListener;

	private MyAlertDialog () {
	}

	public static MyAlertDialog getMyAlertDialog () {
		if (myAlertDialog == null) {
			myAlertDialog = new MyAlertDialog();
		}
		return myAlertDialog;
	}

	public void showDefaultAlert (Context context, String title, String message, String buttonText) {
		new AlertDialog.Builder(context)
			  .setTitle(title)
			  .setMessage(message)
			  .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
				  public void onClick (DialogInterface dialog, int which) {
					  System.exit(0);
				  }
			  })
			  .setIcon(android.R.drawable.ic_dialog_alert)
			  .show();
	}

	public void showColorPicker (final onClickColorListener onClickColorListener, final View view, final int defaultColor) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
		builder.setView(R.layout.colorpicker);
		builder.setCancelable(true);
		builder.setTitle(R.string.colorpicker_title);
		builder.setNegativeButton(R.string.cancel, null);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick (DialogInterface dialog, int which) {
				int color = ColorPicker.getColorPicker().getColorSave();
				view.setBackgroundColor(color);
				onClickColorListener.returnColor(view, color);
			}
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		ColorPicker.getColorPicker().setColorPicker(alertDialog, defaultColor);
	}

	public interface onClickColorListener {
		public void returnColor(View view, int color);
	}
}
