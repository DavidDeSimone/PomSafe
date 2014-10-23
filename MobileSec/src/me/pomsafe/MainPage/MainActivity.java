package me.pomsafe.MainPage;

import me.pomsafe.Backend.FirebaseAuth;
import me.pomsafe.MenuPages.ViewMode;
import me.pomsafe.MobileSec.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.firebase.client.Firebase;


public class MainActivity extends ActionBarActivity {

	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        Firebase.setAndroidContext(this);
	        setContentView(R.layout.activity_main);
	        
	        final EditText userName = (EditText) findViewById(R.id.UserName);
	        final EditText passWord = (EditText) findViewById(R.id.Password);
	        
	        //Log in button
	        final MainActivity mainRef = this;
	        final Button button = (Button) findViewById(R.id.logInButton);
	        button.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View view) {
	        		String usr = userName.getText().toString();
	        		String pass = passWord.getText().toString();
	    
	        		showProgressBar();
	        		//hideLoginButton();
	        		
	        		new FirebaseAuth(mainRef, usr, pass);
	        		

	        		
	        	}
	        });
	        
	        
	        
	        
	    }
	    
	    public void showProgressBar() {
			ProgressBar progress = (ProgressBar)findViewById(R.id.progressBar1);
			progress.setVisibility(ProgressBar.VISIBLE);
		}
		
		public void hideProgressBar() {
			ProgressBar progress = (ProgressBar)findViewById(R.id.progressBar1);
			progress.setVisibility(ProgressBar.INVISIBLE);
		}
		
		public void hideLoginButton() {
			Button login = (Button)findViewById(R.id.logInButton);
			login.setVisibility(Button.INVISIBLE);
		}
		
		public void showLoginButton() {
			Button login = (Button)findViewById(R.id.logInButton);
			login.setVisibility(Button.VISIBLE);
		}
		
	    public void logIn(String usr) {
	    	//Go to next activity
			Intent intent = new Intent(this, ViewMode.class);
			intent.putExtra("USERNAME", usr);
			
			//Start the viewmode
			startActivity(intent);
	    	
	    }
}
