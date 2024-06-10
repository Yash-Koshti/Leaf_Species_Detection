package com.leaf_lore.leaf_lore_frontend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.leaf_lore.leaf_lore_frontend.utils.ApiCalls;

public class Login extends AppCompatActivity {
	private int DELAY_TIME = 10000;
	private TextInputLayout inputUsernameLayout, inputPasswordLayout;
	private EditText inputUsername, inputPassword;
	private Button submitLogin;
	private ProgressBar progressBar;
	private TextView registerLink;
	private ApiCalls apiCalls;
	private Context applicationContext;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		apiCalls = new ApiCalls(this);

		inputUsernameLayout = findViewById(R.id.TIL_LoginUsername);
		inputPasswordLayout = findViewById(R.id.TIL_LoginPassword);
		inputUsername = findViewById(R.id.TIET_LoginUsername);
		inputPassword = findViewById(R.id.TIET_LoginPassword);
		submitLogin = findViewById(R.id.Btn_SubmitLoginForm);
		progressBar = findViewById(R.id.PB_LoginProgress);
		registerLink = findViewById(R.id.TxtV_LoginRegisterLink);

		applicationContext = getApplicationContext();
		sharedPreferences = applicationContext.getSharedPreferences("com.leaf_lore.leaf_lore_frontend", Context.MODE_PRIVATE);

		inputUsername.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0) {
					inputUsernameLayout.setHelperText(null);

					if (s.length() < 4) {
						inputUsernameLayout.setError("Username is too short");
					} else if (s.length() > 20) {
						inputUsernameLayout.setError("Username is too long");
					} else {
						inputUsernameLayout.setError(null);
					}
				} else {
					inputUsernameLayout.setHelperText("Required*");
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		inputPasswordLayout.setErrorIconDrawable(null);
		inputPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0) {
					inputPasswordLayout.setHelperText(null);

					if (s.length() < 8) {
						inputPasswordLayout.setError("Password is too short");
					} else if (s.length() > 20) {
						inputPasswordLayout.setError("Password is too long");
					} else {
						inputPasswordLayout.setError(null);
					}
				} else {
					inputPasswordLayout.setHelperText("Required*");
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		submitLogin.setOnClickListener(v -> {
			String usernameText = inputUsername.getText().toString();
			String passwordText = inputPassword.getText().toString();

			if (isFormValid(usernameText, passwordText)) {
				apiCalls.fetchToken(usernameText, passwordText, sharedPreferences.edit());
				progressBar.setVisibility(ProgressBar.VISIBLE);
				submitLogin.setEnabled(false);
				runPeriodicCheckForApi();
			} else {
				Toast.makeText(Login.this, "Please fill in the form correctly", Toast.LENGTH_SHORT).show();
			}
		});

		SpannableString spannableString = new SpannableString("Register here");
		spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
		registerLink.setText(spannableString);

		registerLink.setOnClickListener(v -> {
			startActivity(new Intent(Login.this, Register.class));
			finish();
		});
	}

	private boolean isFormValid(String usernameText, String passwordText) {
		boolean isValid = true;

		if (usernameText.length() < 4 || usernameText.length() > 20) {
			inputUsernameLayout.setError("Username must be between 4 and 20 characters");
			isValid = false;
		}

		if (passwordText.length() < 8 || passwordText.length() > 20) {
			inputPasswordLayout.setError("Password must be between 8 and 20 characters");
			isValid = false;
		}

		return isValid;
	}

	private void runPeriodicCheckForApi() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!apiCalls.isTokenFetched) {
					apiCalls.fetchToken(inputUsername.getText().toString(), inputPassword.getText().toString(), sharedPreferences.edit());
				} else if (!apiCalls.isUserFetched) {
					DELAY_TIME = 3000;
					apiCalls.fetchUser(sharedPreferences.edit());
				} else {
					Toast.makeText(Login.this, "Login Success!", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(Login.this, MainActivity.class));
					finish();
					return;
				}
				runPeriodicCheckForApi();
			}
		}, DELAY_TIME);
	}
}