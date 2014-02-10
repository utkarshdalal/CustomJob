package seller.listings;

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

public class SellerMyBidsAdapter extends ArrayAdapter<ParseObject> {
	
	ParseObject item;
	RelativeLayout relativeLayoutItem;
	Context adapterContext;
	public static final String OBJECT_ID = "Object ID";

	public SellerMyBidsAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	    adapterContext = context;
	}
	
	private List<ParseObject> items;
	
	public SellerMyBidsAdapter(Context context, int resource, List<ParseObject> items) {
		
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
	        v = vi.inflate(R.layout.adapter_seller_my_bids_item, null);
	
		    item = items.get(position);
			
		    if (item != null) {
		    	TextView textViewAmount = (TextView) v.findViewById(R.id.sellerMyBidsAdapterBidAmount);
		    	TextView textViewPlacedAt = (TextView) v.findViewById(R.id.sellerMyBidsAdapterBidPlacedAt);
		    	TextView textViewItem = (TextView) v.findViewById(R.id.sellerMyBidsAdapterItem);
		    	relativeLayoutItem = (RelativeLayout) v.findViewById(R.id.sellerMyBidsAdapterRelativeLayout);
		    	
		    	textViewAmount.setText("$" + (String)item.get("value"));
		    	
		    	Date date = (Date) item.getCreatedAt();
				DateFormat format = DateFormat.getDateTimeInstance();
		    	
		    	textViewPlacedAt.setText("Placed on: " + format.format(date));
		    	
		    	ParseObject request = item.getParseObject("parent");
		    	textViewItem.setText("On: " + request.getString("title"));
		    	
		    	relativeLayoutItem.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(adapterContext, seller.bid.SellerViewBidDetailsActivity.class);
						intent.putExtra(OBJECT_ID, items.get(position).getObjectId());
						adapterContext.startActivity(intent);
					}		    		
		    	});
		    	
		    }
	    }

	    return v;
	
	}
}