To get the UserAuth App to work do the following.

In mysql...

create database login;
use login;
create table auth (username VARCHAR(8), password VARCHAR(20));
create table record (username VARCHAR(8), login_time VARCHAR(30), logout_time VARCHAR(30));
insert into auth values ("ta","password123");
