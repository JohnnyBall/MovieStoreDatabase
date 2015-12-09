DELIMITER $$

CREATE TRIGGER before_insert_user BEFORE INSERT ON moviestore.user
FOR EACH ROW BEGIN
	IF (NEW.email IN (SELECT u.email FROM moviestore.user u)) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Account is already associated with this email';
	END IF;
END
$$
DELIMITER ;