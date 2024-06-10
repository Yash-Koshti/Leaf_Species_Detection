package com.leaf_lore.leaf_lore_frontend.researcher;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.leaf_lore.leaf_lore_frontend.R;

import java.util.ArrayList;

public class ImageUpload extends AppCompatActivity {
	StorageReference storageReference;
	LinearProgressIndicator totalProgressBar;
	ProgressBar imageProgress;
	Button selectImage, uploadImage;
	ArrayList<Uri> images = new ArrayList<>();
	TextView uploadImageCountText, currentUploadingImageText;
	int totalImages = 0, progressCount = 0, totalProgress = 0;
	private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
		@Override
		public void onActivityResult(ActivityResult result) {
			if (result.getResultCode() == RESULT_OK) {
				images.clear();
				if (result.getData() != null && result.getData().getClipData() != null) {
					int count = result.getData().getClipData().getItemCount();
					for (int i = 0; i < count; i++) {
						images.add(result.getData().getClipData().getItemAt(i).getUri());
					}
					if (images.size() >= 1) {
						uploadImage.setEnabled(true);
						totalImages = images.size();
						uploadImageCountText.setText("Images selected: " + totalImages);
					}
				} else {
					Toast.makeText(ImageUpload.this, "No images found!", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(ImageUpload.this, "Please select an image", Toast.LENGTH_SHORT).show();
			}
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_upload);

		FirebaseApp.initializeApp(ImageUpload.this);
		storageReference = FirebaseStorage.getInstance().getReference();

		MaterialToolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		totalProgressBar = findViewById(R.id.LProgress_UploadImage);
		imageProgress = findViewById(R.id.Progress_OfAnImage);
		selectImage = findViewById(R.id.Btn_SelectUploadImage);
		uploadImage = findViewById(R.id.Btn_UploadImage);
		uploadImageCountText = findViewById(R.id.TxtV_UploadImageCount);
		currentUploadingImageText = findViewById(R.id.TxtV_ImageProgressCount);

		selectImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
				intent.setAction(Intent.ACTION_GET_CONTENT);
				activityResultLauncher.launch(Intent.createChooser(intent, "Select Image"));
			}
		});

		uploadImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				uploadImage.setText("Uploading...");
				uploadImage.setEnabled(false);

				totalProgressBar.setMax(100);

				imageProgress.setVisibility(View.VISIBLE);
				currentUploadingImageText.setVisibility(View.VISIBLE);
				currentUploadingImageText.setText(String.valueOf(progressCount + 1));

				for (Uri image : images) {
					uploadImage(image);
					Log.d("ImageUpload", "Progress count: " + progressCount);
				}
			}
		});
	}


	private void uploadImage(Uri image) {
		Log.d("ImageUpload", "Uploading image: " + image);
		String imageName = getFileName(image);
		StorageReference reference = storageReference.child("Researchers_images/" + imageName);
		reference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
				++progressCount;
				currentUploadingImageText.setText(String.valueOf(progressCount + 1));
				if (progressCount == totalImages)
					onUploadComplete();
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Log.e("ImageUpload", "Error for: " + imageName + "\t", e);
				Toast.makeText(ImageUpload.this, "Error for: " + imageName, Toast.LENGTH_SHORT).show();
			}
		}).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
				float currentProgress = (float) (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
				totalProgress += (int) (currentProgress / totalImages);
				totalProgressBar.setProgress(totalProgress);
			}
		});
	}

	private void onUploadComplete() {
		uploadImage.setText("Upload");
		uploadImage.setEnabled(true);
		Toast.makeText(ImageUpload.this, "All images uploaded successfully", Toast.LENGTH_SHORT).show();

		imageProgress.setVisibility(View.GONE);
		currentUploadingImageText.setVisibility(View.GONE);

		Intent intent = new Intent(ImageUpload.this, ImageMapping.class);
		intent.putExtra("firebaseImageFolder", "Researchers_images");
		startActivity(intent);
		finish();
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
}