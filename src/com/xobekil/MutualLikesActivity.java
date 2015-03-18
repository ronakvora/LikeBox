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
 
public class MutualLikesActivity extends SherlockListFragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mutual_likes, container, false);
		ArrayList<String> mutualLikes = getMutualLikes();
        ArrayAdapterMutualLikes adapter = new ArrayAdapterMutualLikes(getActivity().getApplication(), 
        		mutualLikes);
        setListAdapter(adapter);
        ((TabbedActivity)getActivity()).setMutualLikesTag(getTag());
        return view;
    }
 
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
    
    public ArrayList<String> getMutualLikes() {
    	ArrayList<String> mutualLikes = new ArrayList<String>();  
    	ParseUser user = ParseUser.getCurrentUser(); 
    	List<Object> userLikes = user.getList("mutualLikes");
    	for (int i = 0; i < userLikes.size(); i++) {
    		String s = (String) userLikes.get(i); 
    		mutualLikes.add(s); 
    	}
    	Collections.sort(mutualLikes); 
    	return mutualLikes; 
    	
    }
    
    public void addLike(String s) {
    	ArrayAdapterMutualLikes listAdapter = (ArrayAdapterMutualLikes) getListAdapter();
    	listAdapter.addLike(s);
    	
    }
    
    

 
}