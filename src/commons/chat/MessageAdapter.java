package commons.chat;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs160.customjob.R;
import com.cs160.customjob.R.drawable;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class MessageAdapter extends BaseAdapter {
	private Context mContext = null;
	private List<ParseObject> items;
	
	public MessageAdapter(Context context, List<ParseObject> items) {
		mContext = context;
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
	public int getItemViewType(int position) {
		ParseObject message = items.get(position);
		
        String msgType = (String)message.get("messageType");
        if (msgType.equalsIgnoreCase("text")) {
        	if (message.getParseUser("sender").hasSameId(ParseUser.getCurrentUser())) {
        		return 0;
        	}
        	else {
        		return 1;
        	}
        }
        else {
        	if (message.getParseUser("sender").hasSameId(ParseUser.getCurrentUser())) {
        		 return 2;
        	}
        	else {
        		 return 3;
        	}
        }
	}

	@Override
	public int getViewTypeCount() {
	    return 4; // Count of different layouts
	}
	
	@Override
	public boolean isEnabled(int position) {
	    return false;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		ParseObject message = items.get(position);

	    View v = convertView;
	
	    if (v == null) {
	
	        LayoutInflater vi;
	        vi = LayoutInflater.from(mContext);
	
	        if (getItemViewType(position) == 0) {
	        	v = vi.inflate(R.layout.adapter_chat_text_message_right, null);
	        }
	        else if (getItemViewType(position) == 1) {
	        	v = vi.inflate(R.layout.adapter_chat_text_message_left, null);
	        }
	        else if (getItemViewType(position) == 2) {
	        	v = vi.inflate(R.layout.adapter_chat_attachment_message_right, null);
	        }
	        else if (getItemViewType(position) == 3) {
	        	v = vi.inflate(R.layout.adapter_chat_attachment_message_left, null);
	        }
	    }
	
	    if (message != null) 
	    {
	    	String msgType = (String)message.get("messageType");
	    	
	    	if (getItemViewType(position) <= 1) {
	    		TextView textViewMessage = (TextView)v.findViewById(R.id.textViewMessage);
	        	textViewMessage.setText((String)message.get("text"));
	    	}
	    	else {
	 	        TextView textViewAttachmentType = (TextView) v.findViewById(R.id.textViewAttachmentType);
    	        ImageView imageViewIcon = (ImageView) v.findViewById(R.id.imageViewIcon);
    	        
	        	if (msgType.equalsIgnoreCase("attachment:image")) {
	        		textViewAttachmentType.setText("Image Attachment");
	        		imageViewIcon.setBackgroundResource(drawable.photo_icon);
	        	}	        
	        	else if (msgType.equalsIgnoreCase("attachment:audio")) {
	        		textViewAttachmentType.setText("Audio Attachment");
	        		imageViewIcon.setBackgroundResource(drawable.audio_icon);
	        	}
	        	
	        	textViewAttachmentType.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
	    	}
	    }

	    return v;
	
	}


	@Override
	public Object getItem(int position) {
		return this.items.get(position);
	}
}
