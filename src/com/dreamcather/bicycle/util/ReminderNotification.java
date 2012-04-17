package com.dreamcather.bicycle.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.dreamcather.bicycle.BicycleApp;
import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.activity.Main;

public class ReminderNotification {
	private final static int REMINDER_ID = 1;
	
	public static void startNotification(){
		BicycleApp bicycleApp = BicycleApp.getInstance();
		
		NotificationManager notificationManager = (NotificationManager)bicycleApp.getSystemService(Context.NOTIFICATION_SERVICE);
		
		int icon = R.drawable.ic_reminder_return_bicycle;
		CharSequence tickerText = Utils.getText(R.string.notification_ticket_text);
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		
		CharSequence contentTitle = Utils.getText(R.string.notification_content_title);
		CharSequence contentText = Utils.getText(R.string.notification_content_text);
		
		Intent notificationIntent = new Intent(bicycleApp, Main.class);
		
		notificationIntent.putExtra(Constants.IntentExtraTag.MAIN_REMINDER_FROM_NOTIFICATION, true);
		
		PendingIntent contentIntent = PendingIntent.getActivity(bicycleApp, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(bicycleApp, contentTitle, contentText, contentIntent);
		
		notificationManager.notify(REMINDER_ID, notification);
	}
}
