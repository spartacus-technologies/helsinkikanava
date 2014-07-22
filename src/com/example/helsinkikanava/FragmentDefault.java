package com.example.helsinkikanava;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import HelsinkiKanavaDataAccess.Metadata;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

//Example class for demostrating Fragment. -Eetu
public class FragmentDefault extends Fragment implements OnClickListener, OnTouchListener, IJsonListener{

	//Context parent_ = null;
	View rootView = null;					//Owns all fragment Views
	Scroller scroller = new Scroller();  	//For scrolling year view
	int scroll_speed = 7;
	String active_year = null;
	private Context parent_;
	ArrayList<String> years = null;
	final int content_id_factor = 100;
	final int video_id_factor = 1000;
	Map<String, ArrayList<Metadata>> content = null;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        rootView = inflater.inflate(R.layout.fragment_meetings, container, false);

        //Listeners:
        ((Button)rootView.findViewById(R.id.fragment_meetings_button_left)).setOnClickListener(this);
        ((Button)rootView.findViewById(R.id.fragment_meetings_button_right)).setOnClickListener(this);
        
        ((Button)rootView.findViewById(R.id.fragment_meetings_button_left)).setOnTouchListener(this);
        ((Button)rootView.findViewById(R.id.fragment_meetings_button_right)).setOnTouchListener(this);

        
        WrapperJSON.RegisterListener(this);
        WrapperJSON.RefreshYears();
        
