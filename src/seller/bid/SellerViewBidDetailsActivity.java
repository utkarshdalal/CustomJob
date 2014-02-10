package seller.bid;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.cs160.customjob.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class SellerViewBidDetailsActivity extends Activity {
	
	String objectId;
	TextView mAmount, mDetails, mBy, mPlacedOn, mOn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seller_view_bid_details);
		objectId = getIntent().getExtras().getString(seller.listings.SellerListingsAdapter.OBJECT_ID);
		mAmount = (TextView) findViewById(R.id.sellerViewBidDetailsAmount);
		mDetails = (TextView) findViewById(R.id.sellerViewBidDetailsDetails);
		mBy = (TextView) findViewById(R.id.sellerViewBidDetailsUserName);
		mPlacedOn = (TextView) findViewById(R.id.sellerViewBidDetailsPlacedOn);
		mOn = (TextView) findViewById(R.id.sellerViewBidDetailsOn);
		final ProgressDialog progressDialog = ProgressDialog.show(this, "Retreiving Data", "Please wait...");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Bid");
		query.include("parent");
		query.include("owner");
		query.getInBackground(objectId, new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					Date date = object.getCreatedAt();
					DateFormat format = DateFormat.getDateTimeInstance();
					ParseUser user = object.getParseUser("owner");
					ParseObject item = object.getParseObject("parent");
					mAmount.setText("$" + object.getString("value"));
					mDetails.setText(object.getString("description"));					
					mPlacedOn.setText(format.format(date));
					mOn.setText(item.getString("title"));
					if (user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
			    		mBy.setText("Me");
			    	}
			    	else{
			    		mBy.setText(user.getString("full_name"));
			    	}
			    } else {
			    	Toast.makeText(getApplicationContext(), "Failed to retrieve data\n\nPlease try again later", Toast.LENGTH_LONG).show();
			    	finish();
			    }
				progressDialog.dismiss();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.seller_view_bid_details, menu);
		return true;
	}

}
