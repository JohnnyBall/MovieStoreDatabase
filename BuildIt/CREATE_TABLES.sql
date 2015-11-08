
CREATE TABLE person (
    pid INT,
    pname VARCHAR(80) NOT NULL,
    PRIMARY KEY (pid) );

CREATE TABLE rentals (
    rid INT,
    title VARCHAR(80) NOT NULL,
    releaseDate DATE,
    num_availible_copys INT NOT NULL,
    PRIMARY KEY (rid) );
    
    CREATE TABLE address (
    street VARCHAR(80),
    zip INT,
    phone VARCHAR(80) NOT NULL,
    state VARCHAR(80) NOT NULL,
    city  VARCHAR(80) NOT NULL,
    PRIMARY KEY (street , zip) );
    
    CREATE TABLE has_address (
    pid INT,
	street VARCHAR(80),
    zip INT,
    FOREIGN KEY (pid)    REFERENCES person (pid)     ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (street,zip) REFERENCES address (street,zip) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (pid , street , zip) );

CREATE TABLE user (
    pid INT,
    email VARCHAR(80) NOT NULL,
    is_admin TINYINT,
    rental_quota TINYINT DEFAULT 1,
    user_password VARCHAR(80) NOT NULL,
    FOREIGN KEY (pid) REFERENCES person (pid) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (pid) );
    
CREATE TABLE director (
    pid INT,
    FOREIGN KEY (pid) REFERENCES person (pid) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (pid) );

CREATE TABLE mcast (
    pid INT,
    FOREIGN KEY (pid) REFERENCES person (pid) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (pid) );

CREATE TABLE movie (
    rid INT,
    pid INT NOT NULL,
    rid_of_prequel INT,
    PRIMARY KEY (rid), 
    FOREIGN KEY (rid) REFERENCES rentals (rid) ON DELETE CASCADE ON UPDATE CASCADE, 
	FOREIGN KEY (pid) REFERENCES director (pid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (rid_of_prequel) REFERENCES movie (rid) ON DELETE CASCADE ON UPDATE CASCADE );
    
CREATE TABLE game (
    rid INT,
    FOREIGN KEY (rid) REFERENCES rentals (rid) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (rid) ); 

CREATE TABLE rentals_record_rents (
    Trackingnum INT,
    rid INT,
    pid INT,
    from_Date DATE NOT NULL,
    FOREIGN KEY (pid) REFERENCES user (pid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (rid) REFERENCES rentals (rid) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (rid , pid , Trackingnum) );

CREATE TABLE plays_on_Platform (
    rid INT,
    platName VARCHAR(40),
    FOREIGN KEY (rid)      REFERENCES game (rid) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (rid , platName) );

CREATE TABLE belongs_to_Genre (
    gName VARCHAR(80),
    rid INT,
    FOREIGN KEY (rid)   REFERENCES rentals (rid) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (gName , rid) );

CREATE TABLE has_won_award (
   aTitle VARCHAR(50),
    rid INT,
    FOREIGN KEY (rid)    REFERENCES movie (rid)    ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (rid , aTitle) );

CREATE TABLE was_in (
    pid INT,
    rid INT,
    FOREIGN KEY (pid) REFERENCES mcast (pid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (rid) REFERENCES movie (rid) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (pid , rid) );


    
  
  
  

