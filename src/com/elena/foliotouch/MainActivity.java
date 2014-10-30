package com.elena.foliotouch;

//1st version on GitHub

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends FragmentActivity {

	MyPageAdapter pageAdapter;
	MyFolioReceiver receiver;
	public static boolean currentState = true;
	public List<Integer> colors = Arrays.asList(Color.BLUE, Color.GRAY,
			Color.RED, Color.YELLOW, Color.MAGENTA, Color.GREEN, Color.WHITE);
	public int i = 0;
	String appVersion;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		try {
			appVersion = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.d("FolioTouch", "App version not found " + e.getMessage());
		}
		// to show app over the locked screen
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		// register receiver for folio state changes
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.motorola.intent.ACTION_FOLIO_STATE_CHANGED");

		MyFolioReceiver receiver = new MyFolioReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// do something based on the intent's action
				// //////
				Bundle extras = intent.getExtras();
				if (extras != null) {
					for (String key : extras.keySet()) {
						Object value = extras.get(key);
						Log.d("MyFolioApp", String.format("%s %s (%s)", key,
								value.toString(), value.getClass().getName()));
					}
				}
				// ///////////
				if (extras.getBoolean("FOLIO_STATE") == false) {
					Log.d("MyFolioApp", "closed");
					currentState = false;

					drawClosedView(extras.getInt("FOLIO_LEFT_POSITION"),
							extras.getInt("FOLIO_TOP_POSITION"),
							extras.getInt("FOLIO_WIDTH"),
							extras.getInt("FOLIO_HEIGHT"));

				} else {
					Log.d("MyFolioApp", "open");
					currentState = true;
					drawOpenView();
				}
				// /////////

				// //////
			}
		};
		registerReceiver(receiver, filter);
		// //////////////////////////

		setContentView(R.layout.activity_main);

		List<Fragment> fragments = getFragments();

		pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
		ViewPager pager = (ViewPager) findViewById(R.id.viewpager);

		pager.setAdapter(pageAdapter);
		// pager.setPageMargin(1);
	}

	@Override
	public void onResume() {
		super.onResume();
		// Log.d("FolioTouch", "onResume called");

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(receiver);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			// do nothing
		}
	}

	public void drawClosedView(int left, int top, int width, int height) {

		View v = (View) findViewById(android.R.id.content);
		v.setBackgroundColor(Color.LTGRAY);
		// v.setLeft(left);
		// v.setTop(top);
		// v.setRight(left + width);
		// v.setBottom(top + height);
		v.invalidate();
	}

	public void drawOpenView() {
		// TODO
		View v = (View) findViewById(android.R.id.content);
		v.setBackgroundColor(Color.WHITE);
		// v.setLeft(0);
		// v.setTop(0);
		v.invalidate();
	}

	public void onButtonClick(View view) {
		Button btn = (Button) findViewById(R.id.button1);
		View v = (View) findViewById(android.R.id.content);
		v.setBackgroundColor(colors.get(i % 7));
		i++;
		v.invalidate();
	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();
		fList.add(MyFragment.newInstance("Screen1"));
		fList.add(MyFragment.newInstance("Screen2"));
		fList.add(MyFragment.newInstance("Screen3"));
		return fList;
	}

	// ////////////

	// ///////////////////////

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.about) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setMessage(
					"Folio Touch Test Application\n" + "\u00a9 2014 Elena Last")
					.setTitle("Folio Touch v." + appVersion);

			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			AlertDialog dialog = builder.create();
			dialog.show();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}

	}

}