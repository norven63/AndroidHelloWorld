package com.myAndroid.helloworld.receiver;

import com.myAndroid.helloworld.activity.SendOrderReceiverActivity;

import android.os.Bundle;

import android.util.Log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SecondReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    String string = intent.getStringExtra(SendOrderReceiverActivity.DATA);

    Bundle bundle = getResultExtras(false);// 若上一个接收器没有塞Bundle时:false-则返回null;true-新的对象
    String string2 = "";
    if (null != bundle) {
      string2 = bundle.getString(FirstReceiver.DATA_FROM_FIRST);
    }
    Toast.makeText(context, "2: " + string + " : " + string2, Toast.LENGTH_SHORT).show();

    Log.i("ORDER_RECEIVER", "second");
  }
}
