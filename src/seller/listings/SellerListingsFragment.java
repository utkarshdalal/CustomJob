package seller.listings;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs160.customjob.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SellerListingsFragment extends Fragment {

    private ListView mListViewListings = null;
    private TextView mTextViewNoListings = null;

    public SellerListingsFragment() {
            // TODO Auto-generated constructor stub

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);             
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                    Bundle savedInstanceState) {
            
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_seller_listings,
                            null);
     
            mListViewListings = (ListView)v.findViewById(R.id.listViewListings);
            mListViewListings.setClickable(true);
            mTextViewNoListings = (TextView)v.findViewById(R.id.textViewNoListings);
            
            retrieveData();
            
            return v;
    }
    
    private void retrieveData() {
		//
		// Retreive data from Parse
		//
        
		final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Retreiving Data", "Please wait...");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("CustomerRequest");
		query.whereEqualTo("status", buyer.post.NewCJActivity.PENDING);
		query.orderByDescending("createdAt");
		query.include("owner");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> requestList, ParseException e) {
		        if (e == null) {
		            
		        	if (requestList.size() > 0) {
		        		SellerListingsAdapter adapter = new SellerListingsAdapter(getActivity(), R.layout.adapter_seller_listings_item, requestList);
		        		mListViewListings.setAdapter(adapter);
		        	}
		        	else {
		        		mListViewListings.setEmptyView(mTextViewNoListings);
		        	}
		        	
		        	progressDialog.dismiss();
		        	
		        } else {
		            Toast.makeText(getActivity(), "Failed to retrieve data\n\nPlease try again later", Toast.LENGTH_LONG).show();
		        }
		    }
		});
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public void onResume() {
       super.onResume();
       
       retrieveData();
    }

}