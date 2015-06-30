package com.myAndroid.helloworld.service;

import com.myAndroid.helloworld.R;
import android.content.Context;
import android.widget.RemoteViews;
import android.content.Intent;
import android.widget.RemoteViewsService;

public class MyRemoteViewService extends RemoteViewsService {
  public class MyRemoteViewsFactory implements RemoteViewsFactory {
    private final Context context;
    private String[] content;

    public MyRemoteViewsFactory(Context context) {
      super();
      this.context = context;
    }

    @Override
    public int getCount() {
      if (content != null) {
        return content.length;
      } else {
        return 1;
      }
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public RemoteViews getLoadingView() {
      // TODO Auto-generated method stub
      // 使用默认的加载View
      return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {

      RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.mywidget_item);
      remoteViews.setTextViewText(R.id.widget_item_textview, content[position]);

      /**
       * 该Intent用于填充在AppWidgetProvider里RemoteViews调用setOnClickPendingIntent()时指定的PendingIntent.
       * 它可以传送参数
       */
      Intent fillIntent = new Intent();
      fillIntent.putExtra("widget", "Come from widget");// 传送参数
      remoteViews.setOnClickFillInIntent(R.id.widget_item_textview, fillIntent);

      return remoteViews;
    }

    @Override
    public int getViewTypeCount() {
      return 1;
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }

    /**
     * 第一次被new的时候运行
     */
    @Override
    public void onCreate() {
      content = new String[] {"aaa", "bbb", "ccc"};
    }

    /**
     * 此方法在AppWidgetManager调用notifyAppWidgetViewDataChanged(appWidgetId, viewId)时运行
     */
    @Override
    public void onDataSetChanged() {
      // TODO
    }

    @Override
    public void onDestroy() {
      content = null;
    }
  }

  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new MyRemoteViewsFactory(getApplicationContext());
  }
}
