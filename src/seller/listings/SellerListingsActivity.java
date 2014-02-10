package seller.listings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.Button;
import buyer.listings.AcceptedJobFragment;
import buyer.listings.ListingsActivity;
import buyer.listings.PendingJobFragment;

import com.cs160.customjob.R;
import com.cs160.customjob.R.id;
import com.cs160.customjob.R.layout;
import com.parse.ParseUser;

public class SellerListingsActivity extends FragmentActivity {

	Button backButton;
	FragmentTabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seller_listings);
		backButton = (Button) findViewById(R.id.backButtonListingsScreen);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabcontent);

        Bundle b = new Bundle();
        b.putString("key", "Listings");
        mTabHost.addTab(mTabHost.newTabSpec("listings")
        				.setIndicator("Requests"), SellerListingsFragment.class, b);
        
        b = new Bundle();
        b.putString("key", "My Bids");
        mTabHost.addTab(mTabHost.newTabSpec("mybids")
                        .setIndicator("My Bids"), SellerMyBidsFragment.class, b);
        
        b = new Bundle();
        b.putString("key", "My Jobs");
        mTabHost.addTab(mTabHost.newTabSpec("myjobs")
                        .setIndicator("My Jobs"), SellerMyOrdersFragment.class, b);
        
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
	}

}
