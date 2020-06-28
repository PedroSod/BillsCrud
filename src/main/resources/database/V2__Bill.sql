create TABLE IF NOT EXISTS bill(
    id bigserial,
    fine_bill decimal,
    name varchar,
    overdue_days integer,
    original_amount numeric (15, 2),
    corrected_amount  numeric (15, 2),
    payment_date date,
    due_date date
);