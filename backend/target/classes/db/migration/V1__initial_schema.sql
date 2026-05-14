CREATE TABLE app_user (
  id varchar(36) PRIMARY KEY,
  name varchar(255) NOT NULL,
  email varchar(255) NOT NULL UNIQUE,
  password_hash varchar(255) NOT NULL,
  role enum('ADMIN','STUDENT') NOT NULL,
  created_at datetime(6) NOT NULL,
  updated_at datetime(6) NOT NULL
);

CREATE TABLE student (
  id varchar(36) PRIMARY KEY,
  name varchar(255) NOT NULL,
  email varchar(255) NOT NULL UNIQUE,
  created_at datetime(6) NOT NULL,
  updated_at datetime(6) NOT NULL,
  deleted_at datetime(6) NULL
);

CREATE TABLE course (
  id varchar(36) PRIMARY KEY,
  title varchar(255) NOT NULL,
  code varchar(255) NOT NULL UNIQUE,
  created_at datetime(6) NOT NULL,
  updated_at datetime(6) NOT NULL
);

CREATE TABLE profile (
  id varchar(36) PRIMARY KEY,
  bio varchar(255) NOT NULL,
  avatar_url varchar(255),
  student_id varchar(36) UNIQUE,
  created_at datetime(6) NOT NULL,
  updated_at datetime(6) NOT NULL,
  CONSTRAINT fk_profile_student FOREIGN KEY (student_id) REFERENCES student(id)
);

CREATE TABLE assignment (
  id varchar(36) PRIMARY KEY,
  title varchar(255) NOT NULL,
  due_date date NOT NULL,
  course_id varchar(36) NOT NULL,
  created_at datetime(6) NOT NULL,
  updated_at datetime(6) NOT NULL,
  CONSTRAINT fk_assignment_course FOREIGN KEY (course_id) REFERENCES course(id)
);

CREATE TABLE student_courses_course (
  student_id varchar(36) NOT NULL,
  course_id varchar(36) NOT NULL,
  PRIMARY KEY (student_id, course_id),
  CONSTRAINT fk_scc_student FOREIGN KEY (student_id) REFERENCES student(id),
  CONSTRAINT fk_scc_course FOREIGN KEY (course_id) REFERENCES course(id)
);
