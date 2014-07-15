package com.example.helsinkikanava;

import HelsinkiKanavaDataAccess.HelsinkiKanavaDataAccess;
import HelsinkiKanavaDataAccess.IHelsinkiKanavaDataAccess;
import HelsinkiKanavaDataAccess.Metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by erno on 15/07/14.
 */
public class Model implements IModel
{
    private IHelsinkiKanavaDataAccess myHelsinkiKanavaDataAccess;
    private Map<String, String> mySessions;
    private Map<String/*year*/, ArrayList<Metadata>> metadatas;

    public Model()
    {
        myHelsinkiKanavaDataAccess = new HelsinkiKanavaDataAccess();
        mySessions = myHelsinkiKanavaDataAccess.GetSessions();
    }

    @Override
    public ArrayList<Metadata> GetData(String year)
    {
        //TODO Tarkasta onko jo olemassa

        ArrayList<String> sessions = new ArrayList<String>();

        for (Map.Entry<String, String> entry : mySessions.entrySet())
        {
            if(IsYear(year, entry.getKey()))
            {
                sessions.add(entry.getKey());
            }
        }

        ArrayList<Metadata> metadatas = myHelsinkiKanavaDataAccess.GetMetadatasInArray(sessions);
        Collections.sort(metadatas);

        //TODO lisää talteen
        return metadatas;
    }

    @Override
    public ArrayList<String> GetAvailableYears()
    {
        ArrayList<String> years = new ArrayList<String>();

        for (Map.Entry<String, String> entry : mySessions.entrySet())
        {
            String year = WhatYear(entry.getKey());
            if(year == null || years.contains(year)) continue;

            years.add(year);
        }
        return years;
    }

    private boolean IsYear(String year, String url)
    {
        return WhatYear(url) == year;
    }

    private String WhatYear(String url)
    {
        Pattern pattern = Pattern.compile("\\d+\\-\\d+\\.(\\d\\d\\d\\d)");
        Matcher matcher = pattern.matcher(url);

        String year = null;
        if (matcher.find( ))
        {
            return matcher.group(1);
        }
        return null;
    }
}
