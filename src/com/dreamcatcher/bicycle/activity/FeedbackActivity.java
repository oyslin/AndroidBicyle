package com.dreamcatcher.bicycle.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.dreamcatcher.bicycle.R;
import com.dreamcatcher.bicycle.util.HttpUtils;
import com.dreamcatcher.bicycle.view.ActivityTitle;

public class FeedbackActivity extends Activity {
	private EditText mEditText = null;
	private Button mSendBtn = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		this.init();
	}
	
	private void init(){
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(R.string.title_feedback);
		
		mEditText = (EditText) findViewById(R.id.feedback_edittext);
		mSendBtn = (Button) findViewById(R.id.feedback_send_btn);
		
		mSendBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				onSendBtnClicked();				
			}
		});
	}
	
	private void onSendBtnClicked(){
		String feedbackMsg = mEditText.getText().toString().trim();
		if(!"".equals(feedbackMsg)){
			sendFeedback(feedbackMsg);
		}
	}
	
	private void sendFeedback(final String msg){
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try {
					HttpUtils.sendFeedback(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}								
			}
		}).start();
		super.onBackPressed();
	};
}
