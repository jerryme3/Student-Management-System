## Overview
- This is a Student Management System that is a CLI-based application. It handles both the teacher and student interfaces! A project made by me. Initially made without Maven but I decided to migrate it there so that I don't need to download multiple jar files online and manually put them in dependencies.

## Stacks used
1. Java - This is where the program is purely written with implementation of various design patterns and OOP.
2. PostgreSQL - Used to store persistent data in a database
3. JDBC - Used for the connectivity of Java and PGSQL
4. Maven - Acts for dependency management

## Features
# Teacher
  1. Sign up - lets the teachers to set up their accounts using @plv.edu.ph emails.
  2. Set up their basic information
  3. Sign in
  4. View profile
  5. Edit their informations (e.g., name, password, department)
  6. Edit a student information (e.g., password, gwa)
  7. View all enrolled student in the school
  8. View all enrolled student in a specific program/course in the school
  9. Search for a specific student for viewing
  10. Sign out
# Student
  1. Sign up - lets the students to set up their accounts using @plv.edu.ph emails.
  2. Set up their basic information
  3. Sign in
  4. View profile
  5. Edit their information (e.g., name, program, password)
  6. View the rankings (limited to top 10 only)
  7. Search for a student by their ID
  8. Delete their accounts
  9. Sign out

## How to set up
1. Clone the repository anywhere in your PC
2. Make sure that you have PostgreSQL or the PgAdmin 4 downloaded in your PC
3. Create an .env file that has these specifications:
  ```
  URL = url of your database
  USER = database username
  PASSWORD = password of your database
```
4. Create a file inside the directory where the program runs: src/main/java/com/jerme/sis and call the StudentRepository & TeacherRepository's createTable() methods there to create a table in your database
5. If you are using an IDE just hit the run button there and the program should run. If you are using IntelliJ just hit Alt+F10.

# Notes & Reminder
- Though the program stops the email duplication by row checking and do-while loop, I suggest you to run this query in the workbench of PgAdmin to avoid it and add a layer of security.
```ALTER TABLE table_name ADD CONSTRAINT uniq_const_name UNIQUE (email)```
- Also write your test accounts information to avoid forgetting them (as I do most of the time). The password is hashed in the database, so yeah it's hard to get the password back when you forgot it.
