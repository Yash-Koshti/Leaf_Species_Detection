package com.leaf_lore.leaf_lore_frontend;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.leaf_lore.leaf_lore_frontend.utils.ApiCalls;
import com.leaf_lore.leaf_lore_frontend.utils.ApiCallsConfirmer;

public class Login extends AppCompatActivity {
	private final int DELAY_TIME = 10000;
	private TextInputLayout inputUsernameLayout, inputPasswordLayout;
	private EditText inputUsername, inputPassword;
	private Button submitLogin;
	private ProgressBar progressBar;
	private ApiCalls apiCalls;
	private boolean tokenReceived = false, runPeriodicCheck = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		apiCalls = new ApiCalls(this, new ApiCallsConfirmer() {
			@Override
			public void confirmTokenReceived(boolean confirm) {
				tokenReceived = confirm;
			}

			@Nullable
			@Override
			public void confirmAllSpeciesFetched(boolean confirm) {

			}

			@Nullable
			@Override
			public void confirmAllImageNamesFetched(boolean confirm) {

			}

			@Nullable
			@Override
			public void confirmAllShapesFetched(boolean confirm) {

			}

			@Nullable
			@Override
			public void confirmAllApexesFetched(boolean confirm) {

			}

			@Nullable
			@Override
			public void confirmAllMarginsFetched(boolean confirm) {

			}
		});

		inputUsernameLayout = findViewById(R.id.TIL_LoginUsername);
		inputPasswordLayout = findViewById(R.id.TIL_LoginPassword);
		inputUsername = findViewById(R.id.TIET_LoginUsername);
		inputPassword = findViewById(R.id.TIET_LoginPassword);
		submitLogin = findViewById(R.id.Btn_SubmitLoginForm);
		progressBar = findViewById(R.id.PB_LoginProgress);

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
				apiCalls.fetchToken(usernameText, passwordText);
				progressBar.setVisibility(ProgressBar.VISIBLE);
				submitLogin.setEnabled(false);
				runPeriodicCheck = true;
			}
		});
	}

	private boolean isFormValid(String usernameText, String passwordText) {
		return usernameText.length() < 21 && usernameText.length() > 3 && passwordText.length() < 21 && passwordText.length() > 7;
	}

	private void runPeriodicCheckForApi() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!tokenReceived) {
					apiCalls.fetchToken(inputUsername.getText().toString(), inputPassword.getText().toString());
					runPeriodicCheckForApi();
				}
				runPeriodicCheck = false;
			}
		}, DELAY_TIME);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (runPeriodicCheck) {
			runPeriodicCheckForApi();
		}
	}
}