SELECT * FROM dbvunguyen.student;

SELECT count(student_class_id) as count, student_class_id
FROM dbvunguyen.student 
group by student_class_id
having count = 1;


create database db_usermanager;
use db_usermanager;


create table users (
 id  int(3) NOT NULL AUTO_INCREMENT,
 name varchar(120) NOT NULL,
 email varchar(220) NOT NULL,
 country_id int,
 PRIMARY KEY (id),
 CONSTRAINT fk_country_id FOREIGN KEY (country_id)
    REFERENCES country(id)
);

create table country (
 id  int(3) NOT NULL AUTO_INCREMENT,
 name varchar(120) NOT NULL,
 PRIMARY KEY (id)
);
