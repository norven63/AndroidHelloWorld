package com.myAndroid.helloworld.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.myAndroid.helloworld.R;

import java.io.File;

public class DownloadManagerActivity extends Activity {
  private DownloadManager downloadManager;
  private long referenceID;
  private BroadcastReceiver broadcastReceiver;
  private final Uri uri = Uri.parse("http://www.trinea.cn/wp-content/uploads/2013/05/downloadDemo2.gif");

  @SuppressLint("NewApi")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.downloadactivity);

    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

    Button startButton = (Button) findViewById(R.id.startdownload);
    // 启动下载
    startButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View arg0) {
        DownloadManager.Request request = new Request(uri);
        // 设置下载存放路径
        // request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "xxxxx2.gif");
        request.setDestinationInExternalFilesDir(DownloadManagerActivity.this, Environment.DIRECTORY_DOWNLOADS, "xxxxx2.gif");

        // 自定义Notification
        request.setTitle("testdownload");
        request.setDescription("Test");

        // 开始下载
        referenceID = downloadManager.enqueue(request);
      }
    });

    // 点击"Cancel"按钮取消正在下载的任务
    Button cancelButton = (Button) findViewById(R.id.canceldownload);
    cancelButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View arg0) {
        Query pauseQuery = new Query();
        pauseQuery.setFilterByStatus(DownloadManager.STATUS_RUNNING);

        Cursor cursor = downloadManager.query(pauseQuery);
        int idIndex = cursor.getColumnIndex(DownloadManager.COLUMN_ID);
        int nameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);

        while (cursor.moveToNext()) {
          Toast.makeText(DownloadManagerActivity.this, cursor.getString(nameIndex), Toast.LENGTH_SHORT).show();

          downloadManager.remove(cursor.getLong(idIndex));
        }
      }
    });

    // 点击任务栏下载提示时,取消所有下载任务
    IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED);
    broadcastReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context arg0, Intent arg1) {
        Toast.makeText(DownloadManagerActivity.this, "Clicke Notification!", Toast.LENGTH_SHORT).show();

        // 获得被点击notification的id
        long ids[] = arg1.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
        for (long id : ids) {
          // 查询本次点击的对象
          Query query = new Query();
          query.setFilterById(id);

          Cursor cursor = downloadManager.query(query);

          int fileNameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
          String fileName = cursor.getString(fileNameIndex);

          Toast.makeText(DownloadManagerActivity.this, fileName, Toast.LENGTH_SHORT).show();

          // 删除本次点击的对象
          downloadManager.remove(id);
        }
      }
    };

    registerReceiver(broadcastReceiver, intentFilter);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    unregisterReceiver(broadcastReceiver);
  }
}
