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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.myAndroid.helloworld.R;

public class DownloadManagerActivity extends Activity {
  private DownloadManager downloadManager;
  private long referenceID;
  private BroadcastReceiver broadcastReceiver;
  private Uri uri = Uri
      .parse("http://dl3.c8.sendfile.vip.xunlei.com:8000/3DMGAME-DuckTales.Remastered.CHS.Patch.v1.0-3DM.rar?key=64e447f90642e944f947db154ea92b8c&file_url=%2Fgdrive%2Fresource%2F02%2FA8%2F0279899DDD2BC8489D9C39EC55E1A29FA1D3B0A8&file_type=1&authkey=E7DBE0BDC135569BA4DAA4BEBB0F8D45B3D5C117BFA2AA94DAC3B849FC29AEAD&exp_time=1378583209&from_uid=232777&task_id=5912202781390639106&get_uid=1001782575&f=lixian.vip.xunlei.com&reduce_cdn=1&fid=uoa9VBUGmZa6Ge1W1O83tUfMB8TN508DAAAAAAJ5iZ3dK8hInZw57FXhop+h07Co&mid=666&threshold=150&tid=C3011028D270E157BAF4739FC1A04E8A&srcid=7&verno=1");

  @SuppressLint("NewApi")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.downloadactivity);

    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

    Button startButton = (Button) findViewById(R.id.startdownload);
    startButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View arg0) {
        DownloadManager.Request request = new Request(uri);
        referenceID = downloadManager.enqueue(request);

        request.setTitle("testdownload");
        request.setDescription("Test");
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
        long ids[] = arg1.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
        for (long id : ids) {
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
