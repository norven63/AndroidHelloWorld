package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 注意这里是继承自android.support.v4.app.FragmentActivity,为了调用其getSupportFragmentManager()方法
 * 
 * @author Administrator
 * 
 */
public class ViewPagerActivity extends FragmentActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.viewpager);

    final String[] titles = new String[] {"机器人", "三角形"};

    final List<ImageView> imaViews = new ArrayList<ImageView>();

    ImageView imageView1 = new ImageView(this);
    imageView1.setBackgroundResource(R.drawable.test_image);
    imaViews.add(imageView1);

    ImageView imageView2 = new ImageView(this);
    imageView2.setBackgroundResource(R.drawable.image_view2);
    imaViews.add(imageView2);

    ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
    viewPager.setOnPageChangeListener(new OnPageChangeListener() {

      @Override
      public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

      }

      @Override
      public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public void onPageSelected(int arg0) {
        // TODO Auto-generated method stub

      }
    });

    viewPager.setAdapter(new PagerAdapter() {
      @Override
      public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imaViews.get(position));
      }

      @Override
      public int getCount() {
        return imaViews.size();
      }

      @Override
      public CharSequence getPageTitle(int position) {

        return titles[position];
      }

      @Override
      public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imaViews.get(position));

        return imaViews.get(position);
      }

      @Override
      public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
      }
    });
    // TODO
    // viewPager.setCurrentItem(item);
  }
}
