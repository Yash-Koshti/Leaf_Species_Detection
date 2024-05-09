package com.leaf_lore.leaf_lore_frontend.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leaf_lore.leaf_lore_frontend.models.Specie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApiCalls {
	private static final String BASE_URL = "https://leaf-lore-server.onrender.com";
	private static final String ALL_SPECIES = "/specie/all_species";

	private static ArrayList<Specie> species;

	public static void fetchAllSpecies(Context context, Spinner spinCommonName, Spinner spinScientificName) {
		RequestQueue queue = Volley.newRequestQueue(context);

		StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + ALL_SPECIES,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						species = new ArrayList<>();
						try {
							Log.d("api", "Response: " + response);
							JSONObject responseObject = new JSONObject(response);
							JSONArray result = responseObject.getJSONArray("result");
							Log.d("api", "Result: " + result);
							for (int i = 0; i < result.length(); i++) {
								JSONObject specieObject = result.getJSONObject(i);
								species.add(
										new Specie(
												specieObject.getInt("id"),
												specieObject.getInt("class_number"),
												specieObject.getString("common_name"),
												specieObject.getString("scientific_name"),
												specieObject.getString("created_at"),
												specieObject.getString("updated_at")));
							}
						} catch (JSONException e) {
							Log.e("api", "onResponseError: " + e.getMessage());
							e.printStackTrace();
						}

						populateSpinner(context, spinCommonName, spinScientificName);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
					}
				});

		queue.add(stringRequest);
	}

	public static void populateSpinner(Context context, Spinner spinCommonName, Spinner spinScientificName) {
		ArrayList<String> specieCommonNames = new ArrayList<>();
		ArrayList<String> specieScientificNames = new ArrayList<>();

		specieCommonNames.add("Select Common name");
		specieScientificNames.add("Select Scientific name");

		for (Specie specie : species) {
			specieCommonNames.add(specie.common_name());
			specieScientificNames.add(specie.scientific_name());
		}

		ArrayAdapter<String> commonNameAdapter =
				new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, specieCommonNames);
		commonNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<String> scientificNameAdapter =
				new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, specieScientificNames);

		spinCommonName.setAdapter(commonNameAdapter);
		spinScientificName.setAdapter(scientificNameAdapter);
	}
}
