package com.ui.helsinkikanava;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import HelsinkiKanavaDataAccess.HelsinkiKanavaDataAccess;
import HelsinkiKanavaDataAccess.Metadata;
import HelsinkiKanavaDataAccess.Attendance;
import HelsinkiKanavaDataClasses.Issues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.util.Log;
import android.util.SparseArray;


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
    
    // Key = url, Value = Bitmap image
    private static SparseArray<Bitmap> sessionImages;

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
    
    public static Bitmap GetImage(int id)
    {
    	return sessionImages.get(id);
    }

    /*******************************************************
     * Gets different parties of a specific meeting / session
     * 
     * PRECONDITION
     * The year being queried has to be earlier fetched
     * before this method can return parties of given sessionTitle
     ******************************************************/
    public static TreeSet<String> GetParties(String paSessionTitle)
    {
        Metadata metadata = GetMetadata(paSessionTitle);

        if (metadata == null || metadata.attendance == null) return null;

        TreeSet<String> parties = new TreeSet<String>();

        for (Attendance attendance : metadata.attendance)
        {
            parties.add(attendance.party);
        }
        return parties;
    }

    /*******************************************************
     * Gets the participants of the given party who where present
     * at the given meeting.
     *
     * PRECONDITION
     * Metadatas have to be fetched.
     ******************************************************/
    public static TreeSet<String> GetParticipantsByParty (String paSessionTitle, String paParty)
    {
        Metadata metadata = GetMetadata(paSessionTitle);

        if(metadata == null || metadata.attendance == null) return null;

        TreeSet<String> participants = new TreeSet<String>();

        for (Attendance attendance : metadata.attendance)
        {
            if(attendance.party.equals(paParty)) participants.add(attendance.name);
        }
        return participants;
    }

    /*******************************************************
     * Gets the issue titles of a single session.
     *
     * PRECONDITION
     * Metadatas have to be fetched.
     ******************************************************/
    public static TreeSet<String> GetIssueTitles(String paSessionTitle)
    {
    	Metadata metadata = GetMetadata(paSessionTitle);
    	
    	if (metadata == null || metadata.issues == null) return null;
    	
    	TreeSet<String> issueTitles = new TreeSet<String>();
    	
    	for (Issues issue : metadata.issues)
    	{
    		issueTitles.add(issue.title);
    	}    	
    	return issueTitles;
    }
    
    /*******************************************************
     * Gets the resolutions of a single issue.
     *
     * PRECONDITION
     * Metadatas have to be fetched.
     ******************************************************/
    public static String GetResolutions(String paSessionTitle, String issueTitle)
    {
    	Metadata metadata = GetMetadata(paSessionTitle);
    	
    	if (metadata == null || metadata.issues == null) return null;
    	
    	for (Issues issue : metadata.issues)
    	{
    		if (issue.title.equals(issueTitle))
    		{
    			return issue.resolution;
    		}
    	}
    	return null;
    }
    
    /*******************************************************
     * Gets Metadata of a single session
     *
     * PRECONDITION
     * Metadatas have been fetched
     ******************************************************/
    private static Metadata GetMetadata(String paSessionTitle)
    {
        if(metadatas == null || metadatas.isEmpty()) return null;

        ArrayList<Metadata> yearsMetadatas = null;
        Iterator it = metadatas.entrySet().iterator();

        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();

            yearsMetadatas = (ArrayList<Metadata>) pair.getValue();

            for(Metadata metadata : yearsMetadatas)
            {
                if(paSessionTitle.equals(metadata.title)) return metadata;
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        return null;
    }

    /*******************************************************
     * Get single meeting metadata.
     * 
     * PRECONDITION
     * All years' Metadatas have been fetched!
     ******************************************************/
	public static Metadata RefreshMeetingData(String title)
	{
		for (ArrayList<Metadata> yearData : metadatas.values())
		{
			for (Metadata data : yearData)
			{
				if (data.title.equals(title))
				{
					return data;
				}
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
				myModel = new Model();
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
		
		myModel = new Model(year);
		myModel.start();
	    				
		return true;
	}
	
	/*******************************************************
     * Gets the image for a certain url/session.
     ******************************************************/
	public static boolean RefreshImage(int id, String url)
	{
		if (dataListeners == null)
		{
			return false;
		}
		
		if (sessionImages == null)
		{
			sessionImages = new SparseArray<Bitmap>();
		}
		
		myModel = new Model(id, url);
		myModel.start();
		
		return true;
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
		private String yearOrNot = null;
		int idOrNot = -1;
		private String urlOrNot = null;
		
		/*******************************************************
	     * Constructors
	     ******************************************************/
		public Model()
		{
			CheckDataAccessAndMetadatasInitialize();
		}
		public Model(String year)
		{
			CheckDataAccessAndMetadatasInitialize();
			
			yearOrNot = year;
		}
	    public Model(int id, String url)
	    {
	        CheckDataAccessAndMetadatasInitialize();
	        	        
	        idOrNot = id;
	        urlOrNot = url;
	    }

	    /*******************************************************
	     * Initialize the variables on the first time.
	     ******************************************************/
	    private void CheckDataAccessAndMetadatasInitialize()
	    {
	    	if (myHelsinkiKanavaDataAccess == null)
	    	{
	    		myHelsinkiKanavaDataAccess = new HelsinkiKanavaDataAccess();
	    	}
	    	
	    	if (metadatas == null)
	        {
	        	metadatas = new HashMap<String, ArrayList<Metadata>>();
	        }
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
	    	}
	    	
	    	// First check that url and image id were given. If they were, get image.
	    	// If year is given in the constructor, years data is asked.
	    	if (urlOrNot != null && urlOrNot != "" && idOrNot > -1)
	    	{
	    		if (GetImage())
	    		{
		    		for (IJsonListener listener : dataListeners)
					{
						listener.ImageAvailable(idOrNot);
					}
	    		}
	    		// TODO: in else-clause something for error handling if the image retrieval failed
	    	}
	    	else if (yearOrNot != null && yearOrNot != "")
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
	     * Method for retrieving image for a specific session.
	     ******************************************************/
	    private boolean GetImage()
	    {
	    	// If image already exists, just notify listeners
	    	if (sessionImages.get(idOrNot) != null)
	    	{
	    		return true;
	    	}
	    	
	    	final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
	        final HttpGet getRequest = new HttpGet(urlOrNot);
	    	
	        try 
	        {	        	        
	            HttpResponse response = client.execute(getRequest);
	            final int statusCode = response.getStatusLine().getStatusCode();
	            if (statusCode != HttpStatus.SC_OK) 
	            { 
	                Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + urlOrNot); 
	                return false;
	            }
	            
	            final HttpEntity entity = response.getEntity();
	            if (entity != null) 
	            {
	                InputStream inputStream = null;
	                try 
	                {
	                    inputStream = entity.getContent(); 
	                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
	                    
	                    sessionImages.append(idOrNot, bitmap);
	                    return true;
	                } 
	                finally 
	                {
	                    if (inputStream != null) 
	                    {
	                        inputStream.close();  
	                    }
	                    entity.consumeContent();
	                }
	            }	
	        }
            catch (Exception e) 
	        {
                // Could provide a more explicit error message for IOException or IllegalStateException
                getRequest.abort();
               //Log.i("ImageDownloader", "Error while retrieving bitmap from " + 
                //		urlOrNot + "Exception: " + e.toString());
            }
	        finally 
	        {
	            if (client != null) 
	            {
	                client.close();
	            }
	        }
	        return false;
	    }
	    
	    /*******************************************************
	     * Method for retrieving specific year's session data.
	     ******************************************************/
	    private void GetData()
	    {
	    	// If metadata has been fetched already, just return and notify listeners
	    	if (metadatas.containsKey(yearOrNot))
	    	{
	    		if (metadatas.get(yearOrNot) != null)
	    		{
	    			return;
	    		}
	    		else // if the fetch on previous time had failed and returned null metadata, remove from map and fetch again.
	    		{
	    			metadatas.remove(yearOrNot);
	    		}
	    	}
	    	
	    	// Checks first which sessions belong to the given year
	        ArrayList<String> sessions = new ArrayList<String>();
	
	        for (HashMap.Entry<String, String> entry : mySessions.entrySet())
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
	    	// If years have been fetched already, just return and notify listeners
	    	if (yearsAvailable != null && !yearsAvailable.isEmpty())
			{
	    		return;
			}
	    	
	        ArrayList<String> years = new ArrayList<String>();

	        for (HashMap.Entry<String, String> entry : mySessions.entrySet())
	        {
	            String year = WhatYear(entry.getKey());
	            if(year == null || years.contains(year)) continue;

	            years.add(year);
	        }
	        
	        Collections.sort(years, Collections.reverseOrder());
	        
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
