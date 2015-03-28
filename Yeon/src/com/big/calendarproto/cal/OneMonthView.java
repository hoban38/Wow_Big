package com.big.calendarproto.cal;

import java.util.ArrayList;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.yeon.HLog;
import com.example.yeon.MConfig;


public class OneMonthView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = MConfig.TAG;
    private static final String NAME = "OneMonthView";
    private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());

    private Context mContext;
    private int mYear;
    private int mMonth;

    public OneMonthView(Context context) {
        this(context, null);
    }

    public OneMonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
	public OneMonthView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mContext = context;

        setOrientation(LinearLayout.VERTICAL);

        //?…‰ê³•ï¿½ï¿? èª˜ëªƒ?” ï¿½ê¼®ï¿½ê¼®ï¿½ë¸³ ï§ëš°ê²? ï§ëš®ë±¾ï¿½ë¼? ï¿½ë„ƒï¿½ë’—ï¿½ë–.
        if(weeks == null) {

            weeks = new ArrayList<LinearLayout>(6); //ï¿½ë¸³ï¿½ë––ï¿½ë¿‰ ï§¤ì’•ï¿? 6äºŒï¿½
            dayViews = new ArrayList<OneDayView>(42); // 7ï¿½ì”ª * 6äºŒï¿½ = 42

            LinearLayout ll = null;
            for(int i=0; i<42; i++) {

                if(i % 7 == 0) {
                    //ï¿½ë¸³ äºŒï¿½ ï¿½ì …ï¿½ì” ï¿½ë¸˜ï¿½ì ï¿½ê¹®ï¿½ê½¦
                    ll = new LinearLayout(mContext);
                    LinearLayout.LayoutParams params
                            = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                    params.weight = 1;
                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ll.setLayoutParams(params);
                    ll.setWeightSum(7);

                    weeks.add(ll);
                }

                LinearLayout.LayoutParams params
                        = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 1;

                OneDayView ov = new OneDayView(mContext);
                ov.setLayoutParams(params);
                ov.setOnClickListener(this);

                ll.addView(ov);
                dayViews.add(ov);
            }
        }
        
        //èª˜ëªƒ?”è¹‚ë‹¿ë¦?
        if(isInEditMode()) {
            Calendar cal = Calendar.getInstance();
            make(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        }
        
    }

    /**
     * ï¿½ë??
     * @return 4ï¿½ì˜„?”±ï¿? ï¿½ë?ˆï¿½ë£?
     */
    public int getYear() {
        return mYear;
    }

    /**
     * ï¿½ë––
     * @return 0~11 (Calendar.JANUARY ~ Calendar.DECEMBER)
     */
    public int getMonth() {
        return mMonth;
    }


    /**
     * Any layout manager that doesn't scroll will want this.
     */
    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }
    

    private ArrayList<LinearLayout> weeks = null;
    private ArrayList<OneDayView> dayViews = null;

    public void make(int year, int month)
    {
        if(mYear == year && mMonth == month) {
            HLog.d(TAG, CLASS, ">>>>> same " + year + "." + month);
            return;
        }
        
        long makeTime = System.currentTimeMillis();
        HLog.d(TAG, CLASS, ">>>>> make");
        
        this.mYear = year;
        this.mMonth = month;
        
        //if(viewRect.width() == 0 || viewRect.height() == 0) return;
        
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        cal.setFirstDayOfWeek(Calendar.SUNDAY);//ï¿½ì”ªï¿½ìŠ‚ï¿½ì”ªï¿½ì“£ äºŒì‡±?“½ ï¿½ë–†ï¿½ì˜‰ï¿½ì”ªæ¿¡ï¿½ ï§ï¿½ï¿½ì ™
        
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);//1ï¿½ì”ªï¿½ì“½ ï¿½ìŠ‚ï¿½ì”ª
        int maxOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);//ï§ë‰ï¿½ï§ï¿? ï¿½ì”ªï¿½ë‹”
        ArrayList<OneDayData> oneDayDatas = new ArrayList<OneDayData>();
        
        cal.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - dayOfWeek);//äºŒì‡±?“½ ï§£ï¿½ ï¿½ì”ªæ¿¡ï¿½ ï¿½ì” ï¿½ë£
        //HLog.d(TAG, CLASS, "first day : " + cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.KOREA) + " / " + cal.get(Calendar.DAY_OF_MONTH));

        /* add previous month */
        int seekDay;
        for(;;) {
            seekDay = cal.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek == seekDay) break;
            
            OneDayData one = new OneDayData();
            one.setDay(cal);
            oneDayDatas.add(one);
            //ï¿½ë¸¯?Œ·ï¿? ï§ì•·ï¿?
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        //HLog.d(TAG, CLASS, "this month : " + cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.KOREA) + " / " + cal.get(Calendar.DAY_OF_MONTH));
        /* add this month */
        for(int i=0; i < maxOfMonth; i++) {
            OneDayData one = new OneDayData();
            one.setDay(cal);
            oneDayDatas.add(one);
            //ï¿½ë¸¯?Œ·ï¿? ï§ì•·ï¿?
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        /* add next month */
        for(;;) {
            if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                OneDayData one = new OneDayData();
                one.setDay(cal);
                oneDayDatas.add(one);
            } 
            else {
                break;
            }
            //ï¿½ë¸¯?Œ·ï¿? ï§ì•·ï¿?
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        if(oneDayDatas.size() == 0) return;

        //ï§â‘¤ë±? äºŒì‡°ï¿½ï¿½ ï§ï¿½ï¿½ìŠ¦æ¹²ï¿½
        this.removeAllViews();
        
        int count = 0;
        for(OneDayData oneday : oneDayDatas) {
            
            if(count % 7 == 0) {
                addView(weeks.get(count / 7));
            }
            OneDayView ov = dayViews.get(count);
            ov.setDay(oneday);
            ov.setMsg("");
            ov.refresh();
            count++;
        }

        /* äºŒì‡±?“½ åª›ì’–?‹”ï§ëš°ê²? ?‡¾?‹¿ì¾? ï§ï¿½ï¿½ì ™*/
        this.setWeightSum(getChildCount());


        HLog.d(TAG, CLASS, "<<<<< take timeMillis : " + (System.currentTimeMillis() - makeTime));
 
    }


    protected String doubleString(int value) {

        String temp;
 
        if(value < 10){
            temp = "0"+ String.valueOf(value);
             
        }else {
            temp = String.valueOf(value);
        }
        return temp;
    }
 
    @Override
    public void onClick(View v) {

        OneDayView ov = (OneDayView) v;
        HLog.d(TAG, CLASS, "click " + ov.get(Calendar.MONTH)+1 + "/" + ov.get(Calendar.DAY_OF_MONTH));
        Toast.makeText(mContext, ov.get(Calendar.MONTH)+1 + "/" + ov.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
        
    }

}