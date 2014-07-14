package HelsinkiKanavaDataAccess;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by erno on 11/07/14.
 */
public class HelsinkiKanavaDataAccess implements IHelsinkiKanavaDataAccess
{
    private final String mySESSIONS_URL = "http://www.helsinkikanava.fi/@@opendata-index-v2.0";
    private ObjectMapper mapper;

    public HelsinkiKanavaDataAccess()
    {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
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

    @Override
    public Map<String, Metadata> GetMetadatas(List<String> paUrls)
    {
        Map<String, Metadata> metadatas = new HashMap<String, Metadata>();

        for (String url : paUrls)
        {
            Metadata metadata = GetMetadata(url);

            if (metadata == null) continue;

            metadatas.put(url, metadata);
        }

        return metadatas;
    }

    @Override
    public Map<String, String> GetSessions()
    {
        Map<String, String> sessions = new HashMap<String, String>();

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
}

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

class Attendance
{
    public String name;
    public String party;
    public String seat;
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
    public String absent;
    public String against;
    public String empty;
    //public String for; //FIXME
}