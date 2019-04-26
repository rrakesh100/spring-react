-- DROP TABLE always removes any indexes, rules, triggers, and constraints that exist for the target table.
-- However, to drop a table that is referenced by a view or a foreign-key constraint of another table, CASCADE must be specified.
-- (CASCADE will remove a dependent view entirely, but in the foreign-key case it will only remove the foreign-key constraint, not the other table entirely.)
DROP TABLE IF EXISTS users cascade;
create table users (
  id bigserial primary key,
  first_name text NOT NULL,
  last_name text NOT NULL,
  mobile_number bigint,
  password text NOT NULL,
  email text not null,
  date_of_birth date default null,
  copies_borrowed int default 0,
  address_line1 text ,
  address_line2 text,
  land_mark text,
  district text,
  city text,
  state text default 'Karnataka',
  country text,
  pincode text,
  image_url text,
  deleted boolean NOT NULL DEFAULT false
);


DROP TABLE IF EXISTS books cascade;
create table books (
  id bigserial primary key,
  title text NOT NULL,
  author1 text NOT NULL,
  author2 text NOT NULL,
  author3 text NOT NULL,
  publisher text,
  isbn text,
  copies int,
  deleted boolean NOT NULL DEFAULT false
);


DROP TABLE IF EXISTS book_copies cascade;
create table book_copies (
  id bigserial primary key,
  book_id bigserial ,
  copy_number int,
  is_reference_copy boolean,
  status text,
  current_owner bigserial,
  deleted boolean NOT NULL DEFAULT false,
  constraint fk_1 foreign key(book_id) references books(id),
  constraint fk_2 foreign key(current_owner) references users(id)

);