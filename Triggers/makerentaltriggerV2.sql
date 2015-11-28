DELIMITER $$

CREATE TRIGGER before_insert_makeRental BEFORE INSERT ON moviestore.rentals_record_rents
FOR EACH ROW BEGIN
	CASE(SELECT rental_quota FROM moviestore.user WHERE pid = NEW.pid)
	WHEN(0) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'You can not go over your rental quota';
    ELSE
		CASE(SELECT num_availible_copys FROM moviestore.rentals WHERE rid = NEW.rid)
			WHEN(0) THEN
				SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Current item is out of stock';
			ELSE
				UPDATE moviestore.user, moviestore.rentals
				SET rental_quota = rental_quota - 1, num_availible_copys = num_availible_copys - 1
				WHERE pid = NEW.pid AND rid = NEW.rid;
		END CASE;
	END CASE;
END
$$
DELIMITER ;