package com.example.helsinkikanava;



import HelsinkiKanavaDataAccess.Metadata;
import HelsinkiKanavaDataClasses.Issues;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import android.widget.ImageView.ScaleType;
import android.app.ActionBar.LayoutParams;

public class FragmentVideo extends Fragment implements OnClickListener, IJsonListener{
	
	private static final String TAG = "FragmentVideo";
	View rootView_ = null;					//Owns all fragment Views
	Context parent_ = null;
	private String title_;
	Metadata meeting_data = null;
	
	final int preview_height = 99;
	final int preview_width = 176;
	final String EXTRA_POSITION	= "position";
	
	static int PreviewID = 1;
	
	static int getNewPreviewID(){
		
		++PreviewID;
		return PreviewID;
	}
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        rootView_ = rootView; 
        
        //Set titles:
        ((TextView)rootView.findViewById(R.id.fragment_video_TextView_title)).setText(parseTitleAndDate(title_)[0]);
        ((TextView)rootView.findViewById(R.id.fragment_video_TextView_date)).setText(parseTitleAndDate(title_)[1]);
        
        //Generate dynamic content:
    	meeting_data = WrapperJSON.RefreshMeetingData(title_);
    	WrapperJSON.RegisterListener(this);
        generateVideoContent();
        generatePreview();
        return rootView;
    }
    
    public void onDestroyView(){
    	
    	Log.i(TAG, "onDestroyView()");
    	
    	WrapperJSON.UnregisterListener(this);
    	super.onDestroyView();
    }
    /**
    Note: call this one when implementation is ready.
   */
    public FragmentVideo(Context parent, String title) {
    	
    	parent_ = parent;
    	title_ = title;
    	Log.i("FragmentVideo:FragmentVideo", "title_=" + title_);
    }
    
    void generateVideoEvent(String timestamp, String description){
    	
    	LinearLayout video_event_main_layout = new LinearLayout(getActivity());
    	
    	video_event_main_layout.setOrientation(LinearLayout.HORIZONTAL);
    	video_event_main_layout.setBackgroundResource(R.drawable.videogradient);
    	
    	FrameLayout video_link_layout = new FrameLayout(getActivity()); 
    	
    	TextView video_link = new TextView(getActivity());
    	LayoutParams video_link_layout_params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	
    	video_link.setId(Integer.valueOf(timestamp));
    	video_link.setOnClickListener(this);
    	video_link.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 55);
    	video_link.setText(" ‹ ");
    	//video_link.setBackgroundColor(Color.GRAY);
    	video_link_layout.addView(video_link, video_link_layout_params);
    	//video_link_layout.setGravity(Gravity.CENTER);
    	
    	LinearLayout video_description_layout = new LinearLayout(getActivity());
		video_description_layout.setOrientation(LinearLayout.VERTICAL);
		
		
		TextView event_title = new TextView(getActivity());
		TextView event_description = new TextView(getActivity());
		
		LayoutParams llp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	    llp.setMargins(20, 0, 20, 0); // llp.setMargins(left, top, right, bottom);

		event_title.setLayoutParams(llp);
	    
		event_title.setText(convertSecondstoTime(timestamp));
		event_title.setId(Integer.valueOf(timestamp));
		//event_title.setOnClickListener(this);
		
		Log.i("FragmentVideo:generateVideoEvent", "setId = " + Integer.valueOf(timestamp));
		
		event_description.setText(description);
		event_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

		View spacer =  new View(getActivity());
		spacer.setBackgroundColor(Color.TRANSPARENT);
		spacer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 100));

		video_description_layout.addView(event_title, llp);
		video_description_layout.addView(event_description, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		//video_event_layout.addView(underline, new LayoutParams(LayoutParams.MATCH_PARENT, 5));
		video_description_layout.addView(spacer, new LayoutParams(LayoutParams.MATCH_PARENT, 50));
		
		//year_title.setId(debug*10);
    	
		video_event_main_layout.addView(video_link_layout, llp);
		video_event_main_layout.addView(video_description_layout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		((LinearLayout)rootView_.findViewById(R.id.fragment_video_layout_content)).addView(video_event_main_layout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    }
    
    void generatePreview(){
    	
		//ImageButton with overlay:
		//=========================
		
		LinearLayout video_content_layout = (LinearLayout) rootView_.findViewById(R.id.fragment_video_preview_layout);
		
		LayoutParams previewLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//previewLayoutParams.gravity = Gravity.CENTER;
		
		
		FrameLayout previewLayout = new FrameLayout(getActivity());
		previewLayout.setLayoutParams(previewLayoutParams);
		//previewLayout.setBackgroundResource(R.drawable.videogradient);
		
		ImageButton img_btn = new ImageButton(getActivity());
        img_btn.setImageResource(R.drawable.test_meeting);
        img_btn.setPadding(0, 0, 0, 0);	
		img_btn.setScaleType(ScaleType.CENTER);
		img_btn.setId(getNewPreviewID());
		img_btn.setBackgroundResource(R.drawable.videogradient);
		img_btn.setOnClickListener(this);
		
		//Image size in DP:
		int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
                (float) preview_height, getResources().getDisplayMetrics());
		int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
                (float) preview_width, getResources().getDisplayMetrics());
		
		LayoutParams overlayLayoutParams = new LayoutParams(previewLayoutParams);
		
		//Overlay:
		ImageView overlay = new ImageView(getActivity());
		overlay.setScaleX(0.5f);
		overlay.setScaleY(0.5f);
		overlay.setImageResource(R.drawable.play_new);
		overlay.setId(PreviewID);
		overlay.setOnClickListener(this);
		
		overlayLayoutParams.width = width;
		overlayLayoutParams.height = height;
	
		previewLayout.addView(img_btn, previewLayoutParams);
		previewLayout.addView(overlay, overlayLayoutParams);
		video_content_layout.addView(previewLayout, 0);
				
		WrapperJSON.RefreshImage(PreviewID, meeting_data.video.screenshot_url);

    }
    
    void generateVideoContent(){
    	
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

	@Override
	public void YearsAvailable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DataAvailable(String year) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ImageAvailable(final int id) {
		
		Log.i("FragmentVideo:ImageAvailable", "id=" + id);
		
		if(id != PreviewID) return;
		
		//Run UI updates in external thread:
		getActivity().runOnUiThread(new Runnable(){
			
			 public void run() {
				
				 //Error checking for view already deleted
				 
				try {
					
					((ImageView)rootView_.findViewById(PreviewID)).setImageBitmap(WrapperJSON.GetImage(id));
					
				} catch (NullPointerException e) {
					
					Log.w("FragmentVideo:ImageAvailable", "Warning: view not found.");
					
				}
			 }
		});
	}
}
