package buyer.listings;

import java.util.List;

import seller.listings.SellerListingsAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs160.customjob.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ViewBidsActivity extends Activity {
	
	String objectId;
	ListView mBidsListView;
	TextView mNoBidsTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_bids);
		mBidsListView = (ListView) findViewById(R.id.ViewBidsListView);
		mNoBidsTextView = (TextView) findViewById(R.id.ViewBidsNoBids);
		objectId = getIntent().getExtras().getString(seller.listings.SellerListingsAdapter.OBJECT_ID);
		final ProgressDialog progressDialog = ProgressDialog.show(this, "Retreiving Data", "Please wait...");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Bid");
		query.whereEqualTo("parent", ParseObject.createWithoutData("CustomerRequest", objectId));
		query.include("owner");
		query.orderByDescending("value");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> requestList, ParseException e) {
		        if (e == null) {
		        	if (requestList.size() > 0) {
		        		ViewBidsAdapter adapter = new ViewBidsAdapter(getApplicationContext(), R.layout.adapter_view_bids_item, requestList);
		        		mBidsListView.setAdapter(adapter);
		        	}
		        	else {
		        		mBidsListView.setEmptyView(mNoBidsTextView);
		        	}	       	
		        } else {
		            Toast.makeText(getApplicationContext(), "Failed to retrieve data\n\nPlease try again later", Toast.LENGTH_LONG).show();
		        }
		        progressDialog.dismiss();
		    }
		});
	}
	
	@Override
	protected void onResume() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onResume();
		this.onCreate(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_bids, menu);
		return true;
	}

}
