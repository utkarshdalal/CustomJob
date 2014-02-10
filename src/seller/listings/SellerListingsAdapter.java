package seller.listings;

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

public class SellerListingsAdapter extends ArrayAdapter<ParseObject> {
	
	ParseObject item;
	RelativeLayout relativeLayoutItem;
	Context adapterContext;
	public static final String OBJECT_ID = "Object ID";

	public SellerListingsAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	    adapterContext = context;
	}
	
	private List<ParseObject> items;
	
	public SellerListingsAdapter(Context context, int resource, List<ParseObject> items) {
		
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
	        v = vi.inflate(R.layout.adapter_seller_listings_item, null);
	
		    item = items.get(position);
			
		    if (item != null) {
		    	TextView textViewTitle = (TextView) v.findViewById(R.id.adapter_seller_listings_item_textViewTitle);
		    	TextView textViewTimeLeft = (TextView) v.findViewById(R.id.adapter_seller_listings_item_textViewTimeLeft);
		    	TextView textViewUser = (TextView) v.findViewById(R.id.adapter_seller_listings_item_textViewUser);
		    	relativeLayoutItem = (RelativeLayout) v.findViewById(R.id.adapter_seller_listings_item_relativeLayout);
		    	
		    	textViewTitle.setText((String)item.get("title"));
		    	
		    	long[] diff = TimeDiff.getTimeDifference((Date)item.get("endDateTime"), new Date(System.currentTimeMillis()));
		    	textViewTimeLeft.setText("Time Left:    " + diff[0] + " day(s) " + diff[1] + " hr(s) " + diff[2] + " min(s) ");
		    	
		    	ParseUser user = item.getParseUser("owner");
		    	textViewUser.setText("By: " + user.getString("full_name"));
		    	
		    	relativeLayoutItem.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(adapterContext, seller.bid.SellerViewCJActivity.class);
						intent.putExtra(OBJECT_ID, items.get(position).getObjectId());
						adapterContext.startActivity(intent);
					}		    		
		    	});
		    	
		    }
	    }

	    return v;
	
	}
}