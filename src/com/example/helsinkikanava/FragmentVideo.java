package com.example.helsinkikanava;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.ActionBar.LayoutParams;

public class FragmentVideo extends Fragment {
	
	
	Session meeting_info_ = null;
	View rootView_ = null;					//Owns all fragment Views
	Context parent_ = null;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        rootView_ = rootView;
        
        
        
        //Debug: TODO
        generateVideoEvent("Meaningless talking.", "1:04", "Description1");
        generateVideoEvent("Coffee break.", "2:04", "Description2");
        generateVideoEvent("Voting for rising own salary.", "4:04", "Description3");
        generateVideoEvent("Voting for rising own salary.", "4:04", "Description3");
        generateVideoEvent("Voting for rising own salary.", "4:04", "Description3");
        generateVideoEvent("Voting for rising own salary.", "4:04", "Description3");
        generateVideoEvent("Voting for rising own salary.", "4:04", "Description3");
        generateVideoEvent("Voting for rising own salary.", "4:04", "Description3");
        return rootView;
    }
    
    /**
    Note: Don't use.
   */
    public FragmentVideo(Context parent) {
    	
    	parent_ = parent;
    }
    
    /**
    Note: call this one when implementation is ready.
   */
    public FragmentVideo(Context parent, Session data) {
    	
    	parent_ = parent;
    	meeting_info_ = data;
    }

    /**
    Note: Don't use.
   */
    public FragmentVideo() {

    }
    
    void generateVideoEvent(String title, String timestamp, String description){
    	
    	LinearLayout video_event_layout = new LinearLayout(getActivity());
		video_event_layout.setOrientation(LinearLayout.VERTICAL);
		
		TextView event_title = new TextView(getActivity());
		TextView event_description = new TextView(getActivity());
		event_title.setText(timestamp + ":  " + title);
		event_description.setText(description);
		event_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		
		//View underline = new View(getActivity());
		//underline.setBackgroundColor(Color.BLACK);
		//underline.setPadding(0, 0, 0, 5);
		
		View spacer =  new View(getActivity());
		spacer.setBackgroundColor(Color.TRANSPARENT);
		spacer.setPadding(0, 0, 0, 5);

		video_event_layout.addView(event_title, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		video_event_layout.addView(event_description, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		//video_event_layout.addView(underline, new LayoutParams(LayoutParams.MATCH_PARENT, 5));
		video_event_layout.addView(spacer, new LayoutParams(LayoutParams.MATCH_PARENT, 5));
		
		//year_title.setId(debug*10);
		
		((LinearLayout)rootView_.findViewById(R.id.fragment_video_title_layout)).addView(video_event_layout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

    }
    
    
}
