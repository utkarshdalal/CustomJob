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

public class SellerMyOrdersAdapter extends ArrayAdapter<ParseObject> {
	
	ParseObject item;
	RelativeLayout relativeLayoutItem;
	Context adapterContext;
	public static final String OBJECT_ID = "Object ID";

	public SellerMyOrdersAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	    adapterContext = context;
	}
	
	private List<ParseObject> items;
	
	public SellerMyOrdersAdapter(Context context, int resource, List<ParseObject> items) {
		
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
	        v = vi.inflate(R.layout.adapter_seller_my_orders_item, null);
	
		    item = items.get(position);
			
		    if (item != null) {
		    	TextView textViewAmount = (TextView) v.findViewById(R.id.sellerMyOrdersAdapterBidAmount);
		    	TextView textViewTitle = (TextView) v.findViewById(R.id.sellerMyOrdersAdapterTitle);
		    	TextView textViewAcceptedAt = (TextView) v.findViewById(R.id.sellerMyOrdersAdapterAcceptedAt);
		    	relativeLayoutItem = (RelativeLayout) v.findViewById(R.id.sellerMyOrdersAdapterRelativeLayout);
		    	
		    	textViewAmount.setText("$" + (String)item.get("value"));
				
				ParseObject request = item.getParseObject("parent");
		    	
		    	textViewTitle.setText("Item Name: " + request.getString("title"));
		    	
		    	Date date = request.getUpdatedAt();
				DateFormat format = DateFormat.getDateTimeInstance();
		    	
		    	textViewAcceptedAt.setText("Accepted At: " + format.format(date));
		    	
		    	relativeLayoutItem.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(adapterContext, seller.listings.SellerViewOrderDetailsActivity.class);
						intent.putExtra(OBJECT_ID, items.get(position).getParseObject("parent").getObjectId());
						adapterContext.startActivity(intent);
					}		    		
		    	});
		    	
		    }
	    }

	    return v;
	
	}
}