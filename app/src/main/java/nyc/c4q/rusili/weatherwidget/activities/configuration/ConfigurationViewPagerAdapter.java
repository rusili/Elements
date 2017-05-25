package nyc.c4q.rusili.weatherwidget.activities.configuration;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nyc.c4q.rusili.weatherwidget.activities.configuration.color.FragmentColor;
import nyc.c4q.rusili.weatherwidget.activities.configuration.fonts.FragmentFont;

public class ConfigurationViewPagerAdapter extends FragmentPagerAdapter {
	private static int NUM_PAGES = 3;

	public ConfigurationViewPagerAdapter(FragmentManager fragmentManager){
		super(fragmentManager);
	}

	@Override
	public Fragment getItem (int position) {
		switch (position) {
			case 0: // Fragment # 0 - This will show FirstFragment
				return FragmentColor.newInstance(0, "Colors");
			case 1: // Fragment # 0 - This will show FirstFragment different title
				return FragmentFont.newInstance(1, "Fonts");
			default:
				return null;
		}	}

	// Returns the page title for the top indicator
	@Override
	public CharSequence getPageTitle(int position) {
		return "Page " + position;
	}

	@Override
	public int getCount () {
		return NUM_PAGES;
	}
}
