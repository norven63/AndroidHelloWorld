package com.myAndroid.helloworld.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetSrcFromNet {
  private static String SRCURL = "http://tieba.baidu.com/tb/0608fangyuan-02.jpg";

  // 获得图片的2进制数组
  public static byte[] getSteamByte(InputStream in) throws Exception {
    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

    byte[] buffer = new byte[1024 * 2];

    int length = 0;

    while ((length = in.read(buffer)) != -1) {
      byteOut.write(buffer, 0, length);
    }
    in.close();

    return byteOut.toByteArray();
  }

  public static void main(String[] args) throws Exception {
    // 通过资源URL获得连接，并设置Requst方式和timeout
    URL srcURL = new URL(SRCURL);
    HttpURLConnection conn = (HttpURLConnection) srcURL.openConnection();
    conn.setRequestMethod("GET");
    conn.setConnectTimeout(5 * 1000);

    // 获得该连接中，输入流的二进制数据
    InputStream inStream = conn.getInputStream();
    byte[] srcDate = getSteamByte(inStream);

    // 新建文件，把数据写入其中
    File file = new File("test.jpg");
    FileOutputStream fileOut = new FileOutputStream(file);
    fileOut.write(srcDate);
    fileOut.close();
  }
}
