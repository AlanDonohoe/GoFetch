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
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT ''refers to the subdomains or sub categories that may be assigned to a client. /dresses, /shoes, etc'',
  `category` varchar(45) NOT NULL,
  `users_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_client_category_users1` (`users_id`),
  CONSTRAINT `fk_client_category_users1` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8


-- -----------------------------------------------------
-- Table `url`.`links`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `url`.`links` ;

CREATE  TABLE IF NOT EXISTS `url`.`links` (  
  `links_id` int(11) NOT NULL AUTO_INCREMENT,
  `target_id` int(11) NOT NULL,
  `source_id` int(11) NOT NULL COMMENT ''Represents hyperlinks btwn source which points at target.'',
  `anchor_text` varchar(200) NOT NULL,
  `date_detected` date NOT NULL,
  `final_target_url` varchar(200) NOT NULL,
  `date_expired` date DEFAULT NULL,
  `user_category` varchar(45) DEFAULT NULL,
  `user_assigned_to` varchar(45) DEFAULT NULL,
  `user_campaign` varchar(45) DEFAULT NULL,
  `client` varchar(45) DEFAULT NULL,
  `users_user_id` int(11) DEFAULT NULL,
  `data_entered_by` tinyint(1) DEFAULT NULL,
  `client_category_id` int(11) DEFAULT NULL,
  `client_category_users_user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`links_id`),
  KEY `fk_url_users1` (`users_user_id`),
  KEY `fk_url_client_category1` (`client_category_id`,`client_category_users_user_id`),
  CONSTRAINT `fk_link_client_category1` FOREIGN KEY (`client_category_id`) REFERENCES `client_category` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_link_users1` FOREIGN KEY (`users_user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8511 DEFAULT CHARSET=utf8


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
) ENGINE=InnoDB AUTO_INCREMENT=8802 DEFAULT CHARSET=utf8

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
-- Table `url`.`url`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `url`.`url` ;

CREATE  TABLE IF NOT EXISTS `url`.`url` (
  `url_id` int(11) NOT NULL AUTO_INCREMENT,
  `url_address` varchar(500) NOT NULL,
  `get_social_data` tinyint(1) NOT NULL,
  `get_backlinks` tinyint(1) NOT NULL COMMENT ''If true, this indicates this is a target url and its id should be added to links table as a target'',
  `date` date NOT NULL,
  `page_authority` int(11) DEFAULT NULL,
  `domain_authority` int(11) DEFAULT NULL,
  `user_id` varchar(45) DEFAULT NULL COMMENT ''Ensures that each target url be associated with a user, source urls do not need to be assoc with a user id.'',
  `category` varchar(45) DEFAULT NULL,
  `domain` varchar(200) DEFAULT NULL,
  `doc_title` varchar(45) DEFAULT NULL,
  `seomoz_url` tinyint(1) DEFAULT NULL COMMENT ''If true, this source url was detected by SEOMoz.'',
  `backlinks_got` tinyint(1) DEFAULT NULL,
  `no_of_layers` tinyint(1) DEFAULT NULL,
  `social_data_date` date DEFAULT NULL,
  `social_data_freq` tinyint(4) DEFAULT NULL,
  `users_user_id` int(11) DEFAULT NULL,
  `client_category_id` int(11) DEFAULT NULL,
  `client_category_users_user_id` int(11) DEFAULT NULL,
  `data_entered_by` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`url_id`),
  KEY `fk_url_users1` (`users_user_id`),
  KEY `fk_url_client_category1` (`client_category_id`,`client_category_users_user_id`),
  CONSTRAINT `fk_url_client_category1` FOREIGN KEY (`client_category_id`) REFERENCES `client_category` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_url_users1` FOREIGN KEY (`users_user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8764 DEFAULT CHARSET=utf8


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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;	