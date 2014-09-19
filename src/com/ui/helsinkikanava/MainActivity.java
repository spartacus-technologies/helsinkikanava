package com.ui.helsinkikanava;

import android.graphics.Color;
import android.os.Handler;
import android.widget.Button;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.*;

import java.util.ArrayList;

import com.ui.helsinkikanava.R;

public class MainActivity extends FragmentActivity // implements  OnTouchListener
{
    Fragment frag_default_ = null;
    FragmentSettings frag_settings = null;
    private WrapperJSON wrapperJSON = new WrapperJSON();
    private ArrayList<Session> council_meetings = new ArrayList<Session>();
    private Scroller tab_bar_scroller;
    int scroll_speed = 7;

    public MainActivity()
    {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(this.getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

//        ((Button)this.findViewById(R.id.main_activity_tabs_button_left)).setOnTouchListener(this);
//        ((Button)this.findViewById(R.id.main_activity_tabs_button_right)).setOnTouchListener(this);
//
//        ((Button)findViewById(R.id.main_activity_tab_button_settings)).setTextColor(Color.GRAY);
        ((Button)findViewById(R.id.main_activity_tab_button_councilmeetings)).setTextColor(Color.WHITE);
        
        tab_bar_scroller = new Scroller(this.findViewById(R.id.horizontalScrollView_main_activity_tabs));
        frag_default_ = new FragmentDefault(this);
        frag_settings = new FragmentSettings(this);
        this.getSupportFragmentManager().beginTransaction().add(R.id.container, frag_default_).commit();

        
        if (frag_default_ == null)
        {
            
        }
        //Show default fragment (for debugging)
        if (savedInstanceState == null)
        {
        	 
        }
    }

    //There is no need for menu key
    //************************************
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_MENU)
        {
            return true;
        }
        else
        {
            return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_MENU)
        {
            return true;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu)
    {
        return false;
    }
    //************************************

    //Tabbar buttons - currently only one
    public void onClick(View v)
    {

       //Log.i("MainActivity", "Clicked.id: " + v.getId());

        // Static buttons:
        switch (v.getId())
        {
            //Council meetings tab
            case R.id.main_activity_tab_button_councilmeetings:

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.container, frag_default_);
                transaction.addToBackStack(null);
                transaction.commit();

//                ((Button)findViewById(R.id.main_activity_tab_button_settings)).setTextColor(Color.GRAY);
                ((Button)findViewById(R.id.main_activity_tab_button_councilmeetings)).setTextColor(Color.WHITE);

                break;

            //Settings-button
            case R.id.imageButton_main_activity_settings:
                ActivityPopupSettings settings = new ActivityPopupSettings();
                settings.showDialog(this);
                break;
            //Settings -button
//            case R.id.main_activity_tab_button_settings:
//                FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
//
//                transaction1.replace(R.id.container, frag_settings);
//                transaction1.addToBackStack(null);
//                transaction1.commit();
//
//                ((Button)findViewById(R.id.main_activity_tab_button_settings)).setTextColor(Color.WHITE);
//                ((Button)findViewById(R.id.main_activity_tab_button_councilmeetings)).setTextColor(Color.GRAY);
//                break;

//            //Scroll left:
//            case R.id.main_activity_tabs_button_left:
//
//                break;
//
//            //Scroll right:
//            case R.id.main_activity_tabs_button_right:
//
//                break;

            default:
                break;

        }

    }


    // Scroll tab bar
//    @Override
//    public boolean onTouch(View v, MotionEvent event)
//    {
//        switch (v.getId())
//        {
//            case R.id.main_activity_tabs_button_left:
//
//                tab_bar_scroller.setDirection(-scroll_speed);
//                if(event.getAction() == android.view.MotionEvent.ACTION_DOWN)
//                {
//                    tab_bar_scroller.start();
//                }
//                else if(event.getAction() == android.view.MotionEvent.ACTION_UP)
//                {
//                    tab_bar_scroller.stop();
//                }
//
//                break;
//
//            case R.id.main_activity_tabs_button_right:
//
//                tab_bar_scroller.setDirection(scroll_speed);
//                if(event.getAction() == android.view.MotionEvent.ACTION_DOWN)
//                {
//                    tab_bar_scroller.start();
//                }
//                else if(event.getAction() == android.view.MotionEvent.ACTION_UP)
//                {
//                    tab_bar_scroller.stop();
//                }
//
//                break;
//
//            default:
//                break;
//        }
//
////        v.performClick();
//        return false;
//    }

    //For scrolling tab bar
    class Scroller
    {

        final Handler mHandler = new Handler();
        private int mInterval = 10;

        boolean run = false;
        int direction_ = 0;

        View view_;

        public Scroller (View view)
        {
            view_ = view;
        }
        public void start()
        {
            run  = true;
            runner.run();
        }
        public void stop()
        {
            run = false;
        }

        public void setDirection(int direction)
        {
            direction_ = direction;
        }

        public

        Runnable runner = new Runnable()
        {
            @Override
            public void run()
            {

                mHandler.postDelayed(this, mInterval);

                view_.scrollBy(direction_, 0);

                if(!run)
                {
                    mHandler.removeCallbacks(this);
                }
            }
        };

    }
}
