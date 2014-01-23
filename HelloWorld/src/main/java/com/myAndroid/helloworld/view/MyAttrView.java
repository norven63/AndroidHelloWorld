package com.myAndroid.helloworld.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.myAndroid.helloworld.R;

/**
 * 类名在attrs.xml文件里定义的<declare-styleable>设置name时用到,需要一致
 * 
 * @author Administrator
 */
public class MyAttrView extends TextView {
	public MyAttrView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public MyAttrView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public MyAttrView(Context context) {
		super(context);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyAttrView);
		int testAttr = typedArray.getInt(R.styleable.MyAttrView_testattr, 10);
		typedArray.recycle();

		setText("自定义属性测试: " + testAttr);
	}
}
