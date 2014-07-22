package com.example.helsinkikanava;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.view.View.OnTouchListener;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements IJsonListener, OnTouchListener
{

    private WrapperJSON wrapperJSON = new WrapperJSON();
    private ArrayList<Session> council_meetings = new ArrayList<Session>();
    private Scroller tab_bar_scroller;
    int scroll_speed = 7;

    public MainActivity()
    {
        //FIXME Sallii dataliikenteen käytön pääsäikeessä DataAccess "kirjaston" debuggausta varten :D (voi tulla pääohjelma hiukan jäätäväksi ilman säikeistystä :)
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

/*
        wrapperJSON.RegisterListener(this);
        wrapperJSON.RefreshYears();
        WrapperJSON.RefreshData("2010");
*/


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

        //Show default fragment (for debugging)
        if (savedInstanceState == null)
        {
        	 getFragmentManager().beginTransaction().add(R.id.container, new FragmentDefault(this)).commit();
        }
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override 
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
		
		case R.id.check_updates_menu_button:
			
			//Log.i("MainActivity", "check_updates");
					
			new PopupCheckUpdates().showDialog(this);
			//PopupCheckUpdates.showDialog(this);
			
			break;
			
		case R.id.action_settings:
			
			new ActivityPopupSettings().showDialog(this);
			//PopupCheckUpdates.showDialog(this);
			Log.i("MainActivity", "action_settings");
			break;
		default:
			break;
		}
        
        return super.onOptionsItemSelected(item);
    }


    public void onClick(View v)
    {

        Log.i("MainActivity", "Clicked.id: " + v.getId());

        // Static buttons:
        switch (v.getId())
        {
            //Refresh data
//            case R.id.imageButton_refresh:
//
//                Log.i("MainActivity", "REFRESH" );
//                refreshData();
//
//                break;

            //Council meetings tab
            case R.id.main_activity_tab_button_councilmeetings:
                Log.i("MainActivity", "CouncilMeetings tab" );
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

//                transaction.replace(R.id.container, new FragmentMeetings(this));
                transaction.replace(R.id.container, new FragmentDefault(this));
                transaction.addToBackStack(null);
                transaction.commit();

                ((Button)findViewById(R.id.main_activity_tab_button_news)).setTextColor(Color.GRAY);
                ((Button)findViewById(R.id.main_activity_tab_button_councilmeetings)).setTextColor(Color.WHITE);

                break;

            //Council meetings tab
            case R.id.main_activity_tab_button_news:
                Log.i("MainActivity", "News tab" );

//                FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
//
////                transaction.replace(R.id.container, new FragmentMeetings(this));
//                transaction1.replace(R.id.container, new FragmentDefault());
//                transaction1.addToBackStack(null);
//                transaction1.commit();

                ((Button)findViewById(R.id.main_activity_tab_button_news)).setTextColor(Color.WHITE);
                ((Button)findViewById(R.id.main_activity_tab_button_councilmeetings)).setTextColor(Color.GRAY);

                Intent intent = new Intent(this, ActivityVideo.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

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
        Log.i("MainActivity.onTouch", "alkaa");
        switch (v.getId())
        {
            case R.id.main_activity_tabs_button_left:

                tab_bar_scroller.setDirection(-scroll_speed);
                if(event.getAction() == android.view.MotionEvent.ACTION_DOWN){


                    tab_bar_scroller.start();
                }
                else if(event.getAction() == android.view.MotionEvent.ACTION_UP){

                    tab_bar_scroller.stop();
                }

                break;

            case R.id.main_activity_tabs_button_right:

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

        Log.i("MainActivity", event.toString());
        v.performClick();
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
                Log.i("TabBarScroller", "running...");

                mHandler.postDelayed(this, mInterval);

                view_.scrollBy(direction_, 0);

                if(!run)
                {
                    Log.w("TabBarScroller", "quitting...");
                    mHandler.removeCallbacks(this);
                    //finish();
                }
            }
        };

    }

	@Override
	public void YearsAvailable() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void DataAvailable(String year) {
		// TODO Auto-generated method stub
        /*WrapperJSON.getParties("2010","vuoden2010sessioninUrl");*/
	}
	
	@Override
	public void ImageAvailable(String url) {
		// TODO Auto-generated method stub
		
	}
}
