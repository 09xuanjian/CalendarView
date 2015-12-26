package com.xuan.calendar.calendarview;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


@SuppressLint("SimpleDateFormat")
public class CalendarGridViewAdapter extends BaseAdapter {

	private List<String> gvList;// 存放天
	private List<String> returnDay;//一般特殊日期
	private List<String> specialDay1;//更特殊日期1
	private List<String> specialDay2;//更特殊日期2
	private Context mContext;
	private LayoutInflater mInflater;
	private Drawable mSpecialDateDrawable3;
	private Drawable mSpecialDateDrawable1;
	private Drawable mSpecialDateDrawable2;
	private Drawable mSpecialDateTodayDrawable;
	private int widthPixels;
	private int heightPixels;
	private float density;
	private boolean popFlag = true;
	private final float YM_SIZE = 38f;
	private static String nowday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	private int mWeekdayColor = -1;
	private int mWeekendColor = -1;
	private int bgColor = -1;
	private Drawable todayDrawable;
	private int todayTextColor;
	private float mItmeHeightProportion = 0.08f;
	private float weekdayTextSize;
	private float weekendTextSize;
	private boolean moreDisplayFlag = true;

	public CalendarGridViewAdapter(List<String> gvList,
			Context context) {
		super();
		this.gvList = gvList;
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);

		widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
		heightPixels = mContext.getResources().getDisplayMetrics().heightPixels;
		density = mContext.getResources().getDisplayMetrics().density;
	}
	
	/**
	 * 设置特殊日期显示
	 * @param returnDay
	 */
	public void setSpecialDate(List<String> returnDay,List<String> specialDay1,List<String> specialDay2) {
		this.returnDay = returnDay;
		this.specialDay1 = specialDay1;
		this.specialDay2 = specialDay2;
	}

	/**
	 * 特殊背景设置
	 * @param drawable1
	 * @param drawable2
	 * @param drawable3
	 */
	public void setSpecialDateDrawable(Drawable drawable1,Drawable drawable2,Drawable drawable3) {
		if (drawable1 != null) {
			this.mSpecialDateDrawable1 = drawable1;
		}
		if (drawable2 != null) {
			this.mSpecialDateDrawable2 = drawable2;
		}
		if (drawable3 != null) {
			this.mSpecialDateDrawable3 = drawable3;
		}
	}
	
	public void setSpecialDateTodayDrawable(Drawable drawable) {
		this.mSpecialDateTodayDrawable = drawable;
	}
	/**
	 * 
	 * @category 设置字体显示颜色  
	 * @param weekdayColor -1 代表默认颜色
	 * @param weekendColor -1 代表默认颜色
	 * @param weekdayTextSize -1 代表默认
	 * @param weekendTextSize -1 代表默认
	 */
	public void setWeekdayWeekendColor(int weekdayColor,int weekendColor,float weekdayTextSize,float weekendTextSize) {
		if (weekdayColor == -1) {
			this.mWeekdayColor = mContext.getResources().getColor(
					R.color.return_calendar_normal);
		}else {
			this.mWeekdayColor = weekdayColor;
		}
		
		if (weekendColor == -1) {
			this.mWeekendColor = mContext.getResources().getColor(
					R.color.return_calendar_weekend);
		}else {
			this.mWeekendColor = weekendColor;
		}
		
		if (weekdayTextSize == -1) {
			this.weekdayTextSize = heightPixels / YM_SIZE / density;
		}else {
			this.weekdayTextSize = weekdayTextSize;
		}
		
		if (weekendTextSize == -1) {
			this.weekendTextSize = heightPixels / YM_SIZE / density;
		}else{
			this.weekendTextSize = weekendTextSize;
		}
	}
	
	/**
	 * 设置item的背景色
	 * @param color
	 */
	public void setEveryDayItemBg(int color) {
		this.bgColor = color;
	}
	
	/**
	 * 设置当天的背景（圆形等），设置当天字体的颜色
	 * @param todayDrawable
	 * @param textColor
	 */
	public void setTodayIconColor(Drawable todayDrawable,int textColor) {
		if (todayDrawable == null) {
			this.todayDrawable = mContext.getResources()
					.getDrawable(R.drawable.calendar_today_bg);
		}else {
			this.todayDrawable = todayDrawable;
		}
		
		if (textColor == -1 ){
			this.todayTextColor = mContext.getResources().getColor(
					R.color.return_calendar_now);
		}else {
			this.todayTextColor = textColor;
		}
		
	}
	
	public void setMoreDisplayFlag(boolean flag){
		this.moreDisplayFlag = flag;
	}
	
	/**
	 * 设置每个item的高度，按照比例设置，屏幕高度为基准
	 */
	public void setItemHeight(float heightProportion) {
		this.mItmeHeightProportion = heightProportion;
	}

	@Override
	public int getCount() {
		return gvList.size();
	}

	@Override
	public String getItem(int position) {
		return gvList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class GrideViewHolder {
		TextView tvDay, tv;
		LinearLayout llLayout;
		LinearLayout calendar_item_roorView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GrideViewHolder holder;
		if (convertView == null) {
			holder = new GrideViewHolder();
			convertView = mInflater.inflate(
					R.layout.common_calendar_gridview_item, null);
			holder.calendar_item_roorView = (LinearLayout) convertView.findViewById(R.id.calendar_item_roorView);
			if (bgColor != -1) {
				holder.calendar_item_roorView.setBackgroundColor(bgColor);
			}
			
			holder.tvDay = (TextView) convertView
					.findViewById(R.id.tv_calendar_day);
			holder.llLayout = (LinearLayout) convertView
					.findViewById(R.id.ll_calendar_day);
			initSize(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (GrideViewHolder) convertView.getTag();
		}
		
//		convertView.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				setPopFlag(false);
//				return false;
//			}
//		});
		
		setData(holder, position, convertView);
		return convertView;
	}
	
	public void setPopFlag(boolean flag) {
		this.popFlag = flag;
	}
	
	public boolean getPopFlag() {
		return popFlag;
	}

	private void initSize(GrideViewHolder holder, View convertView) {
		int grideHeight = (int) (heightPixels * mItmeHeightProportion);

		LinearLayout linearLayout1 = (LinearLayout) convertView
				.findViewById(R.id.ll_calendar_day);
		LayoutParams llLayoutParams = (LayoutParams) linearLayout1
				.getLayoutParams();
		llLayoutParams.height = grideHeight;

		LinearLayout linearLayout2 = holder.llLayout;
		LayoutParams llLayoutParams2 = (LayoutParams) linearLayout2
				.getLayoutParams();
		llLayoutParams2.height = grideHeight;
	}

	private void setData(GrideViewHolder holder, int position, View convertView) {
		String[] date = getItem(position).split(",");
		holder.tvDay.setText(date[1]);
		holder.tvDay.setVisibility(View.VISIBLE);
		float YM_size = heightPixels / YM_SIZE / density;
		
		if ((position + 1) % 7 == 0 || (position) % 7 == 0) {
			holder.tvDay.setTextColor(mWeekendColor);
			holder.tvDay.setTextSize(weekendTextSize);
		} else {
			holder.tvDay.setTextColor(mWeekdayColor);
			holder.tvDay.setTextSize(weekdayTextSize);
		}
		if (!date[1].equals(" ")) {
			String day = date[1];
			if (Integer.parseInt(date[1]) < 10) {
				day = "0" + date[1];
			}
			boolean twoFlag = false;
			if ((date[0] + "-" + day).equals(nowday)) {
				holder.tvDay.setTextColor(todayTextColor);
				holder.tvDay.setBackgroundDrawable(todayDrawable);
				twoFlag = true;
			}

			//设置一把的特殊日期
			if (returnDay != null && returnDay.size() > 0) {
				for (int i = 0; i < returnDay.size(); i++) {
					if (returnDay.get(i).equals((date[0] + "-" + day))) {
						if (twoFlag == false) {
							if (mSpecialDateDrawable1!=null) {
								holder.tvDay.setBackgroundDrawable(mSpecialDateDrawable1);
							}
						} else {
							holder.tvDay.setBackgroundDrawable(mSpecialDateTodayDrawable);
						}
					}
				}
			}
			//设置其他特殊日期1
			if (specialDay1 != null && specialDay1.size() > 0) {
				for (int i = 0; i < specialDay1.size(); i++) {
					if (specialDay1.get(i).equals(date[0] + "-" + day) && mSpecialDateDrawable2!=null) {
						holder.tvDay.setBackgroundDrawable(mSpecialDateDrawable2);
						holder.tvDay.setText("");
					}
				}
			}
			
			//设置其他特殊日期2
			if (specialDay2 != null && specialDay2.size() > 0) {
				for (int i = 0; i < specialDay2.size(); i++) {
					if (specialDay2.get(i).equals(date[0] + "-" + day) && mSpecialDateDrawable3 != null) {
						holder.tvDay.setBackgroundDrawable(mSpecialDateDrawable3);
						holder.tvDay.setText("");
					}
				}
			}
			
            //设置特殊显示的
			if (moreDisplayFlag == true) {
				if (date[0].equals("100") && date[1].equals("100")) {
					holder.tvDay.setText("更多");
					holder.tvDay.setTextSize(YM_size);
					holder.tvDay.setTextColor(mContext.getResources().getColor(
							R.color.return_calendar_more));
				} 
			}
			else {
				if (date[0].equals("100") && date[1].equals("100")) {
				    holder.tvDay.setText("");
				}
			}
		}
	}
}
