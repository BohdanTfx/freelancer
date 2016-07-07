--
-- Скрипт сгенерирован Devart dbForge Studio for MySQL, Версия 6.3.358.0
-- Домашняя страница продукта: http://www.devart.com/ru/dbforge/mysql/studio
-- Дата скрипта: 20.02.2016 15:43:11
-- Версия сервера: 5.5.46-0ubuntu0.14.04.2
-- Версия клиента: 4.1
--


-- 
-- Отключение внешних ключей
-- 
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;

-- 
-- Установить режим SQL (SQL mode)
-- 
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 
-- Установка кодировки, с использованием которой клиент будет посылать запросы на сервер
--
SET NAMES 'utf8';

-- 
-- Установка базы данных по умолчанию
--
USE freelancerdb;

--
-- Описание для таблицы admin
--
DROP TABLE IF EXISTS admin;
CREATE TABLE admin (
  id INT(11) NOT NULL AUTO_INCREMENT,
  email VARCHAR(50) DEFAULT NULL,
  password VARCHAR(255) NOT NULL,
  name VARCHAR(50) DEFAULT NULL,
  last_name VARCHAR(50) DEFAULT NULL,
  lang ENUM('en','uk-UA') DEFAULT 'en',
  reg_url VARCHAR(150) DEFAULT NULL,
  reg_date DATETIME DEFAULT NULL,
  uuid VARCHAR(140) DEFAULT NULL,
  version INT(11) DEFAULT 0,
  is_deleted BIT(1) DEFAULT b'0',
  salt VARCHAR(50) DEFAULT NULL,
  img_url VARCHAR(255) DEFAULT NULL,
  send_email VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX reg_url (reg_url),
  UNIQUE INDEX uuid (uuid)
)
ENGINE = INNODB
AUTO_INCREMENT = 3
AVG_ROW_LENGTH = 16384
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы admin_candidate
--
DROP TABLE IF EXISTS admin_candidate;
CREATE TABLE admin_candidate (
  id INT(11) NOT NULL AUTO_INCREMENT,
  email VARCHAR(50) DEFAULT NULL,
  access_key VARCHAR(70) DEFAULT NULL,
  is_deleted BIT(1) DEFAULT b'0',
  version INT(11) DEFAULT NULL,
  PRIMARY KEY (id)
)
ENGINE = INNODB
AUTO_INCREMENT = 14
AVG_ROW_LENGTH = 8192
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы customer
--
DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
  id INT(11) NOT NULL AUTO_INCREMENT,
  email VARCHAR(50) NOT NULL,
  password VARCHAR(255) NOT NULL,
  name VARCHAR(50) DEFAULT NULL,
  last_name VARCHAR(50) NOT NULL,
  zone INT(11) DEFAULT NULL,
  lang ENUM('en','uk-UA') DEFAULT 'en',
  uuid VARCHAR(140) DEFAULT NULL,
  reg_url VARCHAR(150) DEFAULT NULL,
  reg_date DATETIME DEFAULT NULL,
  version INT(11) DEFAULT 0,
  is_deleted BIT(1) DEFAULT b'0',
  salt VARCHAR(50) DEFAULT NULL,
  img_url VARCHAR(255) DEFAULT NULL,
  overview VARCHAR(3000) DEFAULT NULL,
  send_email VARCHAR(50) DEFAULT NULL,
  confirm_code VARCHAR(50) DEFAULT NULL,
  is_first BIT(1) DEFAULT b'0',
  PRIMARY KEY (id),
  UNIQUE INDEX email (email),
  UNIQUE INDEX reg_url (reg_url),
  UNIQUE INDEX uuid (uuid)
)
ENGINE = INNODB
AUTO_INCREMENT = 34
AVG_ROW_LENGTH = 2340
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы developer
--
DROP TABLE IF EXISTS developer;
CREATE TABLE developer (
  id INT(11) NOT NULL AUTO_INCREMENT,
  email VARCHAR(50) NOT NULL,
  password VARCHAR(255) NOT NULL,
  name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  hourly DOUBLE DEFAULT NULL,
  zone INT(11) DEFAULT NULL,
  lang ENUM('en','uk-UA') DEFAULT 'en',
  uuid VARCHAR(140) DEFAULT NULL,
  reg_url VARCHAR(150) DEFAULT NULL,
  reg_date DATETIME DEFAULT NULL,
  version INT(11) DEFAULT 0,
  is_deleted BIT(1) DEFAULT b'0',
  salt VARCHAR(50) DEFAULT NULL,
  img_url VARCHAR(255) DEFAULT NULL,
  overview VARCHAR(3000) DEFAULT NULL,
  `position` VARCHAR(50) DEFAULT NULL,
  send_email VARCHAR(50) DEFAULT NULL,
  confirm_code VARCHAR(50) DEFAULT NULL,
  is_first BIT(1) DEFAULT b'0',
  PRIMARY KEY (id),
  UNIQUE INDEX developer_PK (email),
  UNIQUE INDEX reg_url (reg_url),
  UNIQUE INDEX uuid (uuid)
)
ENGINE = INNODB
AUTO_INCREMENT = 65
AVG_ROW_LENGTH = 1092
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы order_count
--
DROP TABLE IF EXISTS order_count;
CREATE TABLE order_count (
  id INT(11) NOT NULL AUTO_INCREMENT,
  date DATE DEFAULT NULL,
  count INT(11) DEFAULT NULL,
  is_deleted BIT(1) DEFAULT b'0',
  PRIMARY KEY (id)
)
ENGINE = INNODB
AUTO_INCREMENT = 12
AVG_ROW_LENGTH = 2340
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы technology
--
DROP TABLE IF EXISTS technology;
CREATE TABLE technology (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  version INT(11) DEFAULT 0,
  is_deleted BIT(1) DEFAULT b'0',
  PRIMARY KEY (id)
)
ENGINE = INNODB
AUTO_INCREMENT = 18
AVG_ROW_LENGTH = 1489
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы contact
--
DROP TABLE IF EXISTS contact;
CREATE TABLE contact (
  id INT(11) NOT NULL AUTO_INCREMENT,
  cust_id INT(11) DEFAULT NULL,
  dev_id INT(11) DEFAULT NULL,
  phone VARCHAR(15) DEFAULT NULL,
  skype VARCHAR(255) DEFAULT NULL,
  version INT(11) DEFAULT 0,
  is_deleted BIT(1) DEFAULT b'0',
  PRIMARY KEY (id),
  CONSTRAINT FK_contact_customer_id FOREIGN KEY (cust_id)
    REFERENCES customer(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_contact_developer_id FOREIGN KEY (dev_id)
    REFERENCES developer(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 25
AVG_ROW_LENGTH = 1092
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы dev_tech
--
DROP TABLE IF EXISTS dev_tech;
CREATE TABLE dev_tech (
  id INT(11) NOT NULL AUTO_INCREMENT,
  dev_id INT(11) DEFAULT NULL,
  tech_id INT(11) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_dev_tech_developer_id FOREIGN KEY (dev_id)
    REFERENCES developer(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_dev_tech_technology_id FOREIGN KEY (tech_id)
    REFERENCES technology(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 57
AVG_ROW_LENGTH = 862
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы feedback
--
DROP TABLE IF EXISTS feedback;
CREATE TABLE feedback (
  id INT(11) NOT NULL AUTO_INCREMENT,
  dev_id INT(11) DEFAULT NULL,
  cust_id INT(11) DEFAULT NULL,
  comment VARCHAR(1000) DEFAULT NULL,
  rate INT(11) DEFAULT 0,
  author ENUM('dev','customer') NOT NULL,
  version INT(11) DEFAULT 0,
  is_deleted BIT(1) DEFAULT b'0',
  date DATETIME DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_feedback_customer_id FOREIGN KEY (cust_id)
    REFERENCES customer(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_feedback_developer_id FOREIGN KEY (dev_id)
    REFERENCES developer(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 173
AVG_ROW_LENGTH = 4096
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы ordering
--
DROP TABLE IF EXISTS ordering;
CREATE TABLE ordering (
  id INT(11) NOT NULL AUTO_INCREMENT,
  title VARCHAR(120) NOT NULL,
  pay_type ENUM('hourly','fixed') DEFAULT 'fixed',
  descr VARCHAR(3000) DEFAULT NULL,
  customer_id INT(11) DEFAULT NULL,
  date DATETIME NOT NULL,
  payment DOUBLE DEFAULT NULL,
  started BIT(1) DEFAULT b'0',
  started_date DATETIME DEFAULT NULL,
  ended BIT(1) DEFAULT b'0',
  ended_date DATETIME DEFAULT NULL,
  private BIT(1) DEFAULT b'0',
  version INT(11) DEFAULT 0,
  is_deleted BIT(1) DEFAULT b'0',
  zone TINYINT(4) DEFAULT NULL,
  ban BIT(1) DEFAULT b'0',
  complains INT(11) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_ordering_customer_id FOREIGN KEY (customer_id)
    REFERENCES customer(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 20
AVG_ROW_LENGTH = 2730
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы question
--
DROP TABLE IF EXISTS question;
CREATE TABLE question (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  tech_id INT(11) DEFAULT NULL,
  admin_id INT(11) DEFAULT NULL,
  multiple BIT(1) DEFAULT NULL,
  version INT(11) DEFAULT 0,
  is_deleted BIT(1) DEFAULT b'0',
  PRIMARY KEY (id),
  CONSTRAINT FK_question_admin_id FOREIGN KEY (admin_id)
    REFERENCES admin(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_question_technology_id FOREIGN KEY (tech_id)
    REFERENCES technology(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 24
AVG_ROW_LENGTH = 1638
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы test
--
DROP TABLE IF EXISTS test;
CREATE TABLE test (
  id INT(11) NOT NULL AUTO_INCREMENT,
  tech_id INT(11) DEFAULT NULL,
  name VARCHAR(50) DEFAULT NULL,
  admin_id INT(11) DEFAULT NULL,
  pass_score TINYINT(4) DEFAULT NULL,
  sec_per_quest INT(11) DEFAULT NULL,
  version INT(11) DEFAULT 0,
  is_deleted BIT(1) DEFAULT b'0',
  PRIMARY KEY (id),
  CONSTRAINT FK_test_admin_id FOREIGN KEY (admin_id)
    REFERENCES admin(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_test_technology_id FOREIGN KEY (tech_id)
    REFERENCES technology(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 10
AVG_ROW_LENGTH = 1820
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы answer
--
DROP TABLE IF EXISTS answer;
CREATE TABLE answer (
  id INT(11) NOT NULL AUTO_INCREMENT,
  quest_id INT(11) DEFAULT NULL,
  correct BIT(1) NOT NULL DEFAULT b'0',
  name VARCHAR(100) DEFAULT NULL,
  version INT(11) DEFAULT 0,
  is_deleted BIT(1) DEFAULT b'0',
  PRIMARY KEY (id),
  CONSTRAINT FK_answer_question_id FOREIGN KEY (quest_id)
    REFERENCES question(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 35
AVG_ROW_LENGTH = 606
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы complaint
--
DROP TABLE IF EXISTS complaint;
CREATE TABLE complaint (
  id INT(11) NOT NULL AUTO_INCREMENT,
  order_id INT(11) NOT NULL,
  dev_id INT(11) NOT NULL,
  isDeleted INT(11) DEFAULT 0,
  version INT(11) DEFAULT 0,
  PRIMARY KEY (id),
  INDEX dev_id (dev_id),
  INDEX order_id (order_id),
  CONSTRAINT complaint_ibfk_1 FOREIGN KEY (order_id)
    REFERENCES ordering(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT complaint_ibfk_2 FOREIGN KEY (dev_id)
    REFERENCES developer(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 45
AVG_ROW_LENGTH = 528
CHARACTER SET utf8
COLLATE utf8_unicode_ci;

--
-- Описание для таблицы dev_test
--
DROP TABLE IF EXISTS dev_test;
CREATE TABLE dev_test (
  id INT(11) NOT NULL AUTO_INCREMENT,
  dev_id INT(11) DEFAULT NULL,
  test_id INT(11) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_dev_test_developer_id FOREIGN KEY (dev_id)
    REFERENCES developer(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_dev_test_test_id FOREIGN KEY (test_id)
    REFERENCES test(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 1
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы developer_qa
--
DROP TABLE IF EXISTS developer_qa;
CREATE TABLE developer_qa (
  id INT(11) NOT NULL AUTO_INCREMENT,
  dev_id INT(11) DEFAULT NULL,
  test_id INT(11) DEFAULT NULL,
  rate DOUBLE DEFAULT 0,
  expire DATE DEFAULT NULL,
  is_expire BIT(1) DEFAULT b'0',
  version INT(11) DEFAULT 0,
  is_deleted BIT(1) DEFAULT b'0',
  PRIMARY KEY (id),
  INDEX FK_developer_qa_technology_id (test_id),
  CONSTRAINT FK_developer_qa_developer_id FOREIGN KEY (dev_id)
    REFERENCES developer(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_developer_qa_test_id FOREIGN KEY (test_id)
    REFERENCES test(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 15
AVG_ROW_LENGTH = 1170
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы follower
--
DROP TABLE IF EXISTS follower;
CREATE TABLE follower (
  id INT(11) NOT NULL AUTO_INCREMENT,
  dev_id INT(11) DEFAULT NULL,
  message VARCHAR(1000) DEFAULT NULL,
  order_id INT(11) DEFAULT NULL,
  version INT(11) DEFAULT 0,
  is_deleted BIT(1) DEFAULT b'0',
  is_hired BIT(1) DEFAULT NULL,
  cust_id INT(11) DEFAULT NULL,
  author ENUM('dev','customer') DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_follower_customer_id FOREIGN KEY (cust_id)
    REFERENCES customer(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_follower_developer_id FOREIGN KEY (dev_id)
    REFERENCES developer(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_follower_ordering_id FOREIGN KEY (order_id)
    REFERENCES ordering(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 113
AVG_ROW_LENGTH = 1170
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы ordering_technology
--
DROP TABLE IF EXISTS ordering_technology;
CREATE TABLE ordering_technology (
  id INT(11) NOT NULL AUTO_INCREMENT,
  order_id INT(11) DEFAULT NULL,
  tech_id INT(11) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_ordering_technology_ordering_id FOREIGN KEY (order_id)
    REFERENCES ordering(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_ordering_technology_technology_id FOREIGN KEY (tech_id)
    REFERENCES technology(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 59
AVG_ROW_LENGTH = 303
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы test_question
--
DROP TABLE IF EXISTS test_question;
CREATE TABLE test_question (
  id INT(11) NOT NULL AUTO_INCREMENT,
  test_id INT(11) DEFAULT NULL,
  quest_id INT(11) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_test_question_question_id FOREIGN KEY (quest_id)
    REFERENCES question(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_test_question_test_id FOREIGN KEY (test_id)
    REFERENCES test(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 16
AVG_ROW_LENGTH = 1365
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы worker
--
DROP TABLE IF EXISTS worker;
CREATE TABLE worker (
  id INT(11) NOT NULL AUTO_INCREMENT,
  order_id INT(11) DEFAULT NULL,
  dev_id INT(11) DEFAULT NULL,
  new_hourly DOUBLE DEFAULT NULL,
  sum_hours DOUBLE DEFAULT NULL,
  version INT(11) DEFAULT 0,
  is_deleted BIT(1) DEFAULT b'0',
  accepted BIT(1) DEFAULT b'0',
  accept_date DATE DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_salary_developer_id FOREIGN KEY (dev_id)
    REFERENCES developer(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_salary_ordering_id FOREIGN KEY (order_id)
    REFERENCES ordering(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 60
AVG_ROW_LENGTH = 2048
CHARACTER SET utf8
COLLATE utf8_general_ci
COMMENT = 'entity that represents developers on some project';

-- 
-- Вывод данных для таблицы admin
--
INSERT INTO admin VALUES
(1, 'adminfreelancer@gnail.com', 'admin', 'Dmytro', 'Shapovalov', 'en', NULL, NULL, NULL, 0, False, 'admin', NULL, NULL),
(2, 'admin@gmail.com', 'af628e05735ed3562f5b043cbe5f34c3974a62ed9dff79667b8acf0fd136904c', 'Open', 'Task', 'uk-UA', NULL, NULL, NULL, NULL, NULL, 'sZJYnmDUoYkCyUaHUfhgwKgRcojIGJYZkvAluBJjxRBlQcLqXZ', 'uploads/admin/2/', NULL);

-- 
-- Вывод данных для таблицы admin_candidate
--
INSERT INTO admin_candidate VALUES
(12, 'romanrynik@gmail.com', '1540b935-5da0-4feb-bf41-dd45dab56b60', NULL, NULL),
(13, 'mrudevich@inbox.ru', '142d2abc-e58a-40a0-8fe1-226f90da1848', NULL, NULL);

-- 
-- Вывод данных для таблицы customer
--
INSERT INTO customer VALUES
(1, 'kumar@gmail.com', 'Kumar', 'Anil', 'Kumar', 2010, 'en', NULL, NULL, '2004-06-17 00:00:00', 0, False, 'Anil', NULL, NULL, NULL, NULL, False),
(2, 'ryzkov@gmail.com', 'Ryzkov', 'Anton', 'Ryzkov', 2009, 'en', NULL, NULL, '2010-06-21 00:00:00', 0, False, 'Ryzkov', NULL, NULL, NULL, NULL, False),
(3, 'strinic@gmail.com', 'Strinic', 'Ivan', 'Strinic', 2006, 'en', NULL, NULL, '2003-02-11 00:00:00', 0, False, 'Strinic', NULL, NULL, NULL, NULL, False),
(4, 'belousov@gmail.com', 'Belousov', 'Nikolai', 'Belousov', 2009, 'en', NULL, NULL, '2009-06-13 00:00:00', 0, False, 'Belousov', NULL, NULL, NULL, NULL, False),
(5, 'mungki@gmail.com', 'Laulau', 'MungKi', 'Lau', 2007, 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Lau', NULL, NULL, NULL, NULL, False),
(6, 'sadamaza@gmail.com', '6e4938ca228ebe9b6d54b1989940fe5e2c714d93711f4cf8e79f8689f59dc3c8', 'sada', 'maza', NULL, NULL, '7131b214-c305-4dcc-93da-a73f6bab5ba4', NULL, '2016-01-21 00:00:00', NULL, NULL, 'UTQZNuHYMAVzabbGSGfTFTUzHbgsUALugVoggepYNlLfrzYQoq', NULL, NULL, NULL, NULL, False),
(7, 'fff@mail.ru', '79e5b9fe1d8a68d3f5eea4affe27348fb199a364bd10b261b50bc61eefdf6fd8', 'fff', 'fff', NULL, NULL, '95edc503-e7f1-46d1-9606-2332429d62d1', NULL, '2016-01-21 18:30:32', NULL, NULL, 'eWmRkNIsAYIZiiKtibBwDxBbbRfOerkUibGUgZoTjKmfjITwxA', NULL, NULL, NULL, NULL, False),
(8, 'cust@gmail.com', '5a5b75ee3b98d72617ca09ad4f7947f547b04284c848c88c0b135ca6354d43f7', 'cust', 'cust', 2, 'uk-UA', NULL, NULL, '2016-01-25 13:48:34', NULL, NULL, 'FgtmNNEhwWPKdovipYsWIgxwaYuHnbjZyynCBLnlbtMAuvlyss', 'uploads/customer/8/', NULL, NULL, '8786', False),
(9, 'bohdandarkzolochiv@gmail.com', '7c9442aa808693e7683c2f2fe37c02cc0b37bc34d6cdcb1099f7ea426fd426e7', 'Bohdan', 'Zaharkiv', 2, 'en', NULL, '5077d9a6-64a0-4ae6-acdd-46b7563c4558', '2016-02-06 02:02:47', NULL, NULL, 'KTjPyhyxYeMHnefscLUTbePMKQuqqHtZWCnhnhLOabuJmXIdnX', 'uploads/customer/9/', NULL, NULL, NULL, False),
(10, 'customer@gmail.com', '33c52fc2d9145c4170edcefaeb100faedd6346a56f9cb357f7637cad06c6bdad', 'CustomerFirstName', 'CustomerLastName', 2, 'en', NULL, '039dd2f7-a0b3-4eb1-976b-1fbca096d0b6', '2016-02-06 13:46:14', NULL, NULL, 'qBvPyqeQwEoighyZcOguYCvgGbRZYfCeADHumYVfKrandPHlVa', NULL, NULL, NULL, NULL, False),
(11, 'dsgddgdsgdgg@gmail.com', 'a599f1e6013e93bc11b85835821d2cd49f7d15ecd8e8808fc848c7994e9984c2', 'Bohdan', 'Zaharkiv', 2, 'en', NULL, 'f5e43268-a16d-4efd-8749-f069fee26db3', '2016-02-06 13:55:36', NULL, NULL, 'fHcWakrVNNifmSXhnGhXnlkPyvsBTtXKSuCMhKkPjPuqITexrx', 'https://media.licdn.com/mpr/mprx/0_1NOWt-euIGV74wcOPK6WVloSo5k7ZWECqneWq6kuoqNajMUmJLXHs5WSW6L1OweGpNeWKXoD_GwmRDVGsFRvs6I36GwfRS0aJFRElFO2DiY_AUXtvnSXAPrjQBl0tSe0Kkx59MXoHLX', NULL, NULL, NULL, False),
(12, 'bohdanfgfgrgolochiv@gmail.com', 'd00114df82cceebac9cd8126da8280a4a48f4c8461371b406c7e97fd700b91a6', 'Bohdan', 'Zaharkiv', 2, 'en', NULL, '0a0cad2e-f0c7-4a1f-97ff-9231510445e3', '2016-02-06 14:07:52', NULL, NULL, 'dJZIkvztDcXkggrCrfjwrViwNHFXaZpVGzSklzCOAIQgNmAKBJ', 'https://media.licdn.com/mpr/mprx/0_1NOWt-euIGV74wcOPK6WVloSo5k7ZWECqneWq6kuoqNajMUmJLXHs5WSW6L1OweGpNeWKXoD_GwmRDVGsFRvs6I36GwfRS0aJFRElFO2DiY_AUXtvnSXAPrjQBl0tSe0Kkx59MXoHLX', NULL, NULL, NULL, False),
(13, 'bohdasdgsdglochiv@gmail.com', '835684f477422feb05eda7b6ef5a74c99ada258bbc1114d7b79b33a58d7d8686', 'Bohdan', 'Zaharkiv', 2, 'en', NULL, '227ba1a4-bd7e-4817-bf76-8267deaf9d72', '2016-02-06 15:09:36', NULL, NULL, 'YWnCGXTgpNQlrZGylXNJjHutKlfmMvaVSiPosRLIgaRpJIqpPN', 'https://media.licdn.com/mpr/mprx/0_1NOWt-euIGV74wcOPK6WVloSo5k7ZWECqneWq6kuoqNajMUmJLXHs5WSW6L1OweGpNeWKXoD_GwmRDVGsFRvs6I36GwfRS0aJFRElFO2DiY_AUXtvnSXAPrjQBl0tSe0Kkx59MXoHLX', NULL, NULL, NULL, False),
(14, 'bohfsdfdchiv@gmail.com', 'b55210693f8d4aeb53e1baccfc3e2180c853febc3c7082a06dfd6c12bce200f1', 'Bohdan', 'Zaharkiv', 2, 'en', NULL, '68707511-7e27-4f64-ab7a-951ba3bfbc12', '2016-02-06 15:11:00', NULL, NULL, 'GMxgMmcLEgtMsNQRYFKeJDQnyCMkcvUyZkapqTiuVvfWPNDNYt', 'https://media.licdn.com/mpr/mprx/0_1NOWt-euIGV74wcOPK6WVloSo5k7ZWECqneWq6kuoqNajMUmJLXHs5WSW6L1OweGpNeWKXoD_GwmRDVGsFRvs6I36GwfRS0aJFRElFO2DiY_AUXtvnSXAPrjQBl0tSe0Kkx59MXoHLX', NULL, NULL, NULL, False),
(15, 'bohdansdgchiv@gmail.com', '16893b3ad8776a336808ef72e761d441cc9ec7f1a993d5b2e44bfe8240ede396', 'Bohdan', 'Zaharkiv', 2, 'en', NULL, '6692ead3-7d30-41b8-b9aa-dea19868c599', '2016-02-06 15:14:20', NULL, NULL, 'unShxJkHKgdoHlxsZMXJhQjOujPDieDXnzFahCTahNkWAtCgeK', 'https://media.licdn.com/mpr/mprx/0_1NOWt-euIGV74wcOPK6WVloSo5k7ZWECqneWq6kuoqNajMUmJLXHs5WSW6L1OweGpNeWKXoD_GwmRDVGsFRvs6I36GwfRS0aJFRElFO2DiY_AUXtvnSXAPrjQBl0tSe0Kkx59MXoHLX', NULL, NULL, NULL, False),
(16, 'bohdareherhzolochiv@gmail.com', '17fe9b6499398ec5542e9ad3b5ed8b83715175fe08d2b102f137d12ebbe96422', 'Bohdan', 'Zaharkiv', 2, 'en', NULL, '87233a3e-aa8b-4047-bf84-996661bf8e9b', '2016-02-06 15:16:14', NULL, NULL, 'zyZzMvmcIJaFgKYxxCgbaNkSTPyUrgiYzotkXiBPEUoPMFPXrG', 'https://media.licdn.com/mpr/mprx/0_1NOWt-euIGV74wcOPK6WVloSo5k7ZWECqneWq6kuoqNajMUmJLXHs5WSW6L1OweGpNeWKXoD_GwmRDVGsFRvs6I36GwfRS0aJFRElFO2DiY_AUXtvnSXAPrjQBl0tSe0Kkx59MXoHLX', NULL, NULL, NULL, False),
(17, 'jewsdwfdj@wfsfsfdsf.egew', 'f43a37247618aa038d7cc57339a3d9df6942e0ed038d94042aaee8d4b00b842f', 'uhfkjhdush', 'hdfhwjh', 2, 'en', NULL, 'ec1b7652-b470-4055-baf0-9eb866351312', '2016-02-06 15:17:36', NULL, NULL, 'yVPaoxIpArpfyQtUspBGflLVhNmxmteEYOOOlaBcFTZoiNplbo', NULL, NULL, NULL, NULL, False),
(18, 'developer@gmail.com', 'a20133d903a20c6c6426a7d503fbd26378eb1d860352b9420d3aa84db1c1f9f6', 'Test', 'Test', 2, 'en', NULL, '02c829b5-6e5e-4968-b9bd-b77e3bf1e1a1', '2016-02-07 20:40:54', NULL, NULL, 'XEjJcjuVJqQwEBSpxpuAKPdZhWkYbyQhIhEKZLIFCFgDHaqrmP', NULL, NULL, NULL, NULL, False),
(19, 'last19@gmail.com', 'beb2efdfa8188c934f4466aec482c728bcf25cbcd058670c8468719448cb4f04', 'First', 'LasttCust', 2, 'en', NULL, '6cc5c78d-30fc-4cce-ab87-2387b806c340', '2016-02-08 09:15:35', NULL, NULL, 'WxqwzEdRbdVQkJJAubdxtzTXbKfNJOcyAnysWLdfgAtwkzsTXw', 'uploads/customer/19/', NULL, NULL, NULL, False),
(20, 'kest@gmail.com', '7df243e34064b6227ef73b21f234a4b1eed9932c819801aee3feeec396f698b3', 'kestrel', 'mauritius', 2, 'en', NULL, 'e51d703c-8eb7-454c-b06b-86d65bd27a8b', '2016-02-12 19:17:10', NULL, NULL, 'zOFHZWXSxxUqgaMobnAHhzKOnWwRPiFONWAuFqWYmzOlGGZgpF', NULL, NULL, NULL, NULL, NULL),
(21, 'kestrel@gmail.com', '42340513eb891be579c01430835acc1ab69927ba3aae7b9e2393fb134a3ef7c9', 'Max', 'Rudevich', -6, 'uk-UA', NULL, NULL, '2016-02-14 23:55:42', NULL, NULL, 'GRupmIsWZqseSVxrUgXEGZXhIhlLWQXgqxvzxWLLCfKTNMkArK', NULL, 'I''m kestrel !!!', 'mrkestrel@gmail.com', '3646', NULL),
(22, 'l.volodimyr@gmail.com', '9eeddfd6c1fd82e33e2ca54edf4854aae855d1268522587c1da620a535f554cf', 'Volodimyr', 'Lominsky', 2, 'en', NULL, '69543a53-0d13-439a-9266-a94160071877', '2016-02-15 09:44:23', NULL, NULL, 'mDuCSxiPUJmQLWlKiEoPbWsYOTFwQpXQzWkEUXmKtPIiqoVAIC', 'uploads/customer/22/', NULL, NULL, NULL, NULL),
(23, 'bohdandadrkzolochiv@gmail.com', '8b98648f6fb4a8f455c11f444a8e3c2a050daaf6a3ffed37462b4cddb79c0c3b', 'Bohdan', 'Zaharkiv', 2, 'en', NULL, '612414d1-ec08-4786-9e58-5d80ed089049', '2016-02-16 21:30:04', NULL, NULL, 'DZHrrYvTDjiYeSGRtwAqATZwoGrEOsmOgBBhCrJSvEEvmssGcq', 'https://media.licdn.com/mpr/mprx/0_1NOWt-euIGV74wcOPK6WVloSo5k7ZWECqneWq6kuoqNajMUmJLXHs5WSW6L1OweGpNeWKXoD_GwmRDVGsFRvs6I36GwfRS0aJFRElFO2DiY_AUXtvnSXAPrjQBl0tSe0Kkx59MXoHLX', NULL, NULL, NULL, NULL),
(24, 'qweasdzc@gmail.com', '83ad485500259a91136810755514db89499f81c316772dc1b34768f2783d3f26', 'Roman', 'Roman', 2, 'en', NULL, '630a5d6c-629f-4a7c-bf8a-2fd86f375af3', '2016-02-17 11:30:03', NULL, NULL, 'UONOkXBWedLsoOSMgcjtRzIegfOGsrINMICLEzkGJQfMqbnuUJ', NULL, NULL, NULL, NULL, NULL),
(25, 'cust2@gmail.com', 'b68e00a8b110133d7d843a897a71c86a9e1cb305d4727897d6e929db198ec032', 'customer', 'customer', 2, 'en', NULL, '13ceaad5-90d6-4291-9c64-4cb81c021740', '2016-02-18 10:38:14', NULL, NULL, 'znmJGfGIwqswscwkWhJiqyoUDhFrsURzvbRDJSDunHvRIuBjPk', NULL, NULL, NULL, NULL, NULL),
(33, 'l.volodimyr1@gmail.com', '83ff6d95981ab51b1835eee54a1c6409c6f8b6ee7897814b06ce54c1d37b7a75', 'Lominskiy', 'Volodimyr', 2, 'en', '5473ead6-d502-4b9f-bf7f-7d13cff3a81e', 'a4333b70-4594-4db1-885b-b9931304ae21', '2016-02-20 13:25:50', NULL, NULL, 'xkuegNrVaMPSdLCZMtQzlxPemgfUIemMNfbsSTkOOFmNMnsstO', 'https://lh3.googleusercontent.com/-Ez2-rSEpOoU/AAAAAAAAAAI/AAAAAAAAABY/VWeOhCvAQnE/photo.jpg?sz=50', NULL, NULL, NULL, NULL);

-- 
-- Вывод данных для таблицы developer
--
INSERT INTO developer VALUES
(1, 'iuliana@gmail.com', 'Pavaloaie', 'Iuliana', 'Pavaloaie', 13.3, 3, 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Iuli', NULL, 'Darya is the author of Foodist and creator of Summer Tomato, one of TIMEs 50 Best Websites. She received her Ph.D in neuroscience from UCSF and her bachelor’s degree in Molecular and Cell Biology from UC Berkeley.Darya helps people get healthy and lose weight without dieting. Because life should be awesome.She spends most of her time thinking and writing about food, health and science. She eats amazing things daily and hasnt even considered going on a diet since 2007.', NULL, NULL, NULL, False),
(2, 'ivanov@gmail.com', 'Ivanov', 'Sergey', 'Ivanov', 10.2, 4, 'en', NULL, NULL, '2011-10-11 00:00:00', 0, False, 'Serg', NULL, 'Darya is the author of Foodist and creator of Summer Tomato, one of TIMEs 50 Best Websites. She received her Ph.D in neuroscience from UCSF and her bachelor’s degree in Molecular and Cell Biology from UC Berkeley.Darya helps people get healthy and lose weight without dieting. Because life should be awesome.She spends most of her time thinking and writing about food, health and science. She eats amazing things daily and hasnt even considered going on a diet since 2007.', NULL, NULL, '8450', False),
(3, 'andres@gmail.com', 'Pinilla', 'Andres', 'Pinilla Palacios', 13.3, 5, 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Andr', NULL, 'Darya is the author of Foodist and creator of Summer Tomato, one of TIMEs 50 Best Websites. She received her Ph.D in neuroscience from UCSF and her bachelor’s degree in Molecular and Cell Biology from UC Berkeley.Darya helps people get healthy and lose weight without dieting. Because life should be awesome.She spends most of her time thinking and writing about food, health and science. She eats amazing things daily and hasnt even considered going on a diet since 2007.', NULL, NULL, NULL, False),
(4, 'ian@gmail.com', '890845237bd1496d285e26438ea3382eb4c697aa6b31f788b25779961eb6b6aa', 'Ian', 'Anderson', 12.4, 5, 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Ian', NULL, 'Darya is the author of Foodist and creator of Summer Tomato, one of TIMEs 50 Best Websites. She received her Ph.D in neuroscience from UCSF and her bachelor’s degree in Molecular and Cell Biology from UC Berkeley.Darya helps people get healthy and lose weight without dieting. Because life should be awesome.She spends most of her time thinking and writing about food, health and science. She eats amazing things daily and hasnt even considered going on a diet since 2007.', NULL, NULL, NULL, False),
(5, 'hajji@gmail.com', 'Makram', 'Hajji', 'Makram', 11.9, 7, 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Hajj', NULL, 'Darya is the author of Foodist and creator of Summer Tomato, one of TIMEs 50 Best Websites. She received her Ph.D in neuroscience from UCSF and her bachelor’s degree in Molecular and Cell Biology from UC Berkeley.Darya helps people get healthy and lose weight without dieting. Because life should be awesome.She spends most of her time thinking and writing about food, health and science. She eats amazing things daily and hasnt even considered going on a diet since 2007.', NULL, NULL, NULL, False),
(6, 'stanisa@gmail.com', 'Koncic', 'Stanisa', 'Koncic', 10.8, -1, 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Stan', NULL, 'Darya is the author of Foodist and creator of Summer Tomato, one of TIMEs 50 Best Websites. She received her Ph.D in neuroscience from UCSF and her bachelor’s degree in Molecular and Cell Biology from UC Berkeley.Darya helps people get healthy and lose weight without dieting. Because life should be awesome.She spends most of her time thinking and writing about food, health and science. She eats amazing things daily and hasnt even considered going on a diet since 2007.', NULL, NULL, NULL, False),
(7, 'ognev@gmail.com', 'Ognev', 'Ivan', 'Ognev', 25, -3, 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Ogn', NULL, 'Darya is the author of Foodist and creator of Summer Tomato, one of TIMEs 50 Best Websites. She received her Ph.D in neuroscience from UCSF and her bachelor’s degree in Molecular and Cell Biology from UC Berkeley.Darya helps people get healthy and lose weight without dieting. Because life should be awesome.She spends most of her time thinking and writing about food, health and science. She eats amazing things daily and hasnt even considered going on a diet since 2007.', NULL, NULL, NULL, False),
(8, 'gupta@gmail.com', 'Ashish', 'gupta', 'Ashish', 10.2, -2, 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'gupta', NULL, 'Darya is the author of Foodist and creator of Summer Tomato, one of TIMEs 50 Best Websites. She received her Ph.D in neuroscience from UCSF and her bachelor’s degree in Molecular and Cell Biology from UC Berkeley.Darya helps people get healthy and lose weight without dieting. Because life should be awesome.She spends most of her time thinking and writing about food, health and science. She eats amazing things daily and hasnt even considered going on a diet since 2007.', NULL, NULL, NULL, False),
(9, 'lamine@gmail.com', 'Jellad', 'Mohamed', 'Lamine Jellad', 14.2, -5, 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Mohamed', NULL, 'Darya is the author of Foodist and creator of Summer Tomato, one of TIMEs 50 Best Websites. She received her Ph.D in neuroscience from UCSF and her bachelor’s degree in Molecular and Cell Biology from UC Berkeley.Darya helps people get healthy and lose weight without dieting. Because life should be awesome.She spends most of her time thinking and writing about food, health and science. She eats amazing things daily and hasnt even considered going on a diet since 2007.', NULL, NULL, NULL, False),
(10, 'stefan@gmail.com', 'Scekic', 'Stefan', 'Scekic', 14.2, -6, 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Scekic', NULL, 'Darya is the author of Foodist and creator of Summer Tomato, one of TIMEs 50 Best Websites. She received her Ph.D in neuroscience from UCSF and her bachelor’s degree in Molecular and Cell Biology from UC Berkeley.Darya helps people get healthy and lose weight without dieting. Because life should be awesome.She spends most of her time thinking and writing about food, health and science. She eats amazing things daily and hasnt even considered going on a diet since 2007.', NULL, NULL, NULL, False),
(11, 'mrudevich96@gmail.com', '4ef4670e5feefd3f23abd4437d26833a701043e20677713bb8a63ac7839f1223', 'Max', 'Rudevich', 15, 2, 'en', NULL, NULL, '2016-01-21 00:00:00', NULL, NULL, 'uGutvCmwchECwwgkvrMtHBonhLOiJEiSVEjaepMoqSSfHefjcq', 'uploads/developer/11/', 'Max is the author of Foodist and creator of Summer Tomato, one of TIMEs 50 Best Websites. She received her Ph.D in neuroscience from UCSF and her bachelor’s degree in Molecular and Cell Biology from UC Berkeley.Darya helps people get healthy and lose weight without dieting. Because life should be awesome.She spends most of her time thinking and writing about food, health and science. She eats amazing things daily and hasnt even considered going on a diet since 2007.', NULL, NULL, NULL, False),
(13, 'max@gmail.com', '51c8b23f801d375fe1b413e8f24badd709d515e472f07bef76972a9951773df4', 'max', 'max', 15, NULL, NULL, 'c2b9eeef-e708-480f-8af0-4726715fc5df', NULL, '2016-01-21 17:24:15', NULL, NULL, 'UVbQmQVjUPcRuUfXvbWpmznANlpZnczSIvadbaEGqdLYyKyVAF', NULL, 'Darya is the author of Foodist and creator of Summer Tomato, one of TIMEs 50 Best Websites. She received her Ph.D in neuroscience from UCSF and her bachelor’s degree in Molecular and Cell Biology from UC Berkeley.Darya helps people get healthy and lose weight without dieting. Because life should be awesome.She spends most of her time thinking and writing about food, health and science. She eats amazing things daily and hasnt even considered going on a diet since 2007.', NULL, NULL, NULL, False),
(14, 'zdfghdfxc@gmail.com', '9aa7f549a95fa796bced1068a3438918c11963c7159d0d46094ef1a067847684', 'zxc', 'zxc', 15, NULL, NULL, NULL, NULL, '2016-01-21 17:39:37', NULL, NULL, 'ipQjYmSvcrxQJHKALnhzApkKDmYSCsLmLMGGIMEnOjoMZQtZxJ', NULL, 'Darya is the author of Foodist and creator of Summer Tomato, one of TIMEs 50 Best Websites. She received her Ph.D in neuroscience from UCSF and her bachelor’s degree in Molecular and Cell Biology from UC Berkeley.Darya helps people get healthy and lose weight without dieting. Because life should be awesome.She spends most of her time thinking and writing about food, health and science. She eats amazing things daily and hasnt even considered going on a diet since 2007.', NULL, NULL, NULL, False),
(15, 'zxc@gmail.com', 'e6aee8f3372a168d13a3f063f3e6129b7372547f86aedbfd3181679758191c04', 'sad', 'das', 15, NULL, NULL, 'edcd518c-73de-4906-a65e-564687f7653c', NULL, '2016-01-21 17:58:55', NULL, NULL, 'zyrCCyReaICZcxWukFnxYgBUmqsJJFvlfDqWucrKEfUFVWNJFK', NULL, 'Darya is the author of Foodist and creator of Summer Tomato, one of TIMEs 50 Best Websites. She received her Ph.D in neuroscience from UCSF and her bachelor’s degree in Molecular and Cell Biology from UC Berkeley.Darya helps people get healthy and lose weight without dieting. Because life should be awesome.She spends most of her time thinking and writing about food, health and science. She eats amazing things daily and hasnt even considered going on a diet since 2007.', NULL, NULL, NULL, False),
(16, 'user@gmail.com', '3dc2bbbbe9f2936e42f0db60b7f753b8c2f83394f794a60c8d847a4e1eb0f790', 'user', 'user', 15, NULL, NULL, '6a477ccb-7b0e-4446-9845-7b77a5bdb71e', NULL, '2016-01-21 19:08:18', NULL, NULL, 'eRzTFKhvFvkxPdEIJKrfNpSQDOdDxNfrEKwLsaYHHyGAtyWlnL', NULL, 'Darya is the author of Foodist and creator of Summer Tomato, one of TIMEs 50 Best Websites. She received her Ph.D in neuroscience from UCSF and her bachelor’s degree in Molecular and Cell Biology from UC Berkeley.Darya helps people get healthy and lose weight without dieting. Because life should be awesome.She spends most of her time thinking and writing about food, health and science. She eats amazing things daily and hasnt even considered going on a diet since 2007.', NULL, NULL, NULL, False),
(19, 'dev@gmail.com', '5a5b75ee3b98d72617ca09ad4f7947f547b04284c848c88c0b135ca6354d43f7', 'dev', '11', 15, 2, 'en', NULL, NULL, '2016-01-22 14:04:41', NULL, NULL, 'FgtmNNEhwWPKdovipYsWIgxwaYuHnbjZyynCBLnlbtMAuvlyss', 'uploads/developer/19/', 'Darya is the author of Foodist and creator of Summer Tomato, one of TIMEs 50 Best Websites. She received her Ph.D in neuroscience from UCSF and her bachelor’s degree in Molecular and Cell Biology from UC Berkeley.Darya helps people get healthy and lose weight without dieting. Because life should be awesome.She spends most of her time thinking and writing about food, health and science. She eats amazing things daily and hasnt even considered going on a diet since 2007.', 'junic', 'testing@gmail.com', '6506', False),
(20, 'dev2@gmail.com', '5a5b75ee3b98d72617ca09ad4f7947f547b04284c848c88c0b135ca6354d43f7', 'Dev2', 'dev2', NULL, 2, 'en', NULL, 'b3abeda7-8e32-44b0-bbeb-ad9a0ff2dac6', '2016-02-08 04:16:25', NULL, NULL, 'FgtmNNEhwWPKdovipYsWIgxwaYuHnbjZyynCBLnlbtMAuvlyss', NULL, NULL, NULL, NULL, NULL, False),
(21, 'last@gmail.com', '30361a10b4acdb39eae63c0b85fd2f989bb691c1ec3e0e40df2c0c993d3537c4', 'First', 'Last', NULL, 2, 'en', NULL, '322eba80-9a50-4a7a-9ecd-8b33ec719792', '2016-02-08 09:05:39', NULL, NULL, 'gXgzPSJHsxpDhMDGTXAfopUNbSWIoDglzTsyTNoVeWjPOWoWyY', 'uploads/developer/21/', NULL, NULL, NULL, NULL, False),
(22, 'ip1x@gmail.com', 'b3d350e1120cd2e0b83c5a64c9423b1d4a01d3bcbca1c26b75655c025cfd29e9', 'mrip1x', 'ip1x', 5, 4, 'en', NULL, NULL, '2016-02-08 19:23:16', NULL, NULL, 'mALhoWevmGEltBbBZbtGGQHPAjKZfISjtZgwPxcEIamqZrtvOI', 'uploads/developer/22/', 'I''m mrip1x !!!', 'mrip1x', 'g32g@gmail.com', '2008', False),
(23, 'emptydev@gmail.com', '0a3d6c47be6b77c50861275f616beeca1dc630cd647d6cd45c9f03367d20a277', 'emptyDev', 'emptyDev', 44, 2, 'en', '6a477ccb-7b0e-4446-9845-7b77a5bdb222', NULL, '2016-02-09 14:10:17', NULL, NULL, 'SaixqQEMziWCyhZFhIfzqfIzOLwwvcFXKsLSDhylayYnnjltGf', NULL, NULL, 'junic', NULL, '8774', False),
(26, 'new@gmail.com', '98dc9fe24ff34648cecde68372946c0c7dc9dcee99342c2e25d767d84549742b', 'New', 'New', NULL, 2, 'en', NULL, '<null>', '2016-02-10 13:34:07', NULL, NULL, 'XRaNQqSOtJlyxGaYMUKjQwdcAqOYmEqpsOkmpJGGxdLEAoDHlw', 'uploads/developer/26/', NULL, NULL, NULL, NULL, False),
(27, 'test@gmail.com', '3f1f0ddb3362702d9ab4377b86ba51071a23d03f93a18af8d4252b01f2d2b87f', 'Test', 'Test', NULL, 2, 'en', NULL, '7be8e761-2279-4d3f-aad0-0e5b6f21f4b4', '2016-02-10 21:07:07', NULL, NULL, 'LvXsRxjOcycxjJJJpoLTPlOSeQWqNLYlAMCdUQEiHyCAIkYehC', NULL, NULL, NULL, NULL, NULL, False),
(28, 'testDevSignUp@gmail.com', 'b345c1784bfa75c4171af62f63143d54ffce689c5ed2bc464a521e82cc97c28c', 'testDevSignUp', 'testDevSignUp', NULL, 2, 'en', NULL, '0e32bcd1-b4f4-439e-8cfa-ce36182d3322', '2016-02-12 19:19:49', NULL, NULL, 'HzRrnlJEPoIDepwhskHeSJVLJHDEZmtpIJdMTjUixlolAFmRSx', NULL, NULL, NULL, NULL, NULL, NULL),
(29, 'idsfisfi@sdijgdfg.dfhdfh', 'fd1571ae95ec2b54536b549449538f05fc09dab1ce8038048b52012827017e4f', 'ndsigsidhiuh', 'iuhgfiuhgifdgiu', NULL, 2, 'en', NULL, '920f4603-eb27-4fc1-b1c3-d78818666926', '2016-02-17 11:33:08', NULL, NULL, 'XxwBCvtZbUgYEBXTNRVswdCTyUpFsYgkcYZxHLqLwXuitNKFLn', NULL, NULL, NULL, NULL, NULL, NULL),
(30, '7bf12d5b-ebd0-4b31-a67e-82c1a14b2c79', '892435ec25804d789b58c425da5fb074192fe50a25d89de96b95bbc99ac6afde', 'jdhvhdfjgjhbB', 'HBHBDJBFDJBGJ', NULL, 2, 'en', '48d35b35-3724-4bb6-9c38-c0efb6ab2187', 'bbe5ff02-68d8-4373-9f6f-5f5e0837d5fe', '2016-02-17 11:38:59', NULL, NULL, 'TsKXooHzAWLnlaCQWSqvLrcbrqMGonQQMJLzJYHnsIwHWLZNWU', NULL, NULL, NULL, NULL, NULL, NULL),
(62, 'romanrynik@gmail.com', '15296d739ed4c5d826d9ccf1c0cc07e09423666dcb07378341943c23c2e8c09d', 'R', 'R', NULL, 2, 'en', '68f9515c-e983-4fca-a46f-32aa1165228d', NULL, '2016-02-18 16:59:52', NULL, NULL, 'AmuznPmlbFLeLRufQeekYSCRuoIIyGSzYcnedmmYqFSTDSAGWJ', NULL, NULL, NULL, NULL, NULL, NULL),
(63, 'fortest@gmail.com', 'a21d97bab8313670b3f34018892572db90acd9738acfde8e2588f48709b311b9', 'ForTest', 'ForTest', NULL, 2, 'en', 'ac5c1b77-a5d5-46f1-9ad7-c89ecdc7ede5', '0f06a4ef-8811-4802-83d1-4896181a44c4', '2016-02-18 21:52:53', NULL, NULL, 'IdxCigSeDXkQwkQDbHMqMosKbYhiJbeJKgAVrTgJmEGbbOXCaU', NULL, NULL, NULL, NULL, NULL, NULL),
(64, 'studlom1995@gmail.com', '977f13789fa59f21812a48b95235531992ec5992ad3a7ccecee6581138e8bfeb', 'Volodimyr', 'Lominskiy', NULL, 2, 'en', NULL, '19b99e62-3ee0-4288-a513-91ac46bcd4d1', '2016-02-20 10:49:25', NULL, False, 'JJteLjbDhUBeGmVjrIJOupZAhELNKHnTVQCrDRyvySqxoiFmjs', 'https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg?sz=50', 'Few words about me', NULL, NULL, NULL, True);

-- 
-- Вывод данных для таблицы order_count
--
INSERT INTO order_count VALUES
(2, '2016-02-07', 2, NULL),
(5, '2016-02-04', 4, False),
(6, '2016-02-02', 5, False),
(8, '2016-01-10', 2, False),
(9, '2016-02-14', 2, NULL),
(10, '2016-02-18', 1, NULL),
(11, '2016-02-20', 4, NULL);

-- 
-- Вывод данных для таблицы technology
--
INSERT INTO technology VALUES
(1, '.NET', 0, False),
(2, '1C', 0, False),
(3, 'C#', 0, False),
(4, 'C/C++', 0, False),
(5, 'Flash/Flex', 0, False),
(6, 'Java', 0, False),
(7, 'Javascript', 0, False),
(8, 'Mac OS/Objective C', 0, False),
(9, 'PHP', 0, False),
(10, 'Python', 0, False),
(11, 'Ruby', 0, False),
(15, 'qwe', NULL, True),
(16, 'ssssss', NULL, True),
(17, 'newTech', NULL, True);

-- 
-- Вывод данных для таблицы contact
--
INSERT INTO contact VALUES
(1, 1, NULL, '02231768994', 'kumar99923', 0, False),
(2, 2, NULL, '34536658294', 'ryzkov', 0, False),
(3, 3, NULL, '73636489021', 'strinic_ssde', 0, False),
(4, 4, NULL, '55139204752', 'belousov221', 0, False),
(5, 5, NULL, '22395849393', 'mungki_33e', 0, False),
(6, NULL, 1, '28495748932', 'iuliana_ssa', 0, False),
(7, NULL, 2, '89884736483', 'ivanov', 0, False),
(8, NULL, 3, '89047584214', 'ss_hgandres', 0, False),
(9, NULL, 4, '87274859032', 'hajji33422', 0, False),
(10, NULL, 5, '84759298412', 'stanisa', 0, False),
(11, NULL, 6, '22374859204', 'ognev0002', 0, False),
(12, NULL, 7, '73847763748', 'guptassd34', 0, False),
(13, NULL, 8, '65784878362', 'lamine_pflf', 0, False),
(14, NULL, 9, '15467485902', 'stefan4444', 0, False),
(15, NULL, 10, '98057869462', 'llgogd334', 0, False),
(16, NULL, 19, '+380631981498', 'dev_skype', NULL, NULL),
(17, 8, NULL, '+380631981498', 'cust_skype', 0, NULL),
(18, NULL, 22, '+4344454844844', 'mrip1x', NULL, NULL),
(21, 21, NULL, '+380937611708', 'kestrel', NULL, NULL),
(22, NULL, 23, '+380936859949', 'asdasdas', 0, False),
(24, NULL, 11, NULL, 'dvo_oo', NULL, NULL);

-- 
-- Вывод данных для таблицы dev_tech
--
INSERT INTO dev_tech VALUES
(1, 1, 2),
(2, 1, 5),
(3, 2, 2),
(4, 2, 1),
(5, 3, 5),
(6, 3, 8),
(7, 4, 2),
(8, 4, 1),
(9, 5, 6),
(10, 6, 3),
(11, 6, 4),
(12, 7, 2),
(13, 7, 6),
(14, 7, 7),
(15, 8, 8),
(16, 8, 5),
(17, 9, 10),
(18, 9, 11),
(19, 10, 11),
(38, 21, 3),
(50, 21, 5),
(51, 22, 8),
(52, 22, 9);

-- 
-- Вывод данных для таблицы feedback
--
INSERT INTO feedback VALUES
(155, 19, 19, 'qwqrqrqw', 4, 'customer', NULL, NULL, '2016-02-15 13:39:44'),
(156, 19, 19, '111111', 2, 'customer', NULL, NULL, '2016-02-15 13:39:59'),
(157, 15, 19, '!!!!!', 5, 'customer', NULL, NULL, '2016-02-15 13:40:48'),
(158, 21, 19, 'fasfsaf', 5, 'dev', NULL, NULL, '2016-02-15 13:54:29'),
(164, 22, 1, 'g', 5, 'dev', NULL, NULL, '2016-02-15 20:29:46'),
(170, 19, 4, 'feed', 1, 'dev', NULL, NULL, '2016-02-18 10:53:31'),
(171, 19, 19, 'feed if accepted', 1, 'dev', NULL, NULL, '2016-02-18 14:44:05');

-- 
-- Вывод данных для таблицы ordering
--
INSERT INTO ordering VALUES
(1, 'SMM Instagram', 'hourly', 'I need my instagram services sold, good rates for resale on my panel.', 1, '2009-06-13 00:00:00', 20.399999618530273, False, NULL, False, NULL, False, 0, False, NULL, False, 1),
(2, 'mt5 stuff for Stonev', 'hourly', 'will send details of project. Fairly simple EA.', 2, '2015-09-23 00:00:00', 18.899999618530273, False, NULL, False, NULL, False, 0, False, NULL, False, 15),
(3, 'True Mobile Website Design', 'fixed', 'This project is for a web design and development team that can build out True Mobile site for a website which is already responsive. Experienced design and development team candidates only. For this task you would do the following: 1) Using a mockup tool we will provide. Create a full integrated mockup for the current site for about 10 key pages. Once client approves the mockup, you would then design the entire site. Site has approximately 82 pages but would be condensed heavily, we would follow a theme based on a specific theme design pages such as (home, main category page (content), sub category page ( content), dinning menu page and photo gallery), all other pages would adjust to these specific page design. Mock would be done using https://www.fluidui.com/ or other design mockup tools that lets us preview the functionality. Site would view able in various devices (tablets and phones). If this meets the standards, development begins. ', 3, '2009-06-13 00:00:00', 1700, True, '2015-12-13 00:00:00', False, NULL, True, 0, False, NULL, False, 1),
(4, 'Write some Software', 'hourly', 'customized software development project', 4, '2015-06-13 00:00:00', 14.5, True, '2015-07-01 00:00:00', True, '2015-08-21 00:00:00', False, 0, False, NULL, False, 0),
(5, 'Malware/Redirect correction on Website', 'hourly', 'I am having a malware/redirect issue on my website that is causing it to redirect to random other websites. Also, there is not a specific page or product that causes the issue. It happens randomly throughout the site.', 5, '2015-11-03 00:00:00', 21.799999237060547, True, '2015-11-11 00:00:00', False, NULL, False, 0, False, NULL, False, 6),
(6, 'GA Consulting', 'hourly', 'I am searching about a consulence on Google analytics.', 5, '2015-09-23 00:00:00', 13.899999618530273, False, NULL, False, NULL, False, 0, False, NULL, False, 0),
(7, 'new order', 'fixed', 'dfgdsgdsgn hsdfh sjdgh dhgdsghfdhg', 6, '2016-01-08 02:18:13', 50, False, NULL, False, NULL, True, 0, False, 5, False, 0),
(8, 'dgd,osijgjglk ojhoiege', 'fixed', 'gjg ejrgjerogjtoihjoirejg lkehotkhmtrhtrhrtjtjrtthjrtjtrj', 8, '2016-02-06 18:03:50', 50, True, '2016-02-13 18:49:05', True, '2016-02-13 20:59:08', False, NULL, NULL, 2, True, 8),
(9, 'Новий проект для мене', 'fixed', 'Бла бла шось типу опису має бути тут? хм...ше шось, гроші, доляри, дівчата, слава...сподіваюсь цього ніхто не побачить..', 9, '2016-02-07 18:04:08', 150, False, NULL, False, NULL, False, NULL, NULL, 2, False, 0),
(11, 'Finished project by cust cust', 'hourly', 'Finished project by cust custFinished project by cust cust', 8, '2016-02-10 15:36:34', 320, True, '2016-02-10 15:35:04', True, '2016-02-10 15:35:08', False, 0, False, 4, False, 0),
(12, 'In progres project by cust cust', 'fixed', 'In progres project by cust custIn progres project by cust cust', NULL, '2016-02-10 15:37:34', 2400, True, '2016-02-10 15:37:47', False, NULL, False, 0, False, NULL, False, 0),
(13, 'My new Order cust cust', 'fixed', 'asdasdasdasd My new OrderMy new OrderMy new OrderMy new OrderMy new OrderMy new Order', 8, '2016-02-13 15:52:11', 2232, True, '2016-02-18 17:39:40', False, '2016-02-19 13:09:56', False, NULL, NULL, 2, False, 1),
(14, 'My new Order cust cust 3', 'hourly', 'My new Order cust cust 3My new Order cust cust 3My new Order cust cust 3My new Order cust cust 3', 8, '2016-02-13 16:14:02', 54, True, NULL, True, '2016-02-19 13:26:50', False, NULL, NULL, 2, False, 10),
(15, 'New Private Project 2', 'hourly', 'New Private Project 2New Private Project 2New Private Project 2', 8, '2016-02-14 00:14:27', 2002, True, '2016-02-18 16:30:24', True, '2016-02-19 13:11:35', True, NULL, NULL, 2, False, 1),
(16, 'New Private Project 3', 'hourly', 'New Private Project 3New Private Project 3New Private Project 3', 19, '2016-02-14 00:16:11', 23, False, NULL, False, NULL, True, NULL, NULL, 2, True, 4),
(18, 'Test i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18n', 'fixed', 'Test i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18nTest i18n!!!', 8, '2016-02-20 03:31:28', 12000, False, NULL, False, NULL, True, NULL, NULL, 2, NULL, NULL),
(19, '123123123123123', 'hourly', '123123123123123123123123123123123123123123123123123123123123', 8, '2016-02-20 12:36:42', 222, False, NULL, False, NULL, False, NULL, NULL, 2, NULL, NULL);

-- 
-- Вывод данных для таблицы question
--
INSERT INTO question VALUES
(1, 'Р вЂ™ Р С—Р ВµРЎР‚Р ВµР СР ВµР Р…Р Р…Р С•Р в„– РЎвЂљР С‘Р С—Р В° char Р СР С•Р С–РЎС“РЎвЂљ РЎвЂ¦РЎР‚Р В°Р Р…Р С‘РЎвЂљРЎРЉРЎРѓРЎРЏ Р В·Р Р…Р В°РЎвЂЎР ВµР Р…Р С‘РЎРЏ Р С‘Р В· РЎРѓР В»Р ВµР Т‘РЎС“РЎР‹РЎвЂ°Р ВµР С–Р С• Р Т‘Р С‘Р В°Р С—Р В°Р В·Р С•Р Р…Р В°', 6, 1, False, 0, False),
(2, 'Р вЂ™Р С•Р В·Р СР С•Р В¶Р Р…Р В° Р В»Р С‘ Р С—Р ВµРЎР‚Р ВµР С–РЎР‚РЎС“Р В·Р С”Р В° Р С•Р С—Р ВµРЎР‚Р В°РЎвЂљР С•РЎР‚Р С•Р Р† Р Р† Java?', 6, 1, False, 0, True),
(3, 'Р С™Р В°Р С” Р С•Р С—РЎР‚Р ВµР Т‘Р ВµР В»Р С‘РЎвЂљРЎРЉ Р Т‘Р В»Р С‘Р Р…РЎС“ Р СР В°РЎРѓРЎРѓР С‘Р Р†Р В° myarray?', 6, 1, False, 0, True),
(4, 'Р С™Р В°Р С”Р С‘Р СР С‘ РЎРѓР С—Р С•РЎРѓР С•Р В±Р С•Р С(-Р В°Р СР С‘) Р СР С•Р В¶Р ВµРЎвЂљ Р В±РЎвЂ№РЎвЂљРЎРЉ Р Р†РЎвЂ№РЎР‚Р В°Р В¶Р ВµР Р…Р С• РЎвЂЎР С‘РЎРѓР В»Р С• 28?', 6, 1, True, 0, True),
(5, 'Р вЂ™РЎвЂ№Р В±Р ВµРЎР‚Р С‘РЎвЂљР Вµ Р С‘Р В· Р С—РЎР‚Р С‘Р Р†Р ВµР Т‘Р ВµР Р…Р Р…РЎвЂ№РЎвЂ¦ Р Р†РЎРѓР Вµ Р СР С•Р Т‘Р С‘РЎвЂћР С‘Р С”Р В°РЎвЂљР С•РЎР‚РЎвЂ№, Р С”Р С•РЎвЂљР С•РЎР‚РЎвЂ№Р Вµ Р С—РЎР‚Р С‘Р СР ВµР Р…Р С‘Р СРЎвЂ№ Р Т‘Р В»РЎРЏ top-level Р С”Р', 6, 1, True, 0, False),
(6, 'Р С™Р В°Р С”Р В°РЎРЏ РЎРѓРЎвЂљРЎР‚РЎС“Р С”РЎвЂљРЎС“РЎР‚Р В° Р Т‘Р В°Р Р…Р Р…РЎвЂ№РЎвЂ¦, РЎР‚Р ВµР В°Р В»Р С‘Р В·РЎС“РЎР‹РЎвЂ°Р В°РЎРЏ Р С‘Р Р…РЎвЂљР ВµРЎР‚РЎвЂћР ВµР в„–РЎРѓ Map Р С‘РЎРѓР С—Р С•Р В»РЎРЉР В·РЎС“Р ВµРЎвЂљ Р Т‘Р В»РЎРЏ РЎРѓРЎР‚Р В°Р Р†Р Р…Р ', 6, 1, True, 0, False),
(7, 'Р СљР С•Р В¶Р Р…Р С• Р В»Р С‘ Р Т‘Р С‘Р Р…Р В°Р СР С‘РЎвЂЎР ВµРЎРѓР С”Р С‘ Р СР ВµР Р…РЎРЏРЎвЂљРЎРЉ РЎР‚Р В°Р В·Р СР ВµРЎР‚ Р СР В°РЎРѓРЎРѓР С‘Р Р†Р В°?', 6, 1, False, 0, False),
(8, 'Р вЂ™РЎвЂ№Р В±Р ВµРЎР‚Р С‘РЎвЂљР Вµ Р Р†РЎРѓР Вµ Р С—РЎР‚Р В°Р Р†Р С‘Р В»РЎРЉР Р…РЎвЂ№Р Вµ Р Р†Р В°РЎР‚Р С‘Р В°Р Р…РЎвЂљРЎвЂ№ РЎРѓР С•Р В·Р Т‘Р В°Р Р…Р С‘РЎРЏ Р СР В°РЎРѓРЎРѓР С‘Р Р†Р С•Р Р†.', 6, 1, True, 0, False),
(9, 'Р СљР С•Р В¶Р Р…Р С• Р В»Р С‘ Р С—РЎР‚Р С‘ Р С•Р В±РЎР‰РЎРЏР Р†Р В»Р ВµР Р…Р С‘Р С‘ Р С”Р В»Р В°РЎРѓРЎРѓР В° Р С‘РЎРѓР С—Р С•Р В»РЎРЉР В·Р С•Р Р†Р В°РЎвЂљРЎРЉ Р СР С•Р Т‘Р С‘РЎвЂћР С‘Р С”Р В°РЎвЂљР С•РЎР‚РЎвЂ№ abstract Р С‘ final Р С•Р Т‘Р Р…Р С•Р Р†РЎР‚', 6, 1, False, 0, False),
(10, 'Р Р‡Р Р†Р В»РЎРЏРЎР‹РЎвЂљРЎРѓРЎРЏ Р В»Р С‘ Р СР В°РЎРѓРЎРѓР С‘Р Р†РЎвЂ№ Р Р† Java Р С•Р Т‘Р Р…Р С•РЎР‚Р С•Р Т‘Р Р…РЎвЂ№Р СР С‘?', 6, 1, False, 0, False),
(14, 'Question ololo', 6, 2, False, NULL, NULL),
(15, 'lkjhlkfhgjkl df', 6, 2, False, NULL, NULL),
(16, 'What is flex?', 5, 2, False, NULL, NULL),
(17, '1', 5, 2, True, NULL, NULL),
(18, '2', 5, 2, True, NULL, NULL),
(19, '3', 5, 2, True, NULL, NULL),
(20, 'js q1', 7, 2, True, NULL, NULL),
(21, 'q2', 7, 2, False, NULL, NULL),
(22, 'q3', 7, 2, False, NULL, NULL),
(23, 'a5', 1, 2, False, NULL, NULL);

-- 
-- Вывод данных для таблицы test
--
INSERT INTO test VALUES
(1, 6, 'ghd', 2, 80, 20, NULL, True),
(2, 6, 'JavaLab testing', 2, 77, 22, NULL, True),
(3, 5, 'test3', 2, 40, 40, 0, False),
(4, 1, 'test4', 2, 40, 40, 0, False),
(5, 2, 'test5', 2, 40, 40, 0, False),
(6, 7, 'test6', 2, NULL, NULL, 0, False),
(7, 11, 'test7', 2, NULL, NULL, 0, False),
(8, 6, 'Ololo test', 2, 77, 15, NULL, True),
(9, 5, 'Flash/Flex', 2, 80, 15, NULL, NULL);

-- 
-- Вывод данных для таблицы answer
--
INSERT INTO answer VALUES
(8, 14, True, 'hgfhfg', NULL, NULL),
(9, 14, False, '1', NULL, NULL),
(10, 15, True, 'irjhgldfk', NULL, NULL),
(11, 15, False, 'kljfhlkjvgdf', NULL, NULL),
(12, 16, True, '1', NULL, NULL),
(13, 16, False, '2', NULL, NULL),
(14, 17, True, '1', NULL, NULL),
(15, 17, False, '2', NULL, NULL),
(16, 17, True, '3', NULL, NULL),
(17, 18, False, '1', NULL, NULL),
(18, 18, True, '2', NULL, NULL),
(19, 18, False, '3', NULL, NULL),
(20, 18, True, '4', NULL, NULL),
(21, 19, True, '1', NULL, NULL),
(22, 19, False, '2', NULL, NULL),
(23, 19, True, '3', NULL, NULL),
(24, 19, False, '4', NULL, NULL),
(25, 19, True, '5', NULL, NULL),
(26, 20, True, '1', NULL, NULL),
(27, 20, True, '2', NULL, NULL),
(28, 20, False, '3', NULL, NULL),
(29, 21, False, '1', NULL, NULL),
(30, 21, True, '2', NULL, NULL),
(31, 22, True, '1', NULL, NULL),
(32, 22, False, '2', NULL, NULL),
(33, 23, True, 'a5', NULL, NULL),
(34, 23, False, 'a', NULL, NULL);

-- 
-- Вывод данных для таблицы complaint
--
INSERT INTO complaint VALUES
(13, 9, 21, 0, 0),
(14, 2, 21, 0, 0),
(15, 4, 22, 0, 0),
(16, 5, 19, 0, 0),
(17, 12, 19, 0, 0),
(18, 11, 19, 0, 0),
(19, 2, 19, 0, 0),
(20, 1, 21, 0, 0),
(21, 16, 21, 0, 0),
(22, 13, 21, 0, 0),
(23, 15, 21, 0, 0),
(24, 14, 21, 0, 0),
(25, 12, 21, 0, 0),
(26, 3, 21, 0, 0),
(27, 5, 22, 0, 0),
(28, 3, 22, 0, 0),
(29, 3, 22, 0, 0),
(31, 16, 23, 0, 0),
(32, 16, 19, 0, 0),
(33, 15, 19, 0, 0),
(34, 3, 19, 0, 0),
(35, 16, 22, 0, 0),
(36, 18, 19, 0, 0),
(37, 18, 19, 0, 0),
(38, 18, 19, 0, 0),
(39, 13, 19, 0, 0),
(40, 14, 19, 0, 0),
(41, 18, 64, 0, 0),
(42, 18, 64, 0, 0),
(43, 16, 64, 0, 0),
(44, 1, 64, 0, 0);

-- 
-- Вывод данных для таблицы dev_test
--

-- Таблица freelancerdb.dev_test не содержит данных

-- 
-- Вывод данных для таблицы developer_qa
--
INSERT INTO developer_qa VALUES
(1, 19, 1, 0, '2016-03-09', False, NULL, NULL),
(2, 23, 2, 0, NULL, False, 0, False),
(3, 23, 3, 0, NULL, False, 0, False),
(4, 23, 4, 0, NULL, False, 0, False),
(5, 23, 5, 0, NULL, False, 0, False),
(6, 23, 1, 0, NULL, False, 0, False),
(7, 19, 5, 0, NULL, False, 0, False),
(8, 23, 6, 0, NULL, False, 0, False),
(9, 23, 7, 0, NULL, False, 0, False),
(10, 23, 4, 0, NULL, False, 0, False),
(11, 23, 5, 0, NULL, False, 0, False),
(12, 23, 8, 0, '2016-03-14', False, NULL, NULL),
(13, 22, 9, 28.571428571428573, '2016-03-16', False, NULL, NULL),
(14, 23, 9, 0, '2016-03-19', False, NULL, NULL);

-- 
-- Вывод данных для таблицы follower
--
INSERT INTO follower VALUES
(26, 19, 'Aloha!! How are you =)', 4, NULL, NULL, NULL, NULL, 'dev'),
(28, 19, NULL, 7, NULL, NULL, NULL, NULL, 'dev'),
(33, 19, 'pop', 1, NULL, NULL, NULL, NULL, 'dev'),
(41, 19, NULL, 3, NULL, NULL, NULL, NULL, 'dev'),
(46, 21, '', 2, NULL, NULL, NULL, 2, 'dev'),
(59, 23, NULL, 14, NULL, False, NULL, NULL, 'dev'),
(61, 21, 'new', 1, NULL, False, NULL, NULL, 'dev'),
(62, 21, 'fffff', 1, NULL, NULL, NULL, 1, 'dev'),
(67, 22, 'I want this job', 3, NULL, False, NULL, NULL, 'dev'),
(69, 22, 'I want this job.', 4, NULL, False, NULL, NULL, 'dev'),
(81, 6, '', 16, NULL, NULL, NULL, 8, 'customer'),
(101, 22, 'khvcxn', 3, NULL, False, NULL, NULL, 'dev');

-- 
-- Вывод данных для таблицы ordering_technology
--
INSERT INTO ordering_technology VALUES
(1, 2, NULL),
(2, 6, NULL),
(3, 3, NULL),
(4, 4, NULL),
(5, 1, NULL),
(6, 3, NULL),
(7, 6, NULL),
(8, 4, NULL),
(9, 5, NULL),
(10, 3, 2),
(11, 2, 3),
(12, 5, 6),
(13, 4, 5),
(14, 3, 8),
(15, 2, 6),
(16, 4, 2),
(17, 6, 2),
(18, 5, 3),
(19, 1, 2),
(20, 2, 1),
(21, 3, 4),
(22, 4, 7),
(23, 6, 8),
(24, 2, 9),
(25, 3, 3),
(26, 6, 10),
(27, 4, 1),
(28, 1, 3),
(29, 7, 5),
(30, 5, 3),
(31, 3, 5),
(32, 7, 8),
(33, 1, 4),
(34, 8, 9),
(35, 9, 9),
(37, 13, 7),
(38, 14, 11),
(39, 15, 1),
(40, 15, 5),
(41, 15, 7),
(42, 16, 1),
(46, NULL, 5),
(47, NULL, 6),
(48, NULL, 7),
(49, NULL, 9),
(50, NULL, 5),
(51, NULL, 6),
(52, NULL, 7),
(53, NULL, 9),
(54, 18, 5),
(55, 18, 6),
(56, 18, 7),
(57, 18, 9),
(58, 19, 15);

-- 
-- Вывод данных для таблицы test_question
--
INSERT INTO test_question VALUES
(4, 2, 1),
(5, 2, 4),
(6, 2, 3),
(7, 2, 2),
(8, 2, 5),
(9, 8, 5),
(10, 8, 2),
(11, 8, 3),
(12, 8, 14),
(13, 9, 17),
(14, 9, 18),
(15, 9, 19);

-- 
-- Вывод данных для таблицы worker
--
INSERT INTO worker VALUES
(1, 1, 19, NULL, 78, 0, False, True, NULL),
(2, 3, 2, NULL, 88, 0, False, False, NULL),
(3, 3, 9, NULL, 72, 0, False, False, NULL),
(4, 4, 4, 14.5, 123, 0, False, True, NULL),
(5, 4, 5, 14.5, 130, 0, False, True, NULL),
(6, 5, 6, 21.799999237060547, 178, 0, False, True, NULL),
(7, 5, 7, 21.799999237060547, 171, 0, False, True, NULL),
(8, 5, 8, 21.799999237060547, 164, 0, False, True, NULL),
(9, 4, 19, 23, 122, 0, False, True, NULL),
(10, 3, 19, 26, 67, 0, False, True, NULL),
(11, 5, 19, 24.5, 88, 0, False, True, NULL),
(12, 11, 3, 44.2, 122, 0, False, True, NULL),
(23, 8, 23, NULL, NULL, NULL, NULL, True, NULL),
(35, 15, 19, 1252152, NULL, NULL, NULL, True, '2016-02-21'),
(54, 16, 19, NULL, NULL, NULL, NULL, True, '2016-02-17'),
(58, 13, 23, 44, NULL, NULL, NULL, True, '2016-02-18');

-- 
-- Восстановить предыдущий режим SQL (SQL mode)
-- 
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;

-- 
-- Включение внешних ключей
-- 
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;