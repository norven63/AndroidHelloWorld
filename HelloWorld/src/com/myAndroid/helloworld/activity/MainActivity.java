package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.service.SaveFileService;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.view.KeyEvent;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
  // public static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
  // public static final JsonFactory JSON_FACTORY = new JacksonFactory();

  private EditText fileName;
  private EditText fileContent;
  private Button button;
  private Button button2;
  private Button button3;
  private Button button4;
  private Spinner spinner;
  private SaveFileService service;
  private SharedPreferences spf;
  private Editor editor;

  public void IntentManager(View view) {
    try {
      Intent i = new Intent(this, Class.forName(view.getTag().toString()));
      startActivity(i);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
   */
  @Override
  // Action Bar
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    MenuItem mi1 = menu.add(0, 0, 1, "Item1");// 第三个参数用来指定按钮的排列顺序
    mi1.setIcon(R.drawable.ic_launcher);
    // 同时以title显示
    mi1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

    MenuItem mi2 = menu.add(0, 1, 0, "Item2");
    mi2.setIcon(R.drawable.ic_launcher);
    mi2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

    MenuItem mi3 = menu.add(0, 2, 2, "Item3");
    mi3.setIcon(R.drawable.ic_launcher);

    MenuItem mi4 = menu.add(0, 3, 3, "Item4");
    mi4.setIcon(R.drawable.ic_launcher);

    return true;

  }

  // @Override
  // // 重写返回键
  // public boolean onKeyDown(int keyCode, KeyEvent event) {
  // if (keyCode == KeyEvent.KEYCODE_BACK) {
  // // TODO
  // return true;// 当按键为BACK时就会被return掉
  // }
  //
  // return super.onKeyDown(keyCode, event);
  // }

  @Override
  // 点击bar中的选项时触发的事件
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);
    if (item.getItemId() == android.R.id.home) {
      Toast.makeText(this, "应用选项home：" + item.getItemId(), Toast.LENGTH_SHORT).show();

      /**
       * 为程序图片添加返回主界面的功能; FLAG_ACTIVITY_CLEAR_TOP是为了清除back stack中的其他一系列活动,如此一来如果用户单击Back键,应用程序的其他活动将不再显示
       */
      Intent i = new Intent(this, MainActivity.class);
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(i);

    } else {
      Toast.makeText(this, "" + item.getItemId(), Toast.LENGTH_SHORT).show();
    }

    return true;
  }

  public void onSaveFile(View view) {
    fileContent = (EditText) findViewById(R.id.fileContent);
    String content = fileContent.getText().toString();
    try {
      FileOutputStream fos = this.openFileOutput("dev_test.txt", MODE_PRIVATE);
      OutputStreamWriter osw = new OutputStreamWriter(fos);
      osw.write(content);
      osw.flush();
      osw.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Toast.makeText(this, data.getStringExtra("back"), 1).show();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ActionBar aBar = getActionBar();
    aBar.setDisplayHomeAsUpEnabled(true);// 使action bar可以被点击

    fileName = (EditText) findViewById(R.id.fileName);
    fileContent = (EditText) findViewById(R.id.fileContent);
    service = new SaveFileService(this);
    button = (Button) findViewById(R.id.button);
    button2 = (Button) findViewById(R.id.button2);
    button3 = (Button) findViewById(R.id.button3);
    button4 = (Button) findViewById(R.id.button4);
    spinner = (Spinner) findViewById(R.id.spinner);
    // 建立perference文件并开启编辑
    spf = getSharedPreferences("helloworld", MODE_PRIVATE);
    editor = spf.edit();

    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        try {
          service.saveFile(fileName.getText().toString(), fileContent.getText().toString());

          Toast.makeText(MainActivity.this, R.string.saveSuccess, 1).show();
        } catch (Exception e) {
          e.printStackTrace();
          Toast.makeText(MainActivity.this, R.string.saveFail, 1).show();
        }
      }
    });
    // 保存perference文件
    button2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        try {
          editor.putString("fileName", fileName.getText().toString());
          editor.putString("fileContent", fileContent.getText().toString());
          editor.commit();

          Toast.makeText(MainActivity.this, R.string.saveSuccess, 1).show();
        } catch (Exception e) {
          e.printStackTrace();

          Toast.makeText(MainActivity.this, R.string.saveFail, 1).show();
        }
      }
    });
    // 回填perferences值到页面上
    button3.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        fileName.setText(spf.getString("fileName", ""));
        fileContent.setText(spf.getString("fileContent", ""));
      }
    });

    // 显示意图,并等待回值
    button4.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("success", "The Intent has been success!");
        startActivityForResult(intent, 100);
      }
    });

    // 下拉框
    String[] names = { "abc", "efg", "hig" };
    spinner.setAdapter(new ArrayAdapter<Object>(this, android.R.layout.simple_selectable_list_item, names));

    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        Toast.makeText(MainActivity.this, spinner.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

      }

    });

  }
}
