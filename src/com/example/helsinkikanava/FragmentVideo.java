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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.ActionBar.LayoutParams;

public class FragmentVideo extends Fragment {
	
	
	View rootView_ = null;					//Owns all fragment Views
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	
        View rootView = inflater.inflate(R.layout.fragment_default, container, false);
        rootView_ = rootView;
        return rootView;
    }

    public FragmentVideo(Context parent) {

    }
    
    void generateDummyTitles(){
    	
    	LinearLayout year_title = new LinearLayout(getActivity());
		year_title.setOrientation(LinearLayout.VERTICAL);
		
		TextView tv_year_label = new TextView(getActivity());
		tv_year_label.setText("Test");
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
		
		((LinearLayout)rootView_.findViewById(R.id.fragment_video_title_layout)).addView(year_title, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    	
    	
    }
    
    
}
