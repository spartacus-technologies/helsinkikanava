package com.ui.helsinkikanava;

import com.ui.helsinkikanava.R;

import android.app.*;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

public class ActivityPopupSettings extends Activity {

	static Dialog dialog= null;
	static View popUpLayout = null;
	Scroller scroller = new Scroller();
	
	public ActivityPopupSettings(){
		
	}
	
	public void showDialog(Activity caller){
		 
		dialog = new Dialog(caller); 
		
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    
	    ViewGroup layout = (ViewGroup)caller.findViewById(R.layout.popup_settings);
	    //layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	     
	    popUpLayout = LayoutInflater.from(caller).inflate(R.layout.popup_settings, layout);
	    dialog.setContentView(popUpLayout);
	    dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	    dialog.show();
		
	}
	
	class Scroller{		//For scrolling year list
		
		final Handler mHandler = new Handler();
		private int mInterval = 100;
		
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
		
		public Runnable runner = new Runnable() {
			
		    @Override 
		    public void run() {
		    	 
		    //Log.i("Scroller", "running...");
		    	
		    	mHandler.postDelayed(this, mInterval);
		    	
		    	SeekBar bar = ((SeekBar)popUpLayout.findViewById(R.id.popup_checkupdates_progress));
		    	bar.incrementProgressBy(direction_);

				if(bar.getProgress() == bar.getMax()){
					((TextView)popUpLayout.findViewById(R.id.popup_Textview_trole)).setVisibility(View.VISIBLE);
					Log.w("Scroller", "quitting...");
					mHandler.removeCallbacks(this);
					
					//finish();
				}
		    }
		};	
	}
}

