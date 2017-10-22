-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema capstone_project
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `capstone_project` ;

-- -----------------------------------------------------
-- Schema capstone_project
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `capstone_project` DEFAULT CHARACTER SET utf8 ;
USE `capstone_project` ;

-- -----------------------------------------------------
-- Table `capstone_project`.`field_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`field_type` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`field_type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `number_player` INT NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`profile`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`profile` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`profile` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `address` VARCHAR(500) NULL,
  `phone` VARCHAR(20) NOT NULL,
  `longitude` VARCHAR(45) NULL,
  `latitude` VARCHAR(45) NULL,
  `creadit_card` VARCHAR(20) NULL,
  `avatar_url` VARCHAR(500) NULL,
  `rating_score` INT NULL,
  `bonus_point` INT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`role` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(10) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `role_name_UNIQUE` (`role_name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`account` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`account` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL,
  `password` VARCHAR(30) NOT NULL,
  `profile_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  `status` TINYINT(1) NOT NULL,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  PRIMARY KEY (`id`),
  INDEX `account_ref_profile_idx` (`profile_id` ASC),
  INDEX `account_ref_role_idx` (`role_id` ASC),
  CONSTRAINT `account_ref_profile`
    FOREIGN KEY (`profile_id`)
    REFERENCES `capstone_project`.`profile` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `account_ref_role`
    FOREIGN KEY (`role_id`)
    REFERENCES `capstone_project`.`role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`field`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`field` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`field` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `field_owner_id` INT NOT NULL,
  `field_type_id` INT NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `field_ref_field_type_idx` (`field_type_id` ASC),
  INDEX `field_ref_field_owner_idx` (`field_owner_id` ASC),
  CONSTRAINT `field_ref_field_type`
    FOREIGN KEY (`field_type_id`)
    REFERENCES `capstone_project`.`field_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `field_ref_field_owner`
    FOREIGN KEY (`field_owner_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`time_enable`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`time_enable` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`time_enable` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `field_type_id` INT NOT NULL,
  `field_owner_id` INT NOT NULL,
  `date_in_week` VARCHAR(10) NOT NULL,
  `start_time` TIME NOT NULL,
  `end_time` TIME NOT NULL,
  `price` FLOAT NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `time_enable_ref_field_type_idx` (`field_type_id` ASC),
  INDEX `time_enable_ref_field_owner_idx` (`field_owner_id` ASC),
  CONSTRAINT `time_enable_ref_field_type`
    FOREIGN KEY (`field_type_id`)
    REFERENCES `capstone_project`.`field_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `time_enable_ref_field_owner`
    FOREIGN KEY (`field_owner_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`promotion`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`promotion` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`promotion` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `field_type_id` INT NOT NULL,
  `field_owner_id` INT NOT NULL,
  `date_from` DATETIME NOT NULL,
  `date_to` DATETIME NOT NULL,
  `start_time` TIME NOT NULL,
  `end_time` TIME NOT NULL,
  `sale_off` FLOAT NOT NULL,
  `free_services` VARCHAR(1000) NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `promotion_ref_field_type_idx` (`field_type_id` ASC),
  INDEX `promotion_ref_field_owner_idx` (`field_owner_id` ASC),
  CONSTRAINT `promotion_ref_field_type`
    FOREIGN KEY (`field_type_id`)
    REFERENCES `capstone_project`.`field_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `promotion_ref_field_owner`
    FOREIGN KEY (`field_owner_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`time_slot`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`time_slot` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`time_slot` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `field_id` INT NULL,
  `field_owner_id` INT NOT NULL,
  `field_type_id` INT NOT NULL,
  `date` DATE NOT NULL,
  `start_time` TIME NOT NULL,
  `end_time` TIME NOT NULL,
  `price` FLOAT NULL,
  `reserve_status` TINYINT(1) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `time_slot_ref_field_idx` (`field_id` ASC),
  INDEX `time_slot_ref_field_owner_idx` (`field_owner_id` ASC),
  INDEX `time_slot_ref_field_type_idx` (`field_type_id` ASC),
  CONSTRAINT `time_slot_ref_field`
    FOREIGN KEY (`field_id`)
    REFERENCES `capstone_project`.`field` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `time_slot_ref_field_owner`
    FOREIGN KEY (`field_owner_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `time_slot_ref_field_type`
    FOREIGN KEY (`field_type_id`)
    REFERENCES `capstone_project`.`field_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`matching_request`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`matching_request` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`matching_request` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `field_type_id` INT NOT NULL,
  `longitude` VARCHAR(45) NOT NULL,
  `latitude` VARCHAR(45) NOT NULL,
  `address` VARCHAR(500) NOT NULL,
  `date` DATE NOT NULL,
  `start_time` TIME NOT NULL,
  `end_time` TIME NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `matching_request_ref_field_type_idx` (`field_type_id` ASC),
  INDEX `matching_request_ref_user_idx` (`user_id` ASC),
  CONSTRAINT `matching_request_ref_field_type`
    FOREIGN KEY (`field_type_id`)
    REFERENCES `capstone_project`.`field_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `matching_request_ref_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`friendly_match`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`friendly_match` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`friendly_match` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `time_slot_id` INT NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `friendly_match_ref_time_slot_idx` (`time_slot_id` ASC),
  INDEX `friendly_match_ref_user_idx` (`user_id` ASC),
  CONSTRAINT `friendly_match_ref_time_slot`
    FOREIGN KEY (`time_slot_id`)
    REFERENCES `capstone_project`.`time_slot` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `friendly_match_ref_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`tour_match`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`tour_match` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`tour_match` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `opponent_id` INT NOT NULL,
  `time_slot_id` INT NOT NULL,
  `complete_status` TINYINT(1) NOT NULL DEFAULT 0,
  `winner_id` INT NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `tour_match_refer_time_slot_idx` (`time_slot_id` ASC),
  INDEX `tour_match_ref_user1_idx` (`user_id` ASC),
  INDEX `tour_match_ref_user2_idx` (`opponent_id` ASC),
  CONSTRAINT `tour_match_ref_time_slot`
    FOREIGN KEY (`time_slot_id`)
    REFERENCES `capstone_project`.`time_slot` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tour_match_ref_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tour_match_ref_user2`
    FOREIGN KEY (`opponent_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`voucher`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`voucher` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`voucher` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `voucher_value` FLOAT NOT NULL,
  `bonus_point_target` INT NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`bill`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`bill` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`bill` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `friendly_match_id` INT NULL,
  `tour_match_id` INT NULL,
  `voucher_id` INT NULL,
  `date_charge` DATETIME NOT NULL,
  `price` FLOAT NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `bill_refer_friendly_match_idx` (`friendly_match_id` ASC),
  INDEX `bill_refer_to_voucher_idx` (`voucher_id` ASC),
  INDEX `bill_refer_tour_match_idx` (`tour_match_id` ASC),
  INDEX `bill_ref_user_idx` (`user_id` ASC),
  CONSTRAINT `bill_refer_friendly_match`
    FOREIGN KEY (`friendly_match_id`)
    REFERENCES `capstone_project`.`friendly_match` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `bill_refer_tour_match`
    FOREIGN KEY (`tour_match_id`)
    REFERENCES `capstone_project`.`tour_match` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `bill_refer_to_voucher`
    FOREIGN KEY (`voucher_id`)
    REFERENCES `capstone_project`.`voucher` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `bill_ref_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`rating_field`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`rating_field` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`rating_field` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `field_owner_id` INT NOT NULL,
  `rating_score` INT NOT NULL,
  `comment` VARCHAR(1000) NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `rating_field_ref_user_idx` (`user_id` ASC),
  INDEX `rating_field_ref_field_owner_idx` (`field_owner_id` ASC),
  CONSTRAINT `rating_field_ref_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `rating_field_ref_field_owner`
    FOREIGN KEY (`field_owner_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`rating_opponent`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`rating_opponent` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`rating_opponent` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `opponent_id` INT NOT NULL,
  `tour_match_id` INT NOT NULL,
  `win` TINYINT(1) NOT NULL,
  `rating_score` INT NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `rating_opponent_ref_tour_match_idx` (`tour_match_id` ASC),
  INDEX `rating_opponent_ref_user_idx` (`user_id` ASC),
  INDEX `rating_opponent_ref_user2_idx` (`opponent_id` ASC),
  CONSTRAINT `rating_opponent_ref_tour_match`
    FOREIGN KEY (`tour_match_id`)
    REFERENCES `capstone_project`.`tour_match` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `rating_opponent_ref_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `rating_opponent_ref_user2`
    FOREIGN KEY (`opponent_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`favorites_field`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`favorites_field` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`favorites_field` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `field_owner_id` INT NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `favorites_field_ref_user_idx` (`user_id` ASC),
  INDEX `favorites_field_ref_field_owner_idx` (`field_owner_id` ASC),
  CONSTRAINT `favorites_field_ref_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `favorites_field_ref_field_owner`
    FOREIGN KEY (`field_owner_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`voucher_record`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`voucher_record` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`voucher_record` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `voucher_id` INT NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `voucher_record_ref_voucher_idx` (`voucher_id` ASC),
  INDEX `voucher_record_ref_user_idx` (`user_id` ASC),
  CONSTRAINT `voucher_record_ref_voucher`
    FOREIGN KEY (`voucher_id`)
    REFERENCES `capstone_project`.`voucher` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `voucher_record_ref_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`report_opponent`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`report_opponent` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`report_opponent` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `opponent_id` INT NOT NULL,
  `tour_match_id` INT NOT NULL,
  `reason` VARCHAR(1000) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `report_ref_user_idx` (`user_id` ASC),
  INDEX `report_ref_opponent_idx` (`opponent_id` ASC),
  INDEX `report_ref_tour_match_idx` (`tour_match_id` ASC),
  CONSTRAINT `report_ref_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `report_ref_opponent`
    FOREIGN KEY (`opponent_id`)
    REFERENCES `capstone_project`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `report_ref_tour_match`
    FOREIGN KEY (`tour_match_id`)
    REFERENCES `capstone_project`.`tour_match` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
