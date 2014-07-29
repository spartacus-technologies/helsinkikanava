package com.example.helsinkikanava;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import HelsinkiKanavaDataAccess.Metadata;
import HelsinkiKanavaDataClasses.Issues;
import android.R.integer;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.ActionBar.LayoutParams;

public class FragmentVideo extends Fragment implements OnClickListener{
	
	View rootView_ = null;					//Owns all fragment Views
	Context parent_ = null;
	private String title_;
	Metadata meeting_data = null;
	
	final String EXTRA_POSITION	= "position";
	//i.putExtra( EXTRA_POSITION, 10000 );
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        rootView_ = rootView; 
        
        //Set titles:
        ((TextView)rootView.findViewById(R.id.fragment_video_title)).setText(parseTitleAndDate(title_)[0]);
        ((TextView)rootView.findViewById(R.id.fragment_video_date)).setText(parseTitleAndDate(title_)[1]);
        
        //Generate dynamic content:
        generatePreview();
        generateVideoContent();
        return rootView;
    }
    
    /**
    Note: call this one when implementation is ready.
   */
    public FragmentVideo(Context parent, String title) {
    	
    	parent_ = parent;
    	//meeting_info_ = data;
    	title_ = title;
    	Log.i("FragmentVideo:FragmentVideo", "title_=" + title_);
    }
    
    void generateVideoEvent(String timestamp, String description){
    	
    	LinearLayout video_event_layout = new LinearLayout(getActivity());
		video_event_layout.setOrientation(LinearLayout.VERTICAL);
		
		TextView event_title = new TextView(getActivity());
		TextView event_description = new TextView(getActivity());
		event_title.setText(convertSecondstoTime(timestamp));
		event_title.setId(Integer.valueOf(timestamp));
		event_title.setOnClickListener(this);
		Log.i("FragmentVideo:generateVideoEvent", "setId = " + Integer.valueOf(timestamp));
		
		event_description.setText(description);
		event_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		
		//getResources().getDimensionPixelSize(R.attr.textAppearanceLarge);
		//View underline = new View(getActivity());
		//underline.setBackgroundColor(Color.BLACK);
		//underline.setPadding(0, 0, 0, 5);
		
		View spacer =  new View(getActivity());
		spacer.setBackgroundColor(Color.TRANSPARENT);
		spacer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 100));

		video_event_layout.addView(event_title, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		video_event_layout.addView(event_description, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		//video_event_layout.addView(underline, new LayoutParams(LayoutParams.MATCH_PARENT, 5));
		video_event_layout.addView(spacer, new LayoutParams(LayoutParams.MATCH_PARENT, 50));
		
		//year_title.setId(debug*10);
		
		((LinearLayout)rootView_.findViewById(R.id.fragment_video_content)).addView(video_event_layout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

    }
    
    void generatePreview(){
    	

		//ImageButton with overlay:
		//=========================
		Log.i("generatePreview", "test1");
		FrameLayout previewLayout = (FrameLayout) getActivity().findViewById(R.id.fragment_video_preview);
		
		if(previewLayout == null){ 
			Log.w("FragmentVideo:generatePreview", "Warning: layout null -> quitting.");
			return;
		}
		
		LayoutParams l_parameters1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LayoutParams l_parameters2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		l_parameters1.gravity = Gravity.CENTER;
		l_parameters2.gravity = Gravity.CENTER;
		Log.i("generatePreview", "test2");
		
		ImageButton img_btn = new ImageButton(getActivity());
        img_btn.setImageResource(R.drawable.test_meeting);
        Log.i("generatePreview", "test3");
		ImageView overlay = new ImageView(getActivity());
		overlay.setImageResource(R.drawable.play_small);
		Log.i("generatePreview", "test4");
		previewLayout.setLayoutParams(l_parameters1);
		previewLayout.addView(img_btn, l_parameters1);
		//previewLayout.addView(overlay, l_parameters2);
		Log.i("generatePreview", "test5");
    }
    
    void generateVideoContent(){
    	
    	meeting_data = WrapperJSON.RefreshMeetingData(title_);
    	
    	for (Issues iss : meeting_data.issues) {
			
    		generateVideoEvent(iss.video_position, iss.title);
		}

    }
    
    String convertSecondstoTime(String input){
    	
    	int seconds = (Integer.valueOf(input)) % 60 ;
    	int minutes = ((Integer.valueOf(input) /60) % 60);
    	int hours   = ((Integer.valueOf(input) / (60*60)) % 24);
    	
    	
    	//Ghetto:
    	if(minutes < 10 && seconds < 10){
    		
    		return hours + ":0" + minutes + ":0" + seconds;
    	}
    	
    	//Ghetto:
    	if(seconds < 10){
    		
    		return hours + ":" + minutes + ":0" + seconds;
    	}
    	//Ghetompi:
    	if(minutes < 10){
    		
    		return hours + ":0" + minutes + ":" + seconds;
    	}

    	
		return hours + ":" + minutes + ":" + seconds;
		
    }
    
    String[] parseTitleAndDate(String text){
    	
    	String[] returnvalue = new String[2];
    	
    	returnvalue[0] = text.substring(0, text.indexOf("/", 0));
    	returnvalue[1] = text.substring( text.indexOf("/", 0) + 1);
    	
    	Log.i("teststring1", returnvalue[0]);
    	Log.i("teststring2", returnvalue[1]);
    	
		return returnvalue;
    	
    }

	@Override
	public void onClick(View v) {
		
		Uri uri = Uri.parse(meeting_data.video.rtmp.netconnection_url + "/" + meeting_data.video.rtmp.video_id);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.putExtra( EXTRA_POSITION, v.getId()*1000);
		
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			
			Log.w("FragmentDefault.onClick()", "ActivityNotFoundException");
			Toast.makeText(getActivity(), "Warning: video player not found. Consider installing MX Player.", Toast.LENGTH_LONG).show();
		}
		
	}

    
}
