package com.walt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		init();
	}
	
	private void init(){
		ImageView imageView = (ImageView)findViewById(R.id.spashImg);
		imageView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(SplashScreen.this, Main.class));
				finish();
			}
		});
	}
	
}
