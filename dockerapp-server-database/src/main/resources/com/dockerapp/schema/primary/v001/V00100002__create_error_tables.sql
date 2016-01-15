create table error_code(
  id bigint primary key auto_increment,
  code varchar(64),
  unique(code)
);

insert into error_code (code) values
  ('NOT_AUTHORISED'), -- Invalid_Access
  ('FORBIDDEN'),
  ('STUB_ERROR_1'),
  ('STUB_ERROR_2'),
  ('STUB_ERROR_3'),
  ('STUB_ERROR_4');

create table error_cause (
	id bigint primary key auto_increment,
	error_code varchar(64)  not null,
	error_cause varchar(250) not null,
  order_hint int not null default 0,
  foreign key (error_code) references error_code(code)
);

create table error_action (
	id bigint primary key auto_increment,
	error_code varchar(64)  not null,
	error_action varchar(250) not null,
  order_hint int not null default 0,
  foreign key (error_code) references error_code(code)
);

insert into error_cause (error_code, order_hint, error_cause) values
	('STUB_ERROR_1', 0, 'Stub Cause Message 1');
insert into error_action (error_code, order_hint, error_action) values
	('STUB_ERROR_1', 0, 'STUB_ACTION_MESSAGE_1'),
	('STUB_ERROR_1', 1, 'STUB_ACTION_MESSAGE_2');

insert into error_cause (error_code, error_cause) values
	('NOT_AUTHORISED', 'The access credentials used are invalid');
insert into error_action (error_code, order_hint, error_action) values
	('NOT_AUTHORISED', 0, 'Try reverting to the previous password'),
	('NOT_AUTHORISED', 1, 'Contact US with this error message');

insert into error_cause (error_code, error_cause) values
	('STUB_ERROR_4', 'Stub Cause Message 1');
insert into error_action (error_code, error_action) values
	('STUB_ERROR_4', 'STUB_ACTION_MESSAGE_1');

insert into error_cause (error_code, error_cause) values
	('FORBIDDEN', 'You are trying to access a resource you do not have a permissions to access.');
insert into error_action (error_code, error_action) values
	('FORBIDDEN', 'Contact US with this error message.');