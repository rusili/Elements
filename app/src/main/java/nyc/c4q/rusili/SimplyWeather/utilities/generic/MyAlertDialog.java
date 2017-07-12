package nyc.c4q.rusili.SimplyWeather.utilities.generic;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import nyc.c4q.rusili.SimplyWeather.R;

public class MyAlertDialog {
	private static MyAlertDialog myAlertDialog;

	private MyAlertDialog () {
	}

	public static MyAlertDialog getMyAlertDialog () {
		if (myAlertDialog == null) {
			myAlertDialog = new MyAlertDialog();
		}
		return myAlertDialog;
	}

	public void showDefaultAlert(Context context, String title, String message, String buttonText){
		new AlertDialog.Builder(context)
			  .setTitle(title)
			  .setMessage(message)
			  .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int which) {
					  System.exit(0);
				  }
			  })
			  .setIcon(android.R.drawable.ic_dialog_alert)
			  .show();
	}


	public void showColorPicker (Context context, String title, int defaultColor) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(R.layout.colorpicker);
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setNegativeButton("Cancel", null);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick (DialogInterface dialog, int which) {
				//ColorPicker.getColorPicker().getColorSave();
			}
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		ColorPicker.getColorPicker().setColorPicker(alertDialog, defaultColor);
	}

}
