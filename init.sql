-- Create a user for the application
CREATE USER app_user IDENTIFIED BY app_password;
GRANT CONNECT, RESOURCE TO app_user;

-- Create the roles table
CREATE TABLE app_user.role (
                               id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY PRIMARY KEY,
                               name VARCHAR2(50) NOT NULL
);

-- Create the users table
CREATE TABLE app_user.user (
                               id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY PRIMARY KEY,
                               login VARCHAR2(100) UNIQUE NOT NULL,
                               password VARCHAR2(255) NOT NULL,
                               active NUMBER(1) DEFAULT 1 NOT NULL
);

-- Create the user_roles table
CREATE TABLE app_user.user_roles (
                                     user_id NUMBER NOT NULL,
                                     role_id NUMBER NOT NULL,
                                     PRIMARY KEY (user_id, role_id),
                                     FOREIGN KEY (user_id) REFERENCES app_user.user (id),
                                     FOREIGN KEY (role_id) REFERENCES app_user.role (id)
);

-- Insert initial data
INSERT INTO app_user.role (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');