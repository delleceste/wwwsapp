package it.giacomos.android.wwwsapp.slidingtablayout;

/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.giacomos.android.wwwsapp.R;
import it.giacomos.android.wwwsapp.fragments.ForecastFragment;
import it.giacomos.android.wwwsapp.fragments.SituationFragment;
import it.giacomos.android.wwwsapp.network.state.ViewType;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic sample which shows how to use {@link com.example.android.common.view.SlidingTabLayout}
 * to display a custom {@link ViewPager} title strip which gives continuous feedback to the user
 * when scrolling.
 */
public class ForecastTabbedFragment extends Fragment {

	/**
	 * This class represents a tab to be displayed by {@link ViewPager} and it's associated
	 * {@link SlidingTabLayout}.
	 */
	static class SamplePagerItem {
		private final CharSequence mTitle;
		private final int mIndicatorColor;
		private final int mDividerColor;
		private final ViewType mViewType;

		SamplePagerItem(CharSequence title, ViewType vt, int indicatorColor, int dividerColor) {
			mTitle = title;
			mIndicatorColor = indicatorColor;
			mDividerColor = dividerColor;
			mViewType = vt;
		}

		/**
		 * @return A new {@link Fragment} to be displayed by a {@link ViewPager}
		 */
		Fragment createFragment() {
	    	
	    	if(mViewType == ViewType.HOME)
	    		return new SituationFragment();
	    	else
	    	{
	    		Bundle args = new Bundle();
	    		args.putString("ViewType", mViewType.name());
	    		ForecastFragment ff = new ForecastFragment();
	    		ff.setArguments(args);
	    		return ff;
	    	}
		}

		ViewType getViewType()
		{
			return mViewType;
		}
		
		/**
		 * @return the title which represents this tab. In this sample this is used directly by
		 * {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
		 */
		CharSequence getTitle() {
			return mTitle;
		}

		/**
		 * @return the color to be used for indicator on the {@link SlidingTabLayout}
		 */
		int getIndicatorColor() {
			return mIndicatorColor;
		}

		/**
		 * @return the color to be used for right divider on the {@link SlidingTabLayout}
		 */
		int getDividerColor() {
			return mDividerColor;
		}
	}

	static final String LOG_TAG = "ForecastTabbedFragment";

	/**
	 * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
	 * above, but is designed to give continuous feedback to the user when scrolling.
	 */
	private SlidingTabLayout mSlidingTabLayout;

	/**
	 * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
	 */
	private ViewPager mViewPager;

	/**
	 * List of {@link SamplePagerItem} which represent this sample's tabs.
	 */
	private List<SamplePagerItem> mTabs = new ArrayList<SamplePagerItem>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int indicatorColor = getResources().getColor(R.color.accent);
		mSelectedPage = 0;
		// BEGIN_INCLUDE (populate_tabs)
		/**
		 * Populate our tab list with tabs. Each item contains a title, indicator color and divider
		 * color, which are used by {@link SlidingTabLayout}.
		 */
		mTabs.add(new SamplePagerItem(getResources().getString(R.string.situation), // Title
				ViewType.HOME,
				indicatorColor, // Indicator color
				Color.GRAY // Divider color
				));

		mTabs.add(new SamplePagerItem(getResources().getString(R.string.today_title), // Title
				ViewType.TODAY,
				indicatorColor, // Indicator color
				Color.GRAY // Divider color
				));

		mTabs.add(new SamplePagerItem(getResources().getString(R.string.tomorrow_title), // Title
				ViewType.TOMORROW,
				indicatorColor, // Indicator color
				Color.GRAY // Divider color
				));

		mTabs.add(new SamplePagerItem(getResources().getString(R.string.two_days_title), // Title
				ViewType.TWODAYS,
				indicatorColor, // Indicator color
				Color.GRAY // Divider color
				));

		mTabs.add(new SamplePagerItem(getResources().getString(R.string.three_days_title), // Title
				ViewType.THREEDAYS,
				indicatorColor, // Indicator color
				Color.GRAY // Divider color
				));

