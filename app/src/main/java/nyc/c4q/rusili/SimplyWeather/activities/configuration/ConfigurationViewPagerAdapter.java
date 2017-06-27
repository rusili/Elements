package nyc.c4q.rusili.SimplyWeather.activities.configuration;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nyc.c4q.rusili.SimplyWeather.activities.configuration.color.FragmentColor;

public class ConfigurationViewPagerAdapter extends FragmentPagerAdapter {
	private static int NUM_PAGES = 3;

	public ConfigurationViewPagerAdapter (FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	@Override
	public Fragment getItem (int position) {
		switch (position) {
			case 0: // Fragment # 0 - This will show FirstFragment
				return FragmentColor.newInstance(0, "Colors");
			default:
				return null;
		}
	}

	@Override
	public CharSequence getPageTitle (int position) {
		return "Page " + position;
	}

	@Override
	public int getCount () {
		return NUM_PAGES;
	}
}
