package com.xobekil;

import java.util.ArrayList;  
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ArrayAdapterSubclass extends ArrayAdapter<String> {
    
    private ArrayList<String> contacts;
    private Context context;
    private TabbedActivity tabbedActivity; 
    private final int USERNAME = 0;
    private final int DISPLAY_NAME = 1;
    HashMap<String,ArrayList<String>> existingUserInfoByNumber; 
    HashMap<String,ArrayList<String>> existingUserInfoByDisplayName; 
    public ArrayAdapterSubclass(Context context, ArrayList<String> objects, 
    		TabbedActivity tabbedActivity, HashMap<String,ArrayList<String>> existingUserInfo,
    		HashMap<String, ArrayList<String>> existingUserInfo2) {
        
        super(context, R.layout.contacts_custom_row, objects);
        this.contacts = (ArrayList<String>) objects; 
        this.context = context; 
        this.tabbedActivity = tabbedActivity; 
        this.existingUserInfoByNumber = existingUserInfo; 
        this.existingUserInfoByDisplayName = existingUserInfo2; 
        
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.contacts_custom_row, parent, false);
        }
        String mContact = (String)(contacts.get(position));
        if (mContact != null) {
            TextView textView  = (TextView) convertView.findViewById(R.id.checkedtext);
            if (textView != null) {  
            	textView.setText(mContact);
            }
        }
        final ImageButton button = (ImageButton) convertView.findViewById(R.id.add_button);
        button.setOnClickListener(
            new OnClickListener() {
                @Override
                public void onClick(View v) {
                	onClickHelper(position);
                }
                
            });
        
        button.setOnTouchListener(
        		new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (button.isSelected()) {
							button.setImageResource(R.drawable.addselected);
						}
						return false;
					}
        		}
        );		
        
        
        return convertView;
     }
    
    public void addLike(String s) {
    	contacts.add(s); 
    	Collections.sort(contacts);
    	notifyDataSetChanged(); 
    	
    }
    
    public void onClickHelper(int position) {
    	String addedUsername = existingUserInfoByDisplayName.get(contacts.get(position)).get(USERNAME);
    	final String s = contacts.get(position);
    	final ParseUser currentUser = ParseUser.getCurrentUser();
    	final String currentUserUsername = currentUser.getUsername();
    	currentUser.add("myLikes",s);
    	currentUser.add("myLikesUsernames", existingUserInfoByDisplayName.get(s).get(USERNAME));
    	currentUser.saveInBackground(); //probably should take this up
    	ParseQuery<ParseUser> query = ParseUser.getQuery();
    	query.whereEqualTo("username", addedUsername);
    	query.findInBackground(new FindCallback<ParseUser>() {
        	  public void done(List<ParseUser> objects, ParseException e) {
        	    if (e == null) {
        	    	if (objects.size() > 0) {
        	    		ParseUser addedUser = objects.get(0);
            	    	if (addedUser.getList("myLikesUsernames").
            	    			contains(currentUserUsername)) {
            	    		currentUser.add("mutualLikes", s);
            	    		HashMap<String,ArrayList<String>> addedUserInfo = (HashMap<String,ArrayList<String>>) addedUser.get("existingUserInfo");
              	    		String displayName = addedUserInfo.get(currentUser.getString("phoneNumber")).get(DISPLAY_NAME);
              	    		addedUser.add("mutualLikes",displayName); // need to find display name of current user in addedUsers info and then add that name to added users mutual likes
              	    		addedUser.add("myLikes",displayName);
              	    		addedUser.add("myLikesUsernames", currentUserUsername);
            	    		String mutualLikesTag = tabbedActivity.getMutualLikesTag();
                            MutualLikesActivity mutualLikesActivity = 
                            		(MutualLikesActivity) tabbedActivity.getSupportFragmentManager().findFragmentByTag(mutualLikesTag);
                            mutualLikesActivity.addLike(s); 
            	    		currentUser.saveInBackground();
            	    		addedUser.saveInBackground(); 
            	    	
            	    	}
        	    		
        	    	}
        	    } else {
        	        System.out.println(e.toString());
        	    }
        	  }
        	});

        contacts.remove(position);
        notifyDataSetChanged();
        String myLikesTag = tabbedActivity.getMyLikesTag();
        MyLikesActivity myLikesActivity = 
        		(MyLikesActivity) tabbedActivity.getSupportFragmentManager().findFragmentByTag(myLikesTag);
        myLikesActivity.addLike(s); 
        Toast.makeText(getContext(), "Contact added to your likes!", Toast.LENGTH_SHORT).show();
    }
}

