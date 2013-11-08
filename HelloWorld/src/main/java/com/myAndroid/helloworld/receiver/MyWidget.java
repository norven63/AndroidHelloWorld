package com.myAndroid.helloworld.receiver;

import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.activity.SecondActivity;
import com.myAndroid.helloworld.service.MyRemoteViewService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyWidget extends AppWidgetProvider {
  public static final String WIDGET_BORADCAST1 = "myAndroid.helloworld.receiver.MYWIDGET_BORADCAST_ic001";

  public static final String WIDGET_BORADCAST2 = "myAndroid.helloworld.receiver.MYWIDGET_BORADCAST_ic_launcher";

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);

    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.mywidget);
    if (intent.getAction().equals(WIDGET_BORADCAST1)) {
      remoteViews.setImageViewResource(R.id.widget_imageview, R.drawable.ic_001);
    } else if (intent.getAction().equals(WIDGET_BORADCAST2)) {
      remoteViews.setImageViewResource(R.id.widget_imageview, R.drawable.ic_launcher);
    }

    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
    appWidgetManager.updateAppWidget(new ComponentName(context, MyWidget.class), remoteViews);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    for (int i = 0; i < appWidgetIds.length; i++) {
      int appWidgetId = appWidgetIds[i];

      RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.mywidget);
      remoteViews.setImageViewResource(R.id.widget_imageview, R.drawable.ic_launcher);

      Intent intent2 = new Intent(WIDGET_BORADCAST2);
      PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, intent2, 0);
      remoteViews.setOnClickPendingIntent(R.id.widget_imageview, pendingIntent2);

      Intent intent = new Intent(WIDGET_BORADCAST1);
      PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
      remoteViews.setOnClickPendingIntent(R.id.widget_textview, pendingIntent);

      Intent intent3 = new Intent(context, MyRemoteViewService.class);
      remoteViews.setRemoteAdapter(R.id.widget_stackview, intent3);// 这会触发RemoteViewsServiced的onGetViewFactory方法,而这个intent3可以用来传递参数

      Intent intent5 = new Intent(context, SecondActivity.class);// 这个intent5是无法传参数的
      PendingIntent pendingIntentTemplate = PendingIntent.getActivity(context, 0, intent5, 0);
      remoteViews.setPendingIntentTemplate(R.id.widget_stackview, pendingIntentTemplate);

      appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
      // appWidgetManager.updateAppWidget(new ComponentName(context, MyWidget.class), remoteViews);
    }

  }
}
