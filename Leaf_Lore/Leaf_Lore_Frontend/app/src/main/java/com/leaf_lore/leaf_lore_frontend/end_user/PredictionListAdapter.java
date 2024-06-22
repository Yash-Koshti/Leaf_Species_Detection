package com.leaf_lore.leaf_lore_frontend.end_user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf_lore.leaf_lore_frontend.R;
import com.leaf_lore.leaf_lore_frontend.model.Prediction;

import java.util.ArrayList;

public class PredictionListAdapter extends RecyclerView.Adapter<PredictionTextViewHolder> {
	Context context;
	ArrayList<Prediction> predictions;

	public PredictionListAdapter(Context context, ArrayList<Prediction> predictions) {
		this.context = context;
		this.predictions = predictions;
	}

	@NonNull
	@Override
	public PredictionTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new PredictionTextViewHolder(LayoutInflater.from(context).inflate(R.layout.prediction_text_view, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull PredictionTextViewHolder holder, int position) {
		Prediction prediction = predictions.get(position);
		holder.predictedCommonName.setText("Common Name: " + prediction.common_name());
		holder.predictedScientificName.setText("Scientific Name: " + prediction.scientific_name());
		holder.predictionConfidence.setText("Confidence: " + prediction.confidence() + "%");
	}

	@Override
	public int getItemCount() {
		return predictions.size();
	}
}
