package buyer.post;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs160.customjob.R;
import com.cs160.customjob.R.drawable;

public class AttachmentAdapter extends ArrayAdapter<AttachmentAdapter.Item> {

	public AttachmentAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	
	}
	
	private List<AttachmentAdapter.Item> items;
	
	public AttachmentAdapter(Context context, int resource, List<AttachmentAdapter.Item> items) {
	
	    super(context, resource);
	
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
	public View getView(int position, View convertView, ViewGroup parent) {
	
	    View v = convertView;
	
	    if (v == null) {
	
	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext());
	        v = vi.inflate(R.layout.adapter_buyer_newcj_attachment_item, null);
	
	    }
	
	    Item item = items.get(position);
	
	    if (item != null) {
	        TextView textViewItemTitle = (TextView) v.findViewById(R.id.textViewItemTitle);
	        ImageView imageViewAttachmentType = (ImageView) v.findViewById(R.id.imageViewAttachmentType);
	        
	        // Set title
	        textViewItemTitle.setText(item.getTitle());
	        
	        // Set item type icon
	        if (item.getType().equalsIgnoreCase("image")) {
	        	imageViewAttachmentType.setBackgroundResource(drawable.photo_icon);
	        }
	        else if (item.getType().equalsIgnoreCase("audio")) {
	        	imageViewAttachmentType.setBackgroundResource(drawable.audio_icon);
	        }
	        else if (item.getType().equalsIgnoreCase("other")) {
	        	imageViewAttachmentType.setBackgroundResource(drawable.attachment_icon);
	        }
	    }

	    return v;
	
	}
	
	public static class Item {
		private String mTitle = null;
		private String mType = null;
		private byte[] mData = null;
		
		public Item(String title, String type, byte[] data) {
			mTitle = title;
			mType = type;
			mData = data;
		}
		
		public String getTitle() {
			return mTitle;
		}
		
		public String getType() {
			return mType;
		}
		
		public byte[] getData() {
			return mData;
		}
	}
}