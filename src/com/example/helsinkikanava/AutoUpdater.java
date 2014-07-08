package com.example.helsinkikanava;

import java.util.Map;

import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;


public class AutoUpdater {

	
	final static String updateServer = "http://94.237.68.33:2323";
	
	//Performs check for new app version: true==new version
	public static boolean checkForNewVersion(Context caller){
		
		
		//Log.i("test", "test1");
		//Current version:
		int current_version = -1;
		try {
			current_version = caller.getPackageManager()
				    .getPackageInfo(caller.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Log.i("test", "test2");
		
		String json = WrapperJSON.getJSON(updateServer, 3000);
		//Log.i("test", "test3"); 
		JsonParserFactory factory=JsonParserFactory.getInstance();
		JSONParser parser=factory.newJsonParser();
		
		@SuppressWarnings("rawtypes")
		Map jsonData=parser.parseJson(json);
		
		//Log.i("testasd", jsonData.toString());
		//Log.i("testdsa", jsonData.get("version").toString());
		
		int server_version = Integer.valueOf(jsonData.get("version").toString());
		//Log.i("test", "test4");
		Log.i("current_version", "" + current_version);
		Log.i("server_version", "" + server_version);
		
		if(current_version == server_version){
			Log.i("updatestatus", "up to date");
			return false;
		}
		else if (server_version > current_version){
			Log.i("updatestatus", "update needed");
			return true;
		}
		return true;
	}
	
}
