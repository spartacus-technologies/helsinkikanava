package com.example.helsinkikanava;

import java.util.Calendar;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

//Example class for demostrating Fragment. -Eetu
public class FragmentDefault extends Fragment implements OnClickListener, OnTouchListener {

	//Context parent_ = null;
	View rootView = null;					//Owns all fragment Views
	Scroller scroller = new Scroller();  	//For scrolling year view
	int scroll_speed = 7;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	
        rootView = inflater.inflate(R.layout.fragment_meetings, container, false);
        //((Button)rootView.findViewById(R.id.button1)).setOnClickListener(this);
        ((Button)rootView.findViewById(R.id.fragment_meetings_button_left)).setOnClickListener(this);
        ((Button)rootView.findViewById(R.id.fragment_meetings_button_right)).setOnClickListener(this);
        
        ((Button)rootView.findViewById(R.id.fragment_meetings_button_left)).setOnTouchListener(this);
        ((Button)rootView.findViewById(R.id.fragment_meetings_button_right)).setOnTouchListener(this);
        //rootView.findViewById(R.id.fragment_meetings_button_left)
   
        generateYearNavigation();
        generateDummyContent();
        
        return rootView;
    }
    
    public FragmentDefault() {

    }
    
    /*
    public FragmentDefault(Context parent) {
    	
    	parent_ = parent;
    }
    */
    
    private void generateDummyContent(){
    	
    	LinearLayout my_root = (LinearLayout) rootView.findViewById(R.id.fragment_meetings_content);
    	
    	for(int i = 0; i < 6; ++i){
    		
    		LinearLayout meeting_layout = new LinearLayout(getActivity());
    		meeting_layout.setOrientation(LinearLayout.HORIZONTAL);
    		
    		LinearLayout text_layout = new LinearLayout(getActivity());
    		text_layout.setOrientation(LinearLayout.VERTICAL);
    		
    		//source. http://sampleprogramz.com/android/imagebutton.php
    		
    		ImageButton img_btn = new ImageButton(getActivity());
            img_btn.setImageResource(R.drawable.test_meeting);
    		img_btn.setPadding(0, 0, 0, 0);
    		
    		TextView tv_info = new TextView(getActivity());
    		tv_info.setText("This is sample text about some meeting.");
    		tv_info.setPadding(10, 0, 0, 0);
    		tv_info.setTextColor(Color.BLUE);
    		
    		TextView tv_date = new TextView(getActivity());
    		tv_date.setText( 
    						Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "." +
							Calendar.getInstance().get(Calendar.MONTH) + "." +
							Calendar.getInstance().get(Calendar.YEAR)
    					);
    		
    		tv_date.setPadding(10, 0, 0, 0);
    		
    		
    		text_layout.addView(tv_info, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    		text_layout.addView(tv_date, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    		
    		meeting_layout.addView(img_btn, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    		meeting_layout.addView(text_layout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    		
    		ImageView separator = new ImageView(getActivity());
    		separator.setImageResource(R.drawable.separator_horizontal);
    		separator.setPadding(0, 7, 0, 7);
    		
    		//Add views to list:
    		my_root.addView(meeting_layout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    		my_root.addView(separator, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    	}
    	
    	
    }
    
    
    private void generateYearNavigation(){
    		
    	int year_index = 2014;		//TODO
    	int min_year = 2008;		//TODO

    	while(year_index >= min_year){
    	
	    	Button year_button = new Button(getActivity());
	    	year_button.setBackgroundColor(Color.BLACK);
	    	year_button.setText(Integer.toString(year_index));
	    	year_button.setTextColor(Color.WHITE);
	        year_button.setId(min_year);
	        
	        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        		
	        params.addRule(RelativeLayout.CENTER_IN_PARENT);
	        
	        LinearLayout my_root = (LinearLayout) rootView.findViewById(R.id.fragment_meetings_years);
	    	my_root.addView(year_button, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
	    	
	    	--year_index;
    	}
    }
    
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		
		case R.id.button1: 

			((TextView)getView().findViewById(R.id.textView_current_version_value)).setText(AutoUpdater.getCurrentVersion(getActivity()));
			((TextView)getView().findViewById(R.id.textView_server_version_value)).setText(AutoUpdater.getNewVersion());

			break;

		//Scroll left:
		case R.id.fragment_meetings_button_left: 
			
			break;
			
		//Scroll right:	
		case R.id.fragment_meetings_button_right: 
			
			break;
		default:
			break;
		}
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
				
		switch (v.getId()) {
		
		case R.id.fragment_meetings_button_left:
			
	        scroller.setDirection(-scroll_speed);
			if(event.getAction() == android.view.MotionEvent.ACTION_DOWN){
				

				scroller.start();
			}
			else if(event.getAction() == android.view.MotionEvent.ACTION_UP){
				
				scroller.stop();
			}
			
			break;
			
		case R.id.fragment_meetings_button_right:
			
	        scroller.setDirection(scroll_speed);
			if(event.getAction() == android.view.MotionEvent.ACTION_DOWN){
				

				scroller.start();
			}
			else if(event.getAction() == android.view.MotionEvent.ACTION_UP){
				
				scroller.stop();
			}
			
			break;
			
			
		default:
			break;
		}
		
		Log.i("FragmentDefault", event.toString());
		//Log.i("FragmentDefault", "touch");
		
		v.performClick();
		return false;
	}
	
	class Scroller{		//For scrolling year list
		
		final Handler mHandler = new Handler();
		private int mInterval = 10;
		
		boolean run = false;
		int direction_ = 0;
		
		public void start(){
			
			run  = true;
			runner.run();
		}
		public void stop(){
			
			run = false;
		}
		
		public void setDirection(int direction){
			
			direction_ = direction;
		}
		
		public 
		
		Runnable runner = new Runnable() {
			
		    @Override 
		    public void run() {
		    	
		    	Log.i("Scroller", "running...");
		    	
		    	mHandler.postDelayed(this, mInterval);
		    	((HorizontalScrollView)rootView.findViewById(R.id.horizontalScrollView_years)).scrollBy(direction_, 0);
		    	
				if(!run){
					
					Log.w("Scroller", "quitting...");
					mHandler.removeCallbacks(this);
					//finish();
				}
		    }
		};
		
	}
	
}
