package commons.chat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import buyer.post.NewCJActivity;

import com.cs160.customjob.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.RefreshCallback;

public class ChatActivity extends Activity {

	private String customerRequestId = "sdPN7pfdVC";
	private ParseObject customerRequest = null;
	private ImageButton mButtonSendMessage = null;
	private ImageButton mButtonAddAttachment = null;
	private Button mButtonBack = null;
	private EditText mEditTextMessage = null;
	private ListView mListViewMessages = null;
	private Handler mHandler;
	private Dialog mDialog = null;
	
	private static final int TAKE_PICTURE_RETURN_CODE = 1;
	private static final int RECORD_AUDIO_RETURN_CODE = 2;
	private static final String TEMP_STORAGE_PATH = Environment.getExternalStorageDirectory() + "/customjob_temp";
	private static final String TEMP_CAMERA_PICTURE_PATH = TEMP_STORAGE_PATH + "/image.jpg";
	private static final String TEMP_AUDIO_FILE_PATH = TEMP_STORAGE_PATH + "/audio.3gpp";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            customerRequestId = extras.getString("customerRequestId");
        }
        
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Loading", "Please wait...");
		
        ParseObject request = ParseObject.createWithoutData("CustomerRequest", customerRequestId);
        request.refreshInBackground(new RefreshCallback() {

			@Override
			public void done(ParseObject obj, ParseException e)
			{
				progressDialog.dismiss();
				customerRequest = obj;
				
				 mHandler = new Handler();
			        mListViewMessages = (ListView)findViewById(R.id.listViewMessages);
			        mButtonSendMessage = (ImageButton)findViewById(R.id.imageButtonSend);
			        mButtonAddAttachment = (ImageButton)findViewById(R.id.imageButtonAttach);
			        mEditTextMessage = (EditText)findViewById(R.id.editTextMessage);
			        mButtonBack = (Button)findViewById(R.id.buttonBack);
			        
			        // Add footer to mListViewMessages
			        View footerView = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
			        mListViewMessages.addFooterView(footerView);
			        
			        mButtonAddAttachment.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {

							mDialog = new Dialog(ChatActivity.this);
			                mDialog.setContentView(R.layout.fragment_chat_add_attachment);
			                mDialog.setTitle("Select Attachment Type");
			                mDialog.show();
			                
			                ImageButton mButtonAddPhoto = (ImageButton)mDialog.findViewById(R.id.imageButtonAddImage);
			                ImageButton mButtonAddAudio = (ImageButton)mDialog.findViewById(R.id.imageButtonAddAudio);
			                
			            	/* Attachments */
			        		
			        		//
			        		//  Add photo attachment
			        		//
			        		mButtonAddPhoto.setOnClickListener(new View.OnClickListener() {
			        			
			        			@Override
			        			public void onClick(View v) {
			        				/*
			        				MessageService.sendAttachmentMessage(customerRequest, "image", null);
			        				mDialog.dismiss();
			        				
			        				MessageAdapter msgAdapter = new MessageAdapter(ChatActivity.this, MessageService.getMessagesForRequest(customerRequest));
			        				mListViewMessages.setAdapter(msgAdapter);
			        				mListViewMessages.setSelection(msgAdapter.getCount() - 1);
			        				*/
			        				
			        				mDialog.dismiss();
			        				mDialog = null;
			        				
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
			        				/*
			        				MessageService.sendAttachmentMessage(customerRequest, "audio", null);
			        				mDialog.dismiss();
			        				
			        				MessageAdapter msgAdapter = new MessageAdapter(ChatActivity.this, MessageService.getMessagesForRequest(customerRequest));
			        				mListViewMessages.setAdapter(msgAdapter);
			        				mListViewMessages.setSelection(msgAdapter.getCount() - 1);
			        				*/
			        				
			        				mDialog.dismiss();
			        				mDialog = null;
			        				
			        				Intent recordAudioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
			        				File tempAudioFile = new File(TEMP_AUDIO_FILE_PATH);
			        				recordAudioIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempAudioFile));
			        			    startActivityForResult(recordAudioIntent, RECORD_AUDIO_RETURN_CODE);
			        			}
			        		});
						}
					});
			        
			        //
			        // Back button
			        //
			        mButtonBack.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							finish();
						}
					});
			        
			        //
			        // Send message button
			        //
			        mButtonSendMessage.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if (mEditTextMessage.getText().toString().length() == 0) {
								return;
							}

							MessageService.sendTextMessage(customerRequest, mEditTextMessage.getText().toString());
							mEditTextMessage.setText("");
							
							MessageAdapter msgAdapter = new MessageAdapter(ChatActivity.this, MessageService.getMessagesForRequest(customerRequest));
							mListViewMessages.setAdapter(msgAdapter);
							mListViewMessages.setSelection(msgAdapter.getCount() - 1);
						}
					});

			        Log.d("MainActivity", "#### " + MessageService.getMessagesForRequest(customerRequest).size());
					
					// Pull messages periodically
					final Runnable updateMessagesRunnable = new Runnable() {
						@Override
						public void run() {
				            MessageAdapter msgAdapter = new MessageAdapter(ChatActivity.this, MessageService.getMessagesForRequest(customerRequest));
				    		mListViewMessages.setAdapter(msgAdapter);
				    		mListViewMessages.setSelection(msgAdapter.getCount() - 1);
				    		
				    		Log.d("MainActivity", "#### Updated");
				    		
				    		mHandler.postDelayed(this, 5000);
						}
					};
					
					updateMessagesRunnable.run();
			}
			
		});    
        
        /*
        ParseQuery<ParseObject> q = new ParseQuery<ParseObject>("CustomerRequest");
        q.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				
				for (ParseObject obj : objects) {
					obj.put("seller", ParseUser.getCurrentUser());
					obj.saveInBackground();
					
					
				}
			}
        	
        });
        */
        
       
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    if (requestCode == TAKE_PICTURE_RETURN_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent

				MessageService.sendAttachmentMessage(customerRequest, "image", getFileData(TEMP_CAMERA_PICTURE_PATH));

				MessageAdapter msgAdapter = new MessageAdapter(ChatActivity.this, MessageService.getMessagesForRequest(customerRequest));
				mListViewMessages.setAdapter(msgAdapter);
				mListViewMessages.setSelection(msgAdapter.getCount() - 1);
		        
	        }
	        
	        else if (resultCode == RESULT_CANCELED) {
	        	
	        } else {
	            
	        	Toast.makeText(this, "Failed to save the photo you just took, please try again.", Toast.LENGTH_LONG).show();
	        	
	        }
	    }
	    else if (requestCode == RECORD_AUDIO_RETURN_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Audio captured and saved to fileUri specified in the Intent
	        	
	        	
				MessageService.sendAttachmentMessage(customerRequest, "audio", getFileData(TEMP_AUDIO_FILE_PATH));
				
				MessageAdapter msgAdapter = new MessageAdapter(ChatActivity.this, MessageService.getMessagesForRequest(customerRequest));
				mListViewMessages.setAdapter(msgAdapter);
				mListViewMessages.setSelection(msgAdapter.getCount() - 1);
		        
	        }
	        
	        else if (resultCode == RESULT_CANCELED) {
	        	
	        } else {
	            
	        	Toast.makeText(this, "Failed to save the photo you just took, please try again.", Toast.LENGTH_LONG).show();
	        	
	        }
	    }
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
}
