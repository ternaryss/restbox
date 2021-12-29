--- H2 migration for version 1.0.0
---
--- @author TSS

create table if not exists genere (
  gen_id int auto_increment primary key,
  name varchar not null,
  act boolean not null default true,
  create_date timestamp not null default current_timestamp,
  modify_date timestamp not null default current_timestamp,
  unique key genere_unique_name (name)
);

create table if not exists country (
  cou_id int auto_increment primary key,
  name varchar not null,
  act boolean not null default true,
  create_date timestamp not null default current_timestamp,
  modify_date timestamp not null default current_timestamp,
  unique key country_unique_name (name)
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
  foreign key (cou_cou_id) references country (cou_id),
  foreign key (gen_gen_id) references genere (gen_id),
  foreign key (per_per_id) references person (per_id)
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
  select 4 as gen_id, 'Fiction' as name union all
  select 5 as gen_id, 'Action' as name union all
  select 6 as gen_id, 'Sci-Fi' as name union all
  select 7 as gen_id, 'Romance' as name union all
  select 8 as gen id, 'Adventure' as name union all
  select 9 as gen_id, 'Horror' as name union all
  select 10 as gen_id, 'Animation' as name
) where not exists (select * from genere);

insert into country (cou_id, name)
select * from (
  select 1 as cou_id, 'USA' as name union all
  select 2 as cou_id, 'France' as name union all
  select 3 as cou_id, 'New Zealand' as name union all
  select 4 as cou_id, 'Canada' as name union all
  select 5 as cou_id, 'UK' as name
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
  select 19 as per_id, 'Lee' as first_name, null as second_name, 'Cobb' as last_name, parsedatetime('1911-12-08', 'yyyy-MM-dd', 'cest') as birthday, 9 as rate, false as director union all
  select 20 as per_id, 'Lana' as first_name, null as second_name, 'Wachowski' as last_name, parsedatetime('1965-06-21','yyyy-MM-dd','cest') as birthday, 8 as rate, true as director union all
  select 21 as per_id, 'Adam' as first_name, null as second_name, 'McKay' as last_name, parsedatetime('1968-04-17','yyyy-MM-dd','cest') as birthday, 8 as rate, true as director union all
  select 22 as per_id, 'Anthony' as first_name, null as second_name, 'Russo' as last_name, parsedatetime('1970-02-03','yyyy-MM-dd','cest') as birthday, 8 as rate, true as director union all
  select 23 as per_id, 'Denis' as first_name, null as second_name, 'Villeneuve' as last_name, parsedatetime('1967-10-03','yyyy-MM-dd','cest') as birthday, 8  as rate, true as director union all
  select 24 as per_id, 'Rawson' as first_name, 'Marshall' as second_name, 'Thurber' as last_name, parsedatetime('1975-02-09','yyyy-MM-dd','cest') as birthday, 6 as rate, true as director union all
  select 25 as per_id, 'Chloe' as first_name, null as second_name, 'Zhao' as last_name, parsedatetime('1982-03-21','yyyy-MM-dd','cest') as birthday, 8 as rate, true as director union all
  select 26 as per_id, 'Kenneth' as first_name, null as second_name, 'Branagh' as last_name, parsedatetime('1960-12-10','yyyy-MM-dd','cest') as birthday, 8 as rate, true as director union all
  select 27 as per_id, 'Taika' as first_name, null as second_name, 'Waititi' as last_name, parsedatetime('1975-08-16','yyyy-MM-dd','cest') as birthday, 8 as rate, true as director union all
  select 28 as per_id, 'Jim' as first_name, null as second_name, 'Jarmusch' as last_name, parsedatetime('1953-01-22','yyyy-MM-dd','cest') as birthday, 8 as rate, true as director union all
  select 29 as per_id, 'Guillermo' as first_name, null as second_name, 'del Toro' as last_name, parsedatetime('1964-10-09','yyyy-MM-dd','cest') as birthday, 7 as rate, true as director union all
  select 30 as per_id, 'Tim' as first_name, null as second_name, 'Burton' as last_name, parsedatetime('1958-08-25','yyyy-MM-dd','cest') as birthday, 8 as rate, true as director union all
  select 31 as per_id, 'Keanu' as first_name, null as second_name, 'Reeves' as last_name, parsedatetime('1964-08-02','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 32 as per_id, 'Carrie-Anne' as first_name, null as second_name, 'Moss' as last_name, parsedatetime('1967-08-21','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 33 as per_id, 'Leonardo' as first_name, null as second_name, 'DiCaprio' as last_name, parsedatetime('1974-11-11','yyyy-MM-dd','cest') as birthday, 9 as rate, false as director union all
  select 34 as per_id, 'Jennifer' as first_name, null as second_name, 'Lawrence' as last_name, parsedatetime('1990-08-15','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 35 as per_id, 'Meryl' as first_name, null as second_name, 'Streep' as last_name, parsedatetime('1949-06-22','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 36 as per_id, 'Cate' as first_name, null as second_name, 'Blanchett' as last_name, parsedatetime('1969-05-14','yyyy-MM-dd','cest') as birthday, 9 as rate, false as director union all
  select 37 as per_id, 'Timothee' as first_name, null as second_name, 'Chalamet' as last_name, parsedatetime('1995-12-27','yyyy-MM-dd','cest') as birthday, 9 as rate, false as director union all
  select 38 as per_id, 'Rebecca' as first_name, null as second_name, 'Ferguson' as last_name, parsedatetime('1983-10-19','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 39 as per_id, 'Oscar' as first_name, null as second_name, 'Isaac' as last_name, parsedatetime('1979-03-09','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 40 as per_id, 'Jason' as first_name, null as second_name, 'Momoa' as last_name, parsedatetime('1979-08-01','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 41 as per_id, 'Dwayne' as first_name, null as second_name, 'Johnson' as last_name, parsedatetime('1972-05-02','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 42 as per_id, 'Ryan' as first_name, null as second_name, 'Reynolds' as last_name, parsedatetime('1976-10-23','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 43 as per_id, 'Gal' as first_name, null as second_name, 'Gadot' as last_name, parsedatetime('1985-04-30','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 44 as per_id, 'Angelina' as first_name, null as second_name, 'Jolie' as last_name, parsedatetime('1975-06-04','yyyy-MM-dd','cest') as birthday, 9 as rate, false as director union all
  select 45 as per_id, 'Salma' as first_name, null as second_name, 'Hayek' as last_name, parsedatetime('1966-09-02','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 46 as per_id, 'Kit' as first_name, null as second_name, 'Harrington' as last_name, parsedatetime('1986-12-26','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 47 as per_id, 'Richard' as first_name, null as second_name, 'Madden' as last_name, parsedatetime('1986-06-18','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 48 as per_id, 'Kumail' as first_name, null as second_name, 'Nanjiani' as last_name, parsedatetime('1978-02-21','yyyy-MM-dd','cest') as birthday, 7 as rate, false as director union all
  select 49 as per_id, 'Helena' as first_name, 'Bonham' as second_name, 'Carter' as last_name, parsedatetime('1966-05-26','yyyy-MM-dd','cest') as birthday, 9 as rate, false as director union all
  select 50 as per_id, 'Lily' as first_name, null as second_name, 'James' as last_name, parsedatetime('1989-04-05','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 51 as per_id, 'Chris' as first_name, null as second_name, 'Hemsworth' as last_name, parsedatetime('1983-08-11','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 52 as per_id, 'Tom' as first_name, null as second_name, 'Hiddlestone' as last_name, parsedatetime('1981-02-09','yyyy-MM-dd','cest') as birthday, 9 as rate, false as director union all
  select 53 as per_id, 'Idris' as first_name, null as second_name, 'Elba' as last_name, parsedatetime('1972-09-06','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 54 as per_id, 'Tilda' as first_name, null as second_name, 'Swinton' as last_name, parsedatetime('1960-11-05','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 55 as per_id, 'Mia' as first_name, null as second_name, 'Wasikowska' as last_name, parsedatetime('1989-10-14','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 56 as per_id, 'Anne' as first_name, null as second_name, 'Hathaway' as last_name, parsedatetime('1982-11-12','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 57 as per_id, 'Emily' as first_name, null as second_name, 'Watson' as last_name, parsedatetime('1967-01-14','yyyy-MM-dd','cest') as birthday, 8 as rate, false as director union all
  select 58 as per_id, 'Johnny' as first_name, null as second_name, 'Depp' as last_name, parsedatetime('1963-06-09','yyyy-MM-dd','cest') as birthday, 9 as rate, false as director
) where not exists (select * from person);

insert into movie (mov_id, title, description, premiere, rate, length, cou_cou_id, gen_gen_id, per_per_id)
select * from (
  select 1 as mov_id, 'The Shawshank Redemption' as title, 'Adaptation of a Stephen King short story. A banker who is wrongly sentenced to life imprisonment, tries to survive in a brutal prison world.' as description, parsedatetime('1994-09-10', 'yyyy-MM-dd', 'cest') as premiere, 9 as rate, 144 as length, 1 as country, 1 as genere, 1 as director union all
  select 2 as mov_id, 'Intouchables' as title, 'A paralyzed millionaire hires a young suburban boy who has just released from prison to care for him.' as description, parsedatetime('2011-09-23', 'yyyy-MM-dd', 'cest') as premiere, 9 as rate, 112 as length, 2 as country, 1 as genere, 5 as director union all
  select 3 as mov_id, 'The Green Mile' as title, 'A retired prison guard tells a friend about an extraorindary man who was sentenced to death for the murder of two 9-year-old girls.' as description, parsedatetime('1999-12-06', 'yyyy-MM-dd', 'cest'), 9 as rate, 188 as length, 1 as country, 1 as genere, 1 as director union all
  select 4 as mov_id, 'The Godfather' as title, 'A story about a New York Mafia family. An aging Don Corleone wants to hand over power to his son.' as description, parsedatetime('1972-03-15', 'yyyy-MM-dd', 'cest'), 9 as rate, 175 as length, 1 as country, 1 as genere, 12 as director union all
  select 5 as mov_id, '12 Angry Men' as title, 'Twelve jurors are to pronounce sentence in a murder trial. One of them has doubts about the defendant guilt.' as description, parsedatetime('1957-04-10', 'yyyy-MM-dd', 'cest'), 9 as rate, 96 as length, 1 as country, 2 as genere, 16 as director union all
  select 6 as mov_id, 'The Matrix Ressurections' as title, 'To find out if his reality is a physical or mental construct, Mr. Anderson, aka Neo, will have to choose to follow the white rabbit once more. If he\'s learned anything, it\'s that choice, while an illusion, is still the only way out of -- or into -- the Matrix. Neo already knows what he has to do, but what he doesn\'t yet know is that the Matrix is stronger, more secure and far more dangerous than ever before.' as description, parsedatetime('2021-12-22', 'yyyy-MM-dd', 'cest'), 5 as rate, 148 as lenght, 1 as country, 6 as genre, 21 as director union all
  select 7 as mov_id, 'Don\'t look up' as title, 'Kate Dibiasky, an astronomy grad student, and her professor Dr. Randall Mindy make an astounding discovery of a comet orbiting within the solar system. The problem: it\'s on a direct collision course with Earth. The other problem? No one really seems to care. With the help of Dr. Oglethorpe, Kate and Randall embark on a media tour that takes them from the office of an indifferent President Orlean and her sycophantic son and Chief of Staff, Jason, to the airwaves of The Daily Rip, an upbeat morning show hosted by Brie and Jack. With only six months until the comet makes impact, managing the 24-hour news cycle and gaining the attention of the social media obsessed public before it\'s too late proves shockingly comical -- what will it take to get the world to just look up?!' as description, parsedatetime('2021-12-10', 'yyyy-MM-dd', 'cest'), 7 as rate, 145 as lenght, 1 as country, 3 as genre, 21 as director union all
  select 8 as mov_id, 'Avengers: Endgame' as title, 'Adrift in space with no food or water, Tony Stark sends a message to Pepper Potts as his oxygen supply starts to dwindle. Meanwhile, the remaining Avengers -- Thor, Black Widow, Captain America and Bruce Banner -- must figure out a way to bring back their vanquished allies for an epic showdown with Thanos -- the evil demigod who decimated the planet and the universe.' as description, parsedatetime('2019-04-26', 'yyyy-MM-dd', 'cest'), 8 as rate, 182 as lenght, 1 as country, 5 as genre, 22 as director union all
  select 9 as mov_id, 'Dune' as title, 'Paul Atreides, a brilliant and gifted young man born into a great destiny beyond his understanding, must travel to the most dangerous planet in the universe to ensure the future of his family and his people. As malevolent forces explode into conflict over the planet\'s exclusive supply of the most precious resource in existence, only those who can conquer their own fear will survive.' as description, parsedatetime('2021-10-22', 'yyyy-MM-dd', 'cest'), 6 as rate, 155 as lenght, 4 as country, 6 as genre, 23 as director union all
  select 10 as mov_id, 'Red notice' as title, 'In the world of international crime, an Interpol agent attempts to hunt down and capture the world\'s most wanted art thief.' as description, parsedatetime('2021-11-05', 'yyyy-MM-dd', 'cest'), 6 as rate, 118 as lenght, 1 as country, 5 as genre, 24 as director union all
  select 11 as mov_id, 'Eternals' as title, 'Marvel Studios\' Eternals features an exciting new team of Super Heroes in the Marvel Cinematic Universe, ancient aliens who have been living on Earth in secret for thousands of years. Following the events of Avengers: Endgame, an unexpected tragedy forces them out of the shadows to reunite against mankind\'s most ancient enemy, the Deviants.' as description, parsedatetime('2021-11-05', 'yyyy-MM-dd', 'cest'), 7 as rate, 157 as lenght, 1 as country, 5 as genre, 25 as director union all
  select 12 as mov_id, 'Cindirella' as title, 'After her father unexpectedly dies, young Ella (Lily James) finds herself at the mercy of her cruel stepmother (Cate Blanchett) and stepsisters, who reduce her to scullery maid. Despite her circumstances, she refuses to despair. An invitation to a palace ball gives Ella hope that she might reunite with the dashing stranger (Richard Madden) she met in the woods, but her stepmother prevents her from going. Help arrives in the form of a kindly beggar woman who has a magic touch for ordinary things.' as description, parsedatetime('2015-03-13', 'yyyy-MM-dd', 'cest'), 7 as rate, 165 as lenght, 1 as country, 7 as genre, 26 as director union all
  select 13 as mov_id, 'Thor: Ragnarok' as title, 'Imprisoned on the other side of the universe, the mighty Thor finds himself in a deadly gladiatorial contest that pits him against the Hulk, his former ally and fellow Avenger. Thor\'s quest for survival leads him in a race against time to prevent the all-powerful Hela from destroying his home world and the Asgardian civilization.' as description, parsedatetime('2017-11-03', 'yyyy-MM-dd', 'cest'), 8 as rate, 148 as lenght, 1 as country, 8 as genre, 27 as director union all
  select 14 as mov_id, 'Only lovers left alive' as title, 'Artistic, sophisticated and centuries old, two vampire lovers (Tilda Swinton, Tom Hiddleston) ponder their ultimate place in modern society.' as description, parsedatetime('2014-04-11', 'yyyy-MM-dd', 'cest'), 7 as rate, 122 as lenght, 2 as country, 9 as genre, 28 as director union all
  select 15 as mov_id, 'Crimson Peak' as title, 'After marrying the charming and seductive Sir Thomas Sharpe, young Edith (Mia Wasikowska) finds herself swept away to his remote gothic mansion in the English hills. Also living there is Lady Lucille, Thomas\' alluring sister and protector of her family\'s dark secrets. Able to communicate with the dead, Edith tries to decipher the mystery behind the ghostly visions that haunt her new home. As she comes closer to the truth, Edith may learn that true monsters are made of flesh and blood.' as description, parsedatetime('2015-10-16', 'yyyy-MM-dd', 'cest'), 6 as rate, 119 as lenght, 1 as country, 9 as genre, 29 as director union all
  select 16 as mov_id, 'Alice in Wonderland' as title, 'A young girl when she first visited magical Underland, Alice Kingsleigh (Mia Wasikowska) is now a teenager with no memory of the place -- except in her dreams. Her life takes a turn for the unexpected when, at a garden party for her fiance and herself, she spots a certain white rabbit and tumbles down a hole after him. Reunited with her friends the Mad Hatter (Johnny Depp), the Cheshire Cat and others, Alice learns it is her destiny to end the Red Queen\'s (Helena Bonham Carter) reign of terror.' as description, parsedatetime('2010-03-05', 'yyyy-MM-dd', 'cest'), 7 as rate, 108 as lenght, 1 as country, 8 as genre, 30 as director union all
  select 17 as mov_id, 'Corpse Bride' as title, 'Victor (Johnny Depp) and Victoria\'s (Emily Watson) families have arranged their marriage. Though they like each other, Victor is nervous about the ceremony. While he\'s in a forest practicing his lines for the wedding, a tree branch becomes a hand that drags him to the land of the dead. It belongs to Emily, who was murdered after eloping with her love and wants to marry Victor. Victor must get back aboveground before Victoria marries the villainous Barkis Bittern (Richard E. Grant).' as description, parsedatetime('2006-08-23', 'yyyy-MM-dd', 'cest'), 8 as rate, 76 as lenght, 5 as country, 10 as genre, 30 as director union all
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
  select 15 as act_id, 5 as mov_mov_id, 19 as per_per_id union all
  select 16 as act_id, 6 as mov_mov_id, 31 as per_per_id union all
  select 17 as act_id, 6 as mov_mov_id, 32 as per_per_id union all
  select 18 as act_id, 7 as mov_mov_id, 33 as per_per_id union all
  select 19 as act_id, 7 as mov_mov_id, 34 as per_per_id union all
  select 20 as act_id, 7 as mov_mov_id, 35 as per_per_id union all
  select 21 as act_id, 7 as mov_mov_id, 36 as per_per_id union all
  select 22 as act_id, 13 as mov_mov_id, 36 as per_per_id union all
  select 23 as act_id, 7 as mov_mov_id, 37 as per_per_id union all
  select 24 as act_id, 9 as mov_mov_id, 37 as per_per_id union all
  select 25 as act_id, 9 as mov_mov_id, 38 as per_per_id union all
  select 26 as act_id, 9 as mov_mov_id, 39 as per_per_id union all
  select 27 as act_id, 9 as mov_mov_id, 40 as per_per_id union all
  select 28 as act_id, 10 as mov_mov_id, 41 as per_per_id union all
  select 29 as act_id, 10 as mov_mov_id, 42 as per_per_id union all
  select 30 as act_id, 10 as mov_mov_id, 43 as per_per_id union all
  select 31 as act_id, 11 as mov_mov_id, 44 as per_per_id union all
  select 32 as act_id, 11 as mov_mov_id, 45 as per_per_id union all
  select 33 as act_id, 11 as mov_mov_id, 46 as per_per_id union all
  select 34 as act_id, 11 as mov_mov_id, 47 as per_per_id union all
  select 35 as act_id, 12 as mov_mov_id, 47 as per_per_id union all
  select 36 as act_id, 11 as mov_mov_id, 48 as per_per_id union all
  select 37 as act_id, 12 as mov_mov_id, 49 as per_per_id union all
  select 38 as act_id, 16 as mov_mov_id, 49 as per_per_id union all
  select 39 as act_id, 17 as mov_mov_id, 49 as per_per_id union all
  select 40 as act_id, 12 as mov_mov_id, 50 as per_per_id union all
  select 41 as act_id, 8 as mov_mov_id, 51 as per_per_id union all
  select 42 as act_id, 13 as mov_mov_id, 51 as per_per_id union all
  select 43 as act_id, 8 as mov_mov_id, 52 as per_per_id union all
  select 44 as act_id, 13 as mov_mov_id, 52 as per_per_id union all
  select 45 as act_id, 14 as mov_mov_id, 52 as per_per_id union all
  select 46 as act_id, 15 as mov_mov_id, 52 as per_per_id union all
  select 47 as act_id, 13 as mov_mov_id, 53 as per_per_id union all
  select 48 as act_id, 13 as mov_mov_id, 54 as per_per_id union all
  select 49 as act_id, 15 as mov_mov_id, 54 as per_per_id union all
  select 50 as act_id, 14 as mov_mov_id, 55 as per_per_id union all
  select 51 as act_id, 15 as mov_mov_id, 55 as per_per_id union all
  select 52 as act_id, 16 as mov_mov_id, 55 as per_per_id union all
  select 53 as act_id, 16 as mov_mov_id, 56 as per_per_id union all
  select 54 as act_id, 17 as mov_mov_id, 57 as per_per_id union all
  select 55 as act_id, 16 as mov_mov_id, 58 as per_per_id union all
  select 56 as act_id, 17 as mov_mov_id, 58 as per_per_id
) where not exists (select * from actor);
