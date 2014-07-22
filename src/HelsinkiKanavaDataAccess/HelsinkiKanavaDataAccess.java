package HelsinkiKanavaDataAccess;

import HelsinkiKanavaDataClasses.Index;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by erno on 11/07/14.
 * 
 */
public class HelsinkiKanavaDataAccess
{
	// The URL from which to retrieve the sessions and data.
    private final String mySESSIONS_URL = "http://www.helsinkikanava.fi/@@opendata-index-v2.0";
    
    // Mapper to map the JSON objects
    private ObjectMapper mapper;

    /*******************************************************
     * Constructor.
     ******************************************************/
    public HelsinkiKanavaDataAccess()
    {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /*******************************************************
     * Gets a single metadata patch from the server.
     ******************************************************/
    public Metadata GetMetadata(String paUrl)
    {    	
        try
        {
            if(paUrl == null || !IsDataAvailable(paUrl)) return null;
            
            return mapper.readValue(new URL(paUrl), Metadata.class);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /*******************************************************
     * Checks if there is any data available.
     ******************************************************/
    private static boolean IsDataAvailable(String paUrl)
    {
        try
        {
            URL url = new URL(paUrl);

            HttpURLConnection httpURLConnection =  (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("HEAD");
            
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                return true;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    /*******************************************************
     * Returns all the metadata of a session in a map
     ******************************************************/
    public HashMap<String, Metadata> GetMetadatas(List<String> paUrls)
    {
        HashMap<String, Metadata> metadatas = new HashMap<String, Metadata>();

        for (String url : paUrls)
        {
            Metadata metadata = GetMetadata(url);

            if (metadata == null) continue;

            metadatas.put(url, metadata);
        }

        return metadatas;
    }

    /*******************************************************
     * Returns all the metadata in an array.
     ******************************************************/
    public ArrayList<Metadata> GetMetadatasInArray(List<String> paUrls)
    {
        ArrayList<Metadata> metadatas = new ArrayList<Metadata>();

        for (String url : paUrls)
        {
            Metadata metadata = GetMetadata(url);
            
            if (metadata == null) continue;
            
            metadatas.add(metadata);
        }

        return metadatas;
    }

    /*******************************************************
     * Gets all the sessions in a map.
     ******************************************************/
    public HashMap<String, String> GetSessions()
    {
        HashMap<String, String> sessions = new HashMap<String, String>();

        try
        {
            Index index = mapper.readValue(new URL(mySESSIONS_URL), Index.class);
            
            for (Session session : index.sessions)
            {
                sessions.put(session.url, session.title);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return sessions;
    }

    /*******************************************************
     * Gets all the sessions in an array.
     ******************************************************/
    public ArrayList<String> GetSessionsInArray()
    {
        ArrayList<String> sessions = new ArrayList<String>();

        try
        {
            Index index = mapper.readValue(new URL(mySESSIONS_URL), Index.class);

            for (Session session : index.sessions)
            {
                sessions.add(session.url);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return sessions;
    }
}