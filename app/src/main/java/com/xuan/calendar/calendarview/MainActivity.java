package com.xuan.calendar.calendarview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ICalendarView{


    private LinearLayout calendarView_ll;

    private CommonCalendar checkInCalendar;

    private calendarPersenter persenter;
    private View clickView;

    private int heightPixels;
    private int widthPixels;
    private float density;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        persenter = new calendarPersenter(this);
        calendarView_ll = (LinearLayout)findViewById(R.id.return_day_ll);

        heightPixels = getResources().getDisplayMetrics().heightPixels;
        widthPixels = getResources().getDisplayMetrics().widthPixels;
        density = getResources().getDisplayMetrics().density;

        View rootView = findViewById(R.id.root_view);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClickView(null);
            }
        });
        persenter.initData();
    }

    @Override
    public CommonCalendar getCalendar() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (checkInCalendar == null) {
            checkInCalendar = new CommonCalendar(this);
            checkInCalendar.setSpecialDrawable(
                    getResources().getDrawable(R.drawable.check_in_click),
                    getResources().getDrawable(
                            R.drawable.check_in_gift_not_open), getResources()
                            .getDrawable(R.drawable.check_in_gift_opened));
            checkInCalendar.setSpecialTodayDrawable(getResources().getDrawable(
                    R.drawable.check_in_today_special_bg));
            checkInCalendar.setWeekTitleBg(getResources().getDrawable(
                    R.drawable.check_in_week_title_shape));
            checkInCalendar.setWeekTitleTextStyle(12,
                    getResources().getColor(R.color.check_in_week_title));
            checkInCalendar.setTitleHeight(0.06666f);
			checkInCalendar.setTitleSelector(true);
            checkInCalendar.setTitleTextStyle(15,
                    getResources().getColor(R.color.color_666666));
            checkInCalendar.setWeekendWeekdayStyle(
                    getResources().getColor(R.color.check_in_week_title),
                    getResources().getColor(R.color.check_in_week_title),
                    17,
                    17);
            checkInCalendar.setItemHeight((int) (heightPixels * 0.0750));
            checkInCalendar
                    .setCalendarViewPagerHeight((int) (6 * heightPixels * 0.0729));
            checkInCalendar.setMoreDisplayFlay(false);
            checkInCalendar.setLayoutParams(params);
        }
        return checkInCalendar;
    }

    @Override
    public void setReturnDate(List<String> allReturnDate) {
        checkInCalendar.setAllReturnDay(allReturnDate);
    }

    @Override
    public void setTheDate(List<Date> date) {
        checkInCalendar.setDefaultPager(1);
        checkInCalendar.setTheDay(date);
        checkInCalendar.setButtomPointVisiable(true);
    }

    @Override
    public void addView(View view) {
        calendarView_ll.addView(view);
    }

    @Override
    public void jumpBack() {
        finish();
    }

    @Override
    public void showPopWin(View locationView, String disPlayString, String date) {
        if (checkInCalendar != null) {
            // checkInCalendar.setPopwinBg(popwinBgDrawable);
            // checkInCalendar.setPopwinTextStyle(textSize, textColor);
            checkInCalendar.showCalendarData(locationView, disPlayString, date);
        }
    }

    @Override
    public void showNormalToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setNoNetView(boolean visible) {

    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void dismissProgressDialog() {

    }

    @Override
    public void setClickView(View view) {
        clickView = view;
    }

    @Override
    public View getClickView() {
        return clickView;
    }

    @Override
    public void setMidTipsText(String point) {

    }

    @Override
    public void setBottomTipsText(String tipsString) {

    }

    @Override
    public void setMyPoints(String points) {

    }

    @Override
    public void showSpecialToast(String msg) {

    }

    @Override
    public void setSpecialDay1(List<String> speciaList) {
        checkInCalendar.setSpecialDay1(speciaList);
    }

    @Override
    public void setSpecialDay2(List<String> speciaList) {
        checkInCalendar.setSpecialDay2(speciaList);
    }
}
