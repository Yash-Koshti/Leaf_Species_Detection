package com.leaf_lore.leaf_lore_frontend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.leaf_lore.leaf_lore_frontend.researcher.ImageMapping;
import com.leaf_lore.leaf_lore_frontend.researcher.ImageUpload;

public class MainActivity extends AppCompatActivity {
	private DrawerLayout drawerLayout;
	private NavigationView navigationView;
	private ActionBarDrawerToggle drawerToggle;
	private MaterialToolbar toolbar;
	private SharedPreferences sharedPreferences;

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

//		if (!sharedPreferences.getBoolean("isLoggedIn", false)) {
		startActivity(new Intent(MainActivity.this, Login.class));
		finish();
//		}

		// Initialize the DrawerLayout and NavigationView
		drawerLayout = findViewById(R.id.drawer_layout);
		navigationView = findViewById(R.id.navigation_view);

		// Initialize the toolbar
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

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
