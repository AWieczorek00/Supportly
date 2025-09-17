ALTER TABLE agreement
    DROP COLUMN date_from;

ALTER TABLE agreement
    DROP COLUMN date_to;

ALTER TABLE agreement
    ADD SIGNED_DATE DATE;