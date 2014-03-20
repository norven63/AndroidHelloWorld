package com.myAndroid.test;

import android.test.ActivityInstrumentationTestCase2;

import com.myAndroid.helloworld.activity.MainActivity;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private MainActivity mainActivity;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mainActivity = getActivity();
	}

	public void adf() {
		mainActivity.getActionBar();
	}
}
