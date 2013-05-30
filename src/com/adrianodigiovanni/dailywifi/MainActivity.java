package com.adrianodigiovanni.dailywifi;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(true);

		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		TabsAdapter tabsAdapter = new TabsAdapter(this, viewPager);

		tabsAdapter.addTab(actionBar.newTab()
				.setIcon(R.drawable.ic_action_home), HomeFragment.class, null);
		tabsAdapter.addTab(
				actionBar.newTab().setIcon(R.drawable.ic_action_users),
				AccountsFragment.class, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case R.id.action_add:
			Intent intent = new Intent(this,
					AddEditAccountActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private static class TabsAdapter extends FragmentPagerAdapter implements
			ActionBar.TabListener, ViewPager.OnPageChangeListener {

		private final Context mContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabInfoList = new ArrayList<TabInfo>();

		private static final class TabInfo {
			private final Class<?> clss;
			private final Bundle args;

			public TabInfo(Class<?> _clss, Bundle _args) {
				clss = _clss;
				args = _args;
			}
		}

		public TabsAdapter(FragmentActivity fragmentActivity,
				ViewPager viewPager) {
			super((FragmentManager) fragmentActivity
					.getSupportFragmentManager());

			mContext = fragmentActivity;
			mActionBar = fragmentActivity.getActionBar();

			mViewPager = viewPager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo tabInfo = new TabInfo(clss, args);
			tab.setTag(tabInfo);
			tab.setTabListener(this);
			mTabInfoList.add(tabInfo);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo tabInfo = mTabInfoList.get(position);
			return Fragment.instantiate(mContext, tabInfo.clss.getName(),
					tabInfo.args);
		}

		@Override
		public int getCount() {
			return mTabInfoList.size();
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			mActionBar.setSelectedNavigationItem(position);
		}

		@Override
		public void onTabReselected(Tab tab,
				FragmentTransaction fragmentTransaction) {
		}

		@Override
		public void onTabSelected(Tab tab,
				FragmentTransaction fragmentTransaction) {
			Object tag = tab.getTag();
			for (int i = 0; i < mTabInfoList.size(); i++) {
				if (mTabInfoList.get(i) == tag) {
					mViewPager.setCurrentItem(i);
				}
			}
		}

		@Override
		public void onTabUnselected(Tab tab,
				FragmentTransaction fragmentTransaction) {
		}

	}
}
