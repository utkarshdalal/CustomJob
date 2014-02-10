package buyer.listings;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs160.customjob.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class AcceptedListingsAdapter extends ArrayAdapter<ParseObject> {
	
	ParseObject item;
	RelativeLayout relativeLayoutItem;
	Context adapterContext;

	public AcceptedListingsAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	    adapterContext = context;
	}
	
	private List<ParseObject> items;
	
	public AcceptedListingsAdapter(Context context, int resource, List<ParseObject> items) {
	
	    super(context, resource);
	    adapterContext = context;
	    this.items = items;
	}
	

	@Override
	public int getCount() {
		return this.items.size();
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	
	    View v = convertView;
	
	    if (v == null) {
	
	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext()); 
	        v = vi.inflate(R.layout.adapter_accepted_listings_item, null);
	
		    item = items.get(position);
		    

			
		    if (item != null) {
		    	TextView textViewTitle = (TextView) v.findViewById(R.id.AcceptedListingsAdapterTextViewTitle);
		    	TextView textViewWinningBidder = (TextView) v.findViewById(R.id.AcceptedListingsAdapterTextViewWinningBidder);
		    	TextView textViewWinningBid = (TextView) v.findViewById(R.id.AcceptedListingsAdapterTextViewWinningBid);
		    	relativeLayoutItem = (RelativeLayout) v.findViewById(R.id.AcceptedListingsAdapterRelativeLayout);
		    	
		    	ParseUser bidder = item.getParseUser("bidder");
		    	ParseObject bid = item.getParseObject("bid");

		    	textViewTitle.setText((String)item.get("title"));
		    	textViewWinningBidder.setText("Winning Bidder: " + bidder.getString("full_name"));
		    	textViewWinningBid.setText("Winning Bid: " + bid.getString("value"));
		    	relativeLayoutItem.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(adapterContext, buyer.listings.ViewAcceptedJobActivity.class);
						intent.putExtra(seller.listings.SellerListingsAdapter.OBJECT_ID, items.get(position).getObjectId());
						adapterContext.startActivity(intent);
					}		    		
		    	});
		    	
		    }
	    }

	    return v;
	
	}
}