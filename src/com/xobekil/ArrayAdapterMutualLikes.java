package com.xobekil;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArrayAdapterMutualLikes extends ArrayAdapter<String> {
    
    private ArrayList<String> mutualLikes;
    Context context;
    public ArrayAdapterMutualLikes(Context context, ArrayList<String> objects) {
        
        super(context, R.layout.my_likes_custom_row, objects); // change this layout to the correct custom
        mutualLikes = (ArrayList<String>) objects; 
        this.context = context; 
        
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.mutual_likes_custom_row, parent, false);
        }
        String mLike = (String)(mutualLikes.get(position));
        if (mLike != null) {
            TextView textView  = (TextView) convertView.findViewById(R.id.mutual_likes_text); 
            if (textView != null) {          
                textView.setText(mLike); 
            }
        }
        
        return convertView;
     }
    
    public void addLike(String s) {
    	mutualLikes.add(s); 
    	Collections.sort(mutualLikes);
    	notifyDataSetChanged(); 
    	
    }
}

