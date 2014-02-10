package commons;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cs160.customjob.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
 
public class RegisterActivity extends Activity {
	
	public static final boolean BUYER = false;
	public static final boolean SELLER = true;
	
	private Button mButtonRegister = null;
	private EditText mEditTextEmail = null;
	private EditText mEditTextPassword = null;
	private EditText mEditTextFullName = null;
	private EditText mEditTextPhoneNumber = null;
	
	private TextView mDisplayTextEmail = null;
	private TextView mDisplayTextPassword = null;
	private TextView mDisplayTextFullName = null;
	private TextView mDisplayTextRequired = null;
	
	private boolean userType = BUYER;
	
	private RadioGroup mUserTypeRadioGroup = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commons_register);
        
        mButtonRegister = (Button)findViewById(R.id.commons_register_buttonRegister);
        mEditTextEmail = (EditText)findViewById(R.id.editTextEmail);
        mEditTextPassword = (EditText)findViewById(R.id.editTextPassword);
        mEditTextFullName = (EditText)findViewById(R.id.editTextFullName);
        mEditTextPhoneNumber = (EditText)findViewById(R.id.editTextPhoneNumber);
        
        mButtonRegister.setEnabled(false);
        
        // Adding red asterisks to required items
        mDisplayTextEmail = (TextView)findViewById(R.id.displayTextEmail);
        mDisplayTextPassword = (TextView)findViewById(R.id.displayTextPassword);
        mDisplayTextFullName = (TextView)findViewById(R.id.displayTextFullName);
        mDisplayTextRequired = (TextView)findViewById(R.id.displayTextRequired);
        
        mUserTypeRadioGroup = (RadioGroup)findViewById(R.id.RegistrationUserTypeRadioButton);
        
        String addAsterik = "<font color=#372c24>Email</font><font color=#ff0000 >*</font>";
        mDisplayTextEmail.setText(Html.fromHtml(addAsterik));
        addAsterik = "<font color=#372c24>Password</font><font color=#ff0000 >*</font>";
        mDisplayTextPassword.setText(Html.fromHtml(addAsterik));
        addAsterik = "<font color=#372c24>Full Name</font><font color=#ff0000 >*</font>";
        mDisplayTextFullName.setText(Html.fromHtml(addAsterik));
        addAsterik = "<font color=#ff0000 >*</font><font color=#372c24> = required</font>";
        mDisplayTextRequired.setText(Html.fromHtml(addAsterik));
        
        //
        //  Monitor text fields and enable/disable the register button
        //
        TextWatcher textWatcher = new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				setRegisterButtonStatus();
			}
        	
        };

        mEditTextEmail.addTextChangedListener(textWatcher);
        mEditTextPassword.addTextChangedListener(textWatcher);
        mEditTextFullName.addTextChangedListener(textWatcher);
        
        //
        // Register
        //
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				int userTypeID = mUserTypeRadioGroup.getCheckedRadioButtonId();
				if (userTypeID == R.id.sellerRegistrationRadioButton)
				{
					userType = SELLER;
				}
				
				final ProgressDialog progressDialog = ProgressDialog.show(RegisterActivity.this, "Registering New Account", "Please wait...");
				
				ParseUser user = new ParseUser();
                user.setEmail(mEditTextEmail.getText().toString());
                user.setUsername(mEditTextEmail.getText().toString());
                user.setPassword(mEditTextPassword.getText().toString());
                user.put("full_name", mEditTextFullName.getText().toString());
                user.put("phone_number", mEditTextPhoneNumber.getText().toString());
                user.put("user_type", userType);
                user.signUpInBackground(new SignUpCallback() {
					@Override
					public void done(ParseException e) {
						if (e == null) { // succeed
							Toast.makeText(getApplicationContext(),
											"Successully registered!",
											Toast.LENGTH_LONG).show();

							ParseUser.logInInBackground(mEditTextEmail.getText().toString(), 
														mEditTextPassword.getText().toString(), 
														new LogInCallback() {

															@Override
															public void done(
																	ParseUser user,
																	ParseException e) {
																
																progressDialog.dismiss();
																
																setResult(1);
																finish();
																
																Intent i = new Intent(getApplicationContext(), main.MainActivity.class);
														        startActivity(i);
															}
							});
						}
						else { // failed
							Toast.makeText(getApplicationContext(),
									"Failed to register\n\nReason:\n" + e.getMessage(),
									Toast.LENGTH_LONG).show();
							progressDialog.dismiss();
						}
					}
                });
			}
		});
    }
    
    private void setRegisterButtonStatus() {
    	mButtonRegister.setEnabled(
    			mEditTextEmail.getText().length() > 0
    			&& mEditTextPassword.getText().length() > 0 
    			&& mEditTextFullName.getText().length() > 0
    	);
    }
}