package com.leaf_lore.leaf_lore_frontend.researcher;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leaf_lore.leaf_lore_frontend.R;
import com.leaf_lore.leaf_lore_frontend.utils.ApiCalls;

public class ImageMapping extends AppCompatActivity {
	private Spinner spinCommonName, spinScientificName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_mapping);

		spinCommonName = findViewById(R.id.spinCommonName);
		spinScientificName = findViewById(R.id.spinScientificName);

		spinCommonName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String item = parent.getItemAtPosition(position).toString();
				if (!item.equals("Select Common name"))
					Toast.makeText(ImageMapping.this, item, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		spinScientificName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String item = parent.getItemAtPosition(position).toString();
				if (!item.equals("Select Scientific name"))
					Toast.makeText(ImageMapping.this, item, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		ApiCalls.fetchAllSpecies(this, spinCommonName, spinScientificName);
	}
}
