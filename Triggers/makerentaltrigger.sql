DELIMITER $$

CREATE TRIGGER before_insert_makeRental BEFORE INSERT ON moviestore.rentals_record_rents
FOR EACH ROW BEGIN
	UPDATE moviestore.user, moviestore.rentals
    SET rental_quota = rental_quota - 1, num_availible_copys = num_availible_copys - 1
    WHERE pid = NEW.pid AND rid = NEW.rid;
END
$$
DELIMITER ;