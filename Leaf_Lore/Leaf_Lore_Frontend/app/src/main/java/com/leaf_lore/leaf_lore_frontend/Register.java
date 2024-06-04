package com.leaf_lore.leaf_lore_frontend;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.leaf_lore.leaf_lore_frontend.utils.ApiCalls;

public class Register extends AppCompatActivity {
	private final int DELAY_TIME = 10000;
	private TextInputLayout inputUsernameLayout, inputEmailLayout, inputPasswordLayout, inputConfirmPasswordLayout;
	private EditText inputUsername, inputEmail, inputPassword, inputConfirmPassword;
	private Button submitRegister;
	private ApiCalls apiCalls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}
}