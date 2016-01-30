--
-- Скрипт сгенерирован Devart dbForge Studio for MySQL, Версия 6.3.358.0
-- Домашняя страница продукта: http://www.devart.com/ru/dbforge/mysql/studio
-- Дата скрипта: 30.01.2016 12:31:38
-- Версия сервера: 5.6.28-log
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
  PRIMARY KEY (id),
  UNIQUE INDEX reg_url (reg_url),
  UNIQUE INDEX uuid (uuid)
)
ENGINE = INNODB
AUTO_INCREMENT = 2
AVG_ROW_LENGTH = 16384
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
  zone TIMESTAMP NULL DEFAULT NULL,
  lang ENUM('en','uk-UA') DEFAULT 'en',
  uuid VARCHAR(140) DEFAULT NULL,
  reg_url VARCHAR(150) DEFAULT NULL,
  reg_date DATETIME DEFAULT NULL,
  version INT(11) DEFAULT 0,
  is_deleted BIT(1) DEFAULT b'0',
  salt VARCHAR(50) DEFAULT NULL,
  img_url VARCHAR(255) DEFAULT NULL,
  overview VARCHAR(3000) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX email (email),
  UNIQUE INDEX reg_url (reg_url),
  UNIQUE INDEX uuid (uuid)
)
ENGINE = INNODB
AUTO_INCREMENT = 9
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
  zone TIMESTAMP NULL DEFAULT NULL,
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
  PRIMARY KEY (id),
  UNIQUE INDEX reg_url (reg_url),
  UNIQUE INDEX uuid (uuid)
)
ENGINE = INNODB
AUTO_INCREMENT = 20
AVG_ROW_LENGTH = 1092
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
AUTO_INCREMENT = 12
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
AUTO_INCREMENT = 16
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
AUTO_INCREMENT = 20
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
  PRIMARY KEY (id),
  CONSTRAINT FK_feedback_customer_id FOREIGN KEY (cust_id)
    REFERENCES customer(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_feedback_developer_id FOREIGN KEY (dev_id)
    REFERENCES developer(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 5
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
  PRIMARY KEY (id),
  CONSTRAINT FK_ordering_customer_id FOREIGN KEY (customer_id)
    REFERENCES customer(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 7
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
AUTO_INCREMENT = 11
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
AUTO_INCREMENT = 4
AVG_ROW_LENGTH = 5461
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
AUTO_INCREMENT = 41
AVG_ROW_LENGTH = 409
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
  CONSTRAINT FK_developer_qa_developer_id FOREIGN KEY (dev_id)
    REFERENCES developer(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_developer_qa_technology_id FOREIGN KEY (test_id)
    REFERENCES test(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 4
AVG_ROW_LENGTH = 5461
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
  PRIMARY KEY (id),
  CONSTRAINT FK_follower_developer_id FOREIGN KEY (dev_id)
    REFERENCES developer(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_follower_ordering_id FOREIGN KEY (order_id)
    REFERENCES ordering(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 15
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
AUTO_INCREMENT = 1
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
AUTO_INCREMENT = 25
AVG_ROW_LENGTH = 682
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
  PRIMARY KEY (id),
  CONSTRAINT FK_salary_developer_id FOREIGN KEY (dev_id)
    REFERENCES developer(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_salary_ordering_id FOREIGN KEY (order_id)
    REFERENCES ordering(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 9
AVG_ROW_LENGTH = 2048
CHARACTER SET utf8
COLLATE utf8_general_ci
COMMENT = 'entity that represents developers on some project';

-- 
-- Вывод данных для таблицы admin
--
INSERT INTO admin VALUES
(1, 'adminfreelancer@gnail.com', 'admin', 'Dmytro', 'Shapovalov', 'en', NULL, NULL, NULL, 0, False, 'admin', NULL);

-- 
-- Вывод данных для таблицы customer
--
INSERT INTO customer VALUES
(1, 'kumar@gmail.com', 'Kumar', 'Anil', 'Kumar', '2010-11-23 21:34:01', 'en', NULL, NULL, '2004-06-17 00:00:00', 0, False, 'Anil', NULL, NULL),
(2, 'ryzkov@gmail.com', 'Ryzkov', 'Anton', 'Ryzkov', '2009-05-02 13:23:21', 'en', NULL, NULL, '2010-06-21 00:00:00', 0, False, 'Ryzkov', NULL, NULL),
(3, 'strinic@gmail.com', 'Strinic', 'Ivan', 'Strinic', '2006-02-19 16:03:34', 'en', NULL, NULL, '2003-02-11 00:00:00', 0, False, 'Strinic', NULL, NULL),
(4, 'belousov@gmail.com', 'Belousov', 'Nikolai', 'Belousov', '2009-11-28 22:26:01', 'en', NULL, NULL, '2009-06-13 00:00:00', 0, False, 'Belousov', NULL, NULL),
(5, 'mungki@gmail.com', 'Laulau', 'MungKi', 'Lau', '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Lau', NULL, NULL),
(6, 'sadamaza@gmail.com', '6e4938ca228ebe9b6d54b1989940fe5e2c714d93711f4cf8e79f8689f59dc3c8', 'sada', 'maza', NULL, NULL, '7131b214-c305-4dcc-93da-a73f6bab5ba4', NULL, '2016-01-21 00:00:00', NULL, NULL, 'UTQZNuHYMAVzabbGSGfTFTUzHbgsUALugVoggepYNlLfrzYQoq', NULL, NULL),
(7, 'fff@mail.ru', '79e5b9fe1d8a68d3f5eea4affe27348fb199a364bd10b261b50bc61eefdf6fd8', 'fff', 'fff', NULL, NULL, '95edc503-e7f1-46d1-9606-2332429d62d1', NULL, '2016-01-21 18:30:32', NULL, NULL, 'eWmRkNIsAYIZiiKtibBwDxBbbRfOerkUibGUgZoTjKmfjITwxA', NULL, NULL),
(8, 'cust@gmail.com', '5a5b75ee3b98d72617ca09ad4f7947f547b04284c848c88c0b135ca6354d43f7', 'cust', 'cust', NULL, NULL, NULL, NULL, '2016-01-25 13:48:34', NULL, NULL, 'FgtmNNEhwWPKdovipYsWIgxwaYuHnbjZyynCBLnlbtMAuvlyss', NULL, NULL);

-- 
-- Вывод данных для таблицы developer
--
INSERT INTO developer VALUES
(1, 'iuliana@gmail.com', 'Pavaloaie', 'Iuliana', 'Pavaloaie', 13.3, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Iuli', NULL, NULL, NULL),
(2, 'ivanov@gmail.com', 'Ivanov', 'Sergey', 'Ivanov', 10.2, '2011-05-21 22:44:01', 'en', NULL, NULL, '2011-10-11 00:00:00', 0, False, 'Serg', NULL, NULL, NULL),
(3, 'andres@gmail.com', 'Pinilla', 'Andres', 'Pinilla Palacios', 13.3, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Andr', NULL, NULL, NULL),
(4, 'ian@gmail.com', 'Anderson', 'Ian', 'Anderson', 12.4, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Ian', NULL, NULL, NULL),
(5, 'hajji@gmail.com', 'Makram', 'Hajji', 'Makram', 11.9, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Hajj', NULL, NULL, NULL),
(6, 'stanisa@gmail.com', 'Koncic', 'Stanisa', 'Koncic', 10.8, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Stan', NULL, NULL, NULL),
(7, 'ognev@gmail.com', 'Ognev', 'Ivan', 'Ognev', 25.2, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Ogn', NULL, NULL, NULL),
(8, 'gupta@gmail.com', 'Ashish', 'gupta', 'Ashish', 10.2, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'gupta', NULL, NULL, NULL),
(9, 'lamine@gmail.com', 'Jellad', 'Mohamed', 'Lamine Jellad', 14.2, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Mohamed', NULL, NULL, NULL),
(10, 'stefan@gmail.com', 'Scekic', 'Stefan', 'Scekic', 14.2, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Scekic', NULL, NULL, NULL),
(11, 'mrudevich96@gmail.com', '4ef4670e5feefd3f23abd4437d26833a701043e20677713bb8a63ac7839f1223', 'Max', 'Rudevich', NULL, NULL, NULL, 'c69d4658-93a1-46b9-8209-df73602a9d1a', NULL, '2016-01-21 00:00:00', NULL, NULL, 'uGutvCmwchECwwgkvrMtHBonhLOiJEiSVEjaepMoqSSfHefjcq', NULL, NULL, NULL),
(13, 'max@gmail.com', '51c8b23f801d375fe1b413e8f24badd709d515e472f07bef76972a9951773df4', 'max', 'max', NULL, NULL, NULL, 'c2b9eeef-e708-480f-8af0-4726715fc5df', NULL, '2016-01-21 17:24:15', NULL, NULL, 'UVbQmQVjUPcRuUfXvbWpmznANlpZnczSIvadbaEGqdLYyKyVAF', NULL, NULL, NULL),
(14, 'zxc@gmail.com', '9aa7f549a95fa796bced1068a3438918c11963c7159d0d46094ef1a067847684', 'zxc', 'zxc', NULL, NULL, NULL, NULL, NULL, '2016-01-21 17:39:37', NULL, NULL, 'ipQjYmSvcrxQJHKALnhzApkKDmYSCsLmLMGGIMEnOjoMZQtZxJ', NULL, NULL, NULL),
(15, 'zxc@gmail.com', 'e6aee8f3372a168d13a3f063f3e6129b7372547f86aedbfd3181679758191c04', 'sad', 'das', NULL, NULL, NULL, 'edcd518c-73de-4906-a65e-564687f7653c', NULL, '2016-01-21 17:58:55', NULL, NULL, 'zyrCCyReaICZcxWukFnxYgBUmqsJJFvlfDqWucrKEfUFVWNJFK', NULL, NULL, NULL),
(16, 'user@gmail.com', '3dc2bbbbe9f2936e42f0db60b7f753b8c2f83394f794a60c8d847a4e1eb0f790', 'user', 'user', NULL, NULL, NULL, '6a477ccb-7b0e-4446-9845-7b77a5bdb71e', NULL, '2016-01-21 19:08:18', NULL, NULL, 'eRzTFKhvFvkxPdEIJKrfNpSQDOdDxNfrEKwLsaYHHyGAtyWlnL', NULL, NULL, NULL),
(19, 'dev@gmail.com', '890845237bd1496d285e26438ea3382eb4c697aa6b31f788b25779961eb6b6aa', 'dev', 'dev', NULL, NULL, NULL, NULL, NULL, '2016-01-22 14:04:41', NULL, NULL, 'jpmiymMXncXgTxiBICUSKEpcVvyRWqnnZFrGFbzEKErWWyFeNW', NULL, NULL, NULL);

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
(11, 'Ruby', 0, False);

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
(15, NULL, 10, '98057869462', 'llgogd334', 0, False);

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
(19, 10, 11);

-- 
-- Вывод данных для таблицы feedback
--
INSERT INTO feedback VALUES
(1, 4, 4, 'I liked this project, work was fine.', 5, 'dev', 0, False),
(2, 5, 4, 'Work was good, thanx.', 5, 'dev', 0, False),
(3, 4, 4, 'Thank you man, you are professional', 5, 'customer', 0, False),
(4, 4, 5, 'You did it correctly', 4, 'customer', 0, False);

-- 
-- Вывод данных для таблицы ordering
--
INSERT INTO ordering VALUES
(1, 'SMM Instagram', 'hourly', 'I need my instagram services sold, good rates for resale on my panel.', 1, '2009-06-13 00:00:00', 20.399999618530273, False, NULL, False, NULL, False, 0, False, NULL),
(2, 'mt5 stuff for Stonev', 'hourly', 'will send details of project. Fairly simple EA.', 2, '2015-09-23 00:00:00', 18.899999618530273, False, NULL, False, NULL, False, 0, False, NULL),
(3, 'True Mobile Website Design', 'fixed', 'This project is for a web design and development team that can build out True Mobile site for a website which is already responsive. Experienced design and development team candidates only. For this task you would do the following: 1) Using a mockup tool we will provide. Create a full integrated mockup for the current site for about 10 key pages. Once client approves the mockup, you would then design the entire site. Site has approximately 82 pages but would be condensed heavily, we would follow a theme based on a specific theme design pages such as (home, main category page (content), sub category page ( content), dinning menu page and photo gallery), all other pages would adjust to these specific page design. Mock would be done using https://www.fluidui.com/ or other design mockup tools that lets us preview the functionality. Site would view able in various devices (tablets and phones). If this meets the standards, development begins. ', 3, '2009-06-13 00:00:00', 1700, True, '2015-12-13 00:00:00', False, NULL, True, 0, False, NULL),
(4, 'Write some Software', 'hourly', 'customized software development project', 4, '2015-06-13 00:00:00', 14.5, True, '2015-07-01 00:00:00', True, '2015-08-21 00:00:00', False, 0, False, NULL),
(5, 'Malware/Redirect correction on Website', 'hourly', 'I am having a malware/redirect issue on my website that is causing it to redirect to random other websites. Also, there is not a specific page or product that causes the issue. It happens randomly throughout the site.', 5, '2015-11-03 00:00:00', 21.799999237060547, True, '2015-11-11 00:00:00', False, NULL, False, 0, False, NULL),
(6, 'GA Consulting', 'hourly', 'I am searching about a consulence on Google analytics.', 5, '2015-09-23 00:00:00', 13.899999618530273, False, NULL, False, NULL, False, 0, False, NULL);

-- 
-- Вывод данных для таблицы question
--
INSERT INTO question VALUES
(1, 'Question 1 gfdg dfjkf d ghdsfjkh gkdfh gdsf ghdf gdsfg hdf gkldsf kjldsfh kljdsfl ;dsfjl dsjkf ;dfs hfdsh jdsf', 6, 1, False, 0, False),
(2, 'Question 2 hfghlkfh fkdljgh d hglkjdf ghlkfj hgdsfklj gldsfk hgdsfjk ghldfjh gdjkl hgdfl hgjkldsf hgjkl hgldf d', 6, 1, False, 0, False),
(3, 'Question 3 hgdfkhg kdf', 6, 1, False, 0, False),
(4, 'Question 4 ghdfjk ghdfj hgdf', 6, 1, True, 0, False),
(5, 'Question 5 ghfjk hg', 6, 1, True, 0, False),
(6, 'Question 6 ghfjkdj fgdj gdf wope owepwo pq[ xx das dsa daewq qe wp[ oii io ioi io ipo op iui oiu u uoi u uu ui ou iuo uio uiu iui uio u upo uiui iuyth kgh dlfkjg fkdjg dfj ghdf ghdf', 6, 1, True, 0, False),
(7, 'Question 7 gfdj qw 23125765433333333333333333333333333333333333333332 34 32 ', 6, 1, False, 0, False),
(8, 'Question 8 flds sdfj jkg reow[i rowe[ pwe rw ,rew', 6, 1, True, 0, False),
(9, 'Question 9 fnds nfsd fds', 6, 1, False, 0, False),
(10, 'Question 10 hgf dfj ghsdfk hgkdf hgdf', 6, 1, False, 0, False);

-- 
-- Вывод данных для таблицы test
--
INSERT INTO test VALUES
(1, 1, '.net basics', 1, 70, 20, 0, False),
(2, 2, '1c basics', 1, 60, 15, 0, False),
(3, 3, 'c# basics', 1, 65, 17, 0, False);

-- 
-- Вывод данных для таблицы answer
--
INSERT INTO answer VALUES
(1, 1, True, '11', 0, False),
(2, 1, False, '10', 0, False),
(3, 1, True, '11', 0, False),
(4, 1, False, '10', 0, False),
(5, 2, True, '21', 0, False),
(6, 2, False, '20', 0, False),
(7, 2, False, '20', 0, False),
(8, 2, True, '21', 0, False),
(9, 3, False, '30', 0, False),
(10, 3, False, '30', 0, False),
(11, 3, True, '31', 0, False),
(12, 3, False, '30', 0, False),
(13, 4, False, '40', 0, False),
(14, 4, True, '41', 0, False),
(15, 4, False, '40', 0, False),
(16, 4, True, '41', 0, False),
(17, 5, False, '50', 0, False),
(18, 5, True, '51', 0, False),
(19, 5, False, '50', 0, False),
(20, 5, False, '50', 0, False),
(21, 6, True, '61', 0, False),
(22, 6, True, '61', 0, False),
(23, 6, True, '61', 0, False),
(24, 6, True, '61', 0, False),
(25, 7, False, '70', 0, False),
(26, 7, True, '71', 0, False),
(27, 7, False, '70', 0, False),
(28, 7, False, '70', 0, False),
(29, 8, False, '80', 0, False),
(30, 8, True, '81', 0, False),
(31, 8, False, '80', 0, False),
(32, 8, True, '81', 0, False),
(33, 9, False, '90', 0, False),
(34, 9, False, '90', 0, False),
(35, 9, True, '91', 0, False),
(36, 9, False, '90', 0, False),
(37, 10, True, '101', 0, False),
(38, 10, True, '101', 0, False),
(39, 10, False, '100', 0, False),
(40, 10, True, '101', 0, False);

-- 
-- Вывод данных для таблицы developer_qa
--
INSERT INTO developer_qa VALUES
(1, 1, 1, 70, '2016-01-31', False, 0, False),
(2, 1, 2, 40, '2016-01-13', True, 0, False),
(3, 1, 3, 50, '2016-01-04', True, 0, False);

-- 
-- Вывод данных для таблицы follower
--
INSERT INTO follower VALUES
(1, 1, 'I want this job', 3, 0, False),
(2, 2, 'Give it to me', 3, 0, False),
(3, 9, 'I want to do this job!!!!', 3, 0, False),
(4, 3, 'I want this job', 4, 0, False),
(5, 4, 'Give it to me', 4, 0, False),
(6, 6, 'I will take this project', 5, 0, False),
(7, 7, 'I will not let you down!', 5, 0, False),
(8, 8, 'I want to do this', 5, 0, False),
(9, 2, 'I want to try', 5, 0, False),
(10, 9, 'I want to do this job!!!!', 1, 0, False),
(11, 10, 'It is very easy for me, i will do this.', 1, 0, False),
(12, 9, 'I want to do this job!!!!', 2, 0, False),
(13, 10, 'It is very easy for me, i will do this.', 2, 0, False),
(14, 10, 'It is very easy for me, i will do this.', 6, 0, False);

-- 
-- Вывод данных для таблицы ordering_technology
--

-- Таблица freelancerdb.ordering_technology не содержит данных

-- 
-- Вывод данных для таблицы test_question
--
INSERT INTO test_question VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 1, 4),
(5, 1, 5),
(6, 1, 6),
(7, 1, 7),
(8, 1, 8),
(9, 1, 9),
(10, 1, 10),
(11, 2, 1),
(12, 2, 2),
(13, 2, 3),
(14, 2, 4),
(15, 2, 5),
(16, 2, 6),
(17, 3, 7),
(18, 3, 8),
(19, 3, 9),
(20, 3, 10),
(21, 3, 1),
(22, 3, 2),
(23, 3, 3),
(24, 3, 4);

-- 
-- Вывод данных для таблицы worker
--
INSERT INTO worker VALUES
(1, 3, 1, NULL, 78, 0, False),
(2, 3, 2, NULL, 88, 0, False),
(3, 3, 9, NULL, 72, 0, False),
(4, 4, 4, 14.5, 123, 0, False),
(5, 4, 5, 14.5, 130, 0, False),
(6, 5, 6, 21.799999237060547, 178, 0, False),
(7, 5, 7, 21.799999237060547, 171, 0, False),
(8, 5, 8, 21.799999237060547, 164, 0, False);

-- 
-- Восстановить предыдущий режим SQL (SQL mode)
-- 
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;

-- 
-- Включение внешних ключей
-- 
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;