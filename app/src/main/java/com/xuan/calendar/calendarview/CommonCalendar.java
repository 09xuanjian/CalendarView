package com.xuan.calendar.calendarview;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;


@SuppressLint("SimpleDateFormat")
public class CommonCalendar extends LinearLayout {

	private static Context context;

	private List<String> returnDayList;
	private List<String> specialDaysList1;
	private List<String> specialDaysList2;
	public static View viewIn;
	public static View viewOut;
	public static String positionIn;
	public static String positionOut;
	private AdPageAdapter adapter;
	private Drawable drawable1,drawable2,drawable3,specialTodayDrawable;
	private View view;
	// private Calendar cal;
	private int currentPage = 0;
	private List<Calendar> calendars = new ArrayList<Calendar>();

	static long nd = 1000 * 24L * 60L * 60L;// 一天的毫秒数

	private List<String> gvList;// 每月放天
	private List<List<String>> monthList = new ArrayList<List<String>>();// 存放几个月的数据
	private List<Date> firstDates = new ArrayList<Date>();
	private List<ImageView> pagePointImageViews = new ArrayList<ImageView>();
	private OnDaySelectListener callBack;// 回调函数
	private OnCalendarPagerListener pageCallBack;
	private OnPopWinDismissListener itemClickListener;
	private OnCalendarPagerClickListener pageClickListener;
	private static String nowday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sd1 = new SimpleDateFormat("yyyy");
	private int heightPixels;
	private int widthPixels;
	private float density;
	private ViewPager return_date_vp;
	private PopupWindow mMorePopupWindow;
	private RelativeLayout page1_bottom;
	private boolean popFlag = true;
	private int mShowMorePopupWindowWidth;
	private int mShowMorePopupWindowHeight;
	private boolean buttomPointFlag = true;
	private int mPage = 0;

	private final float YEAR_MONTH_TOP_H = 0.044f;
	private final float YEAR_MONTH_H = 0.12f;
	private final float YEAR_MONTH_BOTTOM_H = 0.050f;
	private final float TITLE_PADDING = 0.035f;
	private final float YM_SIZE = 38f;
	private final float WEED_SIZE = 50f;

	private final float WEED_H = 0.1f;
	
	private int bgColor = -1;
	private int weekdayColor = -1;
	private int weekendColor = -1;
	private float weekdayTextSize = -1;
	private float weekendTextSize = -1;
	private Drawable todayDrawable;
	private int todaTextColor = -1;
	private float heightProportion = 0.075f;
	private float mTitleProportion = 0.09f;
	private boolean mTitleSelector = false;
	private Drawable weekTitleBgDrawable;
	private float weekTitleSize = -1;
	private int weekTitleColor = -1;
	private Drawable popwinBgDrawable;
	private float popwinTextSize = -1;
	private int popwinTextColor = -1;
	private float titleTextSize = -1;
	private int titleTextColor = -1;
	private float viewPagerProportion = -1;
	private FixedSpeedScroller mScroller;
	private int mCurrentViewID = 0;         //当前页面
	private int mMyDuration = 100;          //持续时间
	private boolean moreDisplayFlay = true;

	private List<View> dateViews = new ArrayList<View>();

	public CommonCalendar(Context context) {
		super(context);
		setPixels(context);
		CommonCalendar.context = context;
	}

