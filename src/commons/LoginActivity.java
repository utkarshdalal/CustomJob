package commons;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cs160.customjob.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
 
public class LoginActivity extends Activity {
	
	private Button mButtonLogin = null;
	private EditText mEditTextEmail = null;
	private EditText mEditTextPassword = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commons_login);
 
        mButtonLogin = (Button)findViewById(R.id.commons_login_buttonLogin);
        mEditTextEmail = (EditText)findViewById(R.id.commons_login_editTextEmail);
        mEditTextPassword = (EditText)findViewById(R.id.commons_login_editTextPassword);
        
        mButtonLogin.setEnabled(false);
        
        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
        
        registerScreen.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(i, 1);
            }
        });
        
        //
        //  Monitor text fields and enable/disable the login button
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
				setLoginButtonStatus();
			}
        	
        };

        mEditTextEmail.addTextChangedListener(textWatcher);
        mEditTextPassword.addTextChangedListener(textWatcher);
        
        //
        //  Login
        //
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Logging in account", "Please wait...");
				
				ParseUser.logInInBackground(mEditTextEmail.getText().toString(), 
											mEditTextPassword.getText().toString(), 
											new LogInCallback() {

												@Override
												public void done(
														ParseUser user,
														ParseException e) {
													if (e == null) {
														Intent i = new Intent(getApplicationContext(), main.MainActivity.class);
												        startActivity(i);
												        
												        finish();
													}
													else { // failed
														Toast.makeText(getApplicationContext(),
																"Failed to login\n\nReason:\n" + e.getMessage(),
																Toast.LENGTH_LONG).show();
														progressDialog.dismiss();
													}
													
												}
					
				});
			}
		});
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1) {
            finish();
        }
    }
    
    private void setLoginButtonStatus() {
    	mButtonLogin.setEnabled(
    			mEditTextEmail.getText().length() > 0
    			&& mEditTextPassword.getText().length() > 0 
    	);
    }
}