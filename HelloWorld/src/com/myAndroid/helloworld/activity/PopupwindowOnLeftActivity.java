package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.PopupWindow;
import android.widget.Toast;

public class PopupwindowOnLeftActivity extends Activity {
  // 声明PopupWindow对象的引用
  private PopupWindow popupWindow;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.popupwindow);

    View button_popBtn2 = findViewById(R.id.popBtn2);
    final PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.popMenu));
    popupMenu.inflate(R.menu.menu_test);
    popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

      @Override
      public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
          case R.id.menuItem1:
            Toast.makeText(PopupwindowOnLeftActivity.this, "menuItem1被点击了", Toast.LENGTH_SHORT).show();
            break;
          case R.id.menuItem2:
            Toast.makeText(PopupwindowOnLeftActivity.this, "menuItem2被点击了", Toast.LENGTH_SHORT).show();
            break;
          default:
            break;
        }

        return true;
      }
    });

    button_popBtn2.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        popupMenu.show();
      }
    });

    // 点击按钮弹出菜单
    Button button = (Button) findViewById(R.id.popBtn);

    // 点击弹出左侧菜单的显示方式
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        getPopupWindow();
        // 这里选择在哪个View下方显示
        popupWindow.showAsDropDown(findViewById(R.id.popWindow));
        // 这里可以尝试其它效果方式,如popupWindow.showAsDropDown(v,
        // (screenWidth-dialgoWidth)/2, 0);
        // popupWindow.showAtLocation(findViewById(R.id.layout),
        // Gravity.CENTER, 0);
      }
    });
  }

  /**
   * 创建PopupWindow
   */
  protected void initPopuptWindow() {
    // 获取自定义布局文件pop.xml的视图
    View popupWindow_view = getLayoutInflater().inflate(R.layout.popupmenu, null, false);

    // 创建PopupWindow实例,100,220分别是宽度和高度,并将视图放入
    popupWindow = new PopupWindow(popupWindow_view, 220, 220, true);// 这个true表示弹出框会focus，导致它出现后其他控件无法点击
    popupWindow.setOutsideTouchable(true);
    // 点击其他地方消失
    popupWindow_view.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (popupWindow != null && popupWindow.isShowing()) {
          popupWindow.dismiss();
        }

        return false;
      }
    });

  }

  /***
   * 获取PopupWindow实例
   */
  private void getPopupWindow() {
    if (null != popupWindow) {
      popupWindow.dismiss();
      return;
    } else {
      initPopuptWindow();
    }
  }
}