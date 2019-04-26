drop table if exists groups cascade;
create table groups (
  id bigserial primary key,
  name text NOT NULL,
  description text NOT NULL,
  created timestamp without time zone,
  last_updated timestamp without time zone,
  deleted boolean NOT NULL DEFAULT false
);

drop table if exists user_groups cascade;
create table user_groups (
  user_id bigserial,
  group_id bigserial,
  foreign key (user_id) references users(id),
  foreign key (group_id) references groups(id)
);

drop table if exists roles cascade;
create table roles (
 id bigserial primary key,
 name character varying(255),
 created timestamp without time zone,
 last_updated timestamp without time zone,
 deleted boolean NOT NULL DEFAULT false
);



drop table if exists group_roles cascade;
create table group_roles (
 group_id bigserial,
 role_id bigserial,
 foreign key (group_id) references groups(id),
 foreign key (role_id) references roles(id)
);


--can add foreign key reference to the users table

drop table if exists login_attempts cascade;
create table login_attempts (
   user_id bigserial,
   attempts int,
   last_updated timestamp
);


drop table if exists keys cascade;
create table keys(
 id bigserial,
 content text,
 description text,
 expires_at timestamp without time zone,
 issuer text,
 created timestamp without time zone
 );


INSERT INTO users(id,first_name,last_name,mobile_number, password, email,district,city, state,country,pincode) VALUES
            (1, 'rakesh','rampalli','9901250919', '123456', 'rrakesh100@gmail.com', 'BANGALORE URBAN', 'BENGALURU', 'KARNATAKA','INDIA','560075');

INSERT INTO users(id,first_name,last_name,mobile_number, password, email,district,city, state,country,pincode) VALUES
            (2, 'donald','trump','1234567890', '123456', 'trump@gmail.com', 'SOMEDISTRICT', 'DALLAS', 'TEXAS','USA','123456');

INSERT INTO users(id,first_name,last_name,mobile_number, password, email,district,city, state,country,pincode) VALUES
            (3, 'modi','modi','1234567890', '123456', 'modi@gmail.com', 'VADODARA', 'VADODARA', 'GUJARAT','INDIA','400123');

INSERT INTO users(id,first_name,last_name,mobile_number, password, email,district,city, state,country,pincode) VALUES
            (4, 'amit','shah','1234567890', '123456', 'amit@gmail.com', 'AHMEDABAD', 'AHMEDABAD', 'GUJARAT','INDIA','400123');

INSERT INTO users(id,first_name,last_name,mobile_number, password, email,district,city, state,country,pincode) VALUES
            (5, 'putin','vladimir','1234567890', '123456', 'putin@gmail.com', 'VALHALA', 'MOSCOW', 'MOSCOW','RUSSIA','123456');




INSERT INTO GROUPS(id,name,description, created, last_updated, deleted) VALUES (1, 'LIBRARY_ADMIN', 'Root user - Library Admin', now(), NOW(), false);
INSERT INTO GROUPS(id,name,description, created, last_updated, deleted) VALUES (2, 'LIBRARY_USER', 'Standard user - Library User', now(), NOW(), false);
INSERT INTO GROUPS(id,name,description, created, last_updated, deleted) VALUES (3, 'LIBRARY_OFFICE', 'Office user - Library Office', now(), NOW(), false);


INSERT INTO USER_GROUPS(user_id, group_id) VALUES (1, 1);
INSERT INTO USER_GROUPS(user_id, group_id) VALUES (1, 2);
INSERT INTO USER_GROUPS(user_id, group_id) VALUES (1, 3);
INSERT INTO USER_GROUPS(user_id, group_id) VALUES (2, 2);
INSERT INTO USER_GROUPS(user_id, group_id) VALUES (3, 3);
INSERT INTO USER_GROUPS(user_id, group_id) VALUES (3, 2);
INSERT INTO USER_GROUPS(user_id, group_id) VALUES (4, 3);
INSERT INTO USER_GROUPS(user_id, group_id) VALUES (4, 2);
INSERT INTO USER_GROUPS(user_id, group_id) VALUES (5, 2);



INSERT INTO ROLES(id, name ) VALUES (1, 'BOOKS_ISSUER');
INSERT INTO ROLES(id, name ) VALUES (2, 'USER');
INSERT INTO ROLES(id, name ) VALUES (3, 'INVENTORY_MANAGER');
INSERT INTO ROLES(id, name ) VALUES (4, 'LIBRARY_CARD_ISSUER');

INSERT INTO GROUP_ROLES(group_id, role_id ) VALUES (1, 1);
INSERT INTO GROUP_ROLES(group_id, role_id ) VALUES (1, 2);
INSERT INTO GROUP_ROLES(group_id, role_id ) VALUES (1, 3);
INSERT INTO GROUP_ROLES(group_id, role_id ) VALUES (1, 4);
INSERT INTO GROUP_ROLES(group_id, role_id ) VALUES (2, 1);
INSERT INTO GROUP_ROLES(group_id, role_id ) VALUES (3, 1);
INSERT INTO GROUP_ROLES(group_id, role_id ) VALUES (3, 2);
INSERT INTO GROUP_ROLES(group_id, role_id ) VALUES (3, 3);