        return rootView;
    }
    
    
    public FragmentDefault(Context parent) {
    	
    	parent_ = parent;
    }
    
    private void createYearTitle(String year){
    	
    	LinearLayout my_root = (LinearLayout) rootView.findViewById(R.id.fragment_meetings_content);
    	LinearLayout year_title = new LinearLayout(getActivity());
		year_title.setOrientation(LinearLayout.VERTICAL);
		
		TextView tv_year_label = new TextView(getActivity());
		tv_year_label.setText(year);
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
		
		//year_title.setId(debug*10); //TODO
			
		my_root.addView(year_title, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    }
    
    private void generateContent(){
    	
    	LinearLayout my_root = (LinearLayout) rootView.findViewById(R.id.fragment_meetings_content);
    	my_root.removeAllViews();
    	
    	//HIde overlay oading animation:
    	getView().findViewById(R.id.fragment_meetings_overlay).setVisibility(View.INVISIBLE);
    	
    	int content_id_index = 0;
    	
    	//Add year_title:
    	createYearTitle(active_year);
    	
    	//Create data for active year:
    	ArrayList<Metadata> year_data = content.get(active_year);
    	
    	
    	for (Metadata meeting_data : year_data) {
    	    
    		String[] text_content = parseTitleAndDate(meeting_data.title);
    		
    		LinearLayout meeting_layout = new LinearLayout(getActivity());
    		meeting_layout.setOrientation(LinearLayout.HORIZONTAL);
    		
    		LinearLayout text_layout = new LinearLayout(getActivity());
    		text_layout.setOrientation(LinearLayout.VERTICAL);
    		
    		//source. http://sampleprogramz.com/android/imagebutton.php
    		
    		ImageButton img_btn = new ImageButton(getActivity());
            img_btn.setImageResource(R.drawable.play);
    		//img_btn.setImageURI(null);
    		//img_btn.setImageURI(Uri.parse(meeting_data.video.screenshot_url));
    		Log.i("URI test", Uri.parse(meeting_data.video.screenshot_url).toString());
    		
    		img_btn.setPadding(0, 0, 0, 0);	
    		img_btn.setId(Integer.valueOf(active_year)*video_id_factor + content_id_index);
    		Log.i("FragmentDefault:generateContent", "img_btn.setId(" + img_btn.getId() + ")");
    		
    		
    		//Request imagedata:
    		WrapperJSON.RefreshImage(meeting_data.video.screenshot_url);
    		
			img_btn.setOnClickListener(this);
    		/**
    		View overlay = new ImageView(getActivity());
    		overlay.setImageResource(R.drawable.play);
    		img_btn.getOverlay().add(overlay);
    		*/
    		TextView tv_info = new TextView(getActivity());

    		tv_info.setText(text_content[0]);
    		tv_info.setPaintFlags(tv_info.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    		tv_info.setPadding(10, 0, 0, 0);
    		tv_info.setTextColor(Color.BLUE);
    		tv_info.setId(Integer.valueOf(active_year)*content_id_factor + content_id_index);
    		tv_info.setOnClickListener(this);
    		
    		
    		TextView tv_date = new TextView(getActivity());
    		
    		tv_date.setText(text_content[1]);
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
    		++content_id_index;
    	}
    }
    
    String[] parseTitleAndDate(String text){
    	
    	String[] returnvalue = new String[2];
    	
    	returnvalue[0] = text.substring(0, text.indexOf("/", 0));
    	returnvalue[1] = text.substring( text.indexOf("/", 0) + 1);
    	
    	Log.i("teststring1", returnvalue[0]);
    	Log.i("teststring2", returnvalue[1]);
    	
		return returnvalue;
    	
    }
    
    private void generateYearNavigation(){
    	
    	Log.w("generateYearNavigation", "start...");
    	for(String year : years){
    	
    		Log.w("generateYearNavigation", "loop: " + year);
	    	Button year_button = new Button(getActivity());

	    	year_button.setText(year);
	        year_button.setId(Integer.valueOf(year));
	        
	    	year_button.setTextColor(Color.WHITE);
	    	year_button.setBackgroundColor(Color.BLACK);
	        year_button.setOnClickListener(this);
	        
	        //Unselected years with gray:
	        if(year_button.getId() != Integer.valueOf(active_year)){
	        	
	        	year_button.setTextColor(Color.GRAY);
	        }

	        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        		
	        params.addRule(RelativeLayout.CENTER_IN_PARENT);
	        
	        LinearLayout my_root = (LinearLayout) rootView.findViewById(R.id.fragment_meetings_years);
	    	my_root.addView(year_button, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    	}
    	Log.w("generateYearNavigation", "ok...");
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
			
			//Year navigation labels: (not active_year)
			if(v.getId() > 1900 && v.getId() < 2100 && v.getId() != Integer.valueOf(active_year)){
				
		    	//HIde overlay oading animation:
		    	getView().findViewById(R.id.fragment_meetings_overlay).setVisibility(View.VISIBLE);
		    	
				
				Log.i("FragmentDefault", "Bringing " + v.getId() +" to front");
				
				//Change color:
				((TextView)rootView.findViewById(Integer.valueOf(active_year))).setTextColor(Color.GRAY);
				((TextView)rootView.findViewById(v.getId())).setTextColor(Color.WHITE);
				active_year = String.valueOf(v.getId());
				WrapperJSON.RefreshData(active_year);
				
				//Clear content:
				LinearLayout my_root = (LinearLayout) rootView.findViewById(R.id.fragment_meetings_content);
		    	my_root.removeAllViews();
				
		    	
				try {
					
					((ScrollView)rootView.findViewById(R.id.fragment_meetings_scrollView_content)).smoothScrollTo(0, rootView.findViewById(v.getId()*10).getTop());
					
				//Overkill:
				} catch (NullPointerException e) {

					
				}
				
			}
			//Meeting headers:
			else if(v.getId() > 1900*content_id_factor && v.getId() < 2100*content_id_factor){
				
				Intent intent = new Intent(parent_, ActivityVideo.class);
//              intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				
				//get meeting title:
				Log.i("clicked year:", "" + v.getId()/content_id_factor);
				Log.i("clicked view:", "" + (v.getId() % content_id_factor));
				Metadata meet = content.get(String.valueOf(v.getId()/content_id_factor)).get(v.getId() % content_id_factor);
				
				Log.i("title", meet.title);
				intent.putExtra("new_variable_name", meet.title);
				
				startActivity(intent);
			}
			//Imagebuttons for videos:
			else if(v.getId() > 1900*video_id_factor && v.getId() < 2100*video_id_factor){
				
				Log.i("FragmentDefault", "Clicked ID: " + v.getId());

				
				Metadata temp_data = content.get(active_year).get(v.getId() % video_id_factor);
				Log.i("FragmentDefault", "URL: " + temp_data.video.rtmp.netconnection_url + "/" + temp_data.video.rtmp.video_id);
				Uri uri = Uri.parse(temp_data.video.rtmp.netconnection_url + "/" + temp_data.video.rtmp.video_id);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				
				
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

	@Override
	public void YearsAvailable() {

		years = WrapperJSON.GetYears();

		if(years == null || years.size() == 0){
			
			showErrorMessage("Error generating year navigation: no data available");
			return;
		}
		
		else{
			
			//First run(?)
			if(active_year == null) active_year = years.get(0);
			
			//Run UI updates in external thread:
			getActivity().runOnUiThread(new Runnable(){
				
				 public void run() {
					
					//Generate navigation and request metadata:
                    generateYearNavigation();
                    WrapperJSON.RefreshData(String.valueOf(active_year));
                 }
			});	 
		}	
	}

	//Displays error messages in UI with red text:
	void showErrorMessage(String message){
		
		LinearLayout my_root = (LinearLayout) rootView.findViewById(R.id.fragment_meetings_content);
    	my_root.removeAllViews();
		
    	TextView tv_error_message = new TextView(getActivity());
    	tv_error_message.setText(message);
    	tv_error_message.setTextColor(Color.RED);
    	my_root.addView(tv_error_message, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    	
	}
	
	@Override
	public void DataAvailable(final String year) {
		
		Log.i("FragmentMeetings", "DataAvailable for year " + year);
		
		//Create hashmap if non-existent:
		if(content == null) content = new HashMap<String, ArrayList<Metadata>>();
		
		// add year to content map:
		content.put(year, WrapperJSON.GetYearData(year));
				
		Log.i("TSET", content.get(year).toString());
		
		//Run UI updates in external thread:
		getActivity().runOnUiThread(new Runnable(){
			
			 public void run() {
				
				 if(content.get(year) == null || content.get(year).size() == 0){
						
						Log.w("FragmentMeeting" , "Warning: metadata for year " + year + " was null.");
						showErrorMessage("Warning: null or empty response for year " + year + ".");

						return;
					} 
				 generateContent();
             }	
		});
	}
	
	@Override
	public void ImageAvailable(String url) {
		
		Log.i("fragmentdefault", "image available:" + url);
		
	}
}
