package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class IntentActivity extends Activity {
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);

    // 创建一个Intent用来匹配哪些动作应该出现在菜单中
    // Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:18610013076"));

    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_SEND);
    intent.setType("image/*");

    // Menu设置
    int menuGroup = 0;
    int menuItemId = 0;
    int menuItemOrder = Menu.NONE;

    // 提供调用动作的组件的名称,通常为当前的Activity
    ComponentName caller = getComponentName();

    // 定义应该添加的一些Intent
    Intent[] specifiIntents = null;

    // 通过前面Intent创建的菜单项将填充整个数组
    MenuItem[] outSpecificItems = null;

    // 设置任意的选择标识
    int flags = Menu.FLAG_APPEND_TO_GROUP;

    // 填充菜单
    menu.addIntentOptions(menuGroup, menuItemId, menuItemOrder, caller, specifiIntents, intent, flags, outSpecificItems);

    return true;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Button button = new Button(this);

    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");// 注意这里的type
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
        intent.putExtra(Intent.EXTRA_TEXT, "The contents.");
        startActivity(Intent.createChooser(intent, "请选择："));// 显示选择器
      }
    });

    setContentView(button);
  }
}
