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
import com.cs160.customjob.R.id;
import com.cs160.customjob.R.layout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class SellerMyBidsFragment extends Fragment {

    private ListView mListViewMyBids;
    private TextView mTextViewNoMyBids;

    public SellerMyBidsFragment() {
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
            
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_seller_my_bids,
                            null);
     
            mListViewMyBids = (ListView)v.findViewById(R.id.sellerMyBidsFragmentMyBids);
            mTextViewNoMyBids = (TextView)v.findViewById(R.id.sellerMyBidsFragmentNoBids);
            
            retrieveData();
            
            return v;
    }
    
    private void retrieveData() {
		//
		// Retreive data from Parse
		//
        
		final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Retreiving Data", "Please wait...");
		ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("CustomerRequest");
		innerQuery.whereEqualTo("status", buyer.post.NewCJActivity.PENDING);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Bid");
		query.whereMatchesQuery("parent", innerQuery);
		query.whereEqualTo("owner", ParseUser.getCurrentUser());
		query.orderByDescending("createdAt");
		query.include("parent");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> requestList, ParseException e) {
		        if (e == null) {
		            
		        	if (requestList.size() > 0) {
		        		SellerMyBidsAdapter adapter = new SellerMyBidsAdapter(getActivity(), R.layout.adapter_seller_my_bids_item, requestList);
		        		mListViewMyBids.setAdapter(adapter);
		        	}
		        	else {
		        		mListViewMyBids.setEmptyView(mTextViewNoMyBids);
		        	}	        	
		        	
		        } else {
		            Toast.makeText(getActivity(), "Failed to retrieve data\n\nPlease try again later", Toast.LENGTH_LONG).show();
		        }
		        progressDialog.dismiss();
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