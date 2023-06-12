create table routes_with_seats as (
	select r.flight_no, s.seat_no, s.fare_conditions
	from routes as r
	join seats as s on s.aircraft_code=r.aircraft_code
);

create table routes_seats_amount as (
	select f.flight_no, swa.fare_conditions, swa.seat_no, avg(swa.amount)::numeric(10,2) as amount
	from (select tf.flight_id, tf.fare_conditions, tf.amount, bp.seat_no
			from ticket_flights as tf
			join boarding_passes as bp on bp.flight_id=tf.flight_id and bp.ticket_no=tf.ticket_no
		 ) as swa
	join flights as f on f.flight_id=swa.flight_id
	group by f.flight_no, swa.fare_conditions, swa.seat_no
);

create table partial_amount as (
	select rws.flight_no, rws.seat_no, rws.fare_conditions,
		(case when rsa.amount is NULL then 0 else rsa.amount end)::numeric(10,2) as amount,
		(case when length(rws.seat_no)=3
		 	then substring(rws.seat_no from 1 for 2)
		 	else substring(rws.seat_no from 1 for 1)
		 end) as row_no
	from routes_with_seats as rws
	left join routes_seats_amount as rsa
		on rsa.flight_no=rws.flight_no and rsa.seat_no=rws.seat_no and rsa.fare_conditions=rws.fare_conditions
);

create table partial_zero as (
	select *
	from partial_amount
	where amount = 0
);

create table partial_nonzero as (
	select *
	from partial_amount
	where amount != 0
);

create table restored_amount as (
	select pz.flight_no, pz.fare_conditions, pz.seat_no, pz.row_no, max(pn.amount)::numeric(10,2) as amount
	from partial_zero as pz
	inner join partial_nonzero as pn on pn.flight_no=pz.flight_no and pn.fare_conditions=pz.fare_conditions and pn.row_no=pz.row_no
	group by pz.flight_no, pz.seat_no, pz.fare_conditions, pz.row_no
);

create table full_amount as (
	select pa.flight_no, pa.fare_conditions, pa.seat_no,
		(case when pa.amount=0
			then (case when ra.amount is NULL
					then 0
					else ra.amount
				  end)
			else pa.amount
		 end) as amount
	from partial_amount as pa
	left join restored_amount as ra on ra.flight_no=pa.flight_no and ra.fare_conditions=pa.fare_conditions and ra.seat_no=pa.seat_no
);

create table total_amount as (
	select f.flight_no, (case when f.amount != fg.amount then f.fare_conditions || '+' else f.fare_conditions end) as fare_conditions,
		f.seat_no, f.amount
	from full_amount as f
	join (select flight_no, fare_conditions, min(amount) as amount
			from full_amount
			group by flight_no, fare_conditions
		 ) as fg
		on fg.flight_no=f.flight_no and fg.fare_conditions=f.fare_conditions
	order by f.flight_no desc, f.seat_no asc
);

create table grouped_amount as (
	select flight_no, fare_conditions, avg(amount)::numeric(10,2) as amount, count(seat_no) as seat_count
	from total_amount
	group by flight_no, fare_conditions, amount
	order by flight_no desc
);

drop table routes_with_seats, routes_seats_amount, partial_amount, partial_zero, partial_nonzero, restored_amount, full_amount;

select * from total_amount;
