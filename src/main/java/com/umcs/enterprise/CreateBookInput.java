package com.umcs.enterprise;

public record CreateBookInput(CreateBookCoverInput
                                      cover, String title,
                              String author,
                              Integer price) {
}
