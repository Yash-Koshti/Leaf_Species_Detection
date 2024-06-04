package com.leaf_lore.leaf_lore_frontend.utils;

public interface ApiCallsConfirmer {
	void confirmTokenReceived(boolean confirm);

	void confirmAllSpeciesFetched(boolean confirm);

	void confirmAllImageNamesFetched(boolean confirm);

	void confirmAllShapesFetched(boolean confirm);

	void confirmAllApexesFetched(boolean confirm);

	void confirmAllMarginsFetched(boolean confirm);
}
