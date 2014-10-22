package com.hack.views;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.hack.main.R;



public class ViewMode extends ActionBarActivity {

	/*
	 * ViewMode used to display reported Data from TI device.
	 * Information is transmitted from Firebase API
	 * 
	 * (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	private String userName;
	private boolean copFlip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Firebase.setAndroidContext(this);
		setContentView(R.layout.activity_view_mode);
		
		Intent intent = getIntent();
		String userName = intent.getStringExtra("USERNAME");
		
		//When we pull the slam variable from the server, it will trigger
		//the cop boolean, thus we do not allow the call from the 
		//first pull
		copFlip = false;
		
		//Create a refernce to the firebase server (does not start connection)
	    Firebase rootRef = new Firebase("https://pomsafe.firebaseio.com/");
		
	    //Wrapper functions for the even listers when the Firebase DB is changed
	    addRoomTempEventListener(rootRef);
	    addRoomSlamEventListener(rootRef);
		addDoorMovementEventListener(rootRef);
	    
	}
	
	public void addDoorMovementEventListener(Firebase rootRef) {
		
		
		final ViewMode th = this;
		rootRef.child("users/" + 'a' + "/room/open").addValueEventListener(new ValueEventListener() {
	        @Override
	        public void onDataChange(DataSnapshot snapshot) {
	        	if(snapshot != null && snapshot.getValue() != null) {
	        		th.doorLook(snapshot);
	        	}
	        }
	        @Override 
	        public void onCancelled(FirebaseError error) {
	        	
	        }
	        });
		
		
		
	}
	
	public void doorLook(DataSnapshot snapshot) {
		ImageView doorPic = (ImageView)findViewById(R.id.imageView1);
		
		if(snapshot.getValue() != null) {
		String str = snapshot.getValue().toString();
		System.out.println(str);
		
		int mark = Integer.valueOf(str);
		
		
		if(mark != 0) {
    		doorPic.setImageResource(R.drawable.door_open);
    	} else {
    		doorPic.setImageResource(R.drawable.door_closed);
    	}
    	
		}
	}
	
	public void addRoomTempEventListener(Firebase rootRef) {
		final TextView tempView = (TextView)findViewById(R.id.temp);
		
		
		final ViewMode modePtr = this;
	    rootRef.child("users/" + 'a' + "/room/temp").addValueEventListener(new ValueEventListener() {
	        @Override
	        public void onDataChange(DataSnapshot snapshot) {
	        //Get the integer representing the tempurate.
	        if(snapshot.getValue() != null) {
	        	modePtr.showTemp(snapshot.getValue().toString());
	        	//tempView.setText("Hello World");
	        }
	        
	        }
	        @Override 
	        public void onCancelled(FirebaseError error) {
	        	
	        }
	        });
	}
	
	

	
	public void showTemp(String temp) {
		TextView view = (TextView)findViewById(R.id.temp);
		view.setText((CharSequence)(temp + " C"));
	}
	
	public void addRoomSlamEventListener(Firebase rootRef) {
		final ViewMode vw = this;
		
		
		rootRef.child("users/" + 'a' + "/room/slam").addValueEventListener(new ValueEventListener() {
			public void onDataChange(DataSnapshot snapshot) {
		        //Slam da door bro, SLAM DA DOOR
		        
				if(snapshot.getValue() != null && copFlip) {
					
					String str = snapshot.getValue().toString();
					int mark = Integer.valueOf(str);
					
					if(mark != 0) {
						vw.setDialog();
						}
					}
					copFlip = true;
		        }
		        @Override 
		        public void onCancelled(FirebaseError error) { }
		        
				});
		
	}
	
	public void setDialog() {
		new AlertDialog.Builder(this)
	    .setTitle("Your door is open! Did you intend this?")
	    .setMessage("Would you like to call the police?")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	String url = "tel:611";
	            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
	            startActivity(intent);
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
