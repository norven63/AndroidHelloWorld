package com.myAndroid.helloworld.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class MyWidget extends AppWidgetProvider {

  @Override
  public void onReceive(Context context, Intent intent) {
    // TODO Auto-generated method stub
    super.onReceive(context, intent);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    super.onUpdate(context, appWidgetManager, appWidgetIds);
  }

}
