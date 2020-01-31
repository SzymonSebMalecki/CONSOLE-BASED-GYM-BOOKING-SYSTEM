CREATE DATABASE IF NOT EXISTS gymbookingsystem; /* If database not exists already create new one */
USE gymbookingsystem;

DROP TABLE IF EXISTS Clients, Trainers, Bookings; /* Delete Table if it already excists */

/* Create Table Clients */
CREATE TABLE Clients
(
    CLIENT_ID INT AUTO_INCREMENT PRIMARY KEY, /* Primary Key (CLIENT_ID) is incremented once more data is added */
    FNAME VARCHAR(30),
    LNAME VARCHAR(30),
    GENDER VARCHAR(30),
    AGE VARCHAR(10)
);
/* Create Table Clients */

/* Create Table Trainers */
CREATE TABLE Trainers
(
     TRAINER_ID INT AUTO_INCREMENT PRIMARY KEY, /* Primary Key (TRAINER_ID) is incremented once more data is added */
     FNAME VARCHAR(30),
     LNAME VARCHAR(30),
     GENDER VARCHAR(30)
);
/* Create Table Trainers */

/* Create Table Bookings */
CREATE TABLE Bookings
(
    BOOKING_ID INT AUTO_INCREMENT PRIMARY KEY, /* Primary Key (BOOKING_ID) is incremented once more data is added */
    CLIENT_ID INT,
    FOREIGN KEY(CLIENT_ID) REFERENCES Clients(CLIENT_ID),
    TRAINER_ID INT,
    FOREIGN KEY(TRAINER_ID) REFERENCES Trainers(TRAINER_ID),
    BOOKING_DATE VARCHAR(20),
    BOOKING_TIME VARCHAR(20),
    DURATION_OF_SESSION INT,
    FOCUS VARCHAR(30)
);
/* Create Table Bookings */

/* Dummy Data */
INSERT INTO Clients(FNAME, LNAME, GENDER, AGE)
VALUES("John", "Smith", "MALE", "18"), ("Julian", "Johnson", "MALE", "28"), ("Harry", "Jenkins", "MALE", "28"), ("Julia" , "Roberts", "FEMLAE","20"), ("Sylwester", "Stallone", "MALE","73");

INSERT INTO Trainers(FNAME, LNAME, GENDER)
VALUES("Adam", "Smith", "MALE"), ("Leonardo", "Johnson", "MALE"), ("Timothy", "Jenkins","Male"), ("Jennifer", "Smith", "Female"), ("Arthur", "Morgan", "Male");

INSERT INTO Bookings(CLIENT_ID, TRAINER_ID, BOOKING_DATE, BOOKING_TIME, DURATION_OF_SESSION, FOCUS)
VALUES(1, 1,"2020-01-10", "15:00", "20", "CARDIO"), (2, 4,"2020-01-31", "19:00", "40","TREADMIL"), (3, 5,"2020-02-05", "19:00", "50","WEIGHTLIFTING");
/* Dummy Data */