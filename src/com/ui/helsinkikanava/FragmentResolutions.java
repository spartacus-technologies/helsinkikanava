package com.ui.helsinkikanava;

import java.util.TreeSet;

import com.ui.helsinkikanava.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentResolutions extends Fragment {
	
	View rootView_ = null;					//Owns all fragment Views
    Context parent_ = null;
    String session_title_ = null;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        View rootView = inflater.inflate(R.layout.fragment_resolutions, container, false);
        rootView_ = rootView;
        
        generateResolutionListing();
        
        return rootView;
    }
    
    public FragmentResolutions()
    {

    }
    
    public FragmentResolutions(Context parent, String title)
    {
    	parent_ = parent;
        session_title_ = title;
    }
    
    private void generateResolutionListing()
    {
        //Get Parties from wrapperJSON
        TreeSet<String> issueTitles = WrapperJSON.GetIssueTitles(session_title_);

        //There was no data
        if (issueTitles == null ) return;

        displayIssueTitle(issueTitles);
        
        //Get participants for each party from wrapperJSON
        int i = 0;
        for (String issueTitle : issueTitles)
        {
            String resolution = WrapperJSON.GetResolutions(session_title_, issueTitle);

            displayIssueResolution(i, resolution);
            ++i;
        }
        
    }
    
    private void displayIssueTitle(TreeSet<String> resolutions)
    {
        int i = 0;
        for (String resolution : resolutions)
        {
            //Create title for party
            LinearLayout resolution_layout = new LinearLayout( getActivity() );
            resolution_layout.setOrientation(LinearLayout.VERTICAL);
            resolution_layout.setId(i);

            TextView title = new TextView(getActivity());

            title.setText(resolution);
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            View underline = new View(getActivity());
            underline.setBackgroundColor(Color.BLACK);
            underline.setPadding(0, 0, 0, 5);

            View spacer = new View(getActivity());
            spacer.setBackgroundColor(Color.TRANSPARENT);
            spacer.setPadding(0, 0, 0, 5);

            resolution_layout.addView(title, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));

            resolution_layout.addView(underline, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 5));
            resolution_layout.addView(spacer, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 5));

            ((LinearLayout) rootView_.findViewById(R.id.fragment_resolution_content)).addView(resolution_layout, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT));
            ++i;
        }
    }
    
    private void displayIssueResolution(int title, String resolution)
    {
        LinearLayout my_root = (LinearLayout) rootView_.findViewById(title);

        LinearLayout text_layout = new LinearLayout(getActivity());
        text_layout.setOrientation( LinearLayout.VERTICAL );

        TextView name = new TextView(getActivity());

        name.setText(resolution);
        name.setPadding(40,0,0,10);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        View spacer = new View(getActivity());
        spacer.setBackgroundColor(Color.TRANSPARENT);
        spacer.setPadding(0, 0, 0, 5);
        
        text_layout.addView(name, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
        
        text_layout.addView(spacer, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 50));
        
        my_root.addView(text_layout, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
    }
    
}
