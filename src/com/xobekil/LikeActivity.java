package com.xobekil;
 
import java.util.ArrayList;   
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import com.parse.ParseException;
import com.actionbarsherlock.app.SherlockListFragment;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class LikeActivity extends SherlockListFragment {

	private ArrayList<String> invitableUsers = new ArrayList<String>();  
	private HashMap<String, ArrayList<String>> existingUserInfo = new HashMap<String,ArrayList<String>>(); 
	private HashMap<String, ArrayList<String>> existingUserInfo2 = new HashMap<String,ArrayList<String>>(); 
	private ArrayList<String> existingUsers = new ArrayList<String>(); 
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_like, container, false);
		getContacts();
		ArrayAdapterSubclass existingUsersAdapter = new ArrayAdapterSubclass(getActivity().getApplication(), 
        		existingUsers, (TabbedActivity)getActivity(), existingUserInfo, existingUserInfo2);
        setListAdapter(existingUsersAdapter);
        ((TabbedActivity)getActivity()).setLikeTag(getTag());
        return view;
    }
 
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
    
    public void getContacts() {
    	Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    	String[] projection = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
    			ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER,
    			ContactsContract.CommonDataKinds.Phone.NUMBER};

    	Cursor people = getActivity().getApplication().getContentResolver().query(uri, 
    			projection, null, null,null);

    	int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
    	int phoneNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
    	ParseUser user = ParseUser.getCurrentUser();
    	if (people.getCount() > 0) {
        	people.moveToFirst();
        	do {
        	    final String name   = people.getString(indexName);
        	    final String number = people.getString(phoneNumber);
        	    if (number != null && number.length() > 0) {
        	    	//here check if contact is in overall database, not in mutual likes, and
        	    	//not in my likes; only then add it to contacts arraylist.
        	    	//in future add functionality, like snapchat, to have a section where you can
        	    	//invite people not in db to join.
                	final List<Object> userLikes = user.getList("myLikes");
                	final List<Object> mutualLikes = user.getList("mutualLikes");
                	ParseQuery<ParseUser> query = ParseUser.getQuery();
                	query.whereEqualTo("phoneNumber", number);
                	if (getListAdapter() == null) {
	                	try {
							List<ParseUser> objects = query.find();
	                    	if (!userLikes.contains(name) && objects.size() > 0 && !mutualLikes.contains(name)
	                    			&& !existingUsers.contains(name)) {
	                    		existingUsers.add(name);
	                    		String username = objects.get(0).getUsername();
	                    		ArrayList<String> usernameDisplayName = new ArrayList<String>(); 
	                    		usernameDisplayName.add(username);
	                    		usernameDisplayName.add(name);
	                    		ArrayList<String> usernamePhoneNumber = new ArrayList<String>();
	                    		usernamePhoneNumber.add(username);
	                    		usernamePhoneNumber.add(number); 
	                    		existingUserInfo.put(number, usernameDisplayName);
	                    		existingUserInfo2.put(name, usernamePhoneNumber);
	                    	}
	                    	else if (objects.size() <= 0) {
	                    		invitableUsers.add(name);
	                    	}
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
                	}
                	else {
                		query.findInBackground(new FindCallback<ParseUser>() {
                      	  public void done(List<ParseUser> objects, ParseException e) {
                      	    if (e == null) {
                      	        // The query was successful.
                              	if (!userLikes.contains(name) && objects.size() > 0 && !mutualLikes.contains(name)
                              			&& !existingUsers.contains(name)) {
                              		existingUsers.add(name);
    	                    		String username = objects.get(0).getUsername();
    	                    		ArrayList<String> usernameDisplayName = new ArrayList<String>(); 
    	                    		usernameDisplayName.add(username);
    	                    		usernameDisplayName.add(name);
    	                    		ArrayList<String> usernamePhoneNumber = new ArrayList<String>();
    	                    		usernamePhoneNumber.add(username);
    	                    		usernamePhoneNumber.add(number); 
    	                    		existingUserInfo.put(number, usernameDisplayName);
    	                    		existingUserInfo2.put(name, usernamePhoneNumber);
                              	}
                              	else if (objects.size() <= 0) {
                              		invitableUsers.add(name);
                              	}

                      	    } else {
                      	        System.out.println(e.toString());
                      	    }
                      	  }
                      	});
                	}
                	

        	    	
        	    }
        	    
        	} while (people.moveToNext());
    	}
    	user.put("existingUserInfo", existingUserInfo); 
    	user.saveInBackground();
    	people.close(); 
    }

	public void addLike(String s) {
    	ArrayAdapterSubclass listAdapter = (ArrayAdapterSubclass) getListAdapter();
    	listAdapter.addLike(s);
		
	}
	
	
    
    

 
}