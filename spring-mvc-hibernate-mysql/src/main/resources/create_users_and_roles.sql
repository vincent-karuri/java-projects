-- create tables
create table users(
  username varchar(50) not null primary key,
  password varchar(100) not null,
  enabled boolean not null
);

create table authorities (
  username varchar(50) not null,
  authority varchar(50) not null,
constraint fk_authorities_users foreign key(username) references users(username)
);

create unique index idx_auth_username on authorities (username,authority);

-- create admin user
insert into users(username,password,enabled)
values('admin','$2a$10$hbxecwitQQ.dDT4JOFzQAulNySFwEpaFLw38jda6Td.Y/cOiRzDFu',true);

insert into authorities(username,authority)
values('admin','ROLE_ADMIN');
