package com.xuan.calendar.calendarview;

import android.view.View;

import java.util.Date;
import java.util.List;

/**
 * Created by xuanweijian on 15/12/26.
 */
public interface ICalendarView {

    public CommonCalendar getCalendar();

    void setReturnDate(List<String> allReturnDate);

    void setTheDate(List<Date> date);

    void addView(View view);

    void jumpBack();

    void showPopWin(View locationView, String disPlayString,String date);

    void showNormalToast(String msg);

    void setNoNetView(boolean visible);

    void showProgressDialog();

    void dismissProgressDialog();

    void setClickView(View view);

    View getClickView();

    void setMidTipsText(String point);

    void setBottomTipsText(String tipsString);

    void setMyPoints(String points);

    void showSpecialToast(String msg);

    void setSpecialDay1(List<String> speciaList);

    void setSpecialDay2(List<String> speciaList);
}
