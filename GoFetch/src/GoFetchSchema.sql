SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `url` ;
CREATE SCHEMA IF NOT EXISTS `url` DEFAULT CHARACTER SET utf8 ;
USE `url` ;

-- -----------------------------------------------------
-- Table `url`.`url`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `url`.`url` (
  `url_id` INT NOT NULL AUTO_INCREMENT ,
  `url_address` VARCHAR(200) NOT NULL ,
  `get_fb_data` TINYINT(1) NOT NULL ,
  `get_twitter_data` TINYINT(1) NOT NULL ,
  `get_backlinks` TINYINT(1) NOT NULL COMMENT 'If true, this indicates this is a target url and its id should be added to links table as a target' ,
  `date` DATE NOT NULL ,
  `user_id` VARCHAR(45) NULL COMMENT 'Ensures that each target url be associated with a user, source urls do not need to be assoc with a user id.' ,
  `category` VARCHAR(45) NULL ,
  `user_category` VARCHAR(45) NULL ,
  `user_campaign` VARCHAR(45) NULL ,
  `user_assigned_to` VARCHAR(45) NULL ,
  `domain` VARCHAR(45) NULL ,
  `doc_title` VARCHAR(45) NULL ,
  `pa` INT NULL ,
  `da` INT NULL ,
  PRIMARY KEY (`url_id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


-- -----------------------------------------------------
-- Table `url`.`facebook`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `url`.`facebook` (
  `fb_id` INT NOT NULL AUTO_INCREMENT COMMENT 'Represents a URLs FB data for a specific date.' ,
  `url_id` INT NOT NULL ,
  `date` DATE NOT NULL ,
  `total_count` INT NOT NULL ,
  `like_count` INT NOT NULL ,
  `comment_count` INT NOT NULL ,
  `share_count` INT NOT NULL ,
  `click_count` INT NOT NULL ,
  PRIMARY KEY (`fb_id`, `url_id`) ,
  INDEX `fk_facebook_url` (`url_id` ASC) ,
  CONSTRAINT `fk_facebook_url`
    FOREIGN KEY (`url_id` )
    REFERENCES `url`.`url` (`url_id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


-- -----------------------------------------------------
-- Table `url`.`twitter_mention`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `url`.`twitter_mention` (
  `twitter_id` INT NOT NULL AUTO_INCREMENT ,
  `url_id` INT NOT NULL ,
  `date` DATE NOT NULL ,
  `tweeter` VARCHAR(45) NOT NULL ,
  `followers` INT NOT NULL ,
  `followed` INT NOT NULL ,
  `klout_score` INT NOT NULL ,
  `tweet_text` VARCHAR(141) NOT NULL COMMENT 'Represents a twitter mention of the URL with associated data about the tweeter.' ,
  PRIMARY KEY (`twitter_id`, `url_id`) ,
  INDEX `fk_twitter_mention_url1` (`url_id` ASC) ,
  CONSTRAINT `fk_twitter_mention_url1`
    FOREIGN KEY (`url_id` )
    REFERENCES `url`.`url` (`url_id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `url`.`seomoz_data`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `url`.`seomoz_data` (
  `seomoz_id` INT NOT NULL AUTO_INCREMENT ,
  `url_id` INT NOT NULL ,
  `rga_score` VARCHAR(10) NULL ,
  `auditor_rank` INT NULL ,
  `auditor_id` INT NULL ,
  `last_question` INT NULL ,
  `comment` VARCHAR(45) NULL ,
  PRIMARY KEY (`seomoz_id`, `url_id`) ,
  INDEX `fk_seomoz_data_url1` (`url_id` ASC) ,
  CONSTRAINT `fk_seomoz_data_url1`
    FOREIGN KEY (`url_id` )
    REFERENCES `url`.`url` (`url_id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `url`.`links`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `url`.`links` (
  `links_id` INT NOT NULL AUTO_INCREMENT ,
  `target_id` INT NOT NULL ,
  `source_id` INT NOT NULL COMMENT 'Represents hyperlinks btwn source which points at target.' ,
  `anchor_text` VARCHAR(200) NOT NULL ,
  `date_detected` DATE NOT NULL ,
  `date_expired` DATE NULL ,
  PRIMARY KEY (`links_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `url`.`keywords`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `url`.`keywords` (
  `keyword` VARCHAR(45) NOT NULL ,
  `search_volume` INT NOT NULL ,
  `vertical` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`keyword`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `url`.`SEQUENCE`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `url`.`SEQUENCE` (
  `SEQ_NAME` VARCHAR(50) NULL ,
  `SEQ_COUNT` DECIMAL(15) NULL )
ENGINE = InnoDB;

INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
