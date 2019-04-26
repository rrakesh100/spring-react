alter table books add column rack_number text not null;
alter table users add column penalty decimal(15,2) default 0 ;
alter table users add column max_copies int default 5 ;
alter table users add column is_admin boolean default false;

