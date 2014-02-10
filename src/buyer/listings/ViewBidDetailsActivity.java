package buyer.listings;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cs160.customjob.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ViewBidDetailsActivity extends Activity {
	
	String objectId;
	TextView mAmount, mDetails, mBy, mPlacedOn;
	Button mAcceptBidButton;
	ParseObject customerRequest, winningBid;
	ParseUser winningBidder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_bid_details);
		objectId = getIntent().getExtras().getString(seller.listings.SellerListingsAdapter.OBJECT_ID);
		mAmount = (TextView) findViewById(R.id.ViewBidDetailsAmount);
		mDetails = (TextView) findViewById(R.id.ViewBidDetailsDetails);
		mBy = (TextView) findViewById(R.id.ViewBidDetailsUserName);
		mPlacedOn = (TextView) findViewById(R.id.ViewBidDetailsPlacedOn);
		mAcceptBidButton = (Button) findViewById(R.id.ViewBidDetailsAcceptBidButton);
		final ProgressDialog progressDialog = ProgressDialog.show(this, "Retreiving Data", "Please wait...");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Bid");
		query.include("parent");
		query.include("owner");
		query.getInBackground(objectId, new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					winningBid = object;
					Date date = object.getCreatedAt();
					DateFormat format = DateFormat.getDateTimeInstance();
					ParseUser user = object.getParseUser("owner");
					winningBidder = object.getParseUser("owner");
					customerRequest = object.getParseObject("parent");
					mAmount.setText("$" + object.getString("value"));
					mDetails.setText(object.getString("description"));					
					mPlacedOn.setText(format.format(date));
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
		mAcceptBidButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				AlertDialog confirmationDialog = new AlertDialog.Builder(ViewBidDetailsActivity.this)
				.setTitle("Confirm Bid?")
				.setMessage("Are you sure you want to accept this bid? This cannot be undone.")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						customerRequest.put("status", buyer.post.NewCJActivity.ACCEPTED);
						customerRequest.put("bidder", winningBidder);
						customerRequest.put("bid", winningBid);
						customerRequest.saveInBackground();
						Intent intent = new Intent(getApplicationContext(), ListingsActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.create();
				confirmationDialog.show();
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
