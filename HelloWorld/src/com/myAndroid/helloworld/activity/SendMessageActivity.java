package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.service.SendMessageService;

public class SendMessageActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendmessagelayout);
		Button button = (Button) findViewById(R.id.button);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent startService = new Intent(SendMessageActivity.this,
						SendMessageService.class);
				startService(startService);
			}
		});

	}
}
