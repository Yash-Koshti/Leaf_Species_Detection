package com.leaf_lore.leaf_lore_frontend.model;

public record Prediction(String image_path, int class_number, String common_name,
                         String scientific_name,
                         int confidence) {
	public Prediction {
		if (class_number < 0) {
			throw new IllegalArgumentException("class_number must be non-negative");
		}
		if (confidence < 0 || confidence > 100) {
			throw new IllegalArgumentException("confidence must be between 0 and 100");
		}
	}
}
