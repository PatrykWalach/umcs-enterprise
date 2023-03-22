DROP TABLE IF EXISTS book_summary;

CREATE VIEW book_summary AS
SELECT
    book.database_id,
    count(books_orders.database_id) as popularity
FROM book LEFT JOIN books_orders
ON book.database_id = books_orders.book_id
GROUP BY book.database_id;


CREATE RULE book_summary_on_delete AS ON DELETE TO book_summary DO INSTEAD NOTHING;
