CREATE TABLE "user" (
                        email VARCHAR(255) PRIMARY KEY ,
                        full_name VARCHAR(255),
                        birthday DATE,
                        gender VARCHAR(20 )
);

CREATE TABLE "user_address" (
    email VARCHAR(255) PRIMARY KEY ,
    address VARCHAR(255)
);

INSERT INTO "user_address" (email, address)
VALUES ('john.dow@mail.com', 'New-York, 5 avenue 54');
