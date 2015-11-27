DELIMITER $$
CREATE TRIGGER before_update_processReturn BEFORE UPDATE ON moviestore.rentals_record_rents
FOR EACH ROW BEGIN
	UPDATE moviestore.user, moviestore.rentals
    SET rental_quota = rental_quota + 1, num_availible_copys = num_availible_copys + 1
    WHERE OLD.return_Date IS NULL AND NEW.return_Date IS NOT NULL AND pid = NEW.pid AND rid = NEW.rid;
END
$$
DELIMITER ;