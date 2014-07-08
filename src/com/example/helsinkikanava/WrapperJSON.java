package com.example.helsinkikanava;

import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;

//Wrapper class for getting JSON values conveniently
public class WrapperJSON {
	
	final static String URL_helsinki_kanava = "http://www.helsinkikanava.fi/@@opendata-index-v2.0";
	final static int timeout = 2000; //ms?
	
	//Retrieves version info just for example. No real use.
	static public String getVersion(){
		
		JsonParserFactory factory=JsonParserFactory.getInstance();
		JSONParser parser=factory.newJsonParser();
		@SuppressWarnings("rawtypes")
		Map jsonData=parser.parseJson(getJSON(URL_helsinki_kanava, timeout));
		 
		 
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
		return ((String)jsonData.get("version"));

	}
	
	public static String getJSON(String url, int timeout) {
	    try {
	        URL u = new URL(url);
	        HttpURLConnection c = (HttpURLConnection) u.openConnection();
	        c.setRequestMethod("GET");
	        c.setRequestProperty("Content-length", "0");
	        c.setUseCaches(false);
	        c.setAllowUserInteraction(false);
	        c.setConnectTimeout(timeout);
	        c.setReadTimeout(timeout);
	        c.connect();
	        int status = c.getResponseCode();

	        switch (status) {
	            case 200:
	            case 201:
	                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
	                StringBuilder sb = new StringBuilder();
	                String line;
	                while ((line = br.readLine()) != null) {
	                    sb.append(line+"\n");
	                }
	                br.close();
	                return sb.toString();
	        }

	    } catch (MalformedURLException ex) {
	        //Logger.getLogger(DebugServer.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (IOException ex) {
	        //Logger.getLogger(DebugServer.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    return null;
	}
}
