package com.umcs.enterprise.book;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Projection(types = {Book.class},name = "list-item")
public interface BookProjection {
public UUID getId();

    @Value("#{@bookProjectionBean.coversToMap(target.covers)}")
    public Map<Integer ,String> getCovers();

    @Value("#{@bookProjectionBean.getNames(target.authors)}")
    public List<String> getAuthors();

    public BigDecimal getPrice();
    String getTitle();

}
