package com.leaf_lore.leaf_lore_frontend.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.leaf_lore.leaf_lore_frontend.model.Apex;
import com.leaf_lore.leaf_lore_frontend.model.Margin;
import com.leaf_lore.leaf_lore_frontend.model.Shape;
import com.leaf_lore.leaf_lore_frontend.model.Specie;

import java.util.ArrayList;

public class ApiCallsSupport {
	private final Context context;

	public ApiCallsSupport(Context context) {
		this.context = context;
	}

	public void populateSpecieSpinners(ArrayList<Specie> species, Spinner spinCommonName, Spinner spinScientificName) {
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
		scientificNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinCommonName.setAdapter(commonNameAdapter);
		spinScientificName.setAdapter(scientificNameAdapter);
	}

	public void populateShapeSpinner(ArrayList<Shape> shapes, Spinner spinShape) {
		ArrayList<String> shapeNames = new ArrayList<>();
		shapeNames.add("Select Shape");

		for (Shape shape : shapes) {
			shapeNames.add(shape.shape_name());
		}

		ArrayAdapter<String> shapeAdapter =
				new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, shapeNames);
		shapeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinShape.setAdapter(shapeAdapter);
	}

	public void populateApexSpinner(ArrayList<Apex> apexes, Spinner spinApex) {
		ArrayList<String> apexNames = new ArrayList<>();
		apexNames.add("Select Apex");

		for (Apex apex : apexes) {
			apexNames.add(apex.apex_name());
		}

		ArrayAdapter<String> apexAdapter =
				new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, apexNames);
		apexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinApex.setAdapter(apexAdapter);
	}

	public void populateMarginSpinner(ArrayList<Margin> margins, Spinner spinMargin) {
		ArrayList<String> marginNames = new ArrayList<>();
		marginNames.add("Select Margin");

		for (Margin margin : margins) {
			marginNames.add(margin.margin_name());
		}

		ArrayAdapter<String> marginAdapter =
				new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, marginNames);
		marginAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinMargin.setAdapter(marginAdapter);
	}
}
