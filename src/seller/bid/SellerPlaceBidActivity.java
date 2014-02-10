package seller.bid;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cs160.customjob.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class SellerPlaceBidActivity extends Activity {

	String objectId;
	EditText mValue, mDescription;
	Button mPlaceBidButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seller_place_bid);
		objectId = getIntent().getExtras().getString(seller.listings.SellerListingsAdapter.OBJECT_ID);
		mValue = (EditText) findViewById(R.id.sellerPlaceBidValue);
		mDescription = (EditText) findViewById(R.id.sellerPlaceBidDescription);
		mPlaceBidButton = (Button) findViewById(R.id.sellerPlaceBidPlaceBidButton);
		mPlaceBidButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String valueString = mValue.getText().toString();
				if (valueString == ""){
					Toast.makeText(getApplicationContext(), "Please enter a valid value", Toast.LENGTH_SHORT).show();
					return;
				}
				double valueDouble = Double.parseDouble(valueString);
				DecimalFormat df = new DecimalFormat("#.##");
		        String value = df.format(valueDouble);
				String description = mDescription.getText().toString();
				ParseObject bid = new ParseObject("Bid");
				bid.put("value", value);
				bid.put("description", description);
				bid.put("parent", ParseObject.createWithoutData("CustomerRequest", objectId));
				bid.put("owner", ParseUser.getCurrentUser());
				bid.saveInBackground();
				ParseQuery<ParseObject> query = ParseQuery.getQuery("CustomerRequest");
				query.getInBackground(objectId, new GetCallback<ParseObject>() {
					public void done(ParseObject object, ParseException e) {
						if (e == null) {
							object.increment("bids");
							object.saveInBackground();
						} else {
							Toast.makeText(getApplicationContext(), "Could not add bid", Toast.LENGTH_SHORT).show();
						}
					}
				});
				finish();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.seller_place_bid, menu);
		return true;
	}

}
