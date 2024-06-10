package com.leaf_lore.leaf_lore_frontend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.leaf_lore.leaf_lore_frontend.end_user.PredictionHistory;
import com.leaf_lore.leaf_lore_frontend.end_user.SearchLeaf;
import com.leaf_lore.leaf_lore_frontend.model.Role;
import com.leaf_lore.leaf_lore_frontend.researcher.ImageMapping;
import com.leaf_lore.leaf_lore_frontend.researcher.ImageUpload;

public class MainActivity extends AppCompatActivity {
	private DrawerLayout drawerLayout;
	private NavigationView navigationView;
	private ActionBarDrawerToggle drawerToggle;
	private MaterialToolbar toolbar;
	private SharedPreferences sharedPreferences;
	private TextView loggedInUsername, loggedInEmail, loggedInRole;

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) return true;
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sharedPreferences = getSharedPreferences("com.leaf_lore.leaf_lore_frontend", MODE_PRIVATE);

		boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
		long expiryTime = sharedPreferences.getLong("expiry_time", System.currentTimeMillis());

		// If the user is not logged in or the token has expired, redirect to the login page
		if (!isLoggedIn || expiryTime <= System.currentTimeMillis()) {
			startActivity(new Intent(MainActivity.this, Login.class));
			finish();
		}

		// Initialize the DrawerLayout and NavigationView
		drawerLayout = findViewById(R.id.drawer_layout);
		navigationView = findViewById(R.id.navigation_view);

		// Initialize the toolbar
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		String role = sharedPreferences.getString("role", Role.END_USER.toString().toLowerCase());
		if (role.equals(Role.ADMIN.toString().toLowerCase())) {
//			navigationView.inflateMenu(R.menu.admin_menu);
			navigationView.inflateMenu(R.menu.researcher_main_menu);

			loggedInRole = navigationView.getHeaderView(0).findViewById(R.id.TxtV_LoggedInRole);
			loggedInRole.setText(sharedPreferences.getString("role", "Your Role"));
		} else if (role.equals(Role.RESEARCHER.toString().toLowerCase())) {
			navigationView.inflateMenu(R.menu.researcher_main_menu);

			loggedInRole = navigationView.getHeaderView(0).findViewById(R.id.TxtV_LoggedInRole);
			loggedInRole.setText(sharedPreferences.getString("role", "Your Role"));
		} else {
			navigationView.inflateMenu(R.menu.end_user_main_menu);
		}

		loggedInUsername = navigationView.getHeaderView(0).findViewById(R.id.TxtV_LoggedInUsername);
		loggedInEmail = navigationView.getHeaderView(0).findViewById(R.id.TxtV_LoggedInEmail);

		loggedInUsername.setText(sharedPreferences.getString("username", "Your Name"));
		loggedInEmail.setText(sharedPreferences.getString("email", "Your Email"));

		if (drawerLayout != null) {
			drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
			drawerLayout.addDrawerListener(drawerToggle);
			drawerToggle.syncState();
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

			navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(@NonNull MenuItem item) {
					if (item.getItemId() == R.id.action_map_image) {
						Intent intent = new Intent(MainActivity.this, ImageMapping.class);
						intent.putExtra("firebaseImageFolder", "Black_Background");
						startActivity(intent);
					} else if (item.getItemId() == R.id.action_upload_image) {
						startActivity(new Intent(MainActivity.this, ImageUpload.class));
					} else if (item.getItemId() == R.id.action_search_leaf) {
						startActivity(new Intent(MainActivity.this, SearchLeaf.class));
					} else if (item.getItemId() == R.id.action_history) {
						startActivity(new Intent(MainActivity.this, PredictionHistory.class));
					} else if (item.getItemId() == R.id.action_logout) {
						sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();
						sharedPreferences.edit().putString("token", "").apply();
						sharedPreferences.edit().putString("user_id", "").apply();
						startActivity(new Intent(MainActivity.this, Login.class));
						finish();
					}
					drawerLayout.closeDrawer(GravityCompat.START);
					return true;
				}
			});
		} else {
			Log.e("MainActivity", "DrawerLayout is null");
		}
	}

	@NonNull
	@Override
	public OnBackInvokedDispatcher getOnBackInvokedDispatcher() {
		if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
			drawerLayout.closeDrawer(GravityCompat.START);
		}
		return super.getOnBackInvokedDispatcher();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Close the drawer if it is open
		if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
			drawerLayout.closeDrawer(GravityCompat.START);
		}
	}
}
