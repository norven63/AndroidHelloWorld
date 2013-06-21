package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;

import android.widget.ArrayAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

public class PopupwindowOnLeftActivity extends Activity {
  // 声明PopupWindow对象的引用
  private PopupWindow popupWindow;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.popupwindow);
    // 点击按钮弹出菜单
    Button button = (Button) findViewById(R.id.popBtn);

    // 点击弹出左侧菜单的显示方式
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        getPopupWindow();
        // 这里是位置显示方式,在按钮的左下角
        popupWindow.showAsDropDown(v);
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
    // 创建PopupWindow实例,100,220分别是宽度和高度
    popupWindow = new PopupWindow(popupWindow_view, 100, 220, true);

    ListView listView = (ListView) popupWindow_view.findViewById(R.id.menuList);
    String[] menus = { "1", "2", "3" };
    listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, menus));

    // 点击其他地方消失
    popupWindow_view.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (popupWindow != null && popupWindow.isShowing()) {
          popupWindow.dismiss();
          popupWindow = null;
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