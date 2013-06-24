package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.myAndroid.helloworld.R;

public class SecondActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.secondactivity);

    Intent intent = this.getIntent();
    Toast.makeText(this, intent.getStringExtra("success"), 1).show();

    Button button2main = (Button) findViewById(R.id.secondB2main);
    button2main.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        intent.putExtra("back", "This is a visitting!");
        startActivity(intent);
      }
    });

    Button button2third = (Button) findViewById(R.id.secondB2third);
    button2third.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
        intent.putExtra("name", "Luke");
        int[]i=new int[1];
        i[0]=1;
        intent.putExtra("age", i);
        startActivity(intent);
      }
    });

    Button buttonBack = (Button) findViewById(R.id.secondBack);
    buttonBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("back", "I'll be back!");
        setResult(10, intent);
        finish();
      }
    });
  }
}
