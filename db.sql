CREATE TABLE instance (
    id   VARCHAR(55) PRIMARY KEY
);

CREATE TABLE command (
    id  VARCHAR(55) PRIMARY KEY,
    min_time INTEGER,
    sending_tdate INTEGER,
    fee REAL,
    product_id_quantity INTEGER,
    instance_id VARCHAR(55),
    FOREIGN KEY (instance_id) REFERENCES instance(id)
);

CREATE TABLE product_line_type (
    id   VARCHAR(55) PRIMARY KEY,
    product_line_number INTEGER,
    instance_id VARCHAR(55),
    FOREIGN KEY (instance_id) REFERENCES instance(id)
);

CREATE TABLE product_line (
    id   VARCHAR(55) PRIMARY KEY,
    roduct_line_number INTEGER,
    instance_id VARCHAR(55),
    FOREIGN KEY (instance_id) REFERENCES instance(id)
);

CREATE TABLE product_type (
    id   VARCHAR(55) PRIMARY KEY,
    set_up_time INTEGER,
    production_time INTEGER,
    height INTEGER,
    width INTEGER,
    max_unit INTEGER,
    instance_id VARCHAR(55),
    FOREIGN KEY (instance_id) REFERENCES instance(id)
);



CREATE TABLE box_type (
    id   VARCHAR(55) PRIMARY KEY,
    height INTEGER,
    width INTEGER,
    price FLOAT,
    instance_id VARCHAR(55),
    FOREIGN KEY (instance_id) REFERENCES instance(id)
);

CREATE TABLE box (
    id   VARCHAR(55) PRIMARY KEY,
    box_type_id VARCHAR(55),
    FOREIGN KEY (box_type_id) REFERENCES box_type(id),
    command_id VARCHAR(55),
    FOREIGN KEY (command_id) REFERENCES command(id),
    instance_id VARCHAR(55),
    FOREIGN KEY (instance_id) REFERENCES instance(id)
);

CREATE TABLE product (
    id   VARCHAR(55) PRIMARY KEY,
    product_type VARCHAR(55),
    FOREIGN KEY (product_type) REFERENCES product_type(id),
    start_production VARCHAR(55),
    command_id VARCHAR(55),
    FOREIGN KEY (command_id) REFERENCES command(id),
    product_line_id VARCHAR(55),
    FOREIGN KEY (product_line_id) REFERENCES product_line(id),
    box_id VARCHAR(55),
    FOREIGN KEY (box_id) REFERENCES box(id),
    instance_id VARCHAR(55),
    FOREIGN KEY (instance_id) REFERENCES instance(id)
);

CREATE TABLE solution (
    id   VARCHAR(55) PRIMARY KEY,
    fee REAL,
    sending_date INTEGER,
    instance_id VARCHAR(55),
    eval_score  REAL,
    FOREIGN KEY (instance_id) REFERENCES instance(id)
);
