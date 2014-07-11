package com.example.helsinkikanava;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;



public class MainActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
			
			Log.i("MainActivity", "check_updates");
			AutoUpdater.checkForNewVersion(this);
			Toast toast1 = Toast.makeText(this, "Current build date: " + AutoUpdater.getCurrentVersion(this), Toast.LENGTH_LONG);
			Toast toast2 = Toast.makeText(this, "Server version build date: " + AutoUpdater.getNewVersion(), Toast.LENGTH_LONG);
			toast1.show();
			toast2.show();
			
			break;
			
		case R.id.action_settings:
			
			Log.i("MainActivity", "action_settings");
			break;
		default:
			break;
		}
        
        return super.onOptionsItemSelected(item);
    }

}