	public CommonCalendar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPixels(context);
		CommonCalendar.context = context;
	}

	/**
	 * 设置一般特殊的日期(如 回款日，签到日)
	 * @param returnDayList
	 */
	public void setAllReturnDay(List<String> returnDayList) {
		this.returnDayList = returnDayList;
	}
	
	/**
	 * 设置特殊日期1
	 */
    public void setSpecialDay1(List<String> specialDay1) {
    	this.specialDaysList1 = specialDay1;
	}
    
	/**
	 * 设置特殊日期2
	 */
    public void setSpecialDay2(List<String> specialDay2) {
	    this.specialDaysList2 = specialDay2;
    }
	
	/**
	 * 用于设置每个月的第一天，List里面有多少个月，就可以显示多少个月的日历
	 * @param firstDates
	 */
	public void setTheDay(List<Date> firstDates) {
		this.firstDates = firstDates;
		init();
	}
	
	/** 设置日历显示页数
	 * @param months 显示多少个月
	 * @param startDate 开始月份的第一天
	 */
	public void setTheDay(int months,Date startDate) {
		List<String> listDate = getFirstDateList(months ,startDate);// 取月份数据
		List<Date> firstDates = new ArrayList<Date>();
		for (int i = 0; i < listDate.size(); i++) {
			Date date = null;
			try {
				date = simpleDateFormat.parse(listDate.get(i));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			firstDates.add(date);
		}
		setTheDay(firstDates);
	}
	
	/**
	 * 取得first day 列表
	 * @param month 多少个月
	 * @param startDate 哪个月开始
	 * @return
	 */
	public List<String> getFirstDateList(int month , Date startDate) {
		List<String> list = new ArrayList<String>();
		Date date = new Date();
		date = startDate;
		for (int i = 1; i <= month; i++) {
			int nowMon = date.getMonth() + i;
			String yyyy = sd1.format(date);
			list.add(yyyy + "-" + nowMon + "-" + "01");
		}
		return list;
	}
	
	public Date stringToDate(String dateString) {
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 是否有滑动点接口，需要在setTheDay 后调用，不然默认是不显示出来
	 * @param visiable
	 */
	public void setButtomPointVisiable(boolean visiable) {
		this.buttomPointFlag = visiable;
		if (visiable == true) {
			page1_bottom.setVisibility(View.VISIBLE);
		}
	}
	
    /**
     * 进入时候默认页接口，需要在setTheDay 前调用，不然默认是第一页
     * @param page
     */
	public void setDefaultPager(int page) {
		this.mPage = page;
	}
	
	/**
	 * 设置“更多显示与否”，不设置默认显示
	 * @param flag
	 */
	public void setMoreDisplayFlay(boolean flag) {
		this.moreDisplayFlay = flag;
	}
	
	/**
	 * 设置特殊背景效果，setAllReturnDay，里面设置特殊日期的时间
	 * @category 没有特殊直接设置是 null
	 * @param drawable1 特殊1
	 * @param drawable2 更加特殊的
	 * @param drawable3 更加特殊的
	 */
	public void setSpecialDrawable(Drawable drawable1,Drawable drawable2,Drawable drawable3) {
		this.drawable1 = drawable1;
		this.drawable2 = drawable2;
		this.drawable3 = drawable3;
	}
	
	/**
	 * 设置特殊日期刚好是今天的背景
	 * @param drawable
	 */
	public void setSpecialTodayDrawable(Drawable drawable) {
		this.specialTodayDrawable = drawable;
	}
	
	/**
	 * 设置普通的item的背景颜色
	 * @param color 不设置显示默认颜色
	 */
	public void setEveryItemBgColor(int color){
		this.bgColor = color;
	}
	/**
	 * 设置一般日期的颜色，不设置，显示默认颜色大小
	 * @param weekendColor
	 * @param weekdayColor
	 * @param weekendTextSize
	 * @param weekDayTextSize
	 */
	public void setWeekendWeekdayStyle(int weekendColor,int weekdayColor,float weekendTextSize,float weekDayTextSize) {
		this.weekendColor = weekendColor;
		this.weekdayColor = weekdayColor;
		this.weekdayTextSize = weekDayTextSize;
		this.weekendTextSize = weekendTextSize;
	}
	
	/**
	 * 设置当天背景以及字体颜色，不设置显示默认
	 * @param todayDrawable
	 * @param todayTextcolor
	 */
	public void setTodaybg(Drawable todayDrawable,int todayTextcolor) {
		this.todayDrawable = todayDrawable;
		this.todaTextColor = todayTextcolor;
	}
	
	/**
	 * 设置item的高度，已屏幕高度为标准,不设置，默认是0.08的height.
	 * @param height
	 */
	public void setItemHeight(int height) {
		float tempHeight = (float)height / heightPixels;
		this.heightProportion = tempHeight;
	}
	
	/**
	 * 设置title 的item 高度，按比例设置百分比，不设置。默认一个高度
	 */
	public void setTitleHeight(float proportion) {
		this.mTitleProportion = proportion;
	}
	
	public void setTitleTextStyle(float textSize,int textColor) {
		this.titleTextSize = textSize;
		this.titleTextColor = textColor;
	}
	
	/**
	 * 设置星期的背景（一，二，...日）
	 * @param weekDrawable
	 */
	public void setWeekTitleBg(Drawable weekDrawable) {
		this.weekTitleBgDrawable = weekDrawable;
	}
	/**
	 * 设置星期title的字体大小以及颜色
	 * @param textSize
	 * @param textColor
	 */
	public void setWeekTitleTextStyle(float textSize,int textColor) {
		this.weekTitleColor = textColor;
		this.weekTitleSize = textSize;
	}
	/**
	 * 设置title的选择器是否显示，默认不显示
	 * @param displayFlag
	 */
	public void setTitleSelector(boolean displayFlag) {
		this.mTitleSelector = displayFlag;
	}
	
	/**
	 * 设置日历viewpager的高度,不设置默认一个高度
	 */
	public void setCalendarViewPagerHeight(int height){
		this.viewPagerProportion = (float)height/heightPixels;
	}
	
	private void setPixels(Context context) {
		this.heightPixels = context.getResources().getDisplayMetrics().heightPixels;
		this.widthPixels = context.getResources().getDisplayMetrics().widthPixels;
		this.density = context.getResources().getDisplayMetrics().density;
	}
	
	/**
	 * 初始化日期以及view等控件
	 */
	private void init() {
		view = LayoutInflater.from(context).inflate(R.layout.comm_calendar,
				this, true);// 获取布局，开始初始化
		return_date_vp = (ViewPager) view.findViewById(R.id.return_date_vp);
		page1_bottom = (RelativeLayout) view.findViewById(R.id.page1_bottom);
		initSize();
		// 数据部分
		setDateData();
		// 具体每个日期设置
		addGridView(monthList.size());

		initPagePoint();

		adapter = new AdPageAdapter(dateViews);
		return_date_vp.setAdapter(adapter);
		return_date_vp.setOnPageChangeListener(new calendarPagerListener());
		return_date_vp.setOnTouchListener(new calendarPagerClickListener());
		/*主要代码段*/
//		try {             
//			Field mField = ViewPager.class.getDeclaredField("mScroller");             
//			mField.setAccessible(true);   
//			//设置加速度 ，通过改变FixedSpeedScroller这个类中的mDuration来改变动画时间（如mScroller.setmDuration(mMyDuration);） 
//			mScroller = new FixedSpeedScroller(return_date_vp.getContext(), new AccelerateInterpolator());        
//			mField.set(return_date_vp, mScroller);         
//		} catch (Exception e) {         
//			e.printStackTrace();
//		} 
		
		//设置默认显示页
		if (dateViews.size()-1 < mPage) {
			return_date_vp.setCurrentItem(0);
		}else{
			return_date_vp.setCurrentItem(mPage);
		}
		
	}

	private void initPagePoint() {
		LinearLayout pagePointLayout = (LinearLayout) view
				.findViewById(R.id.calendar_page_point_ll);
		
		if (!buttomPointFlag) {
			pagePointLayout.setVisibility(View.GONE);
			return;
		}
		
		for (int i = 0; i < dateViews.size(); i++) {
			ImageView img = new ImageView(context);

			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			if (i == 0) {
				img.setBackgroundDrawable(context.getResources().getDrawable(
						R.drawable.calendar_point1));
			} else {
				layoutParams.setMargins(25, 0, 0, 0);
				img.setBackgroundDrawable(context.getResources().getDrawable(
						R.drawable.canlendar_point2));
			}
			img.setLayoutParams(layoutParams);

			pagePointImageViews.add(img);
			pagePointLayout.addView(img);
		}
		
		
	}

	private void initSize() {
//		LinearLayout titleLayout = (LinearLayout) view
//				.findViewById(R.id.calendar_title_ll);
		RelativeLayout titleLayout_rl= (RelativeLayout) view.findViewById(R.id.calendar_title_rl);
		initLayoutH(titleLayout_rl, heightPixels, mTitleProportion);
		
		TextView calendar_back_iv = (TextView) view.findViewById(R.id.calendar_back_iv);
		TextView calendar_next_iv = (TextView) view.findViewById(R.id.calendar_next_iv);
		if (mTitleSelector) {
			calendar_back_iv.setVisibility(View.VISIBLE);
			calendar_back_iv.setOnClickListener(new calendarTitleSeletor());
			calendar_next_iv.setVisibility(View.VISIBLE);
			calendar_next_iv.setOnClickListener(new calendarTitleSeletor());
		}
		//星期的设置
		LinearLayout calendar_week_title_ll = (LinearLayout) view .findViewById(R.id.calendar_week_title_ll);
//		initLayoutW(calendar_week_title_ll, widthPixels, 0.92777f);
		if (weekTitleBgDrawable != null) {
			calendar_week_title_ll.setBackgroundDrawable(weekTitleBgDrawable);
		}
		setMarginLeft(calendar_back_iv, (int)(widthPixels*TITLE_PADDING));
		setMarginRight(calendar_next_iv, (int)(widthPixels*TITLE_PADDING));
//		setMarginBottom(titleLayout,(int) (heightPixels * (YEAR_MONTH_BOTTOM_H)));

		TextView titleY = (TextView) view.findViewById(R.id.tv_year);
		TextView titleM = (TextView) view.findViewById(R.id.tv_month);
		TextView titleM_Tip = (TextView) view.findViewById(R.id.tv_month_text);

		float YM_size = heightPixels / YM_SIZE / density;
		if (titleTextSize!=-1) {
			titleY.setTextSize(titleTextSize);
			titleM.setTextSize(titleTextSize);
			titleM_Tip.setTextSize(titleTextSize);
		}else {
			titleY.setTextSize(YM_size);
			titleM.setTextSize(YM_size);
			titleM_Tip.setTextSize(YM_size);
		}
		
		if (titleTextSize != -1) {
			titleY.setTextColor(titleTextColor);
			titleM.setTextColor(titleTextColor);
			titleM_Tip.setTextColor(titleTextColor);
		}


		TextView weekMon = (TextView) view.findViewById(R.id.calendar_mon_tv);
		TextView weekfed = (TextView) view.findViewById(R.id.calendar_feb_tv);
		TextView weekW = (TextView) view.findViewById(R.id.calendar_wsd_tv);
		TextView weekFri = (TextView) view.findViewById(R.id.calendar_four_tv);
		TextView weekFir = (TextView) view.findViewById(R.id.calendar_fir_tv);
		TextView weekSat = (TextView) view.findViewById(R.id.calendar_sat_tv);
		TextView weekSun = (TextView) view.findViewById(R.id.calendar_sun_tv);

		float W_size = heightPixels / WEED_SIZE / density;
		if (weekTitleSize == -1) {
			weekMon.setTextSize(W_size);
			weekW.setTextSize(W_size);
			weekfed.setTextSize(W_size);
			weekFri.setTextSize(W_size);
			weekFir.setTextSize(W_size);
			weekSat.setTextSize(W_size);
			weekSun.setTextSize(W_size);
		}else {
			weekMon.setTextSize(weekTitleSize);
			weekW.setTextSize(weekTitleSize);
			weekfed.setTextSize(weekTitleSize);
			weekFri.setTextSize(weekTitleSize);
			weekFir.setTextSize(weekTitleSize);
			weekSat.setTextSize(weekTitleSize);
			weekSun.setTextSize(weekTitleSize);
		}
		
		if (weekTitleColor != -1) {
			weekMon.setTextColor(weekTitleColor);
			weekW.setTextColor(weekTitleColor);
			weekfed.setTextColor(weekTitleColor);
			weekFri.setTextColor(weekTitleColor);
			weekFir.setTextColor(weekTitleColor);
			weekSat.setTextColor(weekTitleColor);
			weekSun.setTextColor(weekTitleColor);
		}

		ViewPager return_date_vp = (ViewPager) view
				.findViewById(R.id.return_date_vp);
		if (viewPagerProportion == -1) {
			initLayoutH(return_date_vp, heightPixels, 0.51f);// 48
		}else {
			initLayoutH(return_date_vp, heightPixels, viewPagerProportion);
		}
		
	}
	
	
	private class calendarTitleSeletor implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.calendar_back_iv:
				if(currentPage != 0){
					currentPage--;
					return_date_vp.setCurrentItem(currentPage, true);
//					mMyDuration -= 100;
				}
//				Log.i("left", mMyDuration+"");
//				mScroller.setmDuration(mMyDuration);
				break;
			case R.id.calendar_next_iv:
				if(currentPage != dateViews.size()-1){
					currentPage++;
					return_date_vp.setCurrentItem(currentPage, true);
//					mMyDuration += 100;
				}
//				Log.i("right", mMyDuration+"");
//				mScroller.setmDuration(mMyDuration);
				break;
			}
		}
		
	}
	

	private void setMarginTop(View view, int top) {
		if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
					.getLayoutParams();
			layoutParams.topMargin = top;
		} else if (view.getLayoutParams() instanceof LayoutParams) {
			LayoutParams layoutParams = (LayoutParams) view
					.getLayoutParams();
			layoutParams.topMargin = top;
		}
	}

	private void setMarginLeft(View view, int left) {
		if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
					.getLayoutParams();
			layoutParams.leftMargin = left;
		} else if (view.getLayoutParams() instanceof LayoutParams) {
			LayoutParams layoutParams = (LayoutParams) view
					.getLayoutParams();
			layoutParams.leftMargin = left;
		}
	}

	private void setMarginRight(View view, int right) {
		if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
					.getLayoutParams();
			layoutParams.rightMargin = right;
		} else if (view.getLayoutParams() instanceof LayoutParams) {
			LayoutParams layoutParams = (LayoutParams) view
					.getLayoutParams();
			layoutParams.rightMargin = right;
		}
	}

	private void setMarginBottom(View view, int bottom) {
		if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
					.getLayoutParams();
			layoutParams.bottomMargin = bottom;
		} else if (view.getLayoutParams() instanceof LayoutParams) {
			LayoutParams layoutParams = (LayoutParams) view
					.getLayoutParams();
			layoutParams.bottomMargin = bottom;
		}
	}

	private void initLayoutH(View view, int mainHeight, float proportion) {
		ViewGroup.LayoutParams layoutParams = view
				.getLayoutParams();
		int height = (int) (mainHeight * proportion);
		layoutParams.height = height;
	}
	
	private void initLayoutW(View view, int mainWidth, float proportion) {
		ViewGroup.LayoutParams layoutParams = view
				.getLayoutParams();
		int width = (int) (mainWidth * proportion);
		layoutParams.width = width;
	}

	// 顶部月份
	private void setTitleDate(int page) {
		TextView tv_year = (TextView) view.findViewById(R.id.tv_year);
		if (calendars.get(page).get(Calendar.YEAR) > new Date().getYear()) {
			tv_year.setVisibility(View.VISIBLE);
			tv_year.setText(calendars.get(page).get(Calendar.YEAR) + "年");
		}
		TextView tv_month = (TextView) view.findViewById(R.id.tv_month);
		tv_month.setText(String.valueOf(firstDates.get(page).getMonth() + 1));
	}
	
	private class calendarPagerClickListener implements OnTouchListener{
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (pageClickListener != null) {
				pageClickListener.OnCalendarPagerClickListener();
			}
			return false;
		}
	}

	// 滑动监听
	private class calendarPagerListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			Log.i("debug", "--------------------StateChanged--------------"+arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			Log.i("debug", "--------------------Scrolled---------------");
		}

		@Override
		public void onPageSelected(int arg0) {
			setTitleDate(arg0);
			currentPage = arg0;
			changePagePoit(arg0);
			
            Log.i("debug", "--------------------Selected"+arg0);
			
			if (pageCallBack != null) {
				int tempMonths =  (firstDates.get(arg0).getMonth() + 1);
				String tempMonthsString ;
				if (tempMonths < 10) {
					tempMonthsString = "0"+tempMonths;
				}else {
					tempMonthsString = ""+tempMonths;
				}
				
				pageCallBack.onCalendarPagerLisrener(
						String.valueOf(firstDates.get(arg0).getMonth() + 1),
						calendars.get(arg0).get(Calendar.YEAR) + "-" + tempMonthsString,
						arg0);
			}
		}

	}

	private void changePagePoit(int page) {
		for (int i = 0; i < pagePointImageViews.size(); i++) {
			if (i == page) {
				pagePointImageViews.get(page).setBackgroundDrawable(
						context.getResources().getDrawable(
								R.drawable.calendar_point1));
			} else {
				pagePointImageViews.get(i).setBackgroundDrawable(
						context.getResources().getDrawable(
								R.drawable.canlendar_point2));
			}
		}
	}

	private void setDateData() {
		monthList.clear();
		for (int i = 0; i < firstDates.size(); i++) {
			final Calendar cal = Calendar.getInstance();// 获取日历实例
			cal.setTime(firstDates.get(i));// cal设置为当天的
			cal.set(Calendar.DATE, 1);// cal设置当前day为当前月第一天
			int tempSum = countNeedHowMuchEmpety(cal);// 获取当前月第一天为星期几
			int dayNumInMonth = getDayNumInMonth(cal);// 获取当前月有多少天
			boolean lastPageFlag = false;
			if (i == (firstDates.size() - 1)) {
				lastPageFlag = true;
			}
			setGvListData(tempSum, dayNumInMonth, cal.get(Calendar.YEAR) + "-"
					+ getMonth((cal.get(Calendar.MONTH) + 1)), lastPageFlag);
			monthList.add(gvList);
			calendars.add(cal);
			if (i == 0) {
				setTitleDate(i);
			}
		}
	}

	private void addGridView(int num) {
		for (int i = 0; i < num; i++) {
			View gridView = LayoutInflater.from(context).inflate(
					R.layout.returndate_calendar, null);
			RetrunDateGridView gv = (RetrunDateGridView) gridView
					.findViewById(R.id.gv_calendar);
			gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
			final CalendarGridViewAdapter gridViewAdapter = new CalendarGridViewAdapter(
					monthList.get(i), context);
            initCalendarGridView(gridViewAdapter);
			gv.setAdapter(gridViewAdapter);
			gv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View arg1,
						int position, long arg3) {
					Log.i("debug", "--------------------onItemClick--------------");
					int[] location1 = new int[2];
					arg1.getLocationOnScreen(location1);

					String choiceDay = (String) adapterView.getAdapter()
							.getItem(position);
					String[] date = choiceDay.split(",");
					String day = date[1];
					if (!" ".equals(day)) {
						if (day.equals("100")) {
							choiceDay = "更多";
						} else {
							if (Integer.parseInt(day) < 10) {
								day = "0" + date[1];
							}
							choiceDay = date[0] + "-" + day;
						}

						if (callBack != null) {// 调用回调函数回调数据
							callBack.onDaySelectListener(arg1, choiceDay);			
						}
					}else{
						if (callBack != null) {// 调用回调函数回调数据
							callBack.onDaySelectListener(arg1, null);			
						}
					}
				}
			});
			dateViews.add(gridView);
		}
	}
	
	//设置每一个的一些样式
	private void initCalendarGridView(CalendarGridViewAdapter gridViewAdapter){
        //设置特殊日期
		gridViewAdapter.setSpecialDate(returnDayList, specialDaysList1, specialDaysList2);
		gridViewAdapter.setSpecialDateTodayDrawable(specialTodayDrawable);
		//设置特殊背景
		gridViewAdapter.setSpecialDateDrawable(drawable1, drawable2, drawable3);
		//设置一般背景颜色
		gridViewAdapter.setEveryDayItemBg(bgColor);
		//设置字体颜色,以及大小
		gridViewAdapter.setWeekdayWeekendColor(weekdayColor, weekendColor,weekdayTextSize,weekendTextSize);
		//设置当天背景以及颜色
		gridViewAdapter.setTodayIconColor(todayDrawable, todaTextColor);
		//设置每个Item的高度
		gridViewAdapter.setItemHeight(heightProportion);
		//设置更多显示
		gridViewAdapter.setMoreDisplayFlag(moreDisplayFlay);
	}
	
	public void setPopWinFlag(boolean flag) {
		this.popFlag = flag;
	}
	

	/**
	 * 为gridview中添加需要展示的数据
	 * 
	 * @param tempSum
	 * @param dayNumInMonth
	 */
	private void setGvListData(int tempSum, int dayNumInMonth, String YM,
			boolean lastPageFlag) {
		gvList = new ArrayList<String>();// 存放天
		gvList.clear();
		for (int i = 0; i < tempSum; i++) {
			gvList.add(" , ");
		}
		for (int j = 1; j <= dayNumInMonth; j++) {
			gvList.add(YM + "," + String.valueOf(j));
		}
		if (lastPageFlag == true) {
			gvList.add("100,100");
		}
		if ((gvList.size()) % 7 != 0) {
			for (int i = 0; i <= (gvList.size()) % 7; i++) {
				gvList.add(" , ");
			}
		}
	}

	private String getMonth(int month) {
		String mon = "";
		if (month < 10) {
			mon = "0" + month;
		} else {
			mon = "" + month;
		}
		return mon;
	}

	public int DateToWeek(String dateString) {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = simpleDateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
		return dayIndex;
	}
	
	/**
	 * 设置弹框的背景，不设置，显示默认的，灰底圆角
	 * @param popwinBgDrawable
	 */
	public void setPopwinBg(Drawable popwinBgDrawable) {
		this.popwinBgDrawable = popwinBgDrawable;
	}
	
	/**
	 * 设置弹框的字体大小颜色，不设置，显示默认的
	 * @param textSize
	 * @param textColor
	 */
	public void setPopwinTextStyle(float textSize,int textColor) {
		
	}
	
	/**
	 * 显示弹出框，String 可以自由定义
	 * @param moreBtnView 对应的日历的item
	 * @param showString 要显示的String
	 * @param date 日期
	 */
	public void showCalendarData(View moreBtnView, String showString ,String date) {
		
		int weedIndex = DateToWeek(date);
		int GridWidth = moreBtnView.getWidth();

		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popWinView = li.inflate(R.layout.calendar_popwin, null, false);

		ImageView img = (ImageView) popWinView
				.findViewById(R.id.calendar_angle);
		LayoutParams imgLayoutParams = (LayoutParams) img.getLayoutParams();
		imgLayoutParams.setMargins((weedIndex - 1) * GridWidth + GridWidth / 5,
				0, 0, 0);

		LinearLayout pop_win_view_ll = (LinearLayout) popWinView.findViewById(R.id.pop_win_view_ll);
		if (popwinBgDrawable != null) {
			pop_win_view_ll.setBackgroundDrawable(popwinBgDrawable);
		}
		TextView pop_win_view = (TextView) popWinView
				.findViewById(R.id.calendar_pop_win_tv);
		LayoutParams popWinLayoutParams = (LayoutParams) pop_win_view
				.getLayoutParams();
		popWinLayoutParams.height = (int) (heightPixels * 0.0584);
		popWinLayoutParams.width = (int) (widthPixels * 0.913);

		pop_win_view.setText(showString);
		if (popwinTextColor!=-1) {
			pop_win_view.setTextColor(popwinTextColor);
		}
		
		if (popwinTextSize == -1) {
			pop_win_view.setTextSize(heightPixels / YM_SIZE / density);
		}else {
			pop_win_view.setTextSize(popwinTextSize);
		}
		

		mMorePopupWindow = new PopupWindow(popWinView,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		mMorePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mMorePopupWindow.setOutsideTouchable(true);
		mMorePopupWindow.setTouchable(false);
		
		mMorePopupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				itemClickListener.OnPopWinDismissListener();
			}
		});

		popWinView.measure(MeasureSpec.UNSPECIFIED,
				MeasureSpec.UNSPECIFIED);
		mShowMorePopupWindowWidth = popWinView.getMeasuredWidth();
		mShowMorePopupWindowHeight = popWinView.getMeasuredHeight();
		
