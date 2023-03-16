package com.umcs.enterprise;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class PageInfoController {


    @SchemaMapping(typeName = "PageInfo")
    public boolean hasNextPage(Connection<Object> connection) {
        return connection.hasNextPage();
    }

    @SchemaMapping(typeName = "PageInfo")
    public boolean hasPreviousPage(Connection<Object> connection) {
        return connection.hasPreviousPage();
    }

    @SchemaMapping(typeName = "PageInfo")
    public Optional<String> startCursor(Connection<Object> connection) {
        return connection.startCursor();
    }

    @SchemaMapping(typeName = "PageInfo")
    public Optional<String> endCursor(Connection<Object> connection) {
        return connection.endCursor();
    }


}