		mTabs.add(new SamplePagerItem(getResources().getString(R.string.four_days_title), // Title
				ViewType.FOURDAYS,
				indicatorColor, // Indicator color
				Color.GRAY // Divider color
				));

		// END_INCLUDE (populate_tabs)
	}

	/**
	 * Inflates the {@link View} which will be displayed by this {@link Fragment}, from the app's
	 * resources.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.main_fragment, container);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		// BEGIN_INCLUDE (setup_slidingtablayout)
		// Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
		// it's PagerAdapter set.
		mSlidingTabLayout = (SlidingTabLayout) getActivity().findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setViewPager(mViewPager);

		// BEGIN_INCLUDE (tab_colorizer)
		// Set a TabColorizer to customize the indicator and divider colors. Here we just retrieve
		// the tab at the position, and return it's set color
		mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

			@Override
			public int getIndicatorColor(int position) {
				return mTabs.get(position).getIndicatorColor();
			}

			@Override
			public int getDividerColor(int position) {
				return mTabs.get(position).getDividerColor();
			}

		});
		// END_INCLUDE (tab_colorizer)
		// END_INCLUDE (setup_slidingtablayout)
	}
	
	// BEGIN_INCLUDE (fragment_onviewcreated)
	/**
	 * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
	 * Here we can pick out the {@link View}s we need to configure from the content view.
	 *
	 * We set the {@link ViewPager}'s adapter to be an instance of
	 * {@link SampleFragmentPagerAdapter}. The {@link SlidingTabLayout} is then given the
	 * {@link ViewPager} so that it can populate itself.
	 *
	 * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) 
	{
		// BEGIN_INCLUDE (setup_viewpager)
		// Get the ViewPager and set it's PagerAdapter so that it can display items
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
		mViewPager.setAdapter(new SampleFragmentPagerAdapter(this.getChildFragmentManager()));
		/* Set the number of pages that should be retained to either side of 
		 * the current page in the view hierarchy in an idle state
		 */
		mViewPager.setOffscreenPageLimit(3);

		// END_INCLUDE (setup_viewpager)

		/* sliding tab layout setViewPager is called inside onActivityCreated because the SlidingTabs
		 * widget may reside outside main_fragment (i.e. moved inside the toolbar in the action bar)
		 */
		
	}
	// END_INCLUDE (fragment_onviewcreated)

	public void setSelectedPage(int page)
	{
		if(mViewPager != null)
			mViewPager.setCurrentItem(page);
		mSelectedPage = page;
	}
	
	public int getSelectedPage()
	{
		return mSelectedPage;
	}
	
	/**
	 * The {@link FragmentPagerAdapter} used to display pages in this sample. The individual pages
	 * are instances of {@link ContentFragment} which just display three lines of text. Each page is
	 * created by the relevant {@link SamplePagerItem} for the requested position.
	 * <p>
	 * The important section of this class is the {@link #getPageTitle(int)} method which controls
	 * what is displayed in the {@link SlidingTabLayout}.
	 */
	class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

		SampleFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		/**
		 * Return the {@link android.support.v4.app.Fragment} to be displayed at {@code position}.
		 * <p>
		 * Here we return the value returned from {@link SamplePagerItem#createFragment()}.
		 */
		@Override
		public Fragment getItem(int i) {
			return mTabs.get(i).createFragment();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		// BEGIN_INCLUDE (pageradapter_getpagetitle)
		/**
		 * Return the title of the item at {@code position}. This is important as what this method
		 * returns is what is displayed in the {@link SlidingTabLayout}.
		 * <p>
		 * Here we return the value returned from {@link SamplePagerItem#getTitle()}.
		 */
		@Override
		public CharSequence getPageTitle(int position) 
		{
			return mTabs.get(position).getTitle();
		}
		// END_INCLUDE (pageradapter_getpagetitle)
 
    }
	
	private int mSelectedPage;

}