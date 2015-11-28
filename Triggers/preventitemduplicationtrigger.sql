DELIMITER $$
CREATE TRIGGER before_insert_newRentalItem BEFORE INSERT ON moviestore.rentals
FOR EACH ROW BEGIN
IF(NEW.title IN (SELECT title FROM moviestore.rentals)) THEN
	SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'The item you are attempting to add already exists in the database';
END IF;
END
$$
DELIMITER ;