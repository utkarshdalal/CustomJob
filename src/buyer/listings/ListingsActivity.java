package buyer.listings;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.cs160.customjob.R;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ListingsActivity extends FragmentActivity {
	
	Button newCJButton, backButton;
	FragmentTabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buyer_listings);
		newCJButton = (Button) findViewById(R.id.buttonNewCJ);
		backButton = (Button) findViewById(R.id.backButtonListingsScreen);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabcontent);

        Bundle b = new Bundle();
        b.putString("key", "Pending Jobs");
        mTabHost.addTab(mTabHost.newTabSpec("pendingjobs").setIndicator("Current Requests"),
                        PendingJobFragment.class, b);
        
        b = new Bundle();
        b.putString("key", "Accepted Jobs");
        mTabHost.addTab(mTabHost.newTabSpec("acceptedjobs")
                        .setIndicator("Accepted Requests"), AcceptedJobFragment.class, b);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ParseUser.logOut();
				
	            // Show login screen
	            Intent i = new Intent(getApplicationContext(), commons.LoginActivity.class);
	            startActivity(i);	
	            
	            finish();
			}
		});
		newCJButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent newCJIntent = new Intent(ListingsActivity.this, buyer.post.NewCJActivity.class);
				ListingsActivity.this.startActivity(newCJIntent);				
			}
		});
	}
}
