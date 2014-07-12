package com.example.helsinkikanava;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

//Example class for demostrating Fragment. -Eetu
public class FragmentDefault extends Fragment implements OnClickListener {

	Context parent_ = null;
	View rootView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	
        rootView = inflater.inflate(R.layout.fragment_meetings, container, false);
        //((Button)rootView.findViewById(R.id.button1)).setOnClickListener(this);
        //((Button)getView().findViewById(R.id.button1)).setOnClickListener(this);
        
        generateYearNavigation();
        return rootView;
    }

    public FragmentDefault(Context parent) {
    	
    	parent_ = parent;
    }

    private void generateYearNavigation(){
    	
    	Log.i("asdas", "adfsd");
    	
    	//TODO
    	int min_year = 2011;
    	int max_year = 2014;

    	Button test = new Button(getActivity());
    	test.setBackgroundColor(Color.BLACK);
    	test.setText("Uusimmat");
    	test.setTextColor(Color.WHITE);
    	
    	ImageButton imgbutton = new ImageButton (getActivity()); 
        
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
        		((int) LayoutParams.WRAP_CONTENT, (int) LayoutParams.WRAP_CONTENT);
        		
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        
        imgbutton.setLayoutParams(params);
        //imgbutton.setImageResource(R.drawable.btn_uusimmat);
        imgbutton.setPadding(0, 0, 0, 0);
        LinearLayout my_root = (LinearLayout) rootView.findViewById(R.id.fragment_meetings_years);
    	my_root.addView(test, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    	//my_root.addView(test, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }
    
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		 
		case R.id.button1: 

			((TextView)getView().findViewById(R.id.textView_current_version_value)).setText(AutoUpdater.getCurrentVersion(parent_));
			((TextView)getView().findViewById(R.id.textView_server_version_value)).setText(AutoUpdater.getNewVersion());

			break;

		default:
			break;
		}
		
	}
}
