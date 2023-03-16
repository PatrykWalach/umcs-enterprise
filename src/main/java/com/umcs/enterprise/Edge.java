package com.umcs.enterprise;

public class Edge<T> {

	private final String cursor;
	private final T node;

	public Edge(T node, Object cursor) {
		this.cursor = cursor.toString();
		this.node = node;
	}

	public String getCursor() {
		return cursor;
	}

	public T getNode() {
		return node;
	}
}
