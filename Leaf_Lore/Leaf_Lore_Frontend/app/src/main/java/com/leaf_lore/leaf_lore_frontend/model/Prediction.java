package com.leaf_lore.leaf_lore_frontend.model;

import java.io.Serializable;

public record Prediction(String image_path, int class_number, String common_name,
                         String scientific_name, int confidence) implements Serializable {
}
