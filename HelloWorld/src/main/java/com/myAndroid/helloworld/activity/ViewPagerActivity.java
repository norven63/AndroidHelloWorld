package com.myAndroid.helloworld.activity;

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

import com.myAndroid.helloworld.R;

/**
 * 注意这里是继承自android.support.v4.app.FragmentActivity,
 * 为了调用其getSupportFragmentManager()方法
 * 
 * 另外，这里实现了一个ViewPage循环滑动的效果
 * 
 */
public class ViewPagerActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.viewpager);

		final String[] titles = new String[] { "三角形", "机器人", "三角形", "机器人" };

		// 图片资源队列,用以作为adapter的数据源
		final List<ImageView> imaViews = new ArrayList<ImageView>();

		/**
		 * 注意下面填充队列的细节。为了实现ViewPager循环滑动的效果，初始化数据的顺序为[1,0,1,0]。1是逻辑上最后一张图，0是逻辑上第一张图。
		 * 在Activity加载的时候默认加载物理上第二张图（即第二位置的0）。
		 * 当图片切换到物理上第一张图时（即第一位置的1）时，则自动切到物理第三张图（即第三位置的1）。
		 * 同理，当图片切换到物理上第四张图时（即第四位置的0）时，则自动切到物理第二张图（即第二位置的0）。
		 */
		ImageView imageView0 = new ImageView(this);
		imageView0.setBackgroundResource(R.drawable.image_view2);
		imaViews.add(imageView0);

		ImageView imageView1 = new ImageView(this);
		imageView1.setBackgroundResource(R.drawable.test_image);
		imaViews.add(imageView1);

		ImageView imageView2 = new ImageView(this);
		imageView2.setBackgroundResource(R.drawable.image_view2);
		imaViews.add(imageView2);

		ImageView imageView3 = new ImageView(this);
		imageView3.setBackgroundResource(R.drawable.test_image);
		imaViews.add(imageView3);

		final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			private int selectedPage;

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// arg0:当前页面索引，及你点击滑动的页面
				// arg1:当前页面偏移的百分比
				// arg2:当前页面偏移的像素位置
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// arg0分为3种状态:
				// arg0==1时表示正在滑动
				// arg0==2时表示滑动完毕了
				// arg0==0时表示什么都没做。
				// 当页面开始滑动的时候，三种状态的变化顺序为（1，2，0）

				if (arg0 == 0) {// 处理的时机是arg0==0，切记！
					if (selectedPage == 0) {
						viewPager.setCurrentItem(2, false);// 请注意这里的false参数,该参数表示切换item的时候不需要滑动动画效果
					} else if (selectedPage == 3) {
						viewPager.setCurrentItem(1, false);
					}
				}
			}

			@Override
			public void onPageSelected(int arg0) {
				selectedPage = arg0;
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

		viewPager.setCurrentItem(1);
	}
}
