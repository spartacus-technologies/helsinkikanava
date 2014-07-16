package com.example.helsinkikanava;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
import android.util.Log;


//Wrapper class for getting JSON values conveniently
public class WrapperJSON {
	
	// Listeners to inform when the data has been updated.
	private ArrayList<IJsonListener> dataListeners;
	
	// Separate thread class for server communication.
	private Model myModel;
	
	// Class variable that does the actual communication with the server.
    private HelsinkiKanavaDataAccess myHelsinkiKanavaDataAccess;
    
    // Map to hold the all the sessions.
    private Map<String, String> mySessions;
    
    // Holds the available years
    private ArrayList<String> yearsAvailable;
    
    // Hashmap for the metadata information.
    private HashMap<String, ArrayList<Metadata>> metadatas;
	
    /*******************************************************
     * Getters for data and years
     ******************************************************/
    public ArrayList<Metadata> GetYearData(String year)
    {
    	return metadatas.get(year);
    }
    
    public ArrayList<String> GetYears()
    {
    	return  yearsAvailable;
    }
    
    /*******************************************************
     * Refreshes the available years.
     ******************************************************/
	public boolean RefreshYears()
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
	public boolean RefreshData(String year)
	{
		if (dataListeners == null)
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
	public void RegisterListener(IJsonListener listener)
	{
		dataListeners.add(listener);
	}
	
	/*******************************************************
     * Unregisters a listener
     ******************************************************/
	public void UnregisterListener(IJsonListener listener)
	{
		dataListeners.remove(listener);
	}
	
	
	/*******************************************************
	 * This class is used as a thread that communicates with the
	 * server. 
	 ******************************************************/
	private class Model extends Thread
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
	     * Calls
	     ******************************************************/
	    @Override
	    public void run()
	    {
	    	if (mySessions == null)
	    	{
	    		mySessions = myHelsinkiKanavaDataAccess.GetSessions();
	    	}
	    	
	    	// If year is given in the constructor, years data is asked
	    	if (yearOrNot != null && yearOrNot != "")
	    	{
	    		GetData();
	    	}
	    	else // If years is empty or null, list of the years are wanted
	    	{
	    		GetAvailableYears();
	    	}
	    }
	    
	    /*******************************************************
	     * Method for retrieving specific year's session data.
	     ******************************************************/
	    private void GetData()
	    {
	    	// Checks first which sessions belong to the given year
	        ArrayList<String> sessions = new ArrayList<String>();
	
	        for (Map.Entry<String, String> entry : mySessions.entrySet())
	        {
	            if(IsYear(yearOrNot, entry.getKey()))
	            {
	                sessions.add(entry.getKey());
	            }
	        }
	
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

	        for (Map.Entry<String, String> entry : mySessions.entrySet())
	        {
	            String year = WhatYear(entry.getKey());
	            if(year == null || years.contains(year)) continue;

	            years.add(year);
	        }
	        yearsAvailable = years;
	    }

	    /*******************************************************
	     * Checks if a year of an URL matches to the given year
	     ******************************************************/
	    private boolean IsYear(String year, String url)
	    {
	        return WhatYear(url) == year;
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
