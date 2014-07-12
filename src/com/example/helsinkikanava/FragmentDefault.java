package com.example.helsinkikanava;

import java.sql.Date;
import java.util.Calendar;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
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
	int debug = 2014;
	
	
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
    		
    		//Add year_title:
    		if(true){
    			
    			LinearLayout year_title = new LinearLayout(getActivity());
    			year_title.setOrientation(LinearLayout.VERTICAL);
    			
    			TextView tv_year_label = new TextView(getActivity());
    			tv_year_label.setText(debug + "");
    			tv_year_label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    			
    			View underline = new View(getActivity());
    			underline.setBackgroundColor(Color.BLACK);
    			underline.setPadding(0, 0, 0, 5);
    			
    			View spacer =  new View(getActivity());
    			spacer.setBackgroundColor(Color.TRANSPARENT);
    			spacer.setPadding(0, 0, 0, 5);
    			
    			/*
    			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    			params.setMargins(0, 0, 0, 10);
    			underline.setLayoutParams(params);
    			*/
    			
    			
    			year_title.addView(tv_year_label, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    			year_title.addView(underline, new LayoutParams(LayoutParams.MATCH_PARENT, 5));
    			year_title.addView(spacer, new LayoutParams(LayoutParams.MATCH_PARENT, 5));
    			
    			year_title.setId(debug*10); //TODO
    			
    			my_root.addView(year_title, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    		}
    		
    		LinearLayout meeting_layout = new LinearLayout(getActivity());
    		meeting_layout.setOrientation(LinearLayout.HORIZONTAL);
    		
    		LinearLayout text_layout = new LinearLayout(getActivity());
    		text_layout.setOrientation(LinearLayout.VERTICAL);
    		
    		//source. http://sampleprogramz.com/android/imagebutton.php
    		
    		ImageButton img_btn = new ImageButton(getActivity());
            img_btn.setImageResource(R.drawable.test_meeting);
    		img_btn.setPadding(0, 0, 0, 0);	
    		//img_btn.setId(id); //TODO
			
    		/**
    		View overlay = new ImageView(getActivity());
    		overlay.setImageResource(R.drawable.play);
    		img_btn.getOverlay().add(overlay);
    		*/
    		TextView tv_info = new TextView(getActivity());
    		tv_info.setText("This is sample text about some meeting.");
    		tv_info.setPadding(10, 0, 0, 0);
    		tv_info.setTextColor(Color.BLUE);
    		
    		TextView tv_date = new TextView(getActivity());
    		
    		//Generate date:
    		//@SuppressWarnings("deprecation")
			//Date date = new Date(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    		//date.
    		
    		tv_date.setText( 
    						Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "." +
							Calendar.getInstance().get(Calendar.MONTH) + "." +
							Calendar.getInstance().get(Calendar.YEAR)
    					);
    		
    		tv_date.setPadding(10, 0, 0, 0);
    		
    		
    		text_layout.addView(tv_info, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    		text_layout.addView(tv_date, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    		
    		meeting_layout.addView(img_btn, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    		
    		//TODO
    		View overlay = new View(getActivity());
			overlay.setBackgroundColor(Color.RED);
			overlay.setLayoutParams(new LayoutParams(img_btn.getLayoutParams()));
    		
    		
    		
    		//meeting_layout.addView(overlay);
    		meeting_layout.addView(text_layout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    		
    		ImageView separator = new ImageView(getActivity());
    		separator.setImageResource(R.drawable.separator_horizontal);
    		separator.setPadding(0, 7, 0, 7);
    		
    		//Add views to list:
    		my_root.addView(meeting_layout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    		my_root.addView(separator, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    		
    		--debug;
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
	        year_button.setId(year_index);
	        year_button.setOnClickListener(this);
	        
	        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        		
	        params.addRule(RelativeLayout.CENTER_IN_PARENT);
	        
	        LinearLayout my_root = (LinearLayout) rootView.findViewById(R.id.fragment_meetings_years);
	    	my_root.addView(year_button, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
	    	
	    	--year_index;
    	}
    }
    
	@Override
	public void onClick(View v) {

		Log.i("FragmentDefault", "Cliked.id: " + v.getId());
		
		//Static buttons:
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

		//Dynamic buttons:
		default:
			
			//Year navigation labels:
			if(v.getId() > 1900 && v.getId() < 2100){
				
				Log.i("FragmentDefault", "Bringing " + v.getId() +" to front");
				
				try {
					
					((ScrollView)rootView.findViewById(R.id.fragment_meetings_scrollView_content)).smoothScrollTo(0, rootView.findViewById(v.getId()*10).getTop());
					
				//Overkill:
				} catch (NullPointerException e) {

					
				}
				
			}
			else{
				
				Log.i("FragmentDefault", "Warning: unknown button.");
			}
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
