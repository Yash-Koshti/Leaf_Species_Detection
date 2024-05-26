package com.leaf_lore.leaf_lore_frontend.model;

public record User(
		String id,
		String name,
		String email,
		String password,
		Role role,
		String created_at,
		String updated_at) {}
