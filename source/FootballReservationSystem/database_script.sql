-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema capstone_project
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema capstone_project
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `capstone_project` DEFAULT CHARACTER SET utf8 ;
USE `capstone_project` ;

-- -----------------------------------------------------
-- Table `capstone_project`.`field_owner`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`field_owner` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`field_owner` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `longitude` FLOAT NOT NULL,
  `latitude` FLOAT NOT NULL,
  `avatarURL` VARCHAR(500) NULL,
  `credit_card` VARCHAR(45) NOT NULL,
  `profits_commission` FLOAT NOT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`field_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`field_type` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`field_type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `number_player` INT NOT NULL,
  `description` VARCHAR(100) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`field`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`field` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`field` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `field_owner_id` INT NOT NULL,
  `field_type_id` INT NOT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  INDEX `field_owner_id_idx` (`field_owner_id` ASC),
  INDEX `field_ref_field_type_idx` (`field_type_id` ASC),
  CONSTRAINT `field_ref_field_owner`
    FOREIGN KEY (`field_owner_id`)
    REFERENCES `capstone_project`.`field_owner` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `field_ref_field_type`
    FOREIGN KEY (`field_type_id`)
    REFERENCES `capstone_project`.`field_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`time_enable_in_week`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`time_enable_in_week` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`time_enable_in_week` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `field_type_id` INT NOT NULL,
  `field_owner_id` INT NOT NULL,
  `week_date` VARCHAR(45) NOT NULL,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `price` FLOAT NOT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT 0,
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
    REFERENCES `capstone_project`.`field_owner` (`id`)
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
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `sale_off` FLOAT NOT NULL,
  `free_services` VARCHAR(1000) NULL,
  `status` TINYINT(1) NOT NULL DEFAULT 0,
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
    REFERENCES `capstone_project`.`field_owner` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`time_slot`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`time_slot` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`time_slot` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `field_id` INT NOT NULL,
  `date` DATETIME NOT NULL,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `price` FLOAT NOT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `time_slot_ref_field_idx` (`field_id` ASC),
  CONSTRAINT `time_slot_ref_field`
    FOREIGN KEY (`field_id`)
    REFERENCES `capstone_project`.`field` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`user` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `team_name` VARCHAR(100) NOT NULL,
  `phone` VARCHAR(45) NOT NULL,
  `rating_score` INT NOT NULL DEFAULT 0,
  `bonus_point` INT NOT NULL DEFAULT 0,
  `credit_card` VARCHAR(45) NOT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
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
  `start_time` DATETIME NOT NULL,
  `duration` INT NOT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT 0,
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
    REFERENCES `capstone_project`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`tour_match_temp`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`tour_match_temp` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`tour_match_temp` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `matching_request_id` INT NOT NULL,
  `opponent_id` INT NOT NULL,
  `time_slot_id` INT NOT NULL,
  `confirm_status` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `tour_match_ref_matching_request_idx` (`matching_request_id` ASC),
  INDEX `tour_match_ref_user_idx` (`opponent_id` ASC),
  INDEX `tour_match_ref_time_slot_idx` (`time_slot_id` ASC),
  CONSTRAINT `tour_match_ref_matching_request`
    FOREIGN KEY (`matching_request_id`)
    REFERENCES `capstone_project`.`matching_request` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tour_match_ref_user`
    FOREIGN KEY (`opponent_id`)
    REFERENCES `capstone_project`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tour_match_ref_time_slot`
    FOREIGN KEY (`time_slot_id`)
    REFERENCES `capstone_project`.`time_slot` (`id`)
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
  `status` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `friendly_match_ref_user_idx` (`user_id` ASC),
  INDEX `friendly_match_ref_time_slot_idx` (`time_slot_id` ASC),
  CONSTRAINT `friendly_match_ref_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `friendly_match_ref_time_slot`
    FOREIGN KEY (`time_slot_id`)
    REFERENCES `capstone_project`.`time_slot` (`id`)
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
  `kick_off_status` TINYINT(1) NULL DEFAULT 0,
  `winner_id` INT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `tour_match_refer_user1_idx` (`user_id` ASC),
  INDEX `tour_match_refer_user2_idx` (`opponent_id` ASC),
  INDEX `tour_match_refer_time_slot_idx` (`time_slot_id` ASC),
  CONSTRAINT `tour_match_refer_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tour_match_refer_user2`
    FOREIGN KEY (`opponent_id`)
    REFERENCES `capstone_project`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tour_match_refer_time_slot`
    FOREIGN KEY (`time_slot_id`)
    REFERENCES `capstone_project`.`time_slot` (`field_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`voucher`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`voucher` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`voucher` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `percent_sale_off` FLOAT NOT NULL,
  `bonus_point_target` INT NOT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`bill`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`bill` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`bill` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `friendly_match_id` INT NULL,
  `tour_match_id` INT NULL,
  `user_id` INT NOT NULL,
  `voucher_id` INT NULL,
  `date_charge` DATETIME NOT NULL,
  `price` FLOAT NOT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `bill_refer_friendly_match_idx` (`friendly_match_id` ASC),
  INDEX `bill_refer_to_user_idx` (`user_id` ASC),
  INDEX `bill_refer_to_voucher_idx` (`voucher_id` ASC),
  INDEX `bill_refer_tour_match_idx` (`tour_match_id` ASC),
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
  CONSTRAINT `bill_refer_to_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `bill_refer_to_voucher`
    FOREIGN KEY (`voucher_id`)
    REFERENCES `capstone_project`.`voucher` (`id`)
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
  `rating_score` INT NOT NULL,
  `comment` VARCHAR(1000) NOT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `rating_field_ref_user_idx` (`user_id` ASC),
  CONSTRAINT `rating_field_ref_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`user` (`id`)
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
  `rating_score` INT NOT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `rating_opponent_ref_user_idx` (`user_id` ASC),
  INDEX `rating_opponent_ref_user2_idx` (`opponent_id` ASC),
  INDEX `rating_opponent_ref_tour_match_idx` (`tour_match_id` ASC),
  CONSTRAINT `rating_opponent_ref_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `rating_opponent_ref_user2`
    FOREIGN KEY (`opponent_id`)
    REFERENCES `capstone_project`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `rating_opponent_ref_tour_match`
    FOREIGN KEY (`tour_match_id`)
    REFERENCES `capstone_project`.`tour_match` (`id`)
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
  `status` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `favorites_field_ref_user_idx` (`user_id` ASC),
  INDEX `favorites_field_ref_field_owner_idx` (`field_owner_id` ASC),
  CONSTRAINT `favorites_field_ref_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `favorites_field_ref_field_owner`
    FOREIGN KEY (`field_owner_id`)
    REFERENCES `capstone_project`.`field_owner` (`id`)
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
  INDEX `voucher_record_ref_user_idx` (`user_id` ASC),
  INDEX `voucher_record_ref_voucher_idx` (`voucher_id` ASC),
  CONSTRAINT `voucher_record_ref_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `voucher_record_ref_voucher`
    FOREIGN KEY (`voucher_id`)
    REFERENCES `capstone_project`.`voucher` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `capstone_project`.`reservation_schedule`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `capstone_project`.`reservation_schedule` ;

CREATE TABLE IF NOT EXISTS `capstone_project`.`reservation_schedule` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `time_slot_id` INT NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `reservation_schedule_ref_user_idx` (`user_id` ASC),
  INDEX `reservation_schedule_ref_time_slot_idx` (`time_slot_id` ASC),
  CONSTRAINT `reservation_schedule_ref_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_project`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `reservation_schedule_ref_time_slot`
    FOREIGN KEY (`time_slot_id`)
    REFERENCES `capstone_project`.`time_slot` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
