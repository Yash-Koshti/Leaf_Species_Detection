package com.leaf_lore.leaf_lore_frontend.models;

public record MappedImage(
    int id, String image_name, int specie_id, int user_id, String created_at, String updated_at) {}
