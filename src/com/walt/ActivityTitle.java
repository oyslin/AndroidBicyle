package com.walt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityTitle extends LinearLayout {
	private TextView mTitleText = null;

	public ActivityTitle(Context context) {
		super(context);
		initTitle();
	}

	public ActivityTitle(Context context, AttributeSet attrs) {
		super(context, attrs);	
		initTitle();
	}
	
	private void initTitle() {
		LayoutInflater inflater = LayoutInflater.from(BicyleApp.getInstance());
		inflater.inflate(R.layout.activity_title, this, true);
		mTitleText = (TextView) findViewById(R.id.activity_title_text);
	}
	
	/**
	 * set activity tile
	 * @param title
	 */
	public void setActivityTitle(CharSequence title) {
		mTitleText.setText(title);
	}
	
	/**
	 * set activity title
	 * @param strId string id in strings.xml
	 */
	public void setActivityTitle(int strId) {
		mTitleText.setText(strId);
	}
}
