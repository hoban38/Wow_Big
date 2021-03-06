package com.example.yeon;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.big.calendarproto.cal.OneMonthView;
import com.example.yeon.R;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

import java.util.Calendar;



/**
 * ??¬λ·°λ?? ?¬?¨? ??κ·Έλ¨Ό?Έ
 * @author brownsoo
 *
 */
public class MonthlyFragment extends Fragment {

    private static final String TAG = MConfig.TAG;
    private static final String NAME = "MonthlyFragment";
    private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());
    
    public static final String ARG_YEAR = "year";
    public static final String ARG_MONTH = "month";

    /**
     * ?¬? ₯? λ³??λ₯? ??Έ?κΈ? ?? λ¦¬μ€?
     * @author Brownsoo
     *
     */
    public interface OnMonthChangeListener {
        /**
         * ? μ§κ? λ°λ?? ?
         * @param year ?
         * @param month ?
         */
        void onChange(int year, int month);
    }
    
    /**
     * κ°?μ§? λ¦¬μ€?.
     */
    private OnMonthChangeListener dummyListener = new OnMonthChangeListener() {
        @Override
        public void onChange(int year, int month) {}
    };
    
    private OnMonthChangeListener listener = dummyListener;
    
    private VerticalViewPager vvpager;
    private MonthlySlidePagerAdapter adapter;
    int mYear = -1;
    int mMonth = -1;

    public static MonthlyFragment newInstance(int year, int month) {

        HLog.d(TAG, NAME, "newInstance " + year + "/" + month);

        MonthlyFragment fragment = new MonthlyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH, month);
        fragment.setArguments(args);
        return fragment;
    }
    public MonthlyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mYear = getArguments().getInt(ARG_YEAR);
            mMonth = getArguments().getInt(ARG_MONTH);
        }
        else {
            Calendar now = Calendar.getInstance();
            mYear = now.get(Calendar.YEAR);
            mMonth = now.get(Calendar.MONTH);
        }

        HLog.d(TAG, CLASS, "onCreate " + mYear + "." + mMonth);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_monthly, container, false);

        vvpager = (VerticalViewPager) v.findViewById(R.id.vviewPager);
        adapter = new MonthlySlidePagerAdapter(getActivity(), mYear, mMonth);
        vvpager.setAdapter(adapter);
        vvpager.setOnPageChangeListener(adapter);
        vvpager.setCurrentItem(adapter.getPosition(mYear, mMonth));
        vvpager.setOffscreenPageLimit(1);
        
        return v;
    }
    
    @Override
    public void onDetach() {
        setOnMonthChangeListener(null);
        super.onDetach();
    }
    

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setOnMonthChangeListener(OnMonthChangeListener listener) {
        if(listener == null) this.listener = dummyListener;
        else this.listener = listener;
    }

    /**
     * ???΄<br>
     * ?, ?? ?΄κΈ? ?? κ°λ¨? ?€λΈμ ?Έ
     * @author Brownsoo
     *
     */
    public class YearMonth {
        public int year;
        public int month;
        
        public YearMonth(int year, int month) {
            this.year = year;
            this.month = month;
        }
    }

    /**
     * 
     * ??¬?© λ·°λ?? ??±?? ??΅?°
     * 
     * @author Brownsoo
     *
     */
    class MonthlySlidePagerAdapter extends PagerAdapter
        implements ViewPager.OnPageChangeListener {

        @SuppressWarnings("unused")
        private Context mContext;
        
        private OneMonthView[] monthViews;
        /** ?μΉκ³?°? ?? κΈ°μ? ? */
        final static int BASE_YEAR = 2015;
        /** ?μΉκ³?°? ?? κΈ°μ? ? */
        final static int BASE_MONTH = Calendar.JANUARY;
        /** λ·°ν?΄???? ?¬?¬?©?  ??΄μ§? κ°?? */
        final static int PAGES = 5;
        /** λ£¨ν?λ₯? 1000 ?΄? ?€? ?  ? ?κ² μ?λ§?, ?΄? ?λ©? λ¬΄ν ?€?¬λ‘€μ΄?Όκ³? ?κ°ν? */
        final static int LOOPS = 1000;
        /** κΈ°μ? ?μΉ?, κΈ°μ? ? μ§μ ?΄?Ή?? ?μΉ? */
        final static int BASE_POSITION = PAGES * LOOPS / 2;
        /** κΈ°μ? ? μ§λ?? κΈ°λ°? Calendar */
        final Calendar BASE_CAL;
        /** ?΄?  ?μΉ? */
        private int previousPosition;
        
        public MonthlySlidePagerAdapter(Context context, int startYear, int startMonth) {
            this.mContext = context;
            //κΈ°μ? Calendar μ§?? 
            Calendar base = Calendar.getInstance();
            base.set(BASE_YEAR, BASE_MONTH, 1);
            BASE_CAL = base;
            
            monthViews = new OneMonthView[PAGES];
            for(int i = 0; i < PAGES; i++) {
                monthViews[i] = new OneMonthView(getActivity());
            }
        }
        
        /**
         * ???΄ κ΅¬νκΈ?
         * @param position ??΄μ§? ?μΉ?
         * @return position ?μΉμ ?΄?Ή?? ???΄
         */
        public YearMonth getYearMonth(int position) {
            Calendar cal = (Calendar)BASE_CAL.clone();
            cal.add(Calendar.MONTH, position - BASE_POSITION);
            return new YearMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        }
        
        /**
         * ??΄μ§? ?μΉ? κ΅¬νκΈ?
         * @param year ?
         * @param month ?
         * @return ??΄μ§? ?μΉ?
         */
        public int getPosition(int year, int month) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, 1);
            return BASE_POSITION + howFarFromBase(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        }

        /**
         * κΈ°μ? ? μ§λ?? κΈ°μ??Όλ‘? λͺλ¬?΄ ?¨?΄? Έ ??μ§? ??Έ
         * @param year λΉκ΅?  ?
         * @param month λΉκ΅?  ?
         * @return ?¬ ?, count of months
         */
        private int howFarFromBase(int year, int month) {
            
            int disY = (year - BASE_YEAR) * 12;
            int disM = month - BASE_MONTH;
            
            return disY + disM;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            
            HLog.d(TAG, CLASS, "instantiateItem " + position);
            
            int howFarFromBase = position - BASE_POSITION;
            Calendar cal = (Calendar) BASE_CAL.clone();
            cal.add(Calendar.MONTH, howFarFromBase);
            
            position = position % PAGES;
            
            container.addView(monthViews[position]);
            
            monthViews[position].make(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
            
            return monthViews[position];
        }
        
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            HLog.d(TAG, CLASS, "destroyItem " + position);
            container.removeView((View) object);
        }        
        
        @Override
        public int getCount() {
            return PAGES * LOOPS;
        }
        
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }
        
        @Override
        public void onPageScrollStateChanged(int state) {
            switch(state) {
            case ViewPager.SCROLL_STATE_IDLE:
                HLog.d(TAG, CLASS, "SCROLL_STATE_IDLE");
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
                HLog.d(TAG, CLASS, "SCROLL_STATE_DRAGGING");
                previousPosition = vvpager.getCurrentItem();
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                HLog.d(TAG, CLASS, "SCROLL_STATE_SETTLING");
                break;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            
            //HLog.d(TAG, CLASS, position + "-  " + positionOffset);
            if(previousPosition != position) {
                previousPosition = position;
                
                YearMonth ym = getYearMonth(position);
                listener.onChange(ym.year, ym.month);
                
                HLog.d(TAG, CLASS, position + " onPageScrolled-  " + ym.year + "." + ym.month);
            }
        }

        @Override
        public void onPageSelected(int position) {
        }
    }
}
