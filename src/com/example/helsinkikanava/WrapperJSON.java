package com.example.helsinkikanava;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection; 
import java.net.MalformedURLException;
import java.net.URL;

import HelsinkiKanavaDataAccess.HelsinkiKanavaDataAccess;
import HelsinkiKanavaDataAccess.Metadata;
import HelsinkiKanavaDataAccess.Attendance;
import android.util.Log;


//Wrapper class for getting JSON values conveniently
public class WrapperJSON {
	
	// Listeners to inform when the data has been updated.
	private static ArrayList<IJsonListener> dataListeners;

	// Separate thread class for server communication.
	private static Model myModel;
	
	// Class variable that does the actual communication with the server.
    private static HelsinkiKanavaDataAccess myHelsinkiKanavaDataAccess;
    
    // Map to hold the all the sessions. Key = url, value = title.
    private static HashMap<String, String> mySessions;
    
    // Holds the available years
    private static ArrayList<String> yearsAvailable;
    
    // Hashmap for the metadata information. Key = year.
    private static HashMap<String, ArrayList<Metadata>> metadatas;

    /*******************************************************
     * Getters
     ******************************************************/
    public static ArrayList<Metadata> GetYearData(String year)
    {
    	return metadatas.get(year);
    }
    
    public static ArrayList<String> GetYears()
    {
    	return  yearsAvailable;
    }

    // PRECONDITION
    // The year being queried has to be earlier fetched
    // before this method can return parties of given sessionUrl
    public static TreeSet<String> GetParties(String year, String paSessionUrl)
    {
        if(metadatas == null || !metadatas.containsKey(year)) return null;

        ArrayList<Metadata> yearsMetadatas = metadatas.get(year);

        //Prevents adding multiple intances and keeps the order of the elements
        TreeSet<String> parties = new TreeSet<String>();

        for(Metadata metadata : yearsMetadatas)
        {
            if(metadata.session_url == paSessionUrl)
            {
                if(metadata.attendance == null) return null;

                for(Attendance attendance : metadata.attendance)
                {
                    parties.add(attendance.party);
                }
                return parties;
            }
        }
        return null;
    }

    /*******************************************************
     * Refreshes the available years.
     ******************************************************/
	static public boolean RefreshYears()
	{
		if (dataListeners == null)
		{
			return false;
		}
		else
		{
			if (yearsAvailable == null)
			{
				myModel = new Model(null);
				myModel.start();
			}
			else // Data already available, just notify listeners
			{
				for (IJsonListener listener : dataListeners)
				{
					listener.YearsAvailable();
				}
			}
			
			return true;	
		}
	}

	/*******************************************************
     * Refreshes the data of certain years.
     ******************************************************/
	public static boolean RefreshData(String year)
	{
		if (dataListeners == null || metadatas == null)
		{
			return false;
		}
		else
		{
			if (!metadatas.containsKey(year))
	    	{
				myModel = new Model(year);
				myModel.start();
	    	}
			else // Data already available, just notify listeners
			{
				for (IJsonListener listener : dataListeners)
				{
					listener.DataAvailable(year);
				}
			}
						
			return true;
		}
	}
	
	/*******************************************************
     * Registers a listener
     ******************************************************/
	public static void RegisterListener(IJsonListener listener)
	{
		if(dataListeners == null) dataListeners = new ArrayList<IJsonListener>();
		
		dataListeners.add(listener);
	}
	
	/*******************************************************
     * Unregisters a listener
     ******************************************************/
	public static void UnregisterListener(IJsonListener listener)
	{
		dataListeners.remove(listener);
	}
	
	
	/*******************************************************
	 * This class is used as a thread that communicates with the
	 * server. 
	 ******************************************************/
	private static class Model extends Thread
	{
		private String yearOrNot;
		
		/*******************************************************
	     * Constructor
	     ******************************************************/
	    public Model(String year)
	    {
	        myHelsinkiKanavaDataAccess = new HelsinkiKanavaDataAccess();
	        metadatas = new HashMap<String, ArrayList<Metadata>>();
	        yearOrNot = year;
	    }

	    /*******************************************************
	     * This method is run every time when some information is needed.
	     * Calls the proper method for getting years or year's data.
	     ******************************************************/
	    @Override
	    public void run()
	    {
	    	if (mySessions == null)
	    	{
	    		mySessions = myHelsinkiKanavaDataAccess.GetSessions();
	    		
	    		Log.i("sessiot: ", mySessions.toString());
	    	}
	    	
	    	// If year is given in the constructor, years data is asked
	    	if (yearOrNot != null && yearOrNot != "")
	    	{
	    		GetData();
	    		for (IJsonListener listener : dataListeners)
				{
					listener.DataAvailable(yearOrNot);
				}
	    	}
	    	else // If years is empty or null, list of the years are wanted
	    	{
	    		GetAvailableYears();
	    		for (IJsonListener listener : dataListeners)
				{
					listener.YearsAvailable();
				}
	    		
	    	}
	    }
	    
	    /*******************************************************
	     * Method for retrieving specific year's session data.
	     ******************************************************/
	    private void GetData()
	    {
	    	// Checks first which sessions belong to the given year
	        ArrayList<String> sessions = new ArrayList<String>();
	
	        for (HashMap.Entry<String, String> entry : mySessions.entrySet())
	        {
	        	Log.i("tatattadsgfsdgt", entry.getKey());
	        	
	            if(IsYear(yearOrNot, entry.getKey()))
	            {
	            	Log.i("tatatta", entry.getKey());
	                sessions.add(entry.getKey());
	            }
	        }
	
	        Log.i("sessiot: ", sessions.toString());
	        
	        ArrayList<Metadata> metadata = myHelsinkiKanavaDataAccess.GetMetadatasInArray(sessions);
	        Collections.sort(metadata);
	        
	        // Add to memory
	        metadatas.put(yearOrNot, metadata);
	    }

	    /*******************************************************
	     * Returns the distinct years that are available.
	     * Special search method parses the different years from the URL.
	     ******************************************************/
	    private void GetAvailableYears()
	    {
	        ArrayList<String> years = new ArrayList<String>();

	        for (HashMap.Entry<String, String> entry : mySessions.entrySet())
	        {
	            String year = WhatYear(entry.getKey());
	            if(year == null || years.contains(year)) continue;

	            years.add(year);
	        }
	        
	        Collections.sort(years);
	        
	        yearsAvailable = years;
	    }

	    /*******************************************************
	     * Checks if a year of an URL matches to the given year
	     ******************************************************/
	    private boolean IsYear(String year, String url)
	    {	
	    	boolean check = false;
	    	
	    	String whatYear = WhatYear(url);
	    		    	
	    	if (whatYear != null)
	    	{
	    		 check = whatYear.equals(year);
	    	}    	
	    	
	        return check;
	    }

	    /*******************************************************
	     * Checks from the year from the URL string
	     ******************************************************/
	    private String WhatYear(String url)
	    {
	        Pattern pattern = Pattern.compile("\\d+\\-\\d+\\.(\\d\\d\\d\\d)");
	        Matcher matcher = pattern.matcher(url);

	        if (matcher.find())
	        {	        	
	            return matcher.group(1);
	        }
	        return null;
	    }
	}
}
