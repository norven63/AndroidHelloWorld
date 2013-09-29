package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;

import android.content.Context;

import android.os.Environment;

import android.util.Log;

import android.content.Intent;

import android.view.View;

import android.os.Bundle;

import android.app.Activity;

public class SendOrderReceiverActivity extends Activity {
  public static final String RECEIVER_PERMISSION = "com.myAndroid.hellorld.orderReceiver.permission";
  public static final String RECEIVER_ACTIVITION = "com.myAndroid.hellorld.orderReceiver.action";

  public static final String DATA = "data";

  public void sendOrderReceiver(View view) {
    Intent intent = new Intent(RECEIVER_ACTIVITION);
    intent.putExtra(DATA, "Data from activity!");
    sendOrderedBroadcast(intent, RECEIVER_PERMISSION);

    Log.i("ORDER_RECEIVER", "Receiver has been sent!");
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_sendorderreceiver);
  }
}
