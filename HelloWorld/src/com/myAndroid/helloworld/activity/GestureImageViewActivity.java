package com.myAndroid.helloworld.activity;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.myAndroid.helloworld.R;

public class GestureImageViewActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gesture_imageview);

		ImageView imageView = (ImageView) findViewById(R.id.image);

		PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
		mAttacher.update();
	}
}
