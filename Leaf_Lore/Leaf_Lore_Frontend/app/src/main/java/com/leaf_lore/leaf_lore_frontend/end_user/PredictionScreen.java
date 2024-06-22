package com.leaf_lore.leaf_lore_frontend.end_user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.leaf_lore.leaf_lore_frontend.R;
import com.leaf_lore.leaf_lore_frontend.model.Prediction;

import java.util.ArrayList;

public class PredictionScreen extends AppCompatActivity {
	ImageView imageView;
	TextView predictionText;
	RecyclerView predictionList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prediction_screen);

		imageView = findViewById(R.id.ImgV_PredictedImage);
		predictionList = findViewById(R.id.RV_PredictionList);

		Intent intent = getIntent();

		ArrayList<Prediction> predictions = (ArrayList<Prediction>) intent.getSerializableExtra("predictions");
		Log.d("api", "Predictions size: " + predictions.size());

		PredictionListAdapter predictionListAdapter = new PredictionListAdapter(this, predictions);
		predictionList.setLayoutManager(new LinearLayoutManager(this));
		predictionList.setAdapter(predictionListAdapter);

		displayImage(predictions.get(0).image_path());
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