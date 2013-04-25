-- updated 24-4-13: Alan Donohoe

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `url` ;
CREATE SCHEMA IF NOT EXISTS `url` DEFAULT CHARACTER SET utf8 ;
USE `url` ;


-- -----------------------------------------------------
-- Table `url`.`SEQUENCE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `url`.`SEQUENCE` ;

CREATE  TABLE IF NOT EXISTS `url`.`SEQUENCE` (
  `SEQ_NAME` varchar(50) DEFAULT NULL,
  `SEQ_COUNT` decimal(15,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8

INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0);


-- -----------------------------------------------------
-- Table `url`.`client_category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `url`.`client_category` ;

CREATE  TABLE IF NOT EXISTS `url`.`client_category` (
 `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'refers to the subdomains or sub categories that may be assigned to a client. /dresses, /shoes, etc',
  `category` varchar(45) NOT NULL,
  `users_id` int(11) NOT NULL,
  `client_default` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_client_category_users1` (`users_id`),
  CONSTRAINT `fk_client_category_users1` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8


-- -----------------------------------------------------
-- Table `url`.`keywords`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `url`.`keywords` ;

CREATE  TABLE IF NOT EXISTS `url`.`keywords` (
  `keyword` varchar(45) NOT NULL,
  `search_volume` int(11) NOT NULL,
  `vertical` varchar(45) NOT NULL,
  PRIMARY KEY (`keyword`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

-- -----------------------------------------------------
-- Table `url`.`link_building_activity`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `url`.`link_building_activity` ;

CREATE  TABLE IF NOT EXISTS `url`.`link_building_activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activity` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8


-- -----------------------------------------------------
-- Table `url`.`links`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `url`.`links` ;

CREATE  TABLE IF NOT EXISTS `url`.`links` (  
   `links_id` int(11) NOT NULL AUTO_INCREMENT,
  `target_id` int(11) NOT NULL,
  `source_id` int(11) NOT NULL COMMENT 'Represents hyperlinks btwn source which points at target.',
  `anchor_text` varchar(200) NOT NULL,
  `date_detected` date NOT NULL,
  `final_target_url` varchar(500) DEFAULT NULL,
  `final_target_url_id` int(11) DEFAULT NULL,
  `date_expired` date DEFAULT NULL,
  `user_category` varchar(45) DEFAULT NULL,
  `user_assigned_to` varchar(45) DEFAULT NULL,
  `user_campaign` varchar(45) DEFAULT NULL,
  `client` varchar(45) DEFAULT NULL,
  `users_user_id` int(11) DEFAULT NULL,
  `data_entered_by` tinyint(1) DEFAULT NULL,
  `client_category_id` int(11) DEFAULT NULL,
  `link_building_activity_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`links_id`),
  KEY `fk_links_client_category` (`client_category_id`),
  KEY `fk_links_users` (`users_user_id`),
  KEY `fk_links_link_building_activity_id` (`link_building_activity_id`),
  CONSTRAINT `fk_links_client_category` FOREIGN KEY (`client_category_id`) REFERENCES `client_category` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_links_link_building_activity` FOREIGN KEY (`link_building_activity_id`) REFERENCES `link_building_activity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_links_users` FOREIGN KEY (`users_user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=800916 DEFAULT CHARSET=utf8

-- -----------------------------------------------------
-- Table `url`.`linksafe_data`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `url`.`linksafe_data` ;

CREATE  TABLE IF NOT EXISTS `url`.`linksafe_data` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
  `url_id` int(11) NOT NULL,
  `rga_score` varchar(10) DEFAULT NULL,
  `auditor_rank` int(11) DEFAULT NULL,
  `auditor_id` int(11) DEFAULT NULL,
  `last_question` int(11) DEFAULT NULL,
  `comment` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`,`url_id`),
  KEY `fk_seomoz_data_url1` (`url_id`),
  CONSTRAINT `fk_seomoz_data_url1` FOREIGN KEY (`url_id`) REFERENCES `url` (`url_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8


-- -----------------------------------------------------
-- Table `url`.`misc_social_data`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `url`.`misc_social_data` ;

CREATE  TABLE IF NOT EXISTS `url`.`misc_social_data` (
  `social_data_id` int(11) NOT NULL AUTO_INCREMENT,
  `url_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `stumble_upon` int(11) DEFAULT NULL,
  `delicious` int(11) DEFAULT NULL,
  `pinterest` int(11) DEFAULT NULL,
  `linkedin` int(11) DEFAULT NULL,
  `google_plus_one` int(11) DEFAULT NULL,
  `twitter` int(11) DEFAULT NULL,
  `fb_total_count` int(11) DEFAULT NULL,
  `fb_like_count` int(11) DEFAULT NULL,
  `fb_comment_count` int(11) DEFAULT NULL,
  `fb_share_count` int(11) DEFAULT NULL,
  `fb_click_count` int(11) DEFAULT NULL,
  `fb_commentsbox_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`social_data_id`,`url_id`),
  KEY `fk_misc_social_data_url1` (`url_id`),
  CONSTRAINT `fk_misc_social_data_url1` FOREIGN KEY (`url_id`) REFERENCES `url` (`url_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=804860 DEFAULT CHARSET=utf8

-- -----------------------------------------------------
-- Table `url`.`url`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `url`.`url` ;

CREATE  TABLE IF NOT EXISTS `url`.`url` (
  `url_id` int(11) NOT NULL AUTO_INCREMENT,
  `url_address` varchar(500) DEFAULT NULL,
  `get_social_data` tinyint(1) NOT NULL,
  `get_backlinks` tinyint(1) NOT NULL COMMENT 'If true, this indicates this is a target url and its id should be added to links table as a target',
  `date` date NOT NULL,
  `page_authority` int(11) DEFAULT NULL,
  `domain_authority` int(11) DEFAULT NULL,
  `user_id` varchar(45) DEFAULT NULL COMMENT 'Ensures that each target url be associated with a user, source urls do not need to be assoc with a user id.',
  `category` varchar(45) DEFAULT NULL,
  `domain` varchar(200) DEFAULT NULL,
  `doc_title` varchar(45) DEFAULT NULL,
  `seomoz_url` tinyint(1) DEFAULT NULL COMMENT 'If true, this source url was detected by SEOMoz.',
  `backlinks_got` tinyint(1) DEFAULT NULL,
  `no_of_layers` tinyint(1) DEFAULT NULL,
  `social_data_freq` tinyint(1) DEFAULT NULL,
  `social_data_date` date DEFAULT NULL,
  `users_user_id` int(11) DEFAULT NULL,
  `client_category_id` int(11) DEFAULT NULL,
  `data_entered_by` tinyint(1) DEFAULT NULL,
  `client_target_url` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`url_id`),
  KEY `fk_url_users1` (`users_user_id`),
  KEY `fk_url_client_category` (`client_category_id`),
  CONSTRAINT `fk_url_client_category` FOREIGN KEY (`client_category_id`) REFERENCES `client_category` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_url_users1` FOREIGN KEY (`users_user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=800891 DEFAULT CHARSET=utf8




-- -----------------------------------------------------
-- Table `url`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `url`.`users` ;

CREATE  TABLE IF NOT EXISTS `url`.`users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(225) NOT NULL,
  `password` varchar(225) NOT NULL,
  `email` varchar(225) DEFAULT NULL,
  `client` tinyint(1) NOT NULL,
  `displayed_name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8


INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (1,'marksandspencer','marksandspencer','alandonohoe123@gmail.com',true,'Marks And Spencer');
INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (2,'atterleyroad','atterleyroad','alandonohoe123@gmail.com',true,'Atterley Road');
INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (3,'whitestuff','whitestuff','alandonohoe123@gmail.com',true,'White Stuff');
INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (4,'gant','gant','alandonohoe123@gmail.com',true,'Gant');
INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (5,'alandonohoe','alandonohoe','alandonohoe123@gmail.com',false,'Alan Donohoe');
INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (6,'forrestholidays','forrestholidays','alandonohoe123@gmail.com',true,'Forrest Holidays');
INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (7,'1926','1926','alandonohoe123@gmail.com',true,'1926');
INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (8,'snowandrock','snowandrock','alandonohoe123@gmail.com',true,'Snow + Rock');
INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (9,'steamertrading','steamertrading','alandonohoe123@gmail.com',true,'Steamer Trading');
INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (10,'thinkpuglia','thinkpuglia','alandonohoe123@gmail.com',true,'Think Puglia');
INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (11,'eclipse','eclipse','alandonohoe123@gmail.com',true,'Eclipse');
INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (12,'naturalworldsafaris','naturalworldsafaris','alandonohoe123@gmail.com',true,'Natural World Safaris');
INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (13,'cvtravel','cvtravel','alandonohoe123@gmail.com',true,'CV Travel');
INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (14,'londoncityairport','londoncityairport','alandonohoe123@gmail.com',true,'London City Airport');
INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (15,'maximise','maximise','alandonohoe123@gmail.com',true,'Maximise');
INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (16,'westfieldlondon','westfieldlondon','alandonohoe123@gmail.com',true,'Westfield London');
INSERT INTO users (id,username,password,email,client,displayed_name) VALUES (17,'westfieldstratford','westfieldstratford','alandonohoe123@gmail.com',true,'Westfield Stratford')

INSERT INTO client_category (id,category,users_id,client_default) VALUES (1,'Dresses',1,null);
INSERT INTO client_category (id,category,users_id,client_default) VALUES (2,'Shoes',1,null);
INSERT INTO client_category (id,category,users_id,client_default) VALUES (3,'Lingerie',1,null);
INSERT INTO client_category (id,category,users_id,client_default) VALUES (4,'Mens Suits',1,null);
INSERT INTO client_category (id,category,users_id,client_default) VALUES (5,'Homepage',1,null);
INSERT INTO client_category (id,category,users_id,client_default) VALUES (6,'Other',1,null);
INSERT INTO client_category (id,category,users_id,client_default) VALUES (7,'Marks And Spencer',1,true);
INSERT INTO client_category (id,category,users_id,client_default) VALUES (8,'1926',7,true);
INSERT INTO client_category (id,category,users_id,client_default) VALUES (9,'Atterley Road',2,true);
INSERT INTO client_category (id,category,users_id,client_default) VALUES (10,'Steamer Trading',9,true);

INSERT INTO link_building_activity (id,activity) VALUES (10,'News Wires');
INSERT INTO link_building_activity (id,activity) VALUES (11,'Link Chase');
INSERT INTO link_building_activity (id,activity) VALUES (12,'Blogger Relations');
INSERT INTO link_building_activity (id,activity) VALUES (13,'Directories');
INSERT INTO link_building_activity (id,activity) VALUES (14,'Media Relations');
INSERT INTO link_building_activity (id,activity) VALUES (15,'Partnerships');
INSERT INTO link_building_activity (id,activity) VALUES (16,'Niche Communities');
INSERT INTO link_building_activity (id,activity) VALUES (18,'Content Creation');

INSERT INTO url (url_id,url_address,get_social_data,get_backlinks,date,page_authority,domain_authority,user_id,category,domain,doc_title,seomoz_url,backlinks_got,no_of_layers,social_data_freq,social_data_date,users_user_id,client_category_id,data_entered_by,client_target_url) VALUES (14,'http://www.marksandspencer.com/Maternity-Bras-Guide-Lingerie-Fit-Style-Guide-Lingerie-Underwear-Womens/b/908657031',1,1,{d '2012-07-19'},43,85,'gary@propellernet.co.uk',null,null,'',null,1,null,1,{d '2013-04-25'},null,null,null,1);
INSERT INTO url (url_id,url_address,get_social_data,get_backlinks,date,page_authority,domain_authority,user_id,category,domain,doc_title,seomoz_url,backlinks_got,no_of_layers,social_data_freq,social_data_date,users_user_id,client_category_id,data_entered_by,client_target_url) VALUES (33,'http://www.marksandspencer.com/Dress-your-Bodyshape-Dresses-Womens/b/1892339031',1,1,{d '2012-07-17'},53,85,'megan@propellernet.co.uk',null,null,'How to Dress for Your Body Shape - What Shap',null,1,null,1,{d '2013-05-09'},null,1,null,1);
INSERT INTO url (url_id,url_address,get_social_data,get_backlinks,date,page_authority,domain_authority,user_id,category,domain,doc_title,seomoz_url,backlinks_got,no_of_layers,social_data_freq,social_data_date,users_user_id,client_category_id,data_entered_by,client_target_url) VALUES (34,'http://www.marksandspencer.com/Dresses-Womens/b/43091030',1,1,{d '2012-07-18'},71,85,'megan@propellernet.co.uk',null,null,'',null,1,null,3,{d '2013-04-25'},1,1,null,1);
INSERT INTO url (url_id,url_address,get_social_data,get_backlinks,date,page_authority,domain_authority,user_id,category,domain,doc_title,seomoz_url,backlinks_got,no_of_layers,social_data_freq,social_data_date,users_user_id,client_category_id,data_entered_by,client_target_url) VALUES (6522,'http://www.marksandspencer.com/Suits-Mens/b/43483030 ',1,1,{d '2012-08-20'},1,84,'rik@propellernet.co.uk',null,null,'',0,1,null,3,{d '2013-04-25'},null,null,null,1);
INSERT INTO url (url_id,url_address,get_social_data,get_backlinks,date,page_authority,domain_authority,user_id,category,domain,doc_title,seomoz_url,backlinks_got,no_of_layers,social_data_freq,social_data_date,users_user_id,client_category_id,data_entered_by,client_target_url) VALUES (6531,'http://www.marksandspencer.com/Mens/b/43371030 ',1,1,{d '2012-08-20'},1,84,'rik@propellernet.co.uk',null,null,'',0,1,null,3,{d '2013-04-25'},null,null,null,1);
INSERT INTO url (url_id,url_address,get_social_data,get_backlinks,date,page_authority,domain_authority,user_id,category,domain,doc_title,seomoz_url,backlinks_got,no_of_layers,social_data_freq,social_data_date,users_user_id,client_category_id,data_entered_by,client_target_url) VALUES (6537,'http://www.marksandspencer.com/Lingerie-Underwear-Womens/b/43233030 ',1,1,{d '2012-08-20'},1,84,'rik@propellernet.co.uk',null,null,'',0,1,null,3,{d '2013-04-25'},null,null,null,1);
INSERT INTO url (url_id,url_address,get_social_data,get_backlinks,date,page_authority,domain_authority,user_id,category,domain,doc_title,seomoz_url,backlinks_got,no_of_layers,social_data_freq,social_data_date,users_user_id,client_category_id,data_entered_by,client_target_url) VALUES (6541,'http://www.marksandspencer.com/Bras-Lingerie-Underwear-Womens/b/43246030 ',1,1,{d '2012-08-20'},1,84,'rik@propellernet.co.uk',null,null,'',0,1,null,3,{d '2013-04-25'},null,null,null,1);
INSERT INTO url (url_id,url_address,get_social_data,get_backlinks,date,page_authority,domain_authority,user_id,category,domain,doc_title,seomoz_url,backlinks_got,no_of_layers,social_data_freq,social_data_date,users_user_id,client_category_id,data_entered_by,client_target_url) VALUES (54382,'http://www.marksandspencer.com/Dress-your-Bodyshape-Dresses-Womens/b/1892339031',1,0,{d '2012-10-20'},39,85,null,null,'marksandspencer.com/','',1,0,0,2,{d '2013-04-25'},null,null,0,1);
INSERT INTO url (url_id,url_address,get_social_data,get_backlinks,date,page_authority,domain_authority,user_id,category,domain,doc_title,seomoz_url,backlinks_got,no_of_layers,social_data_freq,social_data_date,users_user_id,client_category_id,data_entered_by,client_target_url) VALUES (54431,'http://www.marksandspencer.com/Womens/b/42967030',1,0,{d '2012-10-20'},73,85,null,null,'marksandspencer.com/',' ',1,0,0,2,{d '2013-04-25'},null,null,0,1);
INSERT INTO url (url_id,url_address,get_social_data,get_backlinks,date,page_authority,domain_authority,user_id,category,domain,doc_title,seomoz_url,backlinks_got,no_of_layers,social_data_freq,social_data_date,users_user_id,client_category_id,data_entered_by,client_target_url) VALUES (40644,'http://whitelilygreen.blogspot.com/2012/07/m-nursing-bra-review.html',1,0,{d '2012-10-19'},1,94,null,null,'whitelilygreen.blogspot.com/','',1,0,0,1,{d '2013-04-25'},null,null,0,1);
INSERT INTO url (url_id,url_address,get_social_data,get_backlinks,date,page_authority,domain_authority,user_id,category,domain,doc_title,seomoz_url,backlinks_got,no_of_layers,social_data_freq,social_data_date,users_user_id,client_category_id,data_entered_by,client_target_url) VALUES (156267,'http://www.marksandspencer.com/Lingerie-Fit-Style-Guide-Lingerie-Underwear-Womens/b/57537031',1,0,{d '2012-11-22'},48,85,null,null,'marksandspencer.com/','',1,0,null,1,{d '2013-04-27'},null,null,0,1);

INSERT INTO Links (links_id,target_id,source_id,anchor_text,date_detected,final_target_url,final_target_url_id,date_expired,user_category,user_assigned_to,user_campaign,client,users_user_id,data_entered_by,client_category_id,link_building_activity_id) VALUES (40645,14,40644,'Maternity Bra Advice Tool',{d '2012-10-19'},'http://www.marksandspencer.com/Maternity-Bras-Guide-Lingerie-Fit-Style-Guide-Lingerie-Underwear-Womens/b/908657031',null,null,null,null,null,null,null,0,null,null);
INSERT INTO Links (links_id,target_id,source_id,anchor_text,date_detected,final_target_url,final_target_url_id,date_expired,user_category,user_assigned_to,user_campaign,client,users_user_id,data_entered_by,client_category_id,link_building_activity_id) VALUES (156288,14,156267,'Maternity Bras Guide',{d '2012-11-22'},'http://www.marksandspencer.com/Maternity-Bras-Guide-Lingerie-Fit-Style-Guide-Lingerie-Underwear-Womens/b/908657031',null,null,null,null,null,null,null,0,null,null);


INSERT INTO misc_social_data (social_data_id,url_id,date,stumble_upon,delicious,pinterest,linkedin,google_plus_one,twitter,fb_total_count,fb_like_count,fb_comment_count,fb_share_count,fb_click_count,fb_commentsbox_count) VALUES (4818,14,{d '2012-08-12'},0,0,0,0,1,0,0,0,0,0,0,0);
INSERT INTO misc_social_data (social_data_id,url_id,date,stumble_upon,delicious,pinterest,linkedin,google_plus_one,twitter,fb_total_count,fb_like_count,fb_comment_count,fb_share_count,fb_click_count,fb_commentsbox_count) VALUES (10510,14,{d '2012-09-11'},0,0,0,0,1,0,1,1,0,0,0,0);
INSERT INTO misc_social_data (social_data_id,url_id,date,stumble_upon,delicious,pinterest,linkedin,google_plus_one,twitter,fb_total_count,fb_like_count,fb_comment_count,fb_share_count,fb_click_count,fb_commentsbox_count) VALUES (17506,14,{d '2012-09-27'},0,0,0,0,1,0,1,1,0,0,0,0);
INSERT INTO misc_social_data (social_data_id,url_id,date,stumble_upon,delicious,pinterest,linkedin,google_plus_one,twitter,fb_total_count,fb_like_count,fb_comment_count,fb_share_count,fb_click_count,fb_commentsbox_count) VALUES (146131,14,{d '2012-11-17'},0,0,0,0,1,0,2,2,0,0,0,0);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;	