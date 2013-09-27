package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.service.TestService;

public class ThirdActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.thirdactivity);

    Intent intent = this.getIntent();
    String name = intent.getStringExtra("name");
    int[] age = intent.getIntArrayExtra("age");

    TextView text = (TextView) findViewById(R.id.thirdT);
    if (age != null) {
      text.setText("Name is " + name + " ,age is " + age[0]);
    }

    Button button = (Button) findViewById(R.id.thirdB);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 隐式意图
        Intent intent_ = new Intent();
        intent_.setAction("com.myAndroid.helloworld.second");
        // Data和Type不要分开来写,否则报错,原因不明
        intent_.setDataAndType(Uri.parse("thehead://www.itcast.cn"), "image/gif");
        // intent_.setData(Uri.parse("thehead://www.itcast.cn"));
        // intent_.setType("image/gif");

        intent_.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent_);
      }
    });

    Intent serviceIntent = new Intent(this, TestService.class);

    // 这种绑定,不会导致同生共死
    // startService(serviceIntent);

    // 此类绑定会同生共死
    bindService(serviceIntent, new ServiceConnection() {
      @Override
      public void onServiceConnected(ComponentName name, IBinder service) {
        // TODO Auto-generated method stub

      }

      @Override
      public void onServiceDisconnected(ComponentName name) {
        // TODO Auto-generated method stub

      }
    }, Context.BIND_AUTO_CREATE);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    Log.i("TestService", "Activity-onDestory()");
  }
}
