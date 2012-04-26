package com.dreamcatcher.bicycle.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamcatcher.bicycle.BicycleApp;
import com.dreamcatcher.bicycle.R;

public class ActivityTitle extends LinearLayout {
	private TextView mTitleText = null;
	private ImageView mRightImage = null;
	private ImageView mRightImageSplit = null;
	
	private ImageView mLeftImageView = null;
	private ImageView mLeftImageSplit = null;
	
	private boolean mRightImageSelected = false;
	private boolean mLeftImageSelected = false;

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
		mRightImageSplit = (ImageView) findViewById(R.id.activity_title_right_image_split);
		mLeftImageView = (ImageView) findViewById(R.id.activity_title_left_image);
		mLeftImageSplit = (ImageView) findViewById(R.id.activity_title_left_image_split);
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
	
	public void setRightImage(int resId, final IActivityTitleRightImageClickEvent rightImageClickEvent, final boolean changImage){
		mRightImage.setImageResource(resId);
		mRightImage.setVisibility(View.VISIBLE);
		mRightImageSplit.setVisibility(View.VISIBLE);
		if(rightImageClickEvent != null){
			mRightImage.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					rightImageClickEvent.onRightImageClicked();
					if(changImage){
						mRightImageSelected = !mRightImageSelected;
						mRightImage.setSelected(mRightImageSelected);
					}
				}
			});
		}
	}
	
	public void setLeftImage(int resId, final IActivityTitleLeftImageClickEvent leftImageClickEvent, final boolean changeImage){
		mLeftImageView.setImageResource(resId);
		mLeftImageView.setVisibility(View.VISIBLE);
		mLeftImageSplit.setVisibility(View.VISIBLE);
		
		if(leftImageClickEvent != null){
			mLeftImageView.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					leftImageClickEvent.onLeftImageClicked();
					if(changeImage){
						mLeftImageSelected = !mLeftImageSelected;
						mLeftImageView.setSelected(mLeftImageSelected);
					}
				}
			});
		}
	}
	
	public interface IActivityTitleRightImageClickEvent{
		void onRightImageClicked();
	}
	
	public interface IActivityTitleLeftImageClickEvent{
		void onLeftImageClicked();
	}
}
