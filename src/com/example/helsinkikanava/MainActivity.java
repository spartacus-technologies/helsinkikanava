package com.example.helsinkikanava;

import android.app.FragmentTransaction;
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

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements JsonListener
{

    private WrapperJSON wrapperJSON = new WrapperJSON();
    private ArrayList<Session> councilmeetings = new ArrayList<Session>();

    public MainActivity()
    {
//        wrapperJSON.RegisterListener(this);
        refreshData();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



        //Remove title bar
        this.requestWindowFeature(this.getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        //Show default fragment (for debugging)
        if (savedInstanceState == null)
        {
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

//    @Override
    public void onClick(View v) {

        Log.i("MainActivity", "Clicked.id: " + v.getId());

        // Static buttons:
        switch (v.getId())
        {
            //Refresh data
            case R.id.imageButton_refresh:

                Log.i("MainActivity", "REFRESH" );
                refreshData();

                break;

            //Council meetings tab
            case R.id.main_activity_tab_button_councilmeetings:
                Log.i("MainActivity", "CouncilMeetings tab" );
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

//                transaction.replace(R.id.container, new FragmentMeetings(this));
                transaction.replace(R.id.container, new FragmentDefault());
                transaction.addToBackStack(null);
                transaction.commit();

                break;
            //Scroll left:
            case R.id.main_activity_tabs_button_left:

                break;

            //Scroll right:
            case R.id.main_activity_tabs_button_right:

                break;

            default:
                break;

        }

    }

    private void refreshData ()
    {
        wrapperJSON.RefreshJson();
    }


    @Override
    public void NewDataAvailable()
    {
//        wrapperJSON.GetSessions();
    }
}
