package com.ui.helsinkikanava;

import com.example.helsinkikanava.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.view.View.OnTouchListener;

/**
 * Created by vesa on 15.7.2014.
 */
public class ActivityVideo extends Activity implements OnTouchListener
{
    private Scroller tab_bar_scroller;
    int scroll_speed = 7;
    Fragment frag_video_ = null;
    Fragment frag_participant_ = null;
    Fragment frag_resolutions_ = null;
    String title_ = null;

    public ActivityVideo()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(this.getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video);

        ((Button)this.findViewById(R.id.video_activity_tabs_button_left)).setOnTouchListener(this);
        ((Button)this.findViewById(R.id.video_activity_tabs_button_right)).setOnTouchListener(this);

        ((Button)findViewById(R.id.video_activity_tab_button_resolutions)).setTextColor(Color.GRAY);
        ((Button)findViewById(R.id.video_activity_tab_button_participants)).setTextColor(Color.GRAY);
        ((Button)findViewById(R.id.video_activity_tab_button_video)).setTextColor(Color.WHITE);

        //Session title from intent extras
        Bundle extras = getIntent().getExtras();
        String title = null;
        if (extras != null)
        {
            title = extras.getString("new_variable_name");
        }

        //If title is different than previously, create new fragments
        if ( title != null && title_ != null && !title_.equals(title) )
        {
            frag_video_ = null;
            frag_participant_ = null;
            frag_resolutions_ = null;
        }

        //Create fragments
        if ( frag_video_ == null || frag_participant_ == null || frag_resolutions_ == null )
        {
            title_ = title;
            frag_video_ = new FragmentVideo(this, title_);
            frag_participant_ = new FragmentParticipants(this, title_);
            frag_resolutions_ = new FragmentResolutions(this, title_);
        }

        tab_bar_scroller = new Scroller(this.findViewById(R.id.horizontalScrollView_video_activity_tabs));

        //Show video fragment by default
        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction().add(R.id.video_container, frag_video_).commit();
        }
    }

    public void onClick(View v)
    {

       //Log.i("VideoActivity", "Clicked.id: " + v.getId());

        // Static buttons:
        switch (v.getId())
        {

            //Council meetings tab
            case R.id.video_activity_tab_button_video:
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

//                transaction.replace(R.id.container, new FragmentMeetings(this));
                transaction.replace(R.id.video_container, frag_video_);
                transaction.addToBackStack(null);
                transaction.commit();

                ((Button)findViewById(R.id.video_activity_tab_button_resolutions)).setTextColor(Color.GRAY);
                ((Button)findViewById(R.id.video_activity_tab_button_participants)).setTextColor(Color.GRAY);
                ((Button)findViewById(R.id.video_activity_tab_button_video)).setTextColor(Color.WHITE);

                break;

            //Council meetings tab
            case R.id.video_activity_tab_button_participants:
                FragmentTransaction transaction1 = getFragmentManager().beginTransaction();

//                transaction.replace(R.id.container, new FragmentMeetings(this));
                transaction1.replace(R.id.video_container, frag_participant_);
                transaction1.addToBackStack(null);
                transaction1.commit();

                ((Button)findViewById(R.id.video_activity_tab_button_resolutions)).setTextColor(Color.GRAY);
                ((Button)findViewById(R.id.video_activity_tab_button_participants)).setTextColor(Color.WHITE);
                ((Button)findViewById(R.id.video_activity_tab_button_video)).setTextColor(Color.GRAY);

                break;

            //Council meetings tab
            case R.id.video_activity_tab_button_resolutions:

                FragmentTransaction transaction2 = getFragmentManager().beginTransaction();

//                transaction.replace(R.id.container, new FragmentMeetings(this));
                transaction2.replace(R.id.video_container, frag_resolutions_);
                transaction2.addToBackStack(null);
                transaction2.commit();

                ((Button)findViewById(R.id.video_activity_tab_button_resolutions)).setTextColor(Color.WHITE);
                ((Button)findViewById(R.id.video_activity_tab_button_participants)).setTextColor(Color.GRAY);
                ((Button)findViewById(R.id.video_activity_tab_button_video)).setTextColor(Color.GRAY);

                break;

            case R.id.imageButton_video_activity_back:

                this.finish();
                break;

            case R.id.imageButton_video_activity_settings:
                ActivityPopupSettings settings = new ActivityPopupSettings();
                settings.showDialog(this);
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
            case R.id.video_activity_tabs_button_left:

                tab_bar_scroller.setDirection(-scroll_speed);
                if(event.getAction() == android.view.MotionEvent.ACTION_DOWN){


                    tab_bar_scroller.start();
                }
                else if(event.getAction() == android.view.MotionEvent.ACTION_UP){

                    tab_bar_scroller.stop();
                }

                break;

            case R.id.video_activity_tabs_button_right:

                tab_bar_scroller.setDirection(scroll_speed);
                if(event.getAction() == android.view.MotionEvent.ACTION_DOWN){


                    tab_bar_scroller.start();
                }
                else if(event.getAction() == android.view.MotionEvent.ACTION_UP){

                    tab_bar_scroller.stop();
                }

                break;


            default:
                break;
        }

//        v.performClick();
        return false;
    }

    class Scroller
    {		//For scrolling tab bar

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