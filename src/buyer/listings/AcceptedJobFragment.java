package buyer.listings;

import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs160.customjob.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class AcceptedJobFragment extends Fragment {

    private ListView mListViewAcceptedJob = null;
    private TextView mTextViewNoJobs = null;

    public AcceptedJobFragment() {
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
            
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_buyer_accepted_jobs,
                            null);
     
            mListViewAcceptedJob = (ListView)v.findViewById(R.id.listViewAcceptedJob);
            mTextViewNoJobs = (TextView)v.findViewById(R.id.textViewNoAcceptedJob);
            
            retrieveData();
            
            return v;
    }

    private void retrieveData() {
		//
		// Retreive data from Parse
		//
        
		final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Retreiving Data", "Please wait...");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("CustomerRequest");
		query.whereEqualTo("owner", ParseUser.getCurrentUser());
		query.whereEqualTo("status", buyer.post.NewCJActivity.ACCEPTED);
		query.include("bid");
		query.include("bidder");
		query.orderByDescending("updatedAt");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> requestList, ParseException e) {
		        if (e == null) {
		            
		        	if (requestList.size() > 0) {
		        		AcceptedListingsAdapter adapter = new AcceptedListingsAdapter(getActivity(), R.layout.adapter_accepted_listings_item, requestList);
		        		mListViewAcceptedJob.setAdapter(adapter);
		        	}
		        	else {
		        		mListViewAcceptedJob.setEmptyView(mTextViewNoJobs);
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