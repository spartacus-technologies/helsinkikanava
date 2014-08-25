package com.example.helsinkikanava;

import android.util.Log;

public class StaticThreadTest {

	static ThreadTest test = null;
	
	static public void refresh(){
		
		test = new ThreadTest();
		
		test.start();
	}
	
	static class ThreadTest extends Thread{
		
		@Override
		public void run(){
			
		//Log.i("moi", "moi");
		}
		
		
	}
	
}
