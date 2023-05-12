create table if not exists _user(
    phone_number varchar(20) unique not null,
    password_validator varchar(100) not null,
    name varchar(40) not null,
    bio varchar(256)
);

create table if not exists user_token(
    token varchar primary key,
    user_id varchar references _user(phone_number),
    expires_at timestamp not null
);

create table if not exists chat_group(
    id serial primary key,
    name varchar(256),
    created_at timestamp default now() not null
);

create table if not exists template(
    name varchar primary key,
    content varchar not null
);

create table if not exists message(
    id serial primary key,
    obtained boolean default false not null,
    user_from varchar references _user(phone_number),
    chat_to int references chat_group(id),
    content varchar not null,
    template_name varchar references template(name),
    created_at timestamp default now() not null
);

create table if not exists chat_group_member(
    user_id varchar references _user(phone_number),
    group_id int references chat_group(id),
    primary key (user_id, group_id)
);








