-- property id is defined by user after creating a new property, and is thus not auto incremented
CREATE TABLE properties (
    id VARCHAR(10) NOT NULL,
    balance INT,
    price INT,
    moveIn DATE,
    tenants INT,
    description VARCHAR(500),
    PRIMARY KEY (id)
);

CREATE TABLE tenants (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    property VARCHAR(10) NOT NULL,
    name VARCHAR(50),
    email VARCHAR(50),
    phone VARCHAR(50),
    PRIMARY KEY (id),
    FOREIGN KEY (property) REFERENCES properties(id)
);