package com.ui.helsinkikanava;

import com.example.helsinkikanava.R;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentMeetings extends Fragment implements OnClickListener {

	Context parent_ = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	
    	
        View rootView = inflater.inflate(R.layout.fragment_default, container, false);
        ((Button)rootView.findViewById(R.id.button1)).setOnClickListener(this);
        //((Button)getView().findViewById(R.id.button1)).setOnClickListener(this);
        
        return rootView;
    }
    
    public FragmentMeetings() {
    	 	
    }
    
    public FragmentMeetings(Context parent) {
    	
    	parent_ = parent;
    	
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
