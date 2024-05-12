package com.leaf_lore.leaf_lore_frontend.researcher;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
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
import com.leaf_lore.leaf_lore_frontend.utils.ApiCallsConfirmer;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ImageMapping extends AppCompatActivity {
	private final int DELAY_TIME = 5000;
	private Spinner spinCommonName, spinScientificName;
	private ProgressBar progressBar;
	private ConstraintLayout mapImageContainer;
	private TextView loadingText;
	private ImageView actionImage;
	private Button btnPrev, btnNext;
	private ArrayList<Image> images;
	private ApiCalls apiCalls;
	private int totalFetchedImages = -1, imagesCount = 0, currentImageIndex = 0;
	private boolean isAllSpeciesFetched = false, isAllImageNamesFetched = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_mapping);

		// Setting up the API calls
		apiCalls = new ApiCalls(this, new ApiCallsConfirmer() {
			@Override
			public void confirmAllSpeciesFetched(Boolean confirm) {
				if (confirm) {
					isAllSpeciesFetched = true;
				}
			}

			@Override
			public void confirmAllImageNamesFetched(Boolean confirm) {
				if (confirm) {
					isAllImageNamesFetched = true;
				}
			}
		});

		// Setting up the UI components
		spinCommonName = findViewById(R.id.spinCommonName);
		spinScientificName = findViewById(R.id.spinScientificName);
		progressBar = findViewById(R.id.progressBarForMapImage);
		mapImageContainer = findViewById(R.id.MapImageLayout);
		loadingText = findViewById(R.id.TxtV_Loading);
		actionImage = findViewById(R.id.imgVAction);
		btnPrev = findViewById(R.id.Btn_PreviousImage);
		btnNext = findViewById(R.id.Btn_NextImage);

		// Initializing the images array
		images = new ArrayList<>();

		// Setting up the spinner listeners to show the selected item
		spinCommonName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String item = parent.getItemAtPosition(position).toString();
				if (!item.equals("Select Common name")) {
					Toast.makeText(ImageMapping.this, item, Toast.LENGTH_SHORT).show();
					spinScientificName.setSelection(position);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		spinScientificName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String item = parent.getItemAtPosition(position).toString();
				if (!item.equals("Select Scientific name")) {
					Toast.makeText(ImageMapping.this, item, Toast.LENGTH_SHORT).show();
					spinCommonName.setSelection(position);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// Calling all APIs manually
		apiCalls.fetchAllSpecies(spinCommonName, spinScientificName);
		apiCalls.fetchAllImageNames();
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
								if (!apiCalls.imageNames.contains(storageReference.getName())) {
									String url = "https://" + task.getResult().getEncodedAuthority() +
											             task.getResult().getEncodedPath() + "?alt=media&token=" +
											             task.getResult().getQueryParameters("token").get(0);
									images.add(new Image(storageReference.getName(), url));
								}
								imagesCount++;
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

	private void runPeriodicCheckForApi() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (isAllImageNamesFetched) {
					if (isAllSpeciesFetched) {
						if (imagesCount == totalFetchedImages) {
							makeContainerVisible();
						} else {
							runPeriodicCheckForApi();
						}
					} else {
						apiCalls.fetchAllSpecies(spinCommonName, spinScientificName);
						runPeriodicCheckForApi();
					}
				} else {
					apiCalls.fetchAllImageNames();
					runPeriodicCheckForApi();
				}
			}
		}, DELAY_TIME);
	}

	private void makeContainerVisible() {
		Log.d("api", "Images in Array: " + images.size());
		Toast.makeText(ImageMapping.this, "Images Loaded!", Toast.LENGTH_SHORT).show();
		progressBar.setVisibility(View.GONE);
		loadingText.setVisibility(View.GONE);
		mapImageContainer.setVisibility(View.VISIBLE);
		showImageOnAction();
	}

	private void showImageOnAction() {
		if (images.size() > 0) {
			showImage(images.get(currentImageIndex).url());
			btnNext.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (currentImageIndex < images.size() - 1) {
						currentImageIndex++;
						showImage(images.get(currentImageIndex).url());
					}
				}
			});
			btnPrev.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (currentImageIndex > 0) {
						currentImageIndex--;
						showImage(images.get(currentImageIndex).url());
					}
				}
			});
		}
	}

	private void showImage(String url) {
		Glide.with(this).load(url).into(actionImage);
		actionImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse(url), "image/*");
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Running a periodic check for the APIs
		runPeriodicCheckForApi();
	}
}
