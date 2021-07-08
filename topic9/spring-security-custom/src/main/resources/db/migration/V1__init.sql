create table role
(
    id         int primary key auto_increment,
    name varchar(30) not null
);

create table users
(
    id       int primary key auto_increment,
    login    varchar(30) not null,
    password varchar(40) not null,
    company varchar(40) not null,
    role_id int not null,
    constraint fk_user_to_role foreign key (role_id) references role(id),
    unique uniq_login (login)
);

create table permissions
(
    id         int primary key auto_increment,
    permission varchar(30) not null,
    unique uniq_permission (permission)
);

create table role_to_permissions (
    role_id int not null,
    permission_id int not null,
    constraint fk_user_to_permission_user foreign key (role_id) references role(id),
    constraint fk_user_to_permission_permission foreign key (permission_id) references permissions(id)
);

insert into role (name) values
('Admin'),
('User');

insert into users (login, password, company, role_id) values
    ('admin', 'password', 'admin-company', (select id from role where name = 'Admin')),
    ('catalog', 'password', 'catalog-company', (select id from role where name = 'User')),
    ('user', 'password', 'user-company', (select id from role where name = 'User'));

insert into permissions (permission) values
    ('VIEW_ADMIN'),
    ('VIEW_CATALOG');

insert into role_to_permissions (role_id, permission_id) values
    ((select id from role where name = 'Admin'), (select id from permissions where permission = 'VIEW_ADMIN')),
    ((select id from role where name = 'Admin'), (select id from permissions where permission = 'VIEW_CATALOG')),

    ((select id from role where name = 'User'), (select id from permissions where permission = 'VIEW_CATALOG'));