package main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import buyer.post.NewCJActivity;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;
import commons.chat.MessageService;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   
        // Initialize Parse
        Parse.initialize(this, "PVDqkI6a7HjV4ZZX3shEMHOkcNE3k3GLK8fvaTQz", "Qopy12A2elnaqs8ETjdsLWn1viyvx7k6bq5CjaEO"); 
        PushService.subscribe(getApplicationContext(), "UpdateChatMessages", MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        MessageService.start();
        
        //#################
        /*
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
        //#################
        
        
        ParseUser user = ParseUser.getCurrentUser();

        if (user != null) { // logged in
        	
            // Show buyer listings
        	if (user.getBoolean("user_type") == commons.RegisterActivity.BUYER)
        	{
        		Intent i = new Intent(this, buyer.listings.ListingsActivity.class);
                startActivity(i);
        	}
        	
        	else if (user.getBoolean("user_type") == commons.RegisterActivity.SELLER)
        	{
        		Intent i = new Intent(this, seller.listings.SellerListingsActivity.class);
                startActivity(i);
        	}
        }
        else { // not logged in
        	
            // Show login screen
            Intent i = new Intent(this, commons.LoginActivity.class);
            startActivity(i);	
        }
        
        
        //Intent i = new Intent(this, commons.chat.ChatActivity.class);
        //startActivity(i);
        
        // Close current activity
        this.finish();
    }
}
