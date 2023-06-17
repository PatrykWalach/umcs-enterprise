package com.umcs.enterprise.basket;

import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.umcs.enterprise.book.Book;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class SummableService {



    public BigDecimal sumPrice(List<? extends  SummableEdge> books) {


        return books
                .stream()
                .filter(Objects::nonNull)
                .map(edge -> edge.getBook().getPrice().multiply(BigDecimal.valueOf(edge.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ;
    }


    public Integer sumQuantity(List<? extends  SummableEdge> books) {
        return books.stream().mapToInt(SummableEdge::getQuantity).sum();
    }
}
