CREATE USER IF NOT EXIST xiao WITH PASSWORD 'xiao';
GRANT ALL PRIVILEGES ON DATABASE ocpp TO xiao;

CREATE TABLE IF NOT EXIST Reservation(
    id INT UNIQUE PRIMARY KEY,
    reservationId INT,
    connectorId INT,
    transactionId INT,
    date VARCHAR(20),
    start VARCHAR(25),
    stop VARCHAR(25),
    startSoC INT NOT NULL,
    targetSoC INT NOT NULL
);
