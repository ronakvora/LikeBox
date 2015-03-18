package com.xobekil;

import java.util.ArrayList;
import java.util.Collections;

import com.parse.ParseUser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ArrayAdapterMyLikes extends ArrayAdapter<String> {
    
    private ArrayList<String> myLikes;
    Context context;
    private TabbedActivity tabbedActivity;
    public ArrayAdapterMyLikes(Context context, ArrayList<String> objects, 
    		TabbedActivity tabbedActivity) {
        
        super(context, R.layout.my_likes_custom_row, objects); // change this layout to the correct custom
        myLikes = (ArrayList<String>) objects; 
        this.context = context; 
        this.tabbedActivity = tabbedActivity;
        
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.my_likes_custom_row, parent, false);
        }
        String mLike = (String)(myLikes.get(position));
        if (mLike != null) {
            TextView textView  = (TextView) convertView.findViewById(R.id.my_likes_text); 
            if (textView != null) {          
                textView.setText(mLike); 
            }
        }
        
        final ImageButton button = (ImageButton) convertView.findViewById(R.id.delete_button);
        button.setOnClickListener(
            new OnClickListener() {
                @Override
                public void onClick(View v) {
                	String s = myLikes.get(position);
                	ParseUser user = ParseUser.getCurrentUser();
                	user.getList("myLikes").remove(s);
                	user.saveInBackground();
                    myLikes.remove(position);
                    notifyDataSetChanged();
                    String likeTag = tabbedActivity.getLikeTag();
                    LikeActivity likeActivity = 
                    		(LikeActivity) tabbedActivity.getSupportFragmentManager().findFragmentByTag(likeTag);
                    likeActivity.addLike(s); 
                    Toast.makeText(getContext(), "Contact deleted from your likes!", Toast.LENGTH_SHORT).show();
                }
                
            }
        );
        
        button.setOnTouchListener(
        		new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (button.isSelected()) {
							button.setImageResource(R.drawable.deleteselected);
						}
						return false;
					}
        		}
        );		
        
        
        return convertView;
     }
    
    public void addLike(String s) {
    	myLikes.add(s); 
    	Collections.sort(myLikes);
    	notifyDataSetChanged(); 
    	
    }
}

