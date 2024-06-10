package com.leaf_lore.leaf_lore_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.leaf_lore.leaf_lore_frontend.utils.ApiCalls;

public class Register extends AppCompatActivity {
	private final int DELAY_TIME = 10000;
	private TextInputLayout inputUsernameLayout, inputEmailLayout, inputPasswordLayout, inputConfirmPasswordLayout;
	private EditText inputUsername, inputEmail, inputPassword, inputConfirmPassword;
	private Button submitRegister;
	private ProgressBar progressBar;
	private ApiCalls apiCalls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		apiCalls = new ApiCalls(this);

		inputUsernameLayout = findViewById(R.id.TIL_RegisterUsername);
		inputEmailLayout = findViewById(R.id.TIL_RegisterEmail);
		inputPasswordLayout = findViewById(R.id.TIL_RegisterPassword);
		inputConfirmPasswordLayout = findViewById(R.id.TIL_RegisterConfirmPassword);
		inputUsername = findViewById(R.id.TIET_RegisterUsername);
		inputEmail = findViewById(R.id.TIET_RegisterEmail);
		inputPassword = findViewById(R.id.TIET_RegisterPassword);
		inputConfirmPassword = findViewById(R.id.TIET_RegisterConfirmPassword);
		submitRegister = findViewById(R.id.Btn_SubmitRegisterForm);
		progressBar = findViewById(R.id.PB_RegisterProgress);

		inputPasswordLayout.setErrorIconDrawable(null);
		inputConfirmPasswordLayout.setErrorIconDrawable(null);

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
		inputEmail.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0) {
					inputEmailLayout.setHelperText(null);

					if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
						inputEmailLayout.setError("Invalid email address");
					} else {
						inputEmailLayout.setError(null);
					}
				} else {
					inputEmailLayout.setHelperText("Required*");
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
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
		inputConfirmPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0) {
					inputConfirmPasswordLayout.setHelperText(null);

					if (!s.toString().equals(inputPassword.getText().toString())) {
						inputConfirmPasswordLayout.setError("Passwords do not match");
					} else {
						inputConfirmPasswordLayout.setError(null);
					}
				} else {
					inputConfirmPasswordLayout.setHelperText("Required*");
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		submitRegister.setOnClickListener(v -> {
			String usernameText = inputUsername.getText().toString();
			String emailText = inputEmail.getText().toString();
			String passwordText = inputPassword.getText().toString();
			String confirmPasswordText = inputConfirmPassword.getText().toString();

			if (isFormValid(usernameText, emailText, passwordText, confirmPasswordText)) {
				progressBar.setVisibility(ProgressBar.VISIBLE);
				apiCalls.registerUser(usernameText, emailText, passwordText);
				runPeriodicCheckForApi();
			} else {
				Toast.makeText(Register.this, "Please fill in the form correctly", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private boolean isFormValid(String usernameText, String emailText, String passwordText, String confirmPasswordText) {
		boolean isValid = true;
		if (usernameText.length() < 4 || usernameText.length() > 20) {
			inputUsernameLayout.setError("Username must be between 4 and 20 characters");
			isValid = false;
		}
		if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
			inputEmailLayout.setError("Invalid email address");
			isValid = false;
		}
		if (passwordText.length() < 8 || passwordText.length() > 20) {
			inputPasswordLayout.setError("Password must be between 8 and 20 characters");
			isValid = false;
		}
		if (!confirmPasswordText.equals(passwordText)) {
			inputConfirmPasswordLayout.setError("Passwords do not match");
			isValid = false;
		}
		return isValid;
	}

	private void runPeriodicCheckForApi() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!apiCalls.isUserRegistered) {
					apiCalls.registerUser(inputUsername.getText().toString(), inputEmail.getText().toString(), inputPassword.getText().toString());
				} else {
					Toast.makeText(Register.this, "Registration Completed!", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(Register.this, Login.class));
					finish();
					return;
				}
				runPeriodicCheckForApi();
			}
		}, DELAY_TIME);
	}
}