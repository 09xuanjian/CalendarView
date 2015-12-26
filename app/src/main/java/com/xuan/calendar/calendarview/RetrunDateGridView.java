package com.xuan.calendar.calendarview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class RetrunDateGridView extends GridView { 

    public RetrunDateGridView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
    } 

    public RetrunDateGridView(Context context) { 
        super(context); 
    } 

    public RetrunDateGridView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
    } 

    @Override 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
        super.onMeasure(widthMeasureSpec, expandSpec); 
    } 
}
