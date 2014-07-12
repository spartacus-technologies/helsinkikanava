package com.example.helsinkikanava;

import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.Toast;



public class MainActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(this.getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        //Show default fragment (for debugging)
        if (savedInstanceState == null) {
        	
        	 getFragmentManager().beginTransaction().add(R.id.container, new FragmentDefault()).commit();
        }

    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override 
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
		
		case R.id.check_updates_menu_button:
			
			//Log.i("MainActivity", "check_updates");
					
			new PopupCheckUpdates().showDialog(this);
			//PopupCheckUpdates.showDialog(this);
			
			break;
			
		case R.id.action_settings:
			
			new ActivityPopupSettings().showDialog(this);
			//PopupCheckUpdates.showDialog(this);
			Log.i("MainActivity", "action_settings");
			break;
		default:
			break;
		}
        
        return super.onOptionsItemSelected(item);
    }

}
