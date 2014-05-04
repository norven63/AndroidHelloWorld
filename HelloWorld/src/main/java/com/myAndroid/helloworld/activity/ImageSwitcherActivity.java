package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;

import android.view.animation.AnimationUtils;

import android.view.animation.Animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

public class ImageSwitcherActivity extends Activity {
  private int i = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final ImageSwitcher imageSwitcher = new ImageSwitcher(this);

    imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
    imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

    imageSwitcher.setFactory(new ViewFactory() {
      @Override
      public View makeView() {
        ImageView imageView = new ImageView(ImageSwitcherActivity.this);
        imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        return imageView;
      }
    });

    setContentView(imageSwitcher);

    imageSwitcher.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        switch (i) {
          case 0:
            imageSwitcher.setImageResource(R.drawable.ic_launcher);
            i = 1;
            break;
          case 1:
            imageSwitcher.setImageResource(R.drawable.image_view1);
            i = 2;
            break;
          case 2:
            imageSwitcher.setImageResource(R.drawable.image_view2);
            i = 0;
            break;
        }
      }
    });
  }
}
