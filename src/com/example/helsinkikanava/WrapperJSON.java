package com.example.helsinkikanava;

import java.util.ArrayList;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection; 
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;

//Wrapper class for getting JSON values conveniently
public class WrapperJSON {
	
	final static String URL_helsinki_kanava = "http://www.helsinkikanava.fi/@@opendata-index-v2.0";
	final static int timeout = 2000; //ms?
	
	public ArrayList<Session> sessions = null;
	
	/**
	 * Runs the refresh thread once to refresh the session data
	 */
	public String RefreshJson()
	{
		RefreshJsonData refresher = new RefreshJsonData();
		refresher.start();
		
		return "";
	}
	
	//Retrieves version info just for example. No real use.
	static public String getVersion(){
		
		//TODO exception handling
		/*
		//@SuppressWarnings("unchecked")
		//List<String> value=jsonData.get("sessions");
		//Map test = (Map) jsonData.get("sessions");
		@SuppressWarnings("rawtypes")
		Map test = (Map) jsonData.get("root");
		
		@SuppressWarnings("rawtypes")
		List test2 = (List) jsonData.get("sessions");
		Log.i("VALUE", ((Map)test2.get(0)).get("url").toString().replace("\\", ""));
		*/
		//return ((Map)test2.get(0)).get("version").toString().replace("\\", "");
		return "";

	}
	
		
	/**
	 * 
	 * Private class for implementing JSON query in a 
	 * separate thread.
	 * 
	 * @author Marko
	 *
	 */
	private class RefreshJsonData extends Thread
	{
		@Override
		public void run() {
			
			JsonParserFactory factory=JsonParserFactory.getInstance();
			JSONParser parser=factory.newJsonParser();
			@SuppressWarnings("rawtypes")
			Map jsonData = parser.parseJson(getJSON(URL_helsinki_kanava, timeout));
			
			String version = ((String)jsonData.get("version"));
			
			Log.i("getJSON", version);
		}
		
		private String getJSON(String url, int timeout) {
			
			Log.i("getJSON", "test1");
			
		    try {
		        URL u = new URL(url);
		        Log.i("getJSON", "test2");
		        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
		        Log.i("getJSON", "test3");
		        connection.setRequestMethod("GET");
		        connection.setRequestProperty("Content-length", "0");
		        connection.setUseCaches(false);
		        connection.setAllowUserInteraction(false);
		        connection.setConnectTimeout(timeout);
		        connection.setReadTimeout(timeout);
		        connection.connect();
		        Log.i("getJSON", "TTTTTTTTTTOOOOOOOOOOOODDDDDDDDDDDDDOOOOOOOOOOOOOOO");
		        int status = connection.getResponseCode();

		        switch (status) {
		            case 200:
		            	
		                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		                StringBuilder sb = new StringBuilder();
		                String line;
		                while ((line = br.readLine()) != null) {
		                    sb.append(line+"\n");
		                }
		                br.close();
		                Log.i("getJSON", br.toString());
		                return sb.toString();
		                
		            default:
		            	break;
		        }

		    } catch (MalformedURLException ex) {
		    	
		    	Log.w("getJSON:MalformedURLException", ex.toString());
		    	
		    } catch (IOException ex) {
		    	
		    	Log.w("getJSON:IOException", ex.toString());
		    }
		    return null;
		}
	}
}
