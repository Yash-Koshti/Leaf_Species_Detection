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

class ApiCallsSupport {
	private final Context context;

	public ApiCallsSupport(Context context) {
		this.context = context;
	}

	public void populateSpinner(ArrayList<Specie> species, Spinner spinCommonName, Spinner spinScientificName) {
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

public class ApiCalls {
	public final ArrayList<Specie> species = new ArrayList<>();
	public final ArrayList<String> imageNames = new ArrayList<>();
	private final String BASE_URL = "https://leaf-lore-server.onrender.com";
	private final String ALL_SPECIES = "/specie/all_species";
	private final String ALL_IMAGE_NAMES = "/mapped_image/all_image_names";
	private final Context context;
	private final ApiCallsSupport support;
	private final RequestQueue queue;
	private final ApiCallsConfirmer confirmer;
	private final int requestCount = 0;

	public ApiCalls(Context context, ApiCallsConfirmer confirmer) {
		this.context = context;
		this.support = new ApiCallsSupport(context);
		this.queue = Volley.newRequestQueue(context);
		this.confirmer = confirmer;
	}

	public void fetchAllSpecies(Spinner spinCommonName, Spinner spinScientificName) {
		StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + ALL_SPECIES,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						species.clear();
						try {
							JSONObject responseObject = new JSONObject(response);
							Log.d("api", "Response: " + responseObject.getInt("code") + ", " + responseObject.getString("message"));
							JSONArray result = responseObject.getJSONArray("result");
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
							confirmer.confirmAllSpeciesFetched(true);
						} catch (JSONException e) {
							Log.e("api", "onResponseError: " + e.getMessage());
							e.printStackTrace();
						}

						support.populateSpinner(species, spinCommonName, spinScientificName);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("api", "Species:\n\tErrorResponse: " + (error.getMessage() != null ? error.getMessage() : error.toString()));
						Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
					}
				});

		queue.add(stringRequest);
	}

	public void fetchAllImageNames() {
		StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + ALL_IMAGE_NAMES,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							imageNames.clear();
							JSONObject responseObject = new JSONObject(response);
							Log.d("api", "Response: " + responseObject.getInt("code") + ", " + responseObject.getString("message"));
							JSONArray result = responseObject.getJSONArray("result");
							for (int i = 0; i < result.length(); i++) {
								String imageName = result.getString(i);
								imageNames.add(imageName);
							}
							confirmer.confirmAllImageNamesFetched(true);
						} catch (JSONException e) {
							Log.e("api", "onResponseError: " + e.getMessage());
							e.printStackTrace();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("api", "ImageNames:\n\tErrorResponse: " + (error.getMessage() != null ? error.getMessage() : error.toString()));
						Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
					}
				});

		queue.add(stringRequest);
	}
}
