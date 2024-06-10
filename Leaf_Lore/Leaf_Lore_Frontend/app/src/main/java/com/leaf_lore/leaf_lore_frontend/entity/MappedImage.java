package com.leaf_lore.leaf_lore_frontend.entity;

public class MappedImage {
	private String image_name, specie_id, user_id, shape_id, apex_id, margin_id;

	public MappedImage(String image_name, String specie_id, String user_id, String shape_id, String apex_id, String margin_id) {
		this.image_name = image_name;
		this.specie_id = specie_id;
		this.user_id = user_id;
		this.shape_id = shape_id;
		this.apex_id = apex_id;
		this.margin_id = margin_id;
	}

	public String getImage_name() {
		return image_name;
	}

	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}

	public String getSpecie_id() {
		return specie_id;
	}

	public void setSpecie_id(String specie_id) {
		this.specie_id = specie_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getShape_id() {
		return shape_id;
	}

	public void setShape_id(String shape_id) {
		this.shape_id = shape_id;
	}

	public String getApex_id() {
		return apex_id;
	}

	public void setApex_id(String apex_id) {
		this.apex_id = apex_id;
	}

	public String getMargin_id() {
		return margin_id;
	}

	public void setMargin_id(String margin_id) {
		this.margin_id = margin_id;
	}
}
