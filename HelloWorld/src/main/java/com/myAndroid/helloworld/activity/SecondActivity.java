package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.myAndroid.helloworld.R;

public class SecondActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.secondactivity);

     TextView textView = (TextView) findViewById(R.id.text);
     textView.setSelected(true);// 使其文字可滚动

    Intent intent = this.getIntent();
    Toast.makeText(this, intent.getStringExtra("success"), Toast.LENGTH_SHORT).show();

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
        int[] i = new int[1];
        i[0] = 1;
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

    gd = new GestureDetector(this, new SimpleOnGestureListener() {
      private final int FLING_MIN_DISTANCE = 10;// X或者y轴上移动的距离(像素)
      private final int FLING_MIN_VELOCITY = 20;// x或者y轴上的移动速度(像素/秒)

      @Override
      public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Toast.makeText(SecondActivity.this, "Fling", Toast.LENGTH_SHORT).show();

        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
          Toast.makeText(SecondActivity.this, "Fling1", Toast.LENGTH_SHORT).show();

        } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
          Toast.makeText(SecondActivity.this, "Fling", Toast.LENGTH_SHORT).show();

        }

        return true;
      }
    });
  }

  private GestureDetector gd;

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    // 请记住这段代码,为了消除事件捕获冲突,使onFling()能够正常执行
    if (gd.onTouchEvent(ev)) {
      ev.setAction(MotionEvent.ACTION_CANCEL);

    }

    return super.dispatchTouchEvent(ev);
  }

  /**
   * 处理手指touch屏幕事件
   * 
   */
  private float startPoint = 0;

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    // gd.onTouchEvent(event); //手势事件,会和手指touch事件冲突,不可同用

    switch (event.getAction()) {
    case MotionEvent.ACTION_DOWN:
      startPoint = event.getX();

      Toast.makeText(SecondActivity.this, "ACTION_DOWN:" + startPoint, Toast.LENGTH_SHORT).show();

      break;
    case MotionEvent.ACTION_UP:
      startPoint = 0;

      Toast.makeText(SecondActivity.this, "ACTION_UP:" + startPoint, Toast.LENGTH_SHORT).show();

      break;
    case MotionEvent.ACTION_MOVE:
      Toast.makeText(SecondActivity.this, "ACTION_MOVE", Toast.LENGTH_SHORT).show();

      break;
    default:

      break;
    }

    return true;
  }

}
