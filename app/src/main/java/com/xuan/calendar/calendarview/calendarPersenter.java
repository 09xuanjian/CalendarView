package com.xuan.calendar.calendarview;

import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by xuanweijian on 15/12/26.
 */
public class calendarPersenter {

    private String nowday;
    private String nowMonth;
    private SimpleDateFormat simpleDateFormat, sd1, sd2, month;
    private List<String> allReturnDate;
    private List<String> hadOpenDate;
    private List<String> notOpenDate;
    private List<String> giftListDate;
    private final int SHOW_MONTHS = 4;
    private ICalendarView iCalendarView;

    public calendarPersenter(ICalendarView iCalendarView){
        this.iCalendarView = iCalendarView;
    }

    public void initData(){
        initDateFormat();
        getCalendarData();
        //实际应用中，可以得到数据后设置这个
        initCalendarData();
    }
    private void initTestData(String s,String s2,String s3){
        allReturnDate.add(0,s);
        hadOpenDate.add(0,s2);
        notOpenDate.add(0,s3);
    }

    private void initDateFormat() {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        month = new SimpleDateFormat("yyyy-MM");
        nowday = simpleDateFormat.format(new Date());
        nowMonth = month.format(new Date());
        sd1 = new SimpleDateFormat("yyyy");
        sd2 = new SimpleDateFormat("mm");
    }

    public void getCalendarData() {
        //特殊的日期，可以设置不同的图标，也是这个控件的好处
        allReturnDate = new ArrayList<String>();
        hadOpenDate = new ArrayList<String>();
        notOpenDate = new ArrayList<String>();
        //其他
        giftListDate = new ArrayList<String>();
    }

    private void initCalendarData() {
        List<String> listDate = getDateList(SHOW_MONTHS);// 取月份数据
        List<Date> firstDates = new ArrayList<Date>();
        for (int i = 0; i < listDate.size(); i++) {
            Date date = null;
            try {
                date = simpleDateFormat.parse(listDate.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            firstDates.add(date);
            //TODO 测试数据用
            initTestData(listDate.get(0),listDate.get(1),listDate.get(2));

        }
        if (iCalendarView.getCalendar() != null) {
            iCalendarView.setReturnDate(allReturnDate);
            iCalendarView.setSpecialDay1(notOpenDate);
            iCalendarView.setSpecialDay2(hadOpenDate);
            iCalendarView.setTheDate(firstDates);
            iCalendarView.getCalendar().setOnDaySelectListener(
                    new OnDaySelectListener());
            iCalendarView.getCalendar().setOnCalendarPagerListener(
                    new OnCalendarPagerChangeListener());
            iCalendarView.getCalendar().setOnPopWinDismissListener(
                    new CommonCalendar.OnPopWinDismissListener() {
                        @Override
                        public void OnPopWinDismissListener() {

                        }
                    });
            iCalendarView.getCalendar().setOnCalendarPagerClickListener(
                    new CommonCalendar.OnCalendarPagerClickListener() {
                        @Override
                        public void OnCalendarPagerClickListener() {
                            iCalendarView.setClickView(null);
                        }
                    });
        }
        iCalendarView.addView(iCalendarView.getCalendar());

    }

    public List<String> getDateList(int month) {
        List<String> list = new ArrayList<String>();
        Date date = new Date();
        String tempString;

        tempString = date.toString();

        try {
            date = simpleDateFormat.parse(tempString);
            date.setDate(01);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        date = getLastMonthDate(date);

        for (int i = 1; i <= month; i++) {
            int nowMon = date.getMonth() + i;
            String yyyy = sd1.format(date);
            list.add(yyyy + "-" + nowMon + "-" + "01");
        }
        return list;
    }

    private class OnCalendarPagerChangeListener implements
            CommonCalendar.OnCalendarPagerListener {
        @Override
        public void onCalendarPagerLisrener(String month, String fullMonths,
                                            int nowMonth) {// month为月份 ,nowMonth为页码
            iCalendarView.setClickView(null);
        }
    }

    private class OnDaySelectListener implements CommonCalendar.OnDaySelectListener{

        @Override
        public void onDaySelectListener(View view, String date) {


            if (date != null) {
                if (getDisplayString(date)!=null) {
                    iCalendarView.showSpecialToast(getDisplayString(date));
                }
            }
        }
    }

    private Date getLastMonthDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }


    private String getDisplayString(String dateString){
        String tempString = null;

        return tempString;
    }
}
