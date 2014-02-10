package commons.chat;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MessageService extends BroadcastReceiver {
	private static List<ParseObject> allMessages = null;
	
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("MainActivity", "#### Pusht notification received");
        
        refreshMessages();
    }

	public static void start() {
		refreshMessages();
	}
	
	public static List<ParseObject> getMessagesForRequest(ParseObject customerRequest) {
		if (allMessages == null) {
			return new ArrayList<ParseObject>();
		}
		
		List<ParseObject> messagesForRequest = new ArrayList<ParseObject>();
		for (ParseObject message : allMessages) {
			if (message.getParseObject("customerRequest").hasSameId(customerRequest)) {
				messagesForRequest.add(message);		
			}
		}
		
		return messagesForRequest;
	}

	public static void sendTextMessage(ParseObject customerRequest, String text) {
		ParseObject message = new ParseObject("Message");
		message.put("customerRequest", customerRequest);
		message.put("sender", ParseUser.getCurrentUser());
		message.put("receiver", customerRequest.getParseUser("bidder"));
		message.put("messageType", "text");
		message.put("text", text);
		allMessages.add(message);
		message.saveInBackground();
		
		sendNotification();
	}

	public static void sendAttachmentMessage(ParseObject customerRequest, String  attachmentType, byte[] attachmentData) {
		ParseObject message = new ParseObject("Message");
		message.put("customerRequest", customerRequest);
		message.put("sender", ParseUser.getCurrentUser());
		message.put("receiver", customerRequest.getParseUser("bidder"));
		message.put("messageType", "attachment:" + attachmentType);
		allMessages.add(message);
		message.saveInBackground();
		
		sendNotification();
	}

	private static void sendNotification() {
		JSONObject data = null;
		try {
			data = new JSONObject("{\"action\": \"com.cs160.customjob.commons.chat.UPDATE_MESSAGES\"}");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		ParsePush push = new ParsePush();
		push.setChannel("UpdateChatMessages");
		push.setData(data);
		push.sendInBackground();
	}
	
	private static void refreshMessages() 
	{
		Log.d("MainActivity", "#### Refreshing chat messages");
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
		query.orderByAscending("createdAt");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> requestList, ParseException e) {
		    	allMessages = requestList;
		    	
		    	Log.d("MainActivity", "#### " + requestList.size());
		    }
		});
	}
}
