--
-- Скрипт сгенерирован Devart dbForge Studio for MySQL, Версия 6.3.358.0
-- Домашняя страница продукта: http://www.devart.com/ru/dbforge/mysql/studio
-- Дата скрипта: 22.01.2016 12:15:10
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
  PRIMARY KEY (id),
  UNIQUE INDEX email (email),
  UNIQUE INDEX reg_url (reg_url),
  UNIQUE INDEX uuid (uuid)
)
ENGINE = INNODB
AUTO_INCREMENT = 9
AVG_ROW_LENGTH = 2048
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
  PRIMARY KEY (id),
  UNIQUE INDEX reg_url (reg_url),
  UNIQUE INDEX uuid (uuid)
)
ENGINE = INNODB
AUTO_INCREMENT = 18
AVG_ROW_LENGTH = 1024
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
-- Описание для таблицы developer_qa
--
DROP TABLE IF EXISTS developer_qa;
CREATE TABLE developer_qa (
  id INT(11) NOT NULL AUTO_INCREMENT,
  dev_id INT(11) DEFAULT NULL,
  tech_id INT(11) DEFAULT NULL,
  rate DOUBLE DEFAULT 0,
  expire DATE DEFAULT NULL,
  is_expire BIT(1) DEFAULT b'0',
  version INT(11) DEFAULT 0,
  is_deleted BIT(1) DEFAULT b'0',
  PRIMARY KEY (id),
  CONSTRAINT FK_developer_qa_developer_id FOREIGN KEY (dev_id)
    REFERENCES developer(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_developer_qa_technology_id FOREIGN KEY (tech_id)
    REFERENCES technology(id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 1
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
AUTO_INCREMENT = 1
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
AUTO_INCREMENT = 1
CHARACTER SET utf8
COLLATE utf8_general_ci;

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
AUTO_INCREMENT = 1
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
(1, 'kumar@gmail.com', 'Kumar', 'Anil', 'Kumar', '2010-11-23 21:34:01', 'en', NULL, NULL, '2004-06-17 00:00:00', 0, False, 'Anil', NULL),
(2, 'ryzkov@gmail.com', 'Ryzkov', 'Anton', 'Ryzkov', '2009-05-02 13:23:21', 'en', NULL, NULL, '2010-06-21 00:00:00', 0, False, 'Ryzkov', NULL),
(3, 'strinic@gmail.com', 'Strinic', 'Ivan', 'Strinic', '2006-02-19 16:03:34', 'en', NULL, NULL, '2003-02-11 00:00:00', 0, False, 'Strinic', NULL),
(4, 'belousov@gmail.com', 'Belousov', 'Nikolai', 'Belousov', '2009-11-28 22:26:01', 'en', NULL, NULL, '2009-06-13 00:00:00', 0, False, 'Belousov', NULL),
(5, 'mungki@gmail.com', 'Laulau', 'MungKi', 'Lau', '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Lau', NULL),
(6, 'sadamaza@gmail.com', '6e4938ca228ebe9b6d54b1989940fe5e2c714d93711f4cf8e79f8689f59dc3c8', 'sada', 'maza', NULL, NULL, '7131b214-c305-4dcc-93da-a73f6bab5ba4', NULL, '2016-01-21 00:00:00', NULL, NULL, 'UTQZNuHYMAVzabbGSGfTFTUzHbgsUALugVoggepYNlLfrzYQoq', NULL),
(7, 'fff@mail.ru', '79e5b9fe1d8a68d3f5eea4affe27348fb199a364bd10b261b50bc61eefdf6fd8', 'fff', 'fff', NULL, NULL, '95edc503-e7f1-46d1-9606-2332429d62d1', NULL, '2016-01-21 18:30:32', NULL, NULL, 'eWmRkNIsAYIZiiKtibBwDxBbbRfOerkUibGUgZoTjKmfjITwxA', NULL),
(8, 'uuu@gmaill.com', '8047f0a51abd294545fb064a697dfe6389cc7923258dd227e03d0d5936252402', 'uuu', 'uuu', NULL, NULL, NULL, NULL, '2016-01-21 21:52:58', NULL, NULL, 'dgtFJmXAPTWuGcoqQmgCkznPjzTdrudnxbFcBJNDFKyJnnHMAH', NULL);

-- 
-- Вывод данных для таблицы developer
--
INSERT INTO developer VALUES
(1, 'iuliana@gmail.com', 'Pavaloaie', 'Iuliana', 'Pavaloaie', 13.3, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Iuli', NULL),
(2, 'ivanov@gmail.com', 'Ivanov', 'Sergey', 'Ivanov', 10.2, '2011-05-21 22:44:01', 'en', NULL, NULL, '2011-10-11 00:00:00', 0, False, 'Serg', NULL),
(3, 'andres@gmail.com', 'Pinilla', 'Andres', 'Pinilla Palacios', 13.3, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Andr', NULL),
(4, 'ian@gmail.com', 'Anderson', 'Ian', 'Anderson', 12.4, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Ian', NULL),
(5, 'hajji@gmail.com', 'Makram', 'Hajji', 'Makram', 11.9, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Hajj', NULL),
(6, 'stanisa@gmail.com', 'Koncic', 'Stanisa', 'Koncic', 10.8, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Stan', NULL),
(7, 'ognev@gmail.com', 'Ognev', 'Ivan', 'Ognev', 25.2, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Ogn', NULL),
(8, 'gupta@gmail.com', 'Ashish', 'gupta', 'Ashish', 10.2, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'gupta', NULL),
(9, 'lamine@gmail.com', 'Jellad', 'Mohamed', 'Lamine Jellad', 14.2, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Mohamed', NULL),
(10, 'stefan@gmail.com', 'Scekic', 'Stefan', 'Scekic', 14.2, '2008-01-01 00:00:01', 'en', NULL, NULL, '2013-10-11 00:00:00', 0, False, 'Scekic', NULL),
(11, 'mrudevich96@gmail.com', '4ef4670e5feefd3f23abd4437d26833a701043e20677713bb8a63ac7839f1223', 'Max', 'Rudevich', NULL, NULL, NULL, 'c69d4658-93a1-46b9-8209-df73602a9d1a', NULL, '2016-01-21 00:00:00', NULL, NULL, 'uGutvCmwchECwwgkvrMtHBonhLOiJEiSVEjaepMoqSSfHefjcq', NULL),
(13, 'max@gmail.com', '51c8b23f801d375fe1b413e8f24badd709d515e472f07bef76972a9951773df4', 'max', 'max', NULL, NULL, NULL, 'c2b9eeef-e708-480f-8af0-4726715fc5df', NULL, '2016-01-21 17:24:15', NULL, NULL, 'UVbQmQVjUPcRuUfXvbWpmznANlpZnczSIvadbaEGqdLYyKyVAF', NULL),
(14, 'zxc@gmail.com', '9aa7f549a95fa796bced1068a3438918c11963c7159d0d46094ef1a067847684', 'zxc', 'zxc', NULL, NULL, NULL, NULL, NULL, '2016-01-21 17:39:37', NULL, NULL, 'ipQjYmSvcrxQJHKALnhzApkKDmYSCsLmLMGGIMEnOjoMZQtZxJ', NULL),
(15, 'zxc@gmail.com', 'e6aee8f3372a168d13a3f063f3e6129b7372547f86aedbfd3181679758191c04', 'sad', 'das', NULL, NULL, NULL, 'edcd518c-73de-4906-a65e-564687f7653c', NULL, '2016-01-21 17:58:55', NULL, NULL, 'zyrCCyReaICZcxWukFnxYgBUmqsJJFvlfDqWucrKEfUFVWNJFK', NULL),
(16, 'user@gmail.com', '3dc2bbbbe9f2936e42f0db60b7f753b8c2f83394f794a60c8d847a4e1eb0f790', 'user', 'user', NULL, NULL, NULL, '6a477ccb-7b0e-4446-9845-7b77a5bdb71e', NULL, '2016-01-21 19:08:18', NULL, NULL, 'eRzTFKhvFvkxPdEIJKrfNpSQDOdDxNfrEKwLsaYHHyGAtyWlnL', NULL),
(17, 'test@gmail.com', 'd1f6fb3965bf1484579f8fbd10632f17b757fe065ea5f2e221f6124df14b9b79', 'test', 'test', NULL, NULL, NULL, '3429bdba-8547-420d-8a45-cd5ae94de5b8', NULL, '2016-01-21 23:43:22', NULL, NULL, 'jHvBPpeGtpUaBUlmeJlVpfghiiLuqLVEzFSjgCtxZuWYXQqtgH', NULL);

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
-- Вывод данных для таблицы developer_qa
--

-- Таблица freelancerdb.developer_qa не содержит данных

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
(1, 'SMM Instagram', 'hourly', 'I need my instagram services sold, good rates for resale on my panel.', 1, '2009-06-13 00:00:00', 20.399999618530273, False, NULL, False, NULL, False, 0, False),
(2, 'mt5 stuff for Stonev', 'hourly', 'will send details of project. Fairly simple EA.', 2, '2015-09-23 00:00:00', 18.899999618530273, False, NULL, False, NULL, False, 0, False),
(3, 'True Mobile Website Design', 'fixed', 'This project is for a web design and development team that can build out True Mobile site for a website which is already responsive. Experienced design and development team candidates only. For this task you would do the following: 1) Using a mockup tool we will provide. Create a full integrated mockup for the current site for about 10 key pages. Once client approves the mockup, you would then design the entire site. Site has approximately 82 pages but would be condensed heavily, we would follow a theme based on a specific theme design pages such as (home, main category page (content), sub category page ( content), dinning menu page and photo gallery), all other pages would adjust to these specific page design. Mock would be done using https://www.fluidui.com/ or other design mockup tools that lets us preview the functionality. Site would view able in various devices (tablets and phones). If this meets the standards, development begins. ', 3, '2009-06-13 00:00:00', 1700, True, '2015-12-13 00:00:00', False, NULL, True, 0, False),
(4, 'Write some Software', 'hourly', 'customized software development project', 4, '2015-06-13 00:00:00', 14.5, True, '2015-07-01 00:00:00', True, '2015-08-21 00:00:00', False, 0, False),
(5, 'Malware/Redirect correction on Website', 'hourly', 'I am having a malware/redirect issue on my website that is causing it to redirect to random other websites. Also, there is not a specific page or product that causes the issue. It happens randomly throughout the site.', 5, '2015-11-03 00:00:00', 21.799999237060547, True, '2015-11-11 00:00:00', False, NULL, False, 0, False),
(6, 'GA Consulting', 'hourly', 'I am searching about a consulence on Google analytics.', 5, '2015-09-23 00:00:00', 13.899999618530273, False, NULL, False, NULL, False, 0, False);

-- 
-- Вывод данных для таблицы question
--
INSERT INTO question VALUES
(1, 'Р’ РїРµСЂРµРјРµРЅРЅРѕР№ С‚РёРїР° char РјРѕРіСѓС‚ С…СЂР°РЅРёС‚СЊСЃСЏ Р·РЅР°С‡РµРЅРёСЏ РёР· СЃР»РµРґСѓСЋС‰РµРіРѕ РґРёР°РїР°Р·РѕРЅР°', 6, 1, False, 0, False),
(2, 'Р’РѕР·РјРѕР¶РЅР° Р»Рё РїРµСЂРµРіСЂСѓР·РєР° РѕРїРµСЂР°С‚РѕСЂРѕРІ РІ Java?', 6, 1, False, 0, False),
(3, 'РљР°Рє РѕРїСЂРµРґРµР»РёС‚СЊ РґР»РёРЅСѓ РјР°СЃСЃРёРІР° myarray?', 6, 1, False, 0, False),
(4, 'РљР°РєРёРјРё СЃРїРѕСЃРѕР±РѕРј(-Р°РјРё) РјРѕР¶РµС‚ Р±С‹С‚СЊ РІС‹СЂР°Р¶РµРЅРѕ С‡РёСЃР»Рѕ 28?', 6, 1, True, 0, False),
(5, 'Р’С‹Р±РµСЂРёС‚Рµ РёР· РїСЂРёРІРµРґРµРЅРЅС‹С… РІСЃРµ РјРѕРґРёС„РёРєР°С‚РѕСЂС‹, РєРѕС‚РѕСЂС‹Рµ РїСЂРёРјРµРЅРёРјС‹ РґР»СЏ top-level РєР»Р°СЃСЃРѕРІ (РЅРµ РІР»РѕР¶РµРЅРЅС‹С…).', 6, 1, True, 0, False),
(6, 'РљР°РєР°СЏ СЃС‚СЂСѓРєС‚СѓСЂР° РґР°РЅРЅС‹С…, СЂРµР°Р»РёР·СѓСЋС‰Р°СЏ РёРЅС‚РµСЂС„РµР№СЃ Map РёСЃРїРѕР»СЊР·СѓРµС‚ РґР»СЏ СЃСЂР°РІРЅРµРЅРёСЏ РѕР±СЉРµРєС‚РѕРІ РѕРїРµСЂР°С‚РѕСЂ ==, Р° РЅРµ РјРµС‚РѕРґ equals.', 6, 1, True, 0, False),
(7, 'РњРѕР¶РЅРѕ Р»Рё РґРёРЅР°РјРёС‡РµСЃРєРё РјРµРЅСЏС‚СЊ СЂР°Р·РјРµСЂ РјР°СЃСЃРёРІР°?', 6, 1, False, 0, False),
(8, 'Р’С‹Р±РµСЂРёС‚Рµ РІСЃРµ РїСЂР°РІРёР»СЊРЅС‹Рµ РІР°СЂРёР°РЅС‚С‹ СЃРѕР·РґР°РЅРёСЏ РјР°СЃСЃРёРІРѕРІ.', 6, 1, True, 0, False),
(9, 'РњРѕР¶РЅРѕ Р»Рё РїСЂРё РѕР±СЉСЏРІР»РµРЅРёРё РєР»Р°СЃСЃР° РёСЃРїРѕР»СЊР·РѕРІР°С‚СЊ РјРѕРґРёС„РёРєР°С‚РѕСЂС‹ abstract Рё final РѕРґРЅРѕРІСЂРµРјРµРЅРЅРѕ?', 6, 1, False, 0, False),
(10, 'РЇРІР»СЏСЋС‚СЃСЏ Р»Рё РјР°СЃСЃРёРІС‹ РІ Java РѕРґРЅРѕСЂРѕРґРЅС‹РјРё?', 6, 1, False, 0, False);

-- 
-- Вывод данных для таблицы test
--

-- Таблица freelancerdb.test не содержит данных

-- 
-- Вывод данных для таблицы answer
--

-- Таблица freelancerdb.answer не содержит данных

-- 
-- Вывод данных для таблицы dev_test
--

-- Таблица freelancerdb.dev_test не содержит данных

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

-- Таблица freelancerdb.test_question не содержит данных

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