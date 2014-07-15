package com.example.helsinkikanava;


import HelsinkiKanavaDataAccess.Metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erno on 15/07/14.
 */
public interface IModel
{
    public ArrayList<Metadata> GetData(String year);

    public ArrayList<String> GetAvailableYears();
}
