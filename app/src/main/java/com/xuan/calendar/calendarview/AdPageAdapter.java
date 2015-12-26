package com.xuan.calendar.calendarview;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class AdPageAdapter extends PagerAdapter
{
    private List<View> views = null;

    /**
     * 初始化数据源, 即View数组
     */
    public AdPageAdapter(List<View> views)
    {
        this.views = views;
    }

    /**
     * 从ViewPager中删除集合中对应索引的View对象
     */
    @Override
    public void destroyItem(View container, int position, Object object)
    {
        ((ViewPager) container).removeView(views.get(position));
    }

    /**
     * 获取ViewPager的个数
     */
    @Override
    public int getCount()
    {
        return views.size();
    }

    /**
     * 从View集合中获取对应索引的元素, 并添加到ViewPager中
     */
    @Override
    public Object instantiateItem(View container, int position)
    {
        ViewGroup parent = (ViewGroup) views.get(position).getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
//		container.addView(views.get(position));
        ((ViewPager) container).addView(views.get(position), 0);
        return views.get(position);
    }

    /**
     * 是否将显示的ViewPager页面与instantiateItem返回的对象进行关联 这个方法是必须实现的
     */
    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }
}
