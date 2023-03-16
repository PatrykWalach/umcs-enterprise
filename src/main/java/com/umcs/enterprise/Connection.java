package com.umcs.enterprise;

import graphql.GraphQLException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Connection<T> {

    private final Function<Pageable, Page<T>> repository;
    private final Map<String, Object> args;
    private final LongSupplier count;
    private List<Edge<T>> edges = null;
    private Page<T> page;
    private Integer offset = null;
    private int limit;
    private Integer total = null;

    public Connection(Function<Pageable, Page<T>> repository, LongSupplier count, Map<String, Object> args) {

        this
                .repository = repository;
        this.args = args;
        this.count = count;
        if (!(first() != null || Objects.equals(first(), 0) || last() != null || Objects.equals(last(), 0))) {
            throw new GraphQLException(
                    "The ${info.parentType}.${info.fieldName} connection field requires a \"first\" or \"last\" argument"
            );
        }


        if (first() != null && last() != null) {
            throw new GraphQLException(
                    "The ${info.parentType}.${info.fieldName} connection field requires a \"first\" or \"last\" argument, not both"
            );
        }
        if (first() != null && before() != null) {
            throw new GraphQLException(
                    "The ${info.parentType}.${info.fieldName} connection field does not allow a \"before\" argument with \"first\""
            );
        }
        if (last() != null && after() != null) {
            throw new GraphQLException(
                    "The ${info.parentType}.${info.fieldName} connection field does not allow a \"last\" argument with \"after\""
            );
        }

    }

    public List<Edge<T>> getEdges() {
        this.edgesToReturn();
        return this.edges;
    }

    private Integer after() {
        try {
            return Integer.parseInt((String) args.get("after"));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer before() {
        try {
            return Integer.parseInt((String) args.get("before"));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer first() {
        return (Integer) args.get("first");
    }

    private Integer last() {
        return (Integer) args.get("last");
    }

    private void setTotal() {
        if (total != null) {
            return;
        }

        this.total = Long.valueOf(count.getAsLong()).intValue();
    }

    private Integer getTotal() {
        return total;
    }

    private void setOffset() {
        if (offset != null) {
            return;
        }

        if (last() != null) {

            int before = Optional.ofNullable(before()).orElseGet(() -> {
                setTotal();
                return getTotal();
            });

            this.offset = Math.max(0, before - last());
            this.limit = last();
        } else {
            var after = Optional.ofNullable(this.after()).orElse(-1);
            this.offset = after + 1;
            this.limit = first();
        }
    }

    private void edgesToReturn() {
        if (edges != null) {
            return;
        }


        setOffset();

        Pageable request = new OffsetBasedPageRequest(offset, limit);
        this.page = repository.apply(request);


        this.edges = IntStream.range(0, page.getNumberOfElements()).mapToObj((i) -> new Edge<>(page.getContent().get(i), (this.offset + i))).collect(Collectors.toList());
    }

    public boolean hasNextPage() {
        this.edgesToReturn();
        return this.page.hasNext();
    }

    public boolean hasPreviousPage() {
        this.setOffset();
        return this.offset > 0;
    }


    public Optional<String> startCursor() {
        this.edgesToReturn();
        return edges.size() > 0 ? Optional.of((edges.get(0)).getCursor()) : Optional.empty();
    }

    public Optional<String> endCursor() {
        this.edgesToReturn();
        return edges.size() > 0 ? Optional.of((edges.get(edges.size() - 1)).getCursor()) : Optional.empty();

    }


}
