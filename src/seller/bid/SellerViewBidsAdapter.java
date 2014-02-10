package seller.bid;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import commons.TimeDiff;

public class SellerViewBidsAdapter extends ArrayAdapter<ParseObject> {
	
	ParseObject item;
	RelativeLayout relativeLayoutItem;
	Context adapterContext;
	public static final String OBJECT_ID = "Object ID";

	public SellerViewBidsAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	    adapterContext = context;
	}
	
	private List<ParseObject> items;
	
	public SellerViewBidsAdapter(Context context, int resource, List<ParseObject> items) {
		
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
	        v = vi.inflate(R.layout.adapter_seller_view_bids_item, null);
	
		    item = items.get(position);
			
		    if (item != null) {		    	
		    	TextView textViewAmount = (TextView) v.findViewById(R.id.sellerViewBidsAdapterBidAmount);
		    	TextView textViewPlacedAt = (TextView) v.findViewById(R.id.sellerViewBidsAdapterBidPlacedAt);
		    	TextView textViewBidder = (TextView) v.findViewById(R.id.sellerViewBidsAdapterBidder);
		    	relativeLayoutItem = (RelativeLayout) v.findViewById(R.id.sellerViewBidsAdapterRelativeLayout);
		    	
		    	textViewAmount.setText("$" + item.get("value"));
		    	
		    	Date date = (Date) item.getCreatedAt();
				DateFormat format = DateFormat.getDateTimeInstance();
		    	
		    	textViewPlacedAt.setText("Placed on: " + format.format(date));
		    	
		    	ParseUser user = item.getParseUser("owner");
		    	if (user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
		    		textViewBidder.setText("By: Me");
		    	}
		    	else{
		    		textViewBidder.setText("By: " + user.getString("full_name"));
		    	}
		    	relativeLayoutItem.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(adapterContext, seller.bid.SellerViewBidDetailsActivity.class);
						intent.putExtra(OBJECT_ID, items.get(position).getObjectId());
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						adapterContext.startActivity(intent);
					}		    		
		    	});
		    	
		    }
	    }

	    return v;
	
	}
}