insert into _user(phone_number, password_validator, name, bio)
values('1', 'test', 'test_user', 'test');

insert into user_token(token, user_id, expires_at)
values('test_token', '1', '2040-05-09 00:11:12.908501');

insert into _user(phone_number, password_validator, name, bio)
values('2', 'test2', 'test_user2', 'test2');

insert into user_token(token, user_id, expires_at)
values('test_token2', '2', '2040-05-09 00:11:12.908501');

insert into chat_group(id, name)
values (1000, 'test');

insert into chat_group_member(user_id, group_id)
values ('1', 1000);

insert into chat_group_member(user_id, group_id)
values ('2', 1000);

insert into template(name, content)
values ('test_template', 'content');

insert into message(user_from, chat_to, content, template_name)
values ('1', 1000, 'hello_from_1', 'test_template');

insert into message(user_from, chat_to, content, template_name)
values ('2', 1000, 'hello_from_2', 'test_template');