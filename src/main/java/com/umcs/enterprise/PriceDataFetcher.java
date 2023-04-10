package com.umcs.enterprise;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@DgsComponent
public class PriceDataFetcher {

	@DgsData(parentType = "Price")
	public BigDecimal raw(DataFetchingEnvironment env) {
		return env.getSource();
	}

	@DgsData(parentType = "Price")
	public String formatted(DataFetchingEnvironment env) {
		Locale poland = new Locale("pl", "PL");
		return NumberFormat.getCurrencyInstance(poland).format(env.getSource());
	}
}
