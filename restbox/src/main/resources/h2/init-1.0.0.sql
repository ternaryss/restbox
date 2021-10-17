--- H2 migration for version 1.0.0
---
--- @author TSS

create table if not exists genere (
  gen_id int auto_increment primary key,
  name varchar not null,
  act boolean not null default true,
  create_date timestamp not null default current_timestamp,
  modify_date timestamp not null default current_timestamp
);

create table if not exists country (
  cou_id int auto_increment primary key,
  name varchar not null,
  act boolean not null default true,
  create_date timestamp not null default current_timestamp,
  modify_date timestamp not null default current_timestamp
);

create table if not exists person (
  per_id int auto_increment primary key,
  first_name varchar not null,
  second_name varchar null,
  last_name varchar not null,
  birthday date not null,
  rate int not null,
  director boolean not null default false,
  act boolean not null default true,
  create_date timestamp not null default current_timestamp,
  modify_date timestamp not null default current_timestamp
);

create table if not exists movie (
  mov_id int auto_increment primary key,
  title varchar not null,
  description varchar null,
  premiere date not null,
  rate int not null,
  length int not null,
  cou_cou_id int not null,
  gen_gen_id int not null,
  per_per_id int not null,
  act boolean not null default true,
  create_date timestamp not null default current_timestamp,
  modify_date timestamp not null default current_timestamp,
  foreign key (cou_cou_id) references country (cou_id) on delete cascade on update no action,
  foreign key (gen_gen_id) references genere (gen_id) on delete cascade on update no action,
  foreign key (per_per_id) references person (per_id) on delete cascade on update no action
);

create table if not exists actor (
  act_id int auto_increment primary key,
  mov_mov_id int not null,
  per_per_id int not null,
  act boolean not null default true,
  create_date timestamp not null default current_timestamp,
  modify_date timestamp not null default current_timestamp,
  foreign key (mov_mov_id) references movie (mov_id),
  foreign key (per_per_id) references person (per_id)
);

insert into genere (gen_id, name)
select * from (
  select 1 as gen_id, 'Drama' as name union all
  select 2 as gen_id, 'Court drama' as name union all
  select 3 as gen_id, 'Comedy' as name union all
  select 4 as gen_id, 'Fiction' as name
) where not exists (select * from genere);

insert into country (cou_id, name)
select * from (
  select 1 as cou_id, 'USA' as name union all
  select 2 as cou_id, 'France' as name union all
  select 3 as cou_id, 'New Zealand' as name
) where not exists (select * from country);

insert into person (per_id, first_name, second_name, last_name, birthday, rate, director)
select * from (
  select 1 as per_id, 'Frank' as first_name, null as second_name, 'Darabont' as last_name, parsedatetime('1959-01-28', 'yyyy-MM-dd', 'cest') as birthday, 9 as rate, true as director union all
  select 2 as per_id, 'Tim' as first_name, null as second_name, 'Robbins' as last_name, parsedatetime('1958-10-16', 'yyyy-MM-dd', 'cest') as birthday, 9 as rate, false as director union all
  select 3 as per_id, 'Morgan' as first_name, null as second_name, 'Freeman' as last_name, parsedatetime('1937-06-01', 'yyyy-MM-dd', 'cest') as birthday, 9 as rate, false as director union all
  select 4 as per_id, 'Bob' as first_name, null as second_name, 'Gunton' as last_name, parsedatetime('1945-11-15', 'yyyy-MM-dd', 'cest') as birthday, 8 as rate, false as director union all
  select 5 as per_id, 'Olivier' as first_name, null as second_name, 'Nakache' as last_name, parsedatetime('1973-04-14', 'yyyy-MM-dd', 'cest') as birthday, 8 as rate, true as director union all
  select 6 as per_id, 'Francois' as first_name, null as second_name, 'Cluzet' as last_name, parsedatetime('1955-09-21', 'yyyy-MM-dd', 'cest') as birthday, 9 as rate, false as director union all
  select 7 as per_id, 'Omar' as first_name, null as second_name, 'Sy' as last_name, parsedatetime('1978-01-20', 'yyyy-MM-dd', 'cest') as birthday, 9 as rate, false as director union all
  select 8 as per_id, 'Anne' as first_name, 'Le' as second_name, 'Ny' as last_name, parsedatetime('1969-12-31', 'yyyy-MM-dd', 'cest') as birthday, 8 as rate, false as director union all
  select 9 as per_id, 'Tom' as first_name, null as second_name, 'Hanks' as last_name, parsedatetime('1956-07-09', 'yyyy-MM-dd', 'cest') as birthday, 9 as rate, false as director union all
  select 10 as per_id, 'David' as first_name, null as second_name, 'Morse' as last_name, parsedatetime('1953-10-11', 'yyyy-MM-dd', 'cest') as birthday, 8 as rate, false as director union all
  select 11 as per_id, 'Bonnie' as first_name, null as second_name, 'Hunt' as last_name, parsedatetime('1961-09-22', 'yyyy-MM-dd', 'cest') as birthday, 8 as rate, false as director union all
  select 12 as per_id, 'Francis' as first_name, 'Ford' as second_name, 'Coppola' as last_name, parsedatetime('1939-04-07', 'yyyy-MM-dd', 'cest') as birthday, 9 as rate, true as director union all
  select 13 as per_id, 'Marlon' as first_name, null as second_name, 'Brando' as last_name, parsedatetime('1956-07-09', 'yyyy-MM-dd', 'cest') as birthday, 9 as rate, false as director union all
  select 14 as per_id, 'Al' as first_name, null as second_name, 'Pacino' as last_name, parsedatetime('1940-04-25', 'yyyy-MM-dd', 'cest') as birthday, 9 as rate, false as director union all
  select 15 as per_id, 'James' as first_name, null as second_name, 'Caan' as last_name, parsedatetime('1940-03-26', 'yyyy-MM-dd', 'cest') as birthday, 8 as rate, false as director union all
  select 16 as per_id, 'Sidney' as first_name, null as second_name, 'Lumet' as last_name, parsedatetime('1924-06-25', 'yyyy-MM-dd', 'cest') as birthday, 8 as rate, true as director union all
  select 17 as per_id, 'Martin' as first_name, null as second_name, 'Balsam' as last_name, parsedatetime('1919-11-04', 'yyyy-MM-dd', 'cest') as birthday, 8 as rate, false as director union all
  select 18 as per_id, 'John' as first_name, null as second_name, 'Fiedler' as last_name, parsedatetime('1925-02-03', 'yyyy-MM-dd', 'cest') as birthday, 8 as rate, false as director union all
  select 19 as per_id, 'Lee' as first_name, null as second_name, 'Cobb' as last_name, parsedatetime('1911-12-08', 'yyyy-MM-dd', 'cest') as birthday, 9 as rate, false as director
) where not exists (select * from person);

