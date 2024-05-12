package com.leaf_lore.leaf_lore_frontend.researcher;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.leaf_lore.leaf_lore_frontend.R;
import com.leaf_lore.leaf_lore_frontend.models.Image;
import com.leaf_lore.leaf_lore_frontend.utils.ApiCalls;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ImageMapping extends AppCompatActivity {
	private Spinner spinCommonName, spinScientificName;
	private ProgressBar progressBar;
	private ConstraintLayout mapImageContainer;
	private ArrayList<Image> images;
	private int totalFetchedImages = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_mapping);

		spinCommonName = findViewById(R.id.spinCommonName);
		spinScientificName = findViewById(R.id.spinScientificName);
		progressBar = findViewById(R.id.progressBarForMapImage);
		mapImageContainer = findViewById(R.id.MapImageContainer);

		images = new ArrayList<>();

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
		fetchAllImagesFromFirebase();
	}

	private void fetchAllImagesFromFirebase() {
		FirebaseApp.initializeApp(this);

		FirebaseStorage.getInstance().getReference().child("Black_Background").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
			@Override
			public void onSuccess(ListResult listResult) {
				totalFetchedImages = listResult.getItems().size();
				Log.d("api", "listResult Images: " + listResult.getItems().size());
				listResult.getItems().forEach(new Consumer<StorageReference>() {
					@Override
					public void accept(StorageReference storageReference) {
						storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
							@Override
							public void onComplete(@NonNull Task<Uri> task) {
								String url = "https://" + task.getResult().getEncodedAuthority() +
										             task.getResult().getEncodedPath() + "?alt=media&token=" +
										             task.getResult().getQueryParameters("token").get(0);
								images.add(new Image(storageReference.getName(), url));

								if (images.size() == totalFetchedImages) {
									makeContainerVisible();
								}
							}
						});
					}
				});
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Toast.makeText(ImageMapping.this, "There was an error while getting images!", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void makeContainerVisible() {
		Log.d("api", "Images Array: " + images.size());
		Toast.makeText(ImageMapping.this, "Images Loaded!", Toast.LENGTH_SHORT).show();
		progressBar.setVisibility(View.GONE);
		mapImageContainer.setVisibility(View.VISIBLE);
	}
}
