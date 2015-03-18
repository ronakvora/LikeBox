package com.xobekil;

import android.os.Bundle;   
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import com.parse.ParseAnalytics;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		ParseAnalytics.trackAppOpened(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}
	
	public void toTabbedActivity(View view) {
		Intent i = new Intent(this, TabbedActivity.class);     
	    startActivity(i);
	 }

}
