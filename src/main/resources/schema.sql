DROP TABLE IF EXISTS book_summary;

CREATE VIEW book_summary AS
SELECT
    book_id as database_id,
    count(*) as popularity
FROM "books_orders" GROUP BY book_id;


CREATE RULE book_summary_on_delete AS ON DELETE TO book_summary DO INSTEAD NOTHING;
