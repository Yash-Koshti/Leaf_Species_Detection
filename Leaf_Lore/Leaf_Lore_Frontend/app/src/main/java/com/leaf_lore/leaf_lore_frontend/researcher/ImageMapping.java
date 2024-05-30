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
import androidx.appcompat.app.AlertDialog;
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
import com.leaf_lore.leaf_lore_frontend.MainActivity;
import com.leaf_lore.leaf_lore_frontend.R;
import com.leaf_lore.leaf_lore_frontend.entity.MappedImage;
import com.leaf_lore.leaf_lore_frontend.model.Image;
import com.leaf_lore.leaf_lore_frontend.utils.ApiCalls;
import com.leaf_lore.leaf_lore_frontend.utils.ApiCallsConfirmer;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ImageMapping extends AppCompatActivity {
	private int DELAY_TIME = 10000;
	private Spinner spinCommonName, spinScientificName, spinShape, spinApex, spinMargin;
	private ProgressBar progressBar;
	private ConstraintLayout mapImageLayout;
	private TextView loadingText, currentImageText, mappedImagesCountText;
	private ImageView actionImage;
	private Button btnPrev, btnNext, btnSubmit;
	private ArrayList<Image> images;
	private ArrayList<MappedImage> mappedImages;
	private ApiCalls apiCalls;
	private int totalFetchedImages = -1, imagesCount = 0, currentImageIndex = 0;
	private boolean isAllSpeciesFetched = false;
	private boolean isAllMappedImageNamesFetched = false;
	private boolean isAllShapesFetched = false;
	private boolean isAllApexesFetched = false;
	private boolean isAllMarginsFetched = false;
	private boolean runPeriodicCheck = true;

	private String firebaseImageFolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_mapping);

		try {
			firebaseImageFolder = getIntent().getStringExtra("firebaseImageFolder");
		} catch (Exception e) {
			Toast.makeText(this, "Source image folder not found!", Toast.LENGTH_SHORT).show();
			finish();
			startActivity(new Intent(this, MainActivity.class));
		}

		// Setting up the API calls
		apiCalls = new ApiCalls(this, new ApiCallsConfirmer() {
			@Override
			public void confirmAllSpeciesFetched(boolean confirm) {
				isAllSpeciesFetched = confirm;
			}

			@Override
			public void confirmAllImageNamesFetched(boolean confirm) {
				isAllMappedImageNamesFetched = confirm;
			}

			@Override
			public void confirmAllShapesFetched(boolean confirm) {
				isAllShapesFetched = confirm;
			}

			@Override
			public void confirmAllApexesFetched(boolean confirm) {
				isAllApexesFetched = confirm;
			}

			@Override
			public void confirmAllMarginsFetched(boolean confirm) {
				isAllMarginsFetched = confirm;
			}
		});

		// Setting up the UI components
		spinCommonName = findViewById(R.id.spinCommonName);
		spinScientificName = findViewById(R.id.spinScientificName);
		progressBar = findViewById(R.id.progressBarForMapImage);
		mapImageLayout = findViewById(R.id.MapImageLayout);
		loadingText = findViewById(R.id.TxtV_Loading);
		actionImage = findViewById(R.id.imgVAction);
		btnPrev = findViewById(R.id.Btn_PreviousImage);
		btnNext = findViewById(R.id.Btn_NextImage);
		spinShape = findViewById(R.id.spinShape);
		spinApex = findViewById(R.id.spinApex);
		spinMargin = findViewById(R.id.spinMargin);
		btnSubmit = findViewById(R.id.Btn_SubmitMappedImage);
		currentImageText = findViewById(R.id.TxtV_CurrentImageIndex);
		mappedImagesCountText = findViewById(R.id.TxtV_MappedImagesCount);

		// Initializing the images array
		images = new ArrayList<>();
		mappedImages = new ArrayList<>();

		// Setting up the spinner listeners to show the selected item
		spinCommonName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String item = parent.getItemAtPosition(position).toString();
				if (!item.equals("Select Common name")) {
					Toast.makeText(ImageMapping.this, item, Toast.LENGTH_SHORT).show();
				}
				spinScientificName.setSelection(position);
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
				}
				spinCommonName.setSelection(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		spinShape.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String item = parent.getItemAtPosition(position).toString();
				if (!item.equals("Select Shape")) {
					Toast.makeText(ImageMapping.this, item, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		spinApex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String item = parent.getItemAtPosition(position).toString();
				if (!item.equals("Select Apex")) {
					Toast.makeText(ImageMapping.this, item, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		spinMargin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String item = parent.getItemAtPosition(position).toString();
				if (!item.equals("Select Margin")) {
					Toast.makeText(ImageMapping.this, item, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// Calling all APIs manually
		apiCalls.fetchAllMappedImageNames();
		apiCalls.fetchAllSpecies(spinCommonName, spinScientificName);
		apiCalls.fetchAllShapes(spinShape);
		apiCalls.fetchAllApexes(spinApex);
		apiCalls.fetchAllMargins(spinMargin);
		fetchAllImagesFromFirebase();

//		"Black_Background"
	}

	private void fetchAllImagesFromFirebase() {
		FirebaseApp.initializeApp(this);

		FirebaseStorage.getInstance().getReference().child(firebaseImageFolder).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
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
				loadingText.setText("Fetching Mapped Images...");
				if (isAllMappedImageNamesFetched) {
					DELAY_TIME = 3000;
					loadingText.setText("Loading Species...");
					if (isAllSpeciesFetched) {
						loadingText.setText("Loading Shapes...");
						if (isAllShapesFetched) {
							loadingText.setText("Loading Apexes...");
							if (isAllApexesFetched) {
								loadingText.setText("Loading Margins...");
								if (isAllMarginsFetched) {
									DELAY_TIME = 1000;
									loadingText.setText("Loading images (" + ((imagesCount * 100) / totalFetchedImages) + "%)");
									if (imagesCount == totalFetchedImages) {
										makeContainerVisible();
										runPeriodicCheck = false;
									} else {
										runPeriodicCheckForApi();
									}
								} else {
									apiCalls.fetchAllMargins(spinMargin);
									runPeriodicCheckForApi();
								}
							} else {
								apiCalls.fetchAllApexes(spinApex);
								runPeriodicCheckForApi();
							}
						} else {
							apiCalls.fetchAllShapes(spinShape);
							runPeriodicCheckForApi();
						}
					} else {
						apiCalls.fetchAllSpecies(spinCommonName, spinScientificName);
						runPeriodicCheckForApi();
					}
				} else {
					apiCalls.fetchAllMappedImageNames();
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
		mapImageLayout.setVisibility(View.VISIBLE);
		showImageOnAction();
	}

	private void showImageOnAction() {
		if (images.size() > 0) {
			currentImageText.setText("Image " + (currentImageIndex + 1) + " of " + images.size());
			showImage(images.get(currentImageIndex).url());
			btnNext.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (isFormValid()) {
						addCurrentMappedImage();

						if (currentImageIndex < images.size() - 1) {
							currentImageIndex++;
							if (currentImageIndex < mappedImages.size())
								refillForm();
							else
								resetForm();
							showImage(images.get(currentImageIndex).url());
							handleButtonEnabling();
						}

						currentImageText.setText("Image " + (currentImageIndex + 1) + " of " + images.size());
					} else {
						Toast.makeText(ImageMapping.this, "Verify every field is selected!", Toast.LENGTH_SHORT).show();
					}
				}
			});
			btnPrev.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (currentImageIndex > 0) {
						if (isFormValid())
							addCurrentMappedImage();

						currentImageIndex--;
						showImage(images.get(currentImageIndex).url());
						handleButtonEnabling();
						refillForm();

						currentImageText.setText("Image " + (currentImageIndex + 1) + " of " + images.size());
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

	private void handleButtonEnabling() {
		btnPrev.setEnabled(currentImageIndex != 0);
		btnNext.setEnabled(currentImageIndex != images.size() - 1);
	}

	private boolean isFormValid() {
		return spinCommonName.getSelectedItemPosition() != 0 &&
				       spinScientificName.getSelectedItemPosition() != 0 &&
				       spinShape.getSelectedItemPosition() != 0 &&
				       spinApex.getSelectedItemPosition() != 0 &&
				       spinMargin.getSelectedItemPosition() != 0;
	}

	private void addCurrentMappedImage() {
		MappedImage currentMappedImage = new MappedImage(
				images.get(currentImageIndex).name(),
				apiCalls.species.get(spinCommonName.getSelectedItemPosition()).id(),
				"b1d70b20-f508-4222-9872-4022f6f74839", // This should be the user id of the logged in user
				apiCalls.shapes.get(spinShape.getSelectedItemPosition()).id(),
				apiCalls.apexes.get(spinApex.getSelectedItemPosition()).id(),
				apiCalls.margins.get(spinMargin.getSelectedItemPosition()).id());

		if (mappedImages.size() > currentImageIndex)
			mappedImages.remove(currentImageIndex);
		mappedImages.add(currentImageIndex, currentMappedImage);

		Log.d("api", "Mapped Images: " + mappedImages.size());
		mappedImages.forEach(mappedImage -> {
			Log.d("api", "Mapped Image: " + mappedImage.getImage_name());
		});

		mappedImagesCountText.setText("Images Mapped: " + mappedImages.size());
	}

	private void resetForm() {
		spinCommonName.setSelection(0);
		spinScientificName.setSelection(0);
		spinShape.setSelection(0);
		spinApex.setSelection(0);
		spinMargin.setSelection(0);
	}

	private void refillForm() {
		apiCalls.species.forEach(specie -> {
			if (specie.id().equals(mappedImages.get(currentImageIndex).getSpecie_id())) {
				spinCommonName.setSelection(apiCalls.species.indexOf(specie));
				spinScientificName.setSelection(apiCalls.species.indexOf(specie));
			}
		});
		apiCalls.shapes.forEach(shape -> {
			if (shape.id().equals(mappedImages.get(currentImageIndex).getShape_id())) {
				spinShape.setSelection(apiCalls.shapes.indexOf(shape));
			}
		});
		apiCalls.apexes.forEach(apex -> {
			if (apex.id().equals(mappedImages.get(currentImageIndex).getApex_id())) {
				spinApex.setSelection(apiCalls.apexes.indexOf(apex));
			}
		});
		apiCalls.margins.forEach(margin -> {
			if (margin.id().equals(mappedImages.get(currentImageIndex).getMargin_id())) {
				spinMargin.setSelection(apiCalls.margins.indexOf(margin));
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Running a periodic check for the APIs
		if (runPeriodicCheck)
			runPeriodicCheckForApi();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		btnSubmit.setOnClickListener(v -> {
			builder.setMessage("Are you sure you want to submit " + mappedImages.size() + " mapped images?")
					.setCancelable(false)
					.setPositiveButton("Yes", (dialog, id) -> {
						if (mappedImages.size() > 0) {
							apiCalls.sendAllMappedImages(mappedImages);
							mappedImages.forEach(mappedImage -> {
								Log.d("api", "Mapped Image: " + mappedImage.getImage_name() + ", " + mappedImage.getSpecie_id() + ", " + mappedImage.getUser_id() + ", " + mappedImage.getShape_id() + ", " + mappedImage.getApex_id() + ", " + mappedImage.getMargin_id());
							});
							dialog.dismiss();
							finish();
						} else
							Toast.makeText(ImageMapping.this, "No images to submit!", Toast.LENGTH_SHORT).show();
					})
					.setNegativeButton("No", (dialog, id) -> dialog.dismiss())
					.show();
		});
	}
}
