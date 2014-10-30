package com.elena.foliotouch;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ClickableViewAccessibility")
public class MyFragment extends Fragment {

	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	int w, h;
	int viewX, viewY;
	MarginLayoutParams marginParams;

	// private ViewPager mViewPager;

	public static final MyFragment newInstance(String message)

	{
		MyFragment f = new MyFragment();
		Bundle bdl = new Bundle(1);
		bdl.putString(EXTRA_MESSAGE, message);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		String message = getArguments().getString(EXTRA_MESSAGE);
		View v = inflater.inflate(R.layout.activity_my_fragment, container,
				false);
		// mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
		// Log.d("FolioTouch", "padding left " + v.getPaddingLeft()
		// + ", padding top " + v.getPaddingTop());

		TextView messageTextView = (TextView) v.findViewById(R.id.textView);
		messageTextView.setText(message);
		// Button drag = (Button) v.findViewById(R.id.button2);
		ImageView drag = (ImageView) v.findViewById(R.id.imageView);

		drag.setOnTouchListener(new MyTouchListener());
		marginParams = new MarginLayoutParams(drag.getLayoutParams());
		marginParams.setMargins(0, 0, -250, -250);

		// ////////////

		// ////////////
		viewX = v.getLeft();
		viewY = v.getTop();
		Bitmap bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.ball_300x334, null);
		w = bmp.getWidth();
		h = bmp.getHeight();

		Log.d("FolioTouch", "width = " + w + " height = " + h);

		return v;
	}

	// ////////////
	// This defines your touch listener
	private final class MyTouchListener implements OnTouchListener {
		private float mPrevX;
		private float mPrevY;

		public boolean onTouch(View view, MotionEvent event) {

			float currX, currY;
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN: {

				// mViewPager.requestDisallowInterceptTouchEvent(true);
				view.getParent().requestDisallowInterceptTouchEvent(true);
				mPrevX = event.getX();
				mPrevY = event.getY();
				Log.d("FolioTouch", "start from: " + mPrevX + "; " + mPrevY);
				break;
			}

			case MotionEvent.ACTION_MOVE: {

				view.getParent().requestDisallowInterceptTouchEvent(true);
				currX = event.getRawX();
				currY = event.getRawY();
				// Log.d("FolioTouch", "go to: " + currX + "; " + currY);
				MarginLayoutParams marginParams = new MarginLayoutParams(
						view.getLayoutParams());
				marginParams.setMargins((int) (currX - mPrevX - w * 1 / 4),
						(int) (currY - mPrevY - h * 3 / 4), -250, -250);

				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						marginParams);
				view.setLayoutParams(layoutParams);
				break;
			}

			case MotionEvent.ACTION_CANCEL:

				view.getParent().requestDisallowInterceptTouchEvent(false);
				break;

			case MotionEvent.ACTION_UP:

				view.getParent().requestDisallowInterceptTouchEvent(false);
				break;
			}

			return true;
		}
	}

}
