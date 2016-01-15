create table authentication_method(
	name varchar(32) not null,
	unique(name)
);

insert into authentication_method values
	('ENCODE_64');

