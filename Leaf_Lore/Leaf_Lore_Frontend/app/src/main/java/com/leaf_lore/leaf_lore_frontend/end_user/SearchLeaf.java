package com.leaf_lore.leaf_lore_frontend.end_user;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.leaf_lore.leaf_lore_frontend.R;
import com.leaf_lore.leaf_lore_frontend.utils.ApiCalls;

public class SearchLeaf extends AppCompatActivity {
	private final int DELAY_TIME = 1000;
	StorageReference storageReference;
	ProgressBar progressBar, searchingProgressBar;
	ImageView imageView;
	Button selectImage, searchImage;
	Uri image;
	private String pathString;
	private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
		@Override
		public void onActivityResult(ActivityResult result) {
			if (result.getResultCode() == RESULT_OK) {
				if (result.getData() != null) {
					image = result.getData().getData();
					searchImage.setEnabled(true);
					Glide.with(getApplicationContext()).load(image).into(imageView);
				}
			} else {
				Toast.makeText(SearchLeaf.this, "Please select an image", Toast.LENGTH_SHORT).show();
			}
		}
	});
	ApiCalls apiCalls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_leaf);

		FirebaseApp.initializeApp(SearchLeaf.this);
		storageReference = FirebaseStorage.getInstance().getReference();

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		apiCalls = new ApiCalls(this);

		searchingProgressBar = findViewById(R.id.PB_SearchingImage);
		progressBar = findViewById(R.id.LProgress_UploadImage);
		imageView = findViewById(R.id.ImgV_PredictedImage);
		selectImage = findViewById(R.id.Btn_SelectSearchImage);
		searchImage = findViewById(R.id.Btn_UploadSearchImage);

		selectImage.setOnClickListener(v -> {
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			activityResultLauncher.launch(intent);
		});

		searchImage.setOnClickListener(v -> {
			searchingProgressBar.setVisibility(ProgressBar.VISIBLE);
			uploadImage(image);
		});
	}

	private void uploadImage(Uri image) {
		pathString = "Users_images/" + getFileName(image);
		StorageReference reference = storageReference.child(pathString);
		reference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
				searchingProgressBar.setVisibility(ProgressBar.INVISIBLE);
				Toast.makeText(SearchLeaf.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
				apiCalls.fetchModelPrediction(pathString);
				runPeriodicCheckForApi();
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Toast.makeText(SearchLeaf.this, "There was an error while uploading image", Toast.LENGTH_SHORT).show();
			}
		}).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
				progressBar.setMax(Math.toIntExact(snapshot.getTotalByteCount()));
				progressBar.setProgress(Math.toIntExact(snapshot.getBytesTransferred()));
			}
		});
	}

	public String getFileName(Uri uri) {
		String result = null;
		if (uri.getScheme().equals("content")) {
			Cursor cursor = getContentResolver().query(uri, null, null, null, null);
			try {
				if (cursor != null && cursor.moveToFirst()) {
					int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
					result = cursor.getString(columnIndex);
				}
			} finally {
				cursor.close();
			}
		}
		return result;
	}

	private void runPeriodicCheckForApi() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!apiCalls.isModelPredictionFetched) {
//					apiCalls.fetchModelPrediction(pathString);
					runPeriodicCheckForApi();
				} else {
					if (apiCalls.predictions.isEmpty()) {
						Toast.makeText(SearchLeaf.this, "Couldn't identify this leaf!", Toast.LENGTH_SHORT).show();
						return;
					}
					Intent intent = new Intent(SearchLeaf.this, PredictionScreen.class);
//					intent.putExtra("image_path", apiCalls.predictions.get(0).image_path());
//					intent.putExtra("class_number", apiCalls.predictions.get(0).class_number());
//					intent.putExtra("common_name", apiCalls.predictions.get(0).common_name());
//					intent.putExtra("scientific_name", apiCalls.predictions.get(0).scientific_name());
//					intent.putExtra("confidence", apiCalls.predictions.get(0).confidence());

					intent.putExtra("predictions", apiCalls.predictions);

					startActivity(intent);
					finish();
				}
			}
		}, DELAY_TIME);
	}
}