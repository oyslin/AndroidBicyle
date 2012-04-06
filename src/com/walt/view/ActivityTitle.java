package com.walt.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.walt.BicycleApp;
import com.walt.R;

public class ActivityTitle extends LinearLayout {
	private TextView mTitleText = null;
	private ImageView mRightImage = null;

	public ActivityTitle(Context context) {
		super(context);
		initTitle();
	}

	public ActivityTitle(Context context, AttributeSet attrs) {
		super(context, attrs);	
		initTitle();
	}
	
	private void initTitle() {
		LayoutInflater inflater = LayoutInflater.from(BicycleApp.getInstance());
		inflater.inflate(R.layout.activity_title, this, true);
		mTitleText = (TextView) findViewById(R.id.activity_title_text);
		mRightImage = (ImageView) findViewById(R.id.activity_title_right_image);		
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
	
	public void setRightImage(int resId, final IActivityTitleRightImageClickEvent rightImageClickEvent){
		mRightImage.setImageResource(resId);
		mRightImage.setVisibility(View.VISIBLE);
		if(rightImageClickEvent != null){
			mRightImage.setOnClickListener(new OnClickListener() {				
				public void onClick(View v) {
					rightImageClickEvent.onRightImageClicked();
				}
			});
		}
	}
	
	public interface IActivityTitleRightImageClickEvent{
		void onRightImageClicked();
	}
	
}
