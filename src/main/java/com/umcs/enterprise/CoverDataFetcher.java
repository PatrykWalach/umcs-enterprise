package com.umcs.enterprise;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;

@DgsComponent
public class CoverDataFetcher {

	@DgsData(parentType = "Cover")
	public String url(DataFetchingEnvironment env) {
		return "http://192.168.1.103:8080/covers/" + env.<Cover>getSource().getFilename();
	}
}
