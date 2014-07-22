package HelsinkiKanavaDataAccess;

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
    	Log.i("urlia: ", paUrl);
    	
        try
        {
            if(paUrl == null || !IsDataAvailable(paUrl)) return null;

            Log.i("Tanne paastiin: ", "sdfgdfh");
            
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

            Log.i("Lokia2: ", Integer.toString(httpURLConnection.getResponseCode()));
            
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
                Log.i("Yksi sessio: ", session.url);
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

/*******************************************************
 * Class containers for all the different information from the server.
 ******************************************************/
class Index
{
    public String version;
    public List<Session> sessions;
}

class Video
{
    public String mimetype;
    public String height;
    public String width;
    public String http_url;
    public String screenshot_url;
    public Rtmp rtmp;
}

class Rtmp
{
    public String netconnection_url;
    public String video_id;
}

class Issues
{
    public String id;
    public String resolution;
    public List<Statement> statements;
    public String timestamp;
    public String title;
    public String video_position;
    public List<Votings> votings;
    public String proposal;
}

class Statement
{
    public String allocated_time;
    public Participant participant;
    public String duration;
    public String finished;
    public String started;
    public String video_position;
}

class Participant
{
    public String name;
    public String party;
    public String seat;
}

class Votings
{
    public List<Votes> votes;
    public String against_title;
    public String finished;
    public String for_title;
    public Result results;
    public String started;
    public String type;
    public String against_text;
    public String type_text;
    public String for_text;
}

class Votes
{
    public String vote;
    public String voter;
}

class Result
{
    public String absent; // Poissa
    public String against; // Ei
    public String empty; // Tyhj�
    public String behalf; // Jaa
}