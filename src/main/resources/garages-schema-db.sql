CREATE TABLE Garage (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    telephone VARCHAR(50),
    email VARCHAR(100)
);

CREATE TABLE Vehicle (
    id UUID PRIMARY KEY,
    brand VARCHAR(100),
    annee_fabrication INT,
    type_carburant VARCHAR(50),
    garage_id UUID NOT NULL,
    FOREIGN KEY (garage_id) REFERENCES Garage(id)
);

CREATE TABLE Accessory (
    id UUID PRIMARY KEY,
    nom VARCHAR(255),
    description TEXT,
    prix DECIMAL(10, 2),
    type VARCHAR(100),
    vehicle_id UUID NOT NULL,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicle(id)
);

CREATE TABLE OpeningTime (
    day_of_week VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    garage_id UUID NOT NULL
    FOREIGN KEY (garage_id) REFERENCES Garage(id)
);