package com.myAndroid.helloworld.service;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.File;
import java.io.FileOutputStream;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MediaDownloadService extends Service {
  public final class MyBinder extends Binder {
    // TODO
  }

  /**
   * InnerClass: Media下载监听
   */
  private class CustomProgressListener implements MediaHttpDownloaderProgressListener {
    @Override
    public void progressChanged(final MediaHttpDownloader downloader) {
      switch (downloader.getDownloadState()) {
      case MEDIA_IN_PROGRESS:
        // 下载中
        break;
      case MEDIA_COMPLETE:
        // 下载完成
        break;
      default:

        break;
      }
    }
  }

  private final IBinder myBinder = new MyBinder();

  private final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
  private final JsonFactory JSON_FACTORY = new JacksonFactory();

  @Override
  public IBinder onBind(Intent intent) {
    return myBinder;
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    doDownLoad("http://url...");// 注意,这里其实是需要建立一个Task来下载的

    return Service.START_STICKY;
  }

  private void doDownLoad(String url) {
    try {
      MediaHttpDownloader downloader = new MediaHttpDownloader(HTTP_TRANSPORT, new HttpRequestInitializer() {
        @Override
        public void initialize(HttpRequest request) {
          request.setParser(new JsonObjectParser(JSON_FACTORY));
        }
      });

      downloader.setDirectDownloadEnabled(false);// 设为多块下载
      downloader.setChunkSize(MediaHttpUploader.MINIMUM_CHUNK_SIZE);// 设置每一块的大小
      // downloader.setDirectDownloadEnabled(true); // 设为单块下载

      downloader.setProgressListener(new CustomProgressListener());// 设置监听器

      String filePath = "本地文件path";// 用于指定存放在哪里
      File newFile = new File(filePath);
      FileOutputStream outputStream = new FileOutputStream(newFile);

      downloader.download(new GenericUrl(url), outputStream);// 启动下载
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