//		popWinView.setOnClickListener(new OnClickListener() {   
//            
//            @Override 
//            public void onClick(View v) {   
//                if(mMorePopupWindow.isShowing()){   
//                	mMorePopupWindow.dismiss();   
//                }   
//            }   
//        });   
    
		mMorePopupWindow.setContentView(popWinView); 

		if (mMorePopupWindow.isShowing()) {
			mMorePopupWindow.dismiss();
		} else {
			int everyGridWidth = (int) (widthPixels / 7.0f);
			int heightMoreBtnView = moreBtnView.getHeight();
			int[] location = new int[2];
			moreBtnView.getLocationOnScreen(location);
			mMorePopupWindow.showAsDropDown(moreBtnView, -everyGridWidth* (weedIndex - 1), -(heightMoreBtnView + mShowMorePopupWindowHeight));

		}
	}

	/**
	 * 获取当前月的总共天数
	 */
	private int getDayNumInMonth(Calendar cal) {
		return cal.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 获取当前月第一天在第一个礼拜的第几天，得出第一天是星期几
	 * 
	 */
	private int countNeedHowMuchEmpety(Calendar cal) {
		int firstDayInWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return firstDayInWeek;
	}

	// 每个gridview 按键监听
	public interface OnDaySelectListener {
		void onDaySelectListener(View view, String date);
	}

	public void setOnDaySelectListener(OnDaySelectListener o) {
		callBack = o;
	}

	// 滑动监听接口
	public interface OnCalendarPagerListener {
		void onCalendarPagerLisrener(String monthString, String fullMonths, int nowMonth);
	}

	public void setOnCalendarPagerListener(OnCalendarPagerListener o) {
		pageCallBack = o;
	}
	
	//点击监听
	public interface OnPopWinDismissListener {
		void OnPopWinDismissListener();
	}

	public void setOnPopWinDismissListener(OnPopWinDismissListener o) {
		itemClickListener = o;
	}
	
	//pager点击监听
	public interface OnCalendarPagerClickListener {
		void OnCalendarPagerClickListener();
	}

	public void setOnCalendarPagerClickListener(OnCalendarPagerClickListener o) {
		pageClickListener = o;
	}

}
