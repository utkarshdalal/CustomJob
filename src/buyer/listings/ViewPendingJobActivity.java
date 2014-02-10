package buyer.listings;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import seller.bid.SellerViewBidsActivity;
import seller.listings.SellerListingsAdapter;

import com.cs160.customjob.R;
import com.cs160.customjob.R.drawable;
import com.cs160.customjob.R.layout;
import com.cs160.customjob.R.menu;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPendingJobActivity extends Activity {
	
	TextView mTitle, mDescription, mDate, mAttachmentsTitle;
	Button mViewBidsButton;
	String objectId;
	
	private LinearLayout mAttachmentLayout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buyer_view_pending_job);
		mTitle = (TextView) findViewById(R.id.BuyerViewPendingJobTitle);
		mDescription = (TextView) findViewById(R.id.BuyerViewPendingJobDescription);
		mDate = (TextView) findViewById(R.id.BuyerViewPendingJobAcceptingBidsUntilDate);
		mViewBidsButton = (Button) findViewById(R.id.BuyerViewPendingJobViewBidsButton);
		mAttachmentsTitle = (TextView) findViewById(R.id.BuyerViewPendingJobAttachmentsTitle);
		objectId = getIntent().getExtras().getString(seller.listings.SellerListingsAdapter.OBJECT_ID);
		final ProgressDialog progressDialog = ProgressDialog.show(this, "Retreiving Data", "Please wait...");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("CustomerRequest");
		query.getInBackground(objectId, new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					Date date = (Date) object.get("endDateTime");
					DateFormat format = DateFormat.getDateTimeInstance();
					mTitle.setText(object.getString("title"));
					mDescription.setText(object.getString("description"));
					mDate.setText(format.format(date));
			    } else {
			    	Toast.makeText(getApplicationContext(), "Failed to retrieve data\n\nPlease try again later", Toast.LENGTH_LONG).show();
			    	finish();
			    }
				progressDialog.dismiss();
				
				/* Attachment */
				mAttachmentLayout = (LinearLayout)findViewById(R.id.BuyerViewPendingJobAttachmentsContainer);
				ParseQuery<ParseObject> attachmentQuery = ParseQuery.getQuery("Attachment");
				attachmentQuery.whereEqualTo("customer_request", object);
				attachmentQuery.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> attachments, ParseException e) {
						mAttachmentsTitle.setText(attachments.size() + " Attachments:");
						
						for (ParseObject attachment : attachments) {
							 addNewAttachment(attachment);
						}
						
						progressDialog.dismiss();
					}
					
				});
			}
		});
		mViewBidsButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(), ViewBidsActivity.class);
				intent.putExtra(seller.listings.SellerListingsAdapter.OBJECT_ID, objectId);
				startActivity(intent);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.buyer_view_pending_job, menu);
		return true;
	}
	
	private void addNewAttachment(ParseObject attachment) {
    	//
    	//  Add item to mAttachmentLayout
    	//
    	LayoutInflater vi = LayoutInflater.from(getApplicationContext());
        final View v = vi.inflate(R.layout.adapter_attachment, null);

        TextView textViewItemTitle = (TextView) v.findViewById(R.id.textViewItemTitle);
        ImageView imageViewAttachmentType = (ImageView) v.findViewById(R.id.imageViewAttachmentType);

        String attachmentType = attachment.getString("type");
        
        // Set item type icon and title
        if (attachmentType.equalsIgnoreCase("image")) {
        	imageViewAttachmentType.setImageResource(drawable.photo_icon);
        	textViewItemTitle.setText("Image Attachment");
        }
        else if (attachmentType.equalsIgnoreCase("audio")) {
        	imageViewAttachmentType.setImageResource(drawable.audio_icon);
        	textViewItemTitle.setText("Audio Attachment");
        }
        
        textViewItemTitle.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        
        mAttachmentLayout.addView(v);
	}

}
