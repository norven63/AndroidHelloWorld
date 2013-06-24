package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.myAndroid.helloworld.R;

public class ThirdActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.thirdactivity);

    Intent intent = this.getIntent();
    String name = intent.getStringExtra("name");
    int[] age = intent.getIntArrayExtra("age");// ("age", 24);//û��ֵʱ��Ĭ��24

    TextView text = (TextView) findViewById(R.id.thirdT);
    text.setText("Name is " + name + " ,age is " + age[0]);

    Button button = (Button) findViewById(R.id.thirdB);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // ��ʽ��ͼ
        Intent intent_ = new Intent();
        intent_.setAction("com.myAndroid.helloworld.second");
        intent_.setDataAndType(Uri.parse("thehead://www.itcast.cn"), "image/gif");
        startActivity(intent_);
      }
    });
  }
}
