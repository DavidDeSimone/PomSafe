package com.hack.firebase.network;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.hacks.main.Main;

public class FirebaseAuth {

	private boolean auth = false;
	
	public FirebaseAuth(Main main, String userName, String passWord) {
	    Firebase myRef = new Firebase("https://pomsafe.firebaseio.com");
	    
	    
	    final Main mainPtr = main;
	    myRef.authWithPassword(userName, passWord,
	    new Firebase.AuthResultHandler() {
	    @Override
	    public void onAuthenticated(AuthData authData) {
	    // Authentication just completed successfully
	    	mainPtr.logIn(authData.getUid());
	    	auth = true;
	    }
	    @Override
	    public void onAuthenticationError(FirebaseError error) {
	    // Something went wrong :(
	    	auth = false;
	    }
	    });
	}
	
	public boolean getAuthStatus() {
		return auth;
	}
	
	
}
