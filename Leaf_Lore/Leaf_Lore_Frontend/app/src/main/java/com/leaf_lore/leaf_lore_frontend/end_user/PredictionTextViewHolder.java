package com.leaf_lore.leaf_lore_frontend.end_user;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf_lore.leaf_lore_frontend.R;

public class PredictionTextViewHolder extends RecyclerView.ViewHolder {
	TextView predictedCommonName, predictedScientificName, predictionConfidence;

	public PredictionTextViewHolder(@NonNull View itemView) {
		super(itemView);
		predictedCommonName = itemView.findViewById(R.id.TxtV_PredictedCommonName);
		predictedScientificName = itemView.findViewById(R.id.TxtV_PredictedScientificName);
		predictionConfidence = itemView.findViewById(R.id.TxtV_PredictedConfidence);
	}
}
