package com.dreamcatcher.bicycle.view.settinglistview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.dreamcatcher.bicycle.R;
import com.dreamcatcher.bicycle.util.Utils;

public class SettingListview extends ListView {

	public SettingListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SettingListview(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SettingListview(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		setBackgroundResource(R.drawable.listview_bg);
		setCacheColorHint(Utils.getColor(R.color.transparent));
		setDivider(Utils.getDrawable(R.drawable.setting_listview_divider));
		setDividerHeight(1);
		setPadding(1, 1, 1, 1);
	}
}
