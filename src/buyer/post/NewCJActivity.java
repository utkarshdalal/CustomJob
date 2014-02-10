package buyer.post;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cs160.customjob.R;
import com.cs160.customjob.R.drawable;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class NewCJActivity extends Activity {
	
	public static final String PENDING = "Pending";
	public static final String ACCEPTED = "Accepted";

	private Dialog mPicker = null;
	private TimePicker mTimePicker = null;
	private DatePicker mDatePicker = null;
	private Button mButtonSelectEndDate = null;
	private Button mButtonSelectEndTime = null;
	private Button mButtonPostJob = null;
	private EditText mEditTextTitle = null;
	private EditText mEditTextDescription = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US);
	private Drawable mPostJobButtonBackground = null;
	private ImageButton mButtonAddPhoto = null;
	private ImageButton mButtonAddAudio = null;
	private Button mButtonAddFile = null;
	private ScrollView mScrollView = null;
	private LinearLayout mAttachmentLayout = null;
	private int mAttachmentId = 0;
	
	private static final int TAKE_PICTURE_RETURN_CODE = 1;
	private static final int RECORD_AUDIO_RETURN_CODE = 2;
	private static final String TEMP_STORAGE_PATH = Environment.getExternalStorageDirectory() + "/customjob_temp";
	private static final String TEMP_CAMERA_PICTURE_PATH = TEMP_STORAGE_PATH + "/image.jpg";
	private static final String TEMP_AUDIO_FILE_PATH = TEMP_STORAGE_PATH + "/audio.3gpp";
	
	private List<AttachmentItem> mAttachmentItems = new ArrayList<AttachmentItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buyer_newcj);

		if (!new File(TEMP_STORAGE_PATH).exists()) {
			new File(TEMP_STORAGE_PATH).mkdirs();
		}
	
		mEditTextTitle = (EditText)findViewById(R.id.editTextTitle);
		mEditTextDescription = (EditText)findViewById(R.id.newCJEditTextDescription);
		mButtonSelectEndDate = (Button)findViewById(R.id.buttonSelectEndDate);
		mButtonSelectEndTime = (Button)findViewById(R.id.buttonSelectEndTime);
		mButtonPostJob = (Button)findViewById(R.id.buttonPostJob);
		mPostJobButtonBackground = mButtonPostJob.getBackground();
		mButtonAddPhoto = (ImageButton)findViewById(R.id.buyer_newcj_buttonAddPhoto);
		mButtonAddAudio = (ImageButton)findViewById(R.id.buyer_newcj_buttonAddAudio);
		mScrollView = (ScrollView)findViewById(R.id.buyer_newcj_scrollView);
		mAttachmentLayout = (LinearLayout)findViewById(R.id.buyer_newcj_add_attachments_layout);
		mEditTextTitle.requestFocus();
		mButtonPostJob.setEnabled(false);
		mButtonPostJob.setBackgroundColor(Color.LTGRAY);
		
		//
		//	Select End Date
		//
		mButtonSelectEndDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

                mPicker = new Dialog(NewCJActivity.this);
                mPicker.setContentView(R.layout.fragment_date_picker);
                mPicker.setTitle("Select End Date");
 
                mDatePicker = (DatePicker)mPicker.findViewById(R.id.datePicker);
                Button buttonDone = (Button)mPicker.findViewById(R.id.buttonDone);
 
                buttonDone.setOnClickListener(new View.OnClickListener() {
 
                    @Override
                    public void onClick(View view) {

                        mButtonSelectEndDate.setText(mDatePicker.getMonth() + 1 + "/" 
                        							+ mDatePicker.getDayOfMonth() + "/" 
                        							+ mDatePicker.getYear());
                        mPicker.dismiss();
                        setPostJobButtonStatus();
                    }
                });
                mPicker.show();
			}
		});
		
		//
		//	Select End Time
		//
		mButtonSelectEndTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

                mPicker = new Dialog(NewCJActivity.this);
                mPicker.setContentView(R.layout.fragment_time_picker);
                mPicker.setTitle("Select End Time");

                mTimePicker = (TimePicker)mPicker.findViewById(R.id.timePicker);
                mTimePicker.setCurrentHour(18);
                mTimePicker.setCurrentMinute(00);
                Button buttonDone = (Button)mPicker.findViewById(R.id.buttonDone);
 
                buttonDone.setOnClickListener(new View.OnClickListener() {
 
                    @Override
                    public void onClick(View view) {
                    	
                    	String currentMinute = (String) (mTimePicker.getCurrentMinute() < 10? "0" + mTimePicker.getCurrentMinute().toString():mTimePicker.getCurrentMinute().toString());

                        mButtonSelectEndTime.setText(mTimePicker.getCurrentHour() + ":"
                        								+ currentMinute);
                        mPicker.dismiss();
                        setPostJobButtonStatus();
                    }
                });
                mPicker.show();
			}
		});
		
		
		//
		// Post Job
		//
		mButtonPostJob.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final ProgressDialog progressDialog = ProgressDialog.show(NewCJActivity.this, "Posting Job", "Please wait...");
				

				
				final ParseObject customerRequest = new ParseObject("CustomerRequest");
				customerRequest.put("owner", ParseUser.getCurrentUser());
				customerRequest.put("title", mEditTextTitle.getText().toString());
				customerRequest.put("description", mEditTextDescription.getText().toString());
				customerRequest.put("bids", 0);
				customerRequest.put("status", PENDING);

				Date endDateTime = null;
				try {
					endDateTime = sdf.parse(mButtonSelectEndDate.getText().toString() 
											+ " " 
											+ mButtonSelectEndTime.getText().toString());
					customerRequest.put("endDateTime", endDateTime);
				} catch (ParseException e) {
					Log.d("NewCJActivity", e.getMessage());
				}
				
				customerRequest.saveInBackground(new SaveCallback() {

					@Override
					public void done(com.parse.ParseException e) {
						if (e == null) { // succeed
							
							// Upload attachments
							uploadAttachmentsForCustomerRequest(customerRequest, new SaveCallback() {

								@Override
								public void done(com.parse.ParseException e) {
									if (e == null) { // succeed
										Toast.makeText(getApplicationContext(), 
												"Your custom job has been posted successfully!",
												Toast.LENGTH_LONG).show();
										
										finish();
									}
									else { // failed
										Toast.makeText(getApplicationContext(), 
												"Failed to post your job. Please try again later.",
												Toast.LENGTH_LONG).show();
									}
									
									progressDialog.dismiss();
								}
								
							});
							
						}
						else { // failed
							Toast.makeText(getApplicationContext(), 
											"Failed to post your job. Please try again later.",
											Toast.LENGTH_LONG).show();
							Log.d("NewCJActivity", e.getMessage());
							
							progressDialog.dismiss();
						}
					}
				});
			}
		});
		
		
		/* Attachments */
		
		//
		//  Add photo attachment
		//
		mButtonAddPhoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File tempImageFile = new File(TEMP_CAMERA_PICTURE_PATH);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempImageFile));
			    startActivityForResult(takePictureIntent, TAKE_PICTURE_RETURN_CODE);
			}
		});
		
		//
		//  Add audio attachment
		//
		mButtonAddAudio.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent recordAudioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
				File tempAudioFile = new File(TEMP_AUDIO_FILE_PATH);
				recordAudioIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempAudioFile));
			    startActivityForResult(recordAudioIntent, RECORD_AUDIO_RETURN_CODE);
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    if (requestCode == TAKE_PICTURE_RETURN_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	        		
	        	//
	        	//  Add item to mAttachmentItems 
	        	//
	        	final AttachmentItem item = new AttachmentItem("Image " + (++mAttachmentId), 
	        											"image", getFileData(TEMP_CAMERA_PICTURE_PATH));

	        	addNewAttachment(item);
		        
	        }
	        
	        else if (resultCode == RESULT_CANCELED) {
	        	
	        } else {
	            
	        	Toast.makeText(this, "Failed to save the photo you just took, please try again.", Toast.LENGTH_LONG).show();
	        	
	        }
	    }
	    else if (requestCode == RECORD_AUDIO_RETURN_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Audio captured and saved to fileUri specified in the Intent

	        	final AttachmentItem item = new AttachmentItem("Audio " + (++mAttachmentId), 
	        											"audio", getFileData(TEMP_CAMERA_PICTURE_PATH));
	        	addNewAttachment(item);
		        
	        }
	        
	        else if (resultCode == RESULT_CANCELED) {
	        	
	        } else {
	            
	        	Toast.makeText(this, "Failed to save the photo you just took, please try again.", Toast.LENGTH_LONG).show();
	        	
	        }
	    }
	}
	
	private void addNewAttachment(final AttachmentItem item) {
    	//
    	//  Add item to mAttachmentItems 
    	//
    	mAttachmentItems.add(item);
    	
    	//
    	//  Add item to mAttachmentLayout
    	//
    	LayoutInflater vi = LayoutInflater.from(getApplicationContext());
        final View v = vi.inflate(R.layout.adapter_buyer_newcj_attachment_item, null);

        TextView textViewItemTitle = (TextView) v.findViewById(R.id.textViewItemTitle);
        ImageView imageViewAttachmentType = (ImageView) v.findViewById(R.id.imageViewAttachmentType);
        TextView textViewX = (TextView) v.findViewById(R.id.textViewRemove);
        
        textViewX.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v2) {
				mAttachmentItems.remove(item);
				mAttachmentLayout.removeView(v);
			}
		});
    
        // Set title
        textViewItemTitle.setText(item.getTitle());
        
        // Set item type icon
        if (item.getType().equalsIgnoreCase("image")) {
        	imageViewAttachmentType.setImageResource(drawable.photo_icon);
        }
        else if (item.getType().equalsIgnoreCase("audio")) {
        	imageViewAttachmentType.setImageResource(drawable.audio_icon);
        }
        else if (item.getType().equalsIgnoreCase("other")) {
        	imageViewAttachmentType.setImageResource(drawable.attachment_icon);
        }
        
        mAttachmentLayout.addView(v);
        
        scrollDownView();
	}
	
	private void uploadAttachmentsForCustomerRequest(final ParseObject customerRequest, final SaveCallback saveCallback) {

		if (mAttachmentItems.size() <= 0) {
			saveCallback.done(null);
			return;
		}
		
		final int[] count = {0};
		final ParseFile file = new ParseFile(mAttachmentItems.get(0).getData()); 
		final ArrayList<ParseFile> files = new ArrayList<ParseFile>();
		final ArrayList<String> attachmentsType = new ArrayList<String>();
		files.add(file);
		attachmentsType.add(mAttachmentItems.get(0).getType());
		
		file.saveInBackground(new SaveCallback() {

			@Override
			public void done(com.parse.ParseException e) {
				if (e == null) {
					count[0]++;
					if (count[0] < mAttachmentItems.size()) {
						ParseFile file = new ParseFile(mAttachmentItems.get(count[0]).getData());
						files.add(file);
						file.saveInBackground(this);
						attachmentsType.add(mAttachmentItems.get(count[0]).getType());
					}
					
					else { // all files are uploaded
						
						List<ParseObject> allAttachments = new ArrayList<ParseObject>();
						int i=0;
						for (ParseFile file : files) {
							ParseObject attachment = new ParseObject("Attachment");
							attachment.put("customer_request", customerRequest);
							attachment.put("file", file);
							attachment.put("type", attachmentsType.get(i));
							allAttachments.add(attachment);
							i++;
						}

						ParseObject.saveAllInBackground(allAttachments, saveCallback);
					}
				}
				else {
					saveCallback.done(e);
				}
			}
			
		});
	}
	
	 private void scrollDownView()
	 {
	        final Handler handler = new Handler();
	        new Thread(new Runnable() {
	            @Override
	            public void run() {
	                try {Thread.sleep(200);} catch (InterruptedException e) {}
	                handler.post(new Runnable() {
	                    @Override
	                    public void run() {
	                        mScrollView.fullScroll(View.FOCUS_DOWN);
	                    }
	                });
	            }
	        }).start();
	}
	
	private byte[] getFileData(String filePath) {
		
        File file = new File(filePath);

        byte[] fileData = new byte[(int) file.length()];
        try {
              FileInputStream fileInputStream = new FileInputStream(file);
              fileInputStream.read(fileData);
              fileInputStream.close();
         } 
         catch (FileNotFoundException e) {
        	 return null;
         }
         catch (IOException e) {
        	 return null;
         }
 
        return fileData;
	}
	
	private void setPostJobButtonStatus() {
		boolean isDateTimeValid = false;
		try {
			sdf.parse(mButtonSelectEndDate.getText().toString() 
						+ " " 
						+ mButtonSelectEndTime.getText().toString());
			isDateTimeValid = true;
		} catch (ParseException e) {
			isDateTimeValid = false;
		}
	
		if (isDateTimeValid && mEditTextTitle.getText().length() > 0) {
			mButtonPostJob.setEnabled(true);
			mButtonPostJob.setBackground(mPostJobButtonBackground);
		} 
		else {
			mButtonPostJob.setEnabled(false);
			mButtonPostJob.setBackgroundColor(Color.GRAY);
		}
	}
}
