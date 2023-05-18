--------------------------------------------------
-- 1) routes_with_seats - rws
-- 54_214

select r.flight_no, r.aircraft_code, s.seat_no, s.fare_conditions
from routes as r
join seats as s on s.aircraft_code=r.aircraft_code
order by r.flight_no, s.seat_no asc;


-- 2) seats_with_amount - swa
-- 1_894_295

select tf.flight_id, tf.fare_conditions, tf.amount, bp.seat_no
from ticket_flights as tf
join boarding_passes as bp on bp.flight_id=tf.flight_id and bp.ticket_no=tf.ticket_no;


-- 3) routes_seats_amount - rsa
-- 1_894_295

select swa.flight_id, f.flight_no, swa.fare_conditions, swa.seat_no, swa.amount
from (select tf.flight_id, tf.fare_conditions, tf.amount, bp.seat_no
		from ticket_flights as tf
		join boarding_passes as bp on bp.flight_id=tf.flight_id and bp.ticket_no=tf.ticket_no) as swa
join flights as f on f.flight_id=swa.flight_id;

-- routes_seats_amount - grouped by
-- 41_741

select f.flight_no, swa.fare_conditions, swa.seat_no, min(swa.amount)::numeric(10,2) as amount
from (select tf.flight_id, tf.fare_conditions, tf.amount, bp.seat_no
		from ticket_flights as tf
		join boarding_passes as bp on bp.flight_id=tf.flight_id and bp.ticket_no=tf.ticket_no) as swa
join flights as f on f.flight_id=swa.flight_id
group by f.flight_no, swa.fare_conditions, swa.seat_no;


-- 4) all together
-- 54_214

select rws.flight_no, rws.seat_no, rws.fare_conditions, (case when rsa.amount is NULL then 0 else rsa.amount end)::numeric(10,2) as amount
from (select r.flight_no, r.aircraft_code, s.seat_no, s.fare_conditions
		from routes as r
		join seats as s on s.aircraft_code=r.aircraft_code)
	as rws	
left join (select f.flight_no, swa.fare_conditions, swa.seat_no, min(swa.amount)::numeric(10,2) as amount
		from (select tf.flight_id, tf.fare_conditions, tf.amount, bp.seat_no
				from ticket_flights as tf
				join boarding_passes as bp on bp.flight_id=tf.flight_id and bp.ticket_no=tf.ticket_no)
		   as swa
		join flights as f on f.flight_id=swa.flight_id
		group by f.flight_no, swa.fare_conditions, swa.seat_no)
	as rsa
	on rsa.flight_no=rws.flight_no and rsa.seat_no=rws.seat_no and rsa.fare_conditions=rws.fare_conditions;

--------------------------------------------------