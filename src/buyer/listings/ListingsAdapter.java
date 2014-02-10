package buyer.listings;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs160.customjob.R;
import com.parse.ParseObject;
import commons.TimeDiff;

public class ListingsAdapter extends ArrayAdapter<ParseObject> {
	
	ParseObject item;
	RelativeLayout relativeLayoutItem;
	Context adapterContext;

	public ListingsAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	    adapterContext = context;
	}
	
	private List<ParseObject> items;
	
	public ListingsAdapter(Context context, int resource, List<ParseObject> items) {
	
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
	        v = vi.inflate(R.layout.adapter_buyer_listings_item, null);
	
		    item = items.get(position);
		    

			
		    if (item != null) {
		    	TextView textViewTitle = (TextView) v.findViewById(R.id.adapter_buyer_listsings_item_textViewTitle);
		    	TextView textViewTimeLeft = (TextView) v.findViewById(R.id.adapter_buyer_listsings_item_textViewTimeLeft);
		    	TextView textViewNumberOfBids = (TextView) v.findViewById(R.id.adapter_buyer_listings_item_textViewNumberOfBids);
		    	relativeLayoutItem = (RelativeLayout) v.findViewById(R.id.adapter_buyer_listings_item_relativeLayout);
		    	textViewTitle.setText((String)item.get("title"));
		    	
		    	long[] diff = TimeDiff.getTimeDifference((Date)item.get("endDateTime"), new Date(System.currentTimeMillis()));
		    	textViewTimeLeft.setText("Time Left:    " + diff[0] + " day(s) " + diff[1] + " hr(s) " + diff[2] + " min(s) ");
		    	
		    	textViewNumberOfBids.setText("Number of Bids: " + item.getInt("bids"));
		    	
		    	relativeLayoutItem.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(adapterContext, buyer.listings.ViewPendingJobActivity.class);
						intent.putExtra(seller.listings.SellerListingsAdapter.OBJECT_ID, items.get(position).getObjectId());
						adapterContext.startActivity(intent);
					}		    		
		    	});
		    	
		    }
	    }

	    return v;
	
	}
}