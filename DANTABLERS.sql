CREATE TABLE Rentals (
    rid INT,
    title TINYTEXT NOT NULL,
    releaseDate DATE,
    num_availible_copys INT NOT NULL,
    PRIMARY KEY (rid)
);

CREATE TABLE Rentals_Record_Rents (
    Trackingnum INT,
    rid INT,
    pid INT,
    from_Date DATE NOT NULL,
    FOREIGN KEY (pid)
        REFERENCES Person (pid),
    FOREIGN KEY (rid)
        REFERENCES Rentals (rid),
    PRIMARY KEY (rid , pid , Trackingnum)
);

CREATE TABLE Movie (
    rid INT,
    pid INT NOT NULL,
    FOREIGN KEY (rid)
        REFERENCES Rentals (rid),
    PRIMARY KEY (rid)
);

CREATE TABLE Sequel (
    rid_has_sequel INT,
    rid_is_sequel INT NOT NULL,
    FOREIGN KEY (rid_has_sequel)
        REFERENCES Movie (rid),
    FOREIGN KEY (rid_is_sequel)
        REFERENCES Movie (rid),
    PRIMARY KEY (rid_has_sequel , rid_is_sequel)
);

CREATE TABLE game (
    rid INT,
    FOREIGN KEY (rid)
        REFERENCES Rentals (rid),
    PRIMARY KEY (rid)
);

CREATE TABLE plays_on (
    rid INT,
    platName TINYTEXT,
    FOREIGN KEY (rid)
        REFERENCES game (rid),
    FOREIGN KEY (platName)
        REFERENCES Platform (platName),
    PRIMARY KEY (rid , platName)
);

CREATE TABLE genre (
    gName TINYTEXT,
    PRIMARY KEY (gName)
);

CREATE TABLE belongs_To (
    gName TINYTEXT,
    rid INT,
    FOREIGN KEY (gName)
        REFERENCES genre (gName),
    FOREIGN KEY (rid)
        REFERENCES Rentals (rid),
    PRIMARY KEY (gName , rid)
);

CREATE TABLE Platform (
    platName TINYTEXT,
    PRIMARY KEY (platName)
);

CREATE TABLE Award (
    aTitle TINYTEXT,
    PRIMARY KEY (aTitle)
);

CREATE TABLE belongs_To (
    aTitle TINYTEXT,
    rid INT,
    PRIMARY KEY (rid , aTitle)
);

CREATE TABLE has_address (
    pid INT,
    street TINYTEXT,
    zip INT,
    FOREIGN KEY (pid)
        REFERENCES Person (pid),
    FOREIGN KEY (zip)
        REFERENCES address (zip),
    FOREIGN KEY (street)
        REFERENCES address (street),
    PRIMARY KEY (pid , zip , street)
);

CREATE TABLE address (
    street TINYTEXT,
    zip INT,
    street TINYTEXT NOT NULL,
    State TINYTEXT NOT NULL,
    city TINYTEXT NOT NULL,
    PRIMARY KEY (street , zip)
);

CREATE TABLE Person (
    pid INT,
    pname TINYTEXT NOT NULL,
    PRIMARY KEY (pid)
);

CREATE TABLE Director (
    pid INT,
    FOREIGN KEY (pid)
        REFERENCES Person (pid),
    PRIMARY KEY (pid)
);

CREATE TABLE MCast (
    pid INT,
    FOREIGN KEY (pid)
        REFERENCES Person (pid),
    PRIMARY KEY (pid)
);

CREATE TABLE Was_In (
    pid INT,
    rid INT,
    FOREIGN KEY (pid)
        REFERENCES Person (pid),
    FOREIGN KEY (rid)
        REFERENCES Movie (rid),
    PRIMARY KEY (pid , rid)
);

CREATE TABLE User (
    pid INT,
    email TINYTEXT NOT NULL,
    is_admin TINYINT,
    user_password TINYTEXT NOT NULL,
    FOREIGN KEY (pid)
        REFERENCES Person (pid),
    PRIMARY KEY (pid)
);
