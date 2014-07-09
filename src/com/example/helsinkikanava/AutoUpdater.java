package com.example.helsinkikanava;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;


public class AutoUpdater {

	
	final static String updateServer = "http://94.237.68.33:2323/get_version_info";
	
	//Performs check for new app version: true==new version
	public static boolean checkForNewVersion(Context caller){
		
		
		//Log.i("test", "test1");
		
		/**
		//Current version:
		int current_version = -1;
		try {
			current_version = caller.getPackageManager()
				    .getPackageInfo(caller.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			current_version = caller.getPackageManager()
				    .getPackageInfo(caller.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		
		/**
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
		Log.i("current_version", "" + build_date);
		Log.i("server_version", "" + server_version);
		
		if(current_version == server_version){
			Log.i("updatestatus", "up to date");
			return false;
		}
		else if (server_version > current_version){
			Log.i("updatestatus", "update needed");
			return true;
		}
		*/
		return true;
	}
	
	
	
	public static String getCurrentVersion(Context caller){
		
		//Build date:
		String build_date = null;
		try{
		     ApplicationInfo ai = caller.getPackageManager().getApplicationInfo(caller.getPackageName(), 0);
		     ZipFile zf = new ZipFile(ai.sourceDir);
		     ZipEntry ze = zf.getEntry("classes.dex");
			 long time = ze.getTime();
			 build_date = SimpleDateFormat.getInstance().format(new java.util.Date(time));
			 zf.close();
			 
			 
		}catch(Exception e){
			  
		}
		
		return build_date;
	}
	
	static String getNewVersion(){
		
		WrapperJSON wrapperJson = new WrapperJSON();
		
		String json = wrapperJson.RefreshJson();
		
		if(json == null || json == ""){
			
			return "Error! Server down?";
		}
		
		//Log.i("test", "test3"); 
		JsonParserFactory factory=JsonParserFactory.getInstance();
		JSONParser parser=factory.newJsonParser();
		
		@SuppressWarnings("rawtypes")
		Map jsonData=parser.parseJson(json);
		
		//Log.i("testasd", jsonData.toString());
		//Log.i("testdsa", jsonData.get("version").toString());
		
		return jsonData.get("build_date").toString();
	}
	
}
