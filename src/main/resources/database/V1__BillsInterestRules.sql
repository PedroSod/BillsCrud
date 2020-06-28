create TABLE IF NOT EXISTS bills_interest_rules(
    id bigserial,
    fine_bill decimal,
    interest_on_day_Overdue decimal,
    under_day_limit integer,
    over_day_limit integer
);

insert into bills_interest_rules
values (default, 2, 0.1, 1, 3);

insert into bills_interest_rules
values (default, 3, 0.2, 4, 5);


insert into bills_interest_rules
values (default, 5, 0.3, 6, 2147483647);