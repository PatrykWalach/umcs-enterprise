package com.umcs.enterprise;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.boot.web.context.WebServerInitializedEvent;

@DgsComponent
public class CoverDataFetcher {

	@DgsData(parentType = "BookCover")
	public String url(DataFetchingEnvironment env) {
		return "http://localhost:8080/covers/" + env.<BookCover>getSource().getFilename();
	}
}
