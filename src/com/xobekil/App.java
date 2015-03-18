package com.xobekil;
import android.app.Application;   
import com.parse.Parse; 

public class App extends Application {
  
    public static final String notRealKey =
          "asdf8s0d9f8sd09f8asf098asd098cd09sda8098fas0d98fasd09fasf09as8df09a8"

    @Override public void onCreate() { 
        super.onCreate();
        Parse.initialize(this, notRealKey);
        // Your Application ID and Client Key are defined elsewhere 
    }
} 