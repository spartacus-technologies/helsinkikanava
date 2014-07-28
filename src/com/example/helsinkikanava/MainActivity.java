package com.example.helsinkikanava;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.view.View.OnTouchListener;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements  OnTouchListener
{
    FragmentDefault frag_default_ = null;
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

        ((Button)this.findViewById(R.id.main_activity_tabs_button_left)).setOnTouchListener(this);
        ((Button)this.findViewById(R.id.main_activity_tabs_button_right)).setOnTouchListener(this);

        ((Button)findViewById(R.id.main_activity_tab_button_news)).setTextColor(Color.GRAY);
        ((Button)findViewById(R.id.main_activity_tab_button_councilmeetings)).setTextColor(Color.WHITE);

        tab_bar_scroller = new Scroller(this.findViewById(R.id.horizontalScrollView_main_activity_tabs));

        if (frag_default_ == null)
        {
            frag_default_ = new FragmentDefault(this);
        }
        //Show default fragment (for debugging)
        if (savedInstanceState == null)
        {
        	 getFragmentManager().beginTransaction().add(R.id.container, frag_default_).commit();
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

        Log.i("MainActivity", "Clicked.id: " + v.getId());

        // Static buttons:
        switch (v.getId())
        {
            //Council meetings tab
            case R.id.main_activity_tab_button_councilmeetings:
                Log.i("MainActivity", "CouncilMeetings tab" );
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

//                transaction.replace(R.id.container, new FragmentMeetings(this));
                transaction.replace(R.id.container, frag_default_);
                transaction.addToBackStack(null);
                transaction.commit();

                ((Button)findViewById(R.id.main_activity_tab_button_news)).setTextColor(Color.GRAY);
                ((Button)findViewById(R.id.main_activity_tab_button_councilmeetings)).setTextColor(Color.WHITE);

                break;

            //DEBUG -button
            case R.id.main_activity_tab_button_news:
                break;

            //Scroll left:
            case R.id.main_activity_tabs_button_left:

                break;

            //Scroll right:
            case R.id.main_activity_tabs_button_right:

                break;

            default:
                break;

        }

    }


    // Scroll tab bar
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (v.getId())
        {
            case R.id.main_activity_tabs_button_left:

                tab_bar_scroller.setDirection(-scroll_speed);
                if(event.getAction() == android.view.MotionEvent.ACTION_DOWN)
                {
                    tab_bar_scroller.start();
                }
                else if(event.getAction() == android.view.MotionEvent.ACTION_UP)
                {
                    tab_bar_scroller.stop();
                }

                break;

            case R.id.main_activity_tabs_button_right:

                tab_bar_scroller.setDirection(scroll_speed);
                if(event.getAction() == android.view.MotionEvent.ACTION_DOWN)
                {
                    tab_bar_scroller.start();
                }
                else if(event.getAction() == android.view.MotionEvent.ACTION_UP)
                {
                    tab_bar_scroller.stop();
                }

                break;

            default:
                break;
        }

//        v.performClick();
        return false;
    }

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