insert into movie (mov_id, title, description, premiere, rate, length, cou_cou_id, gen_gen_id, per_per_id)
select * from (
  select 1 as mov_id, 'The Shawshank Redemption' as title, 'Adaptation of a Stephen King short story. A banker who is wrongly sentenced to life imprisonment, tries to survive in a brutal prison world.' as description, parsedatetime('1994-09-10', 'yyyy-MM-dd', 'cest') as premiere, 9 as rate, 144 as length, 1 as country, 1 as genere, 1 as director union all
  select 2 as mov_id, 'Intouchables' as title, 'A paralyzed millionaire hires a young suburban boy who has just released from prison to care for him.' as description, parsedatetime('2011-09-23', 'yyyy-MM-dd', 'cest') as premiere, 9 as rate, 112 as length, 2 as country, 1 as genere, 5 as director union all
  select 3 as mov_id, 'The Green Mile' as title, 'A retired prison guard tells a friend about an extraorindary man who was sentenced to death for the murder of two 9-year-old girls.' as description, parsedatetime('1999-12-06', 'yyyy-MM-dd', 'cest'), 9 as rate, 188 as length, 1 as country, 1 as genere, 1 as director union all
  select 4 as mov_id, 'The Godfather' as title, 'A story about a New York Mafia family. An aging Don Corleone wants to hand over power to his son.' as description, parsedatetime('1972-03-15', 'yyyy-MM-dd', 'cest'), 9 as rate, 175 as length, 1 as country, 1 as genere, 12 as director union all
  select 5 as mov_id, '12 Angry Men' as title, 'Twelve jurors are to pronounce sentence in a murder trial. One of them has doubts about the defendant guilt.' as description, parsedatetime('1957-04-10', 'yyyy-MM-dd', 'cest'), 9 as rate, 96 as length, 1 as country, 2 as genere, 16 as director
) where not exists (select * from movie);

insert into actor (act_id, mov_mov_id, per_per_id)
select * from (
  select 1 as act_id, 1 as mov_mov_id, 2 as per_per_id union all
  select 2 as act_id, 1 as mov_mov_id, 3 as per_per_id union all
  select 3 as act_id, 1 as mov_mov_id, 4 as per_per_id union all
  select 4 as act_id, 2 as mov_mov_id, 6 as per_per_id union all
  select 5 as act_id, 2 as mov_mov_id, 7 as per_per_id union all
  select 6 as act_id, 2 as mov_mov_id, 8 as per_per_id union all
  select 7 as act_id, 3 as mov_mov_id, 9 as per_per_id union all
  select 8 as act_id, 3 as mov_mov_id, 10 as per_per_id union all
  select 9 as act_id, 3 as mov_mov_id, 11 as per_per_id union all
  select 10 as act_id, 4 as mov_mov_id, 13 as per_per_id union all
  select 11 as act_id, 4 as mov_mov_id, 14 as per_per_id union all
  select 12 as act_id, 4 as mov_mov_id, 15 as per_per_id union all
  select 13 as act_id, 5 as mov_mov_id, 17 as per_per_id union all
  select 14 as act_id, 5 as mov_mov_id, 18 as per_per_id union all
  select 15 as act_id, 5 as mov_mov_id, 19 as per_per_id
) where not exists (select * from actor);
