package com.example.helsinkikanava;

/**
 * Created by vesa on 12.7.2014.
 */
public interface IJsonListener
{
	public void YearsAvailable();
    public void DataAvailable(String year);
    public void ImageAvailable(String url);
}
