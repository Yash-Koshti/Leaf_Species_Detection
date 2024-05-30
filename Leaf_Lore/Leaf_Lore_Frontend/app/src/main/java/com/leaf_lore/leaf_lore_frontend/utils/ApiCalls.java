package com.leaf_lore.leaf_lore_frontend.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leaf_lore.leaf_lore_frontend.entity.MappedImage;
import com.leaf_lore.leaf_lore_frontend.model.Apex;
import com.leaf_lore.leaf_lore_frontend.model.Margin;
import com.leaf_lore.leaf_lore_frontend.model.Shape;
import com.leaf_lore.leaf_lore_frontend.model.Specie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApiCalls {
	private static final String BASE_URL = "https://leaf-lore-server.onrender.com";
	private static final String ALL_SPECIES = "/specie/all_species";
	private static final String ALL_MAPPED_IMAGE_NAMES = "/mapped_image/all_image_names";
	private static final String ALL_SHAPES = "/shape/all_shapes";
	private static final String ALL_APEXES = "/apex/all_apexes";
	private static final String ALL_MARGINS = "/margin/all_margins";
	private static final String CREATE_MAPPED_IMAGE = "/mapped_image/create_mapped_image";
	public final ArrayList<Specie> species = new ArrayList<>();
	public final ArrayList<String> imageNames = new ArrayList<>();
	public final ArrayList<Shape> shapes = new ArrayList<>();
	public final ArrayList<Apex> apexes = new ArrayList<>();
	public final ArrayList<Margin> margins = new ArrayList<>();
	private final Context context;
	private final ApiCallsSupport support;
	private final RequestQueue queue;
	private final ApiCallsConfirmer confirmer;

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
												specieObject.getString("id"),
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

						support.populateSpecieSpinners(species, spinCommonName, spinScientificName);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("api", "Species:\n\tErrorResponse: " + getErrorMessage(error));
					}
				});

		queue.add(stringRequest);
	}

	public void fetchAllMappedImageNames() {
		StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + ALL_MAPPED_IMAGE_NAMES,
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
						Log.e("api", "MappedImageNames:\n\tErrorResponse: " + getErrorMessage(error));
						if (error.networkResponse != null && error.networkResponse.statusCode == 404)
							confirmer.confirmAllImageNamesFetched(true);
					}
				});

		queue.add(stringRequest);
	}

	public void fetchAllShapes(Spinner spinShape) {
		StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + ALL_SHAPES,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							shapes.clear();
							JSONObject responseObject = new JSONObject(response);
							Log.d("api", "Response: " + responseObject.getInt("code") + ", " + responseObject.getString("message"));
							JSONArray result = responseObject.getJSONArray("result");
							for (int i = 0; i < result.length(); i++) {
								JSONObject shapeObject = result.getJSONObject(i);
								shapes.add(
										new Shape(
												shapeObject.getString("id"),
												shapeObject.getString("shape_name"),
												shapeObject.getString("created_at"),
												shapeObject.getString("updated_at")));
							}
							confirmer.confirmAllShapesFetched(true);
						} catch (JSONException e) {
							Log.e("api", "onResponseError: " + e.getMessage());
							e.printStackTrace();
						}

						support.populateShapeSpinner(shapes, spinShape);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("api", "Shapes:\n\tErrorResponse: " + getErrorMessage(error));
					}
				});

		queue.add(stringRequest);
	}

	public void fetchAllApexes(Spinner spinApex) {
		StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + ALL_APEXES,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							apexes.clear();
							JSONObject responseObject = new JSONObject(response);
							Log.d("api", "Response: " + responseObject.getInt("code") + ", " + responseObject.getString("message"));
							JSONArray result = responseObject.getJSONArray("result");
							for (int i = 0; i < result.length(); i++) {
								JSONObject apexObject = result.getJSONObject(i);
								apexes.add(
										new Apex(
												apexObject.getString("id"),
												apexObject.getString("apex_name"),
												apexObject.getString("created_at"),
												apexObject.getString("updated_at")));
							}
							confirmer.confirmAllApexesFetched(true);
						} catch (JSONException e) {
							Log.e("api", "onResponseError: " + e.getMessage());
							e.printStackTrace();
						}

						support.populateApexSpinner(apexes, spinApex);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("api", "Apexes:\n\tErrorResponse: " + getErrorMessage(error));
					}
				});

		queue.add(stringRequest);
	}

	public void fetchAllMargins(Spinner spinMargin) {
		StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + ALL_MARGINS,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							margins.clear();
							JSONObject responseObject = new JSONObject(response);
							Log.d("api", "Response: " + responseObject.getInt("code") + ", " + responseObject.getString("message"));
							JSONArray result = responseObject.getJSONArray("result");
							for (int i = 0; i < result.length(); i++) {
								JSONObject marginObject = result.getJSONObject(i);
								margins.add(
										new Margin(
												marginObject.getString("id"),
												marginObject.getString("margin_name"),
												marginObject.getString("created_at"),
												marginObject.getString("updated_at")));
							}
							confirmer.confirmAllMarginsFetched(true);
						} catch (JSONException e) {
							Log.e("api", "onResponseError: " + e.getMessage());
							e.printStackTrace();
						}

						support.populateMarginSpinner(margins, spinMargin);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("api", "Margins:\n\tErrorResponse: " + getErrorMessage(error));
					}
				});

		queue.add(stringRequest);
	}

	public void sendAllMappedImages(ArrayList<MappedImage> mappedImages) {
		for (MappedImage mappedImage : mappedImages) {
			JSONObject params = new JSONObject();
			JSONObject jsonBody = new JSONObject();

			try {
				params.put("image_name", mappedImage.getImage_name());
				params.put("specie_id", mappedImage.getSpecie_id());
				params.put("user_id", mappedImage.getUser_id());
				params.put("shape_id", mappedImage.getShape_id());
				params.put("apex_id", mappedImage.getApex_id());
				params.put("margin_id", mappedImage.getMargin_id());

				jsonBody.put("params", params);
			} catch (JSONException e) {
				Log.e("api", "sendAllMappedImagesError: " + e.getMessage());
				e.printStackTrace();
			}
			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + CREATE_MAPPED_IMAGE, jsonBody,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(@Nullable JSONObject response) {
							try {
								Log.d("api", "Response: " + response.getInt("code") + ", " + response.getString("message"));
//								confirmer.confirmMappedImageCreated(true);
							} catch (JSONException e) {
								Log.e("api", "onResponseError: " + e.getMessage());
								e.printStackTrace();
							}
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e("api", "MappedImage:\n\tErrorResponse: " + getErrorMessage(error));
						}
					});

			queue.add(jsonObjectRequest);
		}
	}

	private String getErrorMessage(VolleyError error) {
		String errorMessage = "";
		if (error.networkResponse == null) {
			errorMessage = error.getMessage() != null ? error.getMessage() : error.toString();
		} else {
			try {
				JSONObject errorObject = new JSONObject(new String(error.networkResponse.data));
				errorMessage = errorObject.getString("detail");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
		return errorMessage;
	}
}
