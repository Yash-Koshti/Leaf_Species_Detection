package com.leaf_lore.leaf_lore_frontend.end_user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.leaf_lore.leaf_lore_frontend.R;
import com.leaf_lore.leaf_lore_frontend.model.Prediction;

import java.util.ArrayList;

public class PredictionScreen extends AppCompatActivity {
	ImageView imageView;
	TextView predictionText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prediction_screen);

		imageView = findViewById(R.id.ImgV_PredictedImage);
		predictionText = findViewById(R.id.TxtV_PredictionResult);

		Intent intent = getIntent();
		ArrayList<Prediction> predictions = (ArrayList<Prediction>) intent.getSerializableExtra("predictions");

		Prediction prediction = predictions.get(0);
		predictionText.setText(prediction.common_name() + ": " + prediction.confidence() + "%");

		displayImage(prediction.image_path());
	}

	private void displayImage(String imagePath) {
		String imageName = imagePath.split("/")[1];

		FirebaseApp.initializeApp(this);

		FirebaseStorage.getInstance().getReference().child("Predictions").child(imageName).getDownloadUrl().addOnSuccessListener(uri -> {
			Glide.with(this).load(uri).into(imageView);
		}).addOnFailureListener(e -> {
			Toast.makeText(this, "There was an error while loading the image!", Toast.LENGTH_SHORT).show();
		});
	}
}