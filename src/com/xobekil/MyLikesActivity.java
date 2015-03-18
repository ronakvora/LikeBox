package com.xobekil;
 
import java.util.ArrayList;  
import java.util.Collections;
import java.util.List;

import com.actionbarsherlock.app.SherlockListFragment;
import com.parse.ParseUser;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyLikesActivity extends SherlockListFragment {

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_likes, container, false);
		ArrayList<String> myLikes = getMyLikes();
        ArrayAdapterMyLikes adapter = new ArrayAdapterMyLikes(getActivity().getApplication(), 
        		myLikes, (TabbedActivity)getActivity());
        setListAdapter(adapter);
        ((TabbedActivity)getActivity()).setMyLikesTag(getTag());
        return view;
    }
 
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
    
    public ArrayList<String> getMyLikes() {
    	ArrayList<String> myLikes = new ArrayList<String>();  
    	ParseUser user = ParseUser.getCurrentUser(); 
    	List<Object> userLikes = user.getList("myLikes");
    	for (int i = 0; i < userLikes.size(); i++) {
    		String s = (String) userLikes.get(i); 
    		myLikes.add(s); 
    	}
    	Collections.sort(myLikes); 
    	return myLikes; 
    	
    }
    
    public void addLike(String s) {
    	ArrayAdapterMyLikes listAdapter = (ArrayAdapterMyLikes) getListAdapter();
    	listAdapter.add(s);
    	
    }
    
    

 
}